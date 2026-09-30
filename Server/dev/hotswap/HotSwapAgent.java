package hotswap;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Small Java 8 development agent that watches build/classes and redefines
 * already-loaded classes when javac replaces their .class files.
 *
 * Standard HotSwap can change method bodies, constants used by methods and
 * similar implementation details. Structural edits (fields/method signatures,
 * inheritance, etc.) are rejected by the JVM; when that happens we write a
 * restart flag for dev.ps1 to restart the server automatically.
 */
public final class HotSwapAgent {
    private static final long POLL_MS = 250L;

    private HotSwapAgent() {
    }

    public static void premain(String agentArgs, final Instrumentation instrumentation) {
        final String[] args = splitArgs(agentArgs);
        final Path classesRoot = Paths.get(args[0]).toAbsolutePath().normalize();
        final Path restartFlag = Paths.get(args[1]).toAbsolutePath().normalize();

        System.out.println("[HotSwap] Watching " + classesRoot);
        if (!instrumentation.isRedefineClassesSupported()) {
            System.out.println("[HotSwap] JVM does not support class redefinition; edits will auto-restart.");
            requestRestart(restartFlag, "JVM does not support redefineClasses");
            return;
        }

        Thread watcher = new Thread(new Runnable() {
            @Override
            public void run() {
                watch(classesRoot, restartFlag, instrumentation);
            }
        }, "server-hotswap-watcher");
        watcher.setDaemon(true);
        watcher.start();
    }

    private static String[] splitArgs(String agentArgs) {
        String classes = "build/classes";
        String restart = "build/hotswap-restart.flag";
        if (agentArgs != null && agentArgs.length() > 0) {
            String[] parts = agentArgs.split(";", 2);
            if (parts.length > 0 && parts[0].trim().length() > 0) {
                classes = parts[0].trim();
            }
            if (parts.length > 1 && parts[1].trim().length() > 0) {
                restart = parts[1].trim();
            }
        }
        return new String[] { classes, restart };
    }

    private static void watch(Path classesRoot, Path restartFlag, Instrumentation instrumentation) {
        Map<Path, Long> timestamps = snapshot(classesRoot);
        while (true) {
            try {
                Thread.sleep(POLL_MS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }

            Map<Path, Long> current = snapshot(classesRoot);
            for (Map.Entry<Path, Long> entry : current.entrySet()) {
                Path classFile = entry.getKey();
                Long oldTimestamp = timestamps.get(classFile);
                if (oldTimestamp == null || entry.getValue().longValue() == oldTimestamp.longValue()) {
                    continue;
                }

                if (tryRedefine(classFile, classesRoot, restartFlag, instrumentation)) {
                    timestamps.put(classFile, entry.getValue());
                }
            }

            // Keep entries for files that still exist and add newly-created class files.
            for (Map.Entry<Path, Long> entry : current.entrySet()) {
                if (!timestamps.containsKey(entry.getKey())) {
                    timestamps.put(entry.getKey(), entry.getValue());
                }
            }
            timestamps.keySet().retainAll(current.keySet());
        }
    }

    private static Map<Path, Long> snapshot(Path root) {
        final Map<Path, Long> result = new HashMap<Path, Long>();
        if (!Files.isDirectory(root)) {
            return result;
        }
        try (Stream<Path> stream = Files.walk(root)) {
            stream.forEach(path -> {
                if (Files.isRegularFile(path) && path.toString().endsWith(".class")) {
                    try {
                        result.put(path.toAbsolutePath().normalize(), Files.getLastModifiedTime(path).toMillis());
                    } catch (IOException ignored) {
                    }
                }
            });
        } catch (IOException ex) {
            System.err.println("[HotSwap] Unable to scan classes: " + ex.getMessage());
        }
        return result;
    }

    private static boolean tryRedefine(Path classFile, Path root, Path restartFlag,
            Instrumentation instrumentation) {
        String className = toClassName(classFile, root);
        Class<?> loadedClass = findLoadedClass(className, instrumentation);

        // If it has not been loaded yet, the normal class loader will pick up the
        // newly compiled bytes later; no redefine is required.
        if (loadedClass == null) {
            System.out.println("[HotSwap] Compiled " + className + " (not loaded yet)");
            return true;
        }

        if (!instrumentation.isModifiableClass(loadedClass)) {
            requestRestart(restartFlag, "Class is not modifiable: " + className);
            return true;
        }

        try {
            byte[] bytes = Files.readAllBytes(classFile);
            instrumentation.redefineClasses(new ClassDefinition(loadedClass, bytes));
            System.out.println("[HotSwap] Reloaded " + className);
            return true;
        } catch (UnsupportedOperationException ex) {
            System.out.println("[HotSwap] Structural change detected in " + className + "; requesting restart.");
            requestRestart(restartFlag, className + ": " + safeMessage(ex));
            return true;
        } catch (Throwable ex) {
            // A class file may briefly be visible while javac is replacing it.
            // Leave its old timestamp in place so the next poll retries it.
            System.err.println("[HotSwap] Reload failed for " + className + ": " + safeMessage(ex));
            return false;
        }
    }

    private static Class<?> findLoadedClass(String className, Instrumentation instrumentation) {
        Class<?>[] classes = instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : classes) {
            if (clazz.getName().equals(className)) {
                return clazz;
            }
        }
        return null;
    }

    private static String toClassName(Path file, Path root) {
        String relative = root.relativize(file).toString();
        relative = relative.substring(0, relative.length() - ".class".length());
        return relative.replace(File.separatorChar, '.').replace('/', '.');
    }

    private static void requestRestart(Path flag, String reason) {
        try {
            Path parent = flag.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(flag, reason.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.err.println("[HotSwap] Could not create restart flag: " + ex.getMessage());
        }
    }

    private static String safeMessage(Throwable throwable) {
        String message = throwable.getMessage();
        return message == null ? throwable.getClass().getName() : message;
    }
}
