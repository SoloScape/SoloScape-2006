import com.rs2.cache.js5.Definitions;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/** Fail-closed audit of raw content and conservative Java item-production sites. */
public final class StrictContentChecks {
    private final Set<Integer> valid;
    private final List<String> findings = new ArrayList<>();
    private final Set<Integer> roots = new TreeSet<>();
    private final Set<Integer> sourceRoots = new TreeSet<>();
    private int items, tables;
    private static final int[] VIRTUAL_TABLES = {
        6391,6392,6393,6394,6395,6396,6397,6398,6399,6400,
        6401,6402,6403,6404,6405,6406,6407,6408,6409,6410,6411,6412,6413
    };

    private StrictContentChecks(Set<Integer> valid) { this.valid = valid; }

    public static void main(String[] args) throws Exception {
        selfTest();
        if (args.length == 1 && args[0].equals("--self-test")) {
            System.out.println("Strict content scanner fixture checks passed.");
            return;
        }
        StrictContentChecks scan = new StrictContentChecks(Definitions.readGroup(10).keySet());
        scan.scanGround(Files.readAllBytes(Paths.get("data/world/Item spawn.dat")));
        scan.scanNpcSpawns(Files.readAllBytes(Paths.get("data/npcs/Npc spawn.dat")));
        // Native NPCs conservatively include scripted, transformed and summoned NPCs.
        scan.roots.addAll(Definitions.readGroup(9).keySet());
        // Raw clue tables are checked even when ItemDefinition filtering would hide a leak.
        scan.roots.addAll(Arrays.asList(6420, 6421, 6422));
        scan.scanSources(Paths.get("src/main/java"));
        scan.scanDrops(Files.readAllBytes(Paths.get("data/npcs/Npc drops.dat")));
        Path report = Paths.get("../qa-output/strict-content-checks/report.tsv");
        Files.createDirectories(report.getParent());
        List<String> output = new ArrayList<>();
        output.add("status\tsource\tdetail");
        output.addAll(scan.findings);
        Files.write(report, output, StandardCharsets.UTF_8);
        System.out.println("Checked " + scan.items + " item references and " + scan.tables
                + " drop tables against " + scan.valid.size() + " native 443 item IDs.");
        long invalid = scan.findings.stream().filter(s -> s.startsWith("INVALID\t")).count();
        long candidates = scan.findings.stream().filter(s -> s.startsWith("REVIEW\t")).count();
        System.out.println("Invalid data references: " + invalid + "; source candidates: " + candidates
                + "; unresolved coverage: " + (scan.findings.size() - invalid - candidates)
                + ". Report: " + report.toAbsolutePath());
        scan.findings.stream().filter(s -> s.startsWith("INVALID\t")).limit(8).forEach(System.out::println);
        scan.findings.stream().filter(s -> !s.startsWith("INVALID\t")).limit(4).forEach(System.out::println);
        if (!scan.findings.isEmpty()) {
            throw new AssertionError("Strict content scan failed; fix invalid references and resolve coverage gaps.");
        }
        System.out.println("Strict content checks passed.");
    }

    private void item(int id, String source) {
        items++;
        if (!valid.contains(id)) findings.add("INVALID\t" + source + "\titem " + id);
    }
    private void gap(String source, String detail) {
        findings.add("UNRESOLVED\t" + source + "\t" + detail.replace('\n', ' ').replace('\t', ' '));
    }
    private static void end(DataInputStream in) throws IOException {
        if (in.available() != 0) throw new IOException("Trailing bytes in content file");
    }
    private void scanGround(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        in.readUnsignedByte();
        int count = in.readUnsignedShort();
        for (int i = 0; i < count; i++) {
            int id = in.readUnsignedShort() - count - i;
            in.readUnsignedShort(); in.readUnsignedByte();
            int x = in.readUnsignedShort(), y = in.readUnsignedShort(), z = in.readUnsignedByte();
            item(id, "Item spawn.dat record " + i + " (" + x + "," + y + "," + z + ")");
        }
        end(in);
    }
    private void scanNpcSpawns(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        in.readUnsignedByte();
        int count = in.readUnsignedShort();
        for (int i = 0; i < count; i++) {
            int id = in.readUnsignedShort() - count - i;
            // Include members' spawns: strict mode is independent of F2P.
            in.readUnsignedByte(); in.readUnsignedByte();
            in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedByte();
            if (id <= 3851) roots.add(id); // GameplayHelper's unconditional loader cutoff.
        }
        end(in);
    }

    private static final class DropTable {
        int alias = -1;
        final List<Integer> ids = new ArrayList<>();
    }
    private void scanDrops(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        int version = in.readUnsignedByte();
        if (version != 1 && version != 2) throw new IOException("Unknown drop format " + version);
        DropTable[] data = new DropTable[in.readUnsignedShort()];
        for (int npc = 0; npc < data.length; npc++) {
            int kind = in.readUnsignedByte();
            DropTable table = data[npc] = new DropTable();
            if (kind == 2) table.alias = in.readUnsignedShort();
            else if (kind == 1) {
                for (int group = 0; group < 7; group++) {
                    int count = in.readUnsignedByte();
                    for (int n = 0; n < count; n++) {
                        if (group >= 5) {
                            in.readUnsignedByte();
                            if (version == 1) in.readUnsignedByte(); else in.readUnsignedShort();
                            in.readInt();
                        }
                        int mode = in.readUnsignedByte();
                        List<Integer> ids = new ArrayList<>();
                        if (mode < 3) ids.add(in.readUnsignedShort());
                        if (mode == 0) in.readUnsignedShort();
                        else if (mode == 1) { in.readUnsignedShort(); in.readUnsignedShort(); }
                        else if (mode >= 2 && mode <= 4) {
                            int options = in.readUnsignedByte();
                            for (int j = 0; j < options; j++) {
                                if (mode >= 3) ids.add(in.readUnsignedShort());
                                in.readUnsignedShort();
                                if (mode == 4) in.readUnsignedShort();
                            }
                        } else throw new IOException("Unknown drop entry mode " + mode);
                        if (group == 0 || group == 5 || group == 6) table.ids.addAll(ids);
                    }
                }
            } else if (kind != 0) throw new IOException("Unknown drop table kind " + kind);
        }
        end(in);
        Set<Integer> visited = new HashSet<>();
        for (int root : new TreeSet<>(roots)) visit(root, data, visited, new HashSet<>(), "root " + root);
        for (int root : new TreeSet<>(sourceRoots)) visit(root, data, visited, new HashSet<>(), "candidate root " + root);
    }
    private void visit(int id, DropTable[] data, Set<Integer> visited, Set<Integer> active, String source) {
        if (id < 0 || id >= data.length) { gap(source, "missing table " + id); return; }
        if (active.contains(id)) { gap(source, "cyclic table " + id); return; }
        if (!visited.add(id)) return;
        active.add(id);
        tables++;
        DropTable table = data[id];
        String next = source + " -> table " + id;
        if (table.alias >= 0) visit(table.alias, data, visited, active, next);
        else for (int item : table.ids) {
            if (item < 65000) {
                if (source.startsWith("candidate root")) {
                    items++;
                    if (!valid.contains(item)) findings.add("REVIEW\t" + next + "\titem " + item + " (script reachability needs review)");
                } else item(item, next);
            }
            else if (item == 65000) { /* explicit no-drop sentinel */ }
            else if (item <= 65007) { /* pools and clues are inspected in Java sources */ }
            else if (item <= 65030) {
                visit(VIRTUAL_TABLES[item - 65008], data, visited, active, next);
                if (item == 65018) for (int t = 6414; t <= 6418; t++) visit(t, data, visited, active, next);
            } else if (item == 65036) visit(6419, data, visited, active, next);
            else if (item >= 65040 && item <= 65042) visit(6423 + item - 65040, data, visited, active, next);
            else gap(next, "unknown virtual item " + item);
            // 5509 may resolve to any available essence pouch.
            if (item == 5509) for (int pouch : new int[]{5509,5510,5512,5514}) item(pouch, next + " pouch pool");
        }
        active.remove(id);
    }

    private void scanSources(Path directory) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) throw new IOException("A JDK is required for source coverage");
        List<File> files;
        try (java.util.stream.Stream<Path> paths = Files.walk(directory)) {
            files = paths.filter(p -> p.toString().endsWith(".java")).map(Path::toFile).collect(Collectors.toList());
        }
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            JavacTask task = (JavacTask) compiler.getTask(null, manager, diagnostics,
                    Arrays.asList("-proc:none"), null, manager.getJavaFileObjectsFromFiles(files));
            for (CompilationUnitTree unit : task.parse()) scanUnit(unit, Trees.instance(task));
        }
        for (Diagnostic<?> d : diagnostics.getDiagnostics()) if (d.getKind() == Diagnostic.Kind.ERROR) {
            gap("Java parser", d.toString());
        }
    }
    private void scanUnit(CompilationUnitTree unit, Trees trees) {
        // Do not guess identifiers by name: local shadowing and mutable arrays need data flow.
        Map<String, Integer> constants = Collections.emptyMap();
        new TreeScanner<Void, Void>() {
            String source(Tree node) {
                long pos = trees.getSourcePositions().getStartPosition(unit, node);
                return unit.getSourceFile().getName() + ":" + unit.getLineMap().getLineNumber(pos);
            }
            @Override public Void visitIf(IfTree node, Void unused) {
                String condition = node.getCondition().toString();
                if (condition.matches("\\(*ServerSettings\\.content2007Enabled\\)*")) {
                    scan(node.getElseStatement(), unused); return null;
                }
                if (condition.matches("\\(*!ServerSettings\\.content2007Enabled\\)*")) {
                    scan(node.getThenStatement(), unused); return null;
                }
                return super.visitIf(node, unused);
            }
            @Override public Void visitNewClass(NewClassTree node, Void unused) {
                String type = node.getIdentifier().toString();
                if ((type.equals("ItemStack") || type.endsWith(".ItemStack")) && !node.getArguments().isEmpty()) {
                    Integer id = number(node.getArguments().get(0), constants);
                    if (id == null) gap(source(node), "dynamic ItemStack ID: " + node.getArguments().get(0));
                    else if (id >= 0) {
                        // Constructors may also be used for comparisons, removal, or UI.
                        items++;
                        if (!valid.contains(id)) findings.add("REVIEW\t" + source(node) + "\tItemStack " + id);
                    } // -1 is a source/UI empty-slot sentinel.
                }
                return super.visitNewClass(node, unused);
            }
            @Override public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
                String call = node.getMethodSelect().toString();
                if ((call.endsWith(".rollDrops") || call.endsWith(".rollWeightedDrops")) && node.getArguments().size() >= 2) {
                    Integer root = number(node.getArguments().get(1), constants);
                    if (root != null) sourceRoots.add(root);
                    else gap(source(node), "dynamic drop table: " + node.getArguments().get(1));
                }
                if (call.endsWith(".spawnNpc") && !node.getArguments().isEmpty()) {
                    Integer root = number(node.getArguments().get(0), constants);
                    if (root != null) sourceRoots.add(root);
                    else gap(source(node), "dynamic NPC spawn: " + node.getArguments().get(0));
                }
                return super.visitMethodInvocation(node, unused);
            }
            @Override public Void visitVariable(VariableTree node, Void unused) {
                String name = node.getName().toString().toLowerCase(Locale.ROOT);
                if (node.getInitializer() instanceof NewArrayTree &&
                        (name.contains("rewarditem") || name.endsWith("drop_item_ids"))) {
                    checkArray((NewArrayTree)node.getInitializer());
                }
                return super.visitVariable(node, unused);
            }
            @Override public Void visitAssignment(AssignmentTree node, Void unused) {
                if (node.getExpression() instanceof NewArrayTree &&
                        node.getVariable().toString().toLowerCase(Locale.ROOT).contains("rewarditem")) {
                    checkArray((NewArrayTree)node.getExpression());
                }
                return super.visitAssignment(node, unused);
            }
            void checkArray(NewArrayTree node) {
                if (node.getInitializers() == null) return;
                for (ExpressionTree entry : node.getInitializers()) {
                    Integer id = number(entry, constants);
                    if (id == null) gap(source(entry), "dynamic reward/pool ID: " + entry);
                    else if (id != 65000) item(id, source(entry));
                }
            }
        }.scan(unit, null);
    }
    private static Integer number(Tree tree, Map<String,Integer> constants) {
        if (tree instanceof LiteralTree && ((LiteralTree)tree).getValue() instanceof Number)
            return ((Number)((LiteralTree)tree).getValue()).intValue();
        if (tree instanceof ParenthesizedTree) return number(((ParenthesizedTree)tree).getExpression(), constants);
        if (tree instanceof IdentifierTree) return constants.get(tree.toString());
        if (tree instanceof UnaryTree && tree.getKind() == Tree.Kind.UNARY_MINUS) {
            Integer n = number(((UnaryTree)tree).getExpression(), constants); return n == null ? null : -n;
        }
        return null;
    }
    private static void selfTest() throws Exception {
        StrictContentChecks s = new StrictContentChecks(new HashSet<>(Arrays.asList(995,7332,10511)));
        s.item(7332, "native"); s.item(10665, "duplicate"); s.item(100, "hole below ceiling");
        if (s.findings.size() != 2) throw new AssertionError("Cache membership check failed");
        byte[] spawn = {1,0,1, 3,(byte)228, 0,1,0, 0,1,0,2,0}; // encoded 995 + count
        s.scanGround(spawn);
        if (s.findings.size() != 2) throw new AssertionError("Spawn offset decoding failed");
        try { s.scanGround(Arrays.copyOf(spawn, spawn.length - 1)); throw new AssertionError("Truncation accepted"); }
        catch (EOFException expected) { }
        DropTable[] graph = {new DropTable(),new DropTable(),new DropTable()};
        graph[0].alias = 1; graph[1].ids.add(10665);
        s.visit(0, graph, new HashSet<>(), new HashSet<>(), "alias fixture");
        graph[1].alias = 0;
        s.visit(0, graph, new HashSet<>(), new HashSet<>(), "cycle fixture");
        graph[2].ids.add(65099);
        s.visit(2, graph, new HashSet<>(), new HashSet<>(), "virtual fixture");
        if (s.findings.size() != 5) throw new AssertionError("Graph validation failed");
        DropTable[] nested = new DropTable[6426];
        for (int i = 0; i < nested.length; i++) nested[i] = new DropTable();
        nested[0].ids.add(65018);
        nested[6418].ids.add(10665); // The rarest combat-level branch must also be visited.
        StrictContentChecks nestedFixture = new StrictContentChecks(s.valid);
        nestedFixture.visit(0, nested, new HashSet<>(), new HashSet<>(), "nested fixture");
        if (nestedFixture.findings.size() != 1 || !nestedFixture.findings.get(0).contains("6418"))
            throw new AssertionError("Virtual level-dependent branches were not fully scanned");
        // Real binary parsing: weighted multi-item entry, guaranteed/independent drops,
        // ignored legacy groups, aliases, and both supported chance numerator formats.
        for (int version : new int[]{1,2}) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            out.writeByte(version); out.writeShort(2);
            out.writeByte(2); out.writeShort(1);
            out.writeByte(1);
            for (int group = 0; group < 7; group++) {
                out.writeByte(1);
                if (group >= 5) {
                    out.writeByte(0);
                    if (version == 1) out.writeByte(1); else out.writeShort(1);
                    out.writeInt(100);
                }
                out.writeByte(3); out.writeByte(2);
                out.writeShort(995); out.writeShort(1);
                out.writeShort(10665); out.writeShort(1);
            }
            StrictContentChecks fixture = new StrictContentChecks(s.valid);
            fixture.roots.add(0);
            fixture.scanDrops(bytes.toByteArray());
            if (fixture.findings.size() != 3) throw new AssertionError("Drop groups/options/alias parsing failed");
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String text = "class Fixture { void run(int dynamic) { "
                + "if (ServerSettings.content2007Enabled) { new ItemStack(10665); } "
                + "else { new ItemStack(7332); } new ItemStack(dynamic); "
                + "new ItemStack(10665); int[] rewardItems = new int[]{7332,10665}; "
                + "NpcDropManager.rollDrops(null,42,false); "
                + "if (ServerSettings.content2007Enabled) { NpcDropManager.rollDrops(null,43,false); } "
                + "/* new ItemStack(99999); */ } }";
        JavaFileObject source = new SimpleJavaFileObject(java.net.URI.create("string:///Fixture.java"), JavaFileObject.Kind.SOURCE) {
            @Override public CharSequence getCharContent(boolean ignore) { return text; }
        };
        JavacTask task = (JavacTask)compiler.getTask(null, null, null, Arrays.asList("-proc:none"), null, Arrays.asList(source));
        StrictContentChecks fixture = new StrictContentChecks(s.valid);
        for (CompilationUnitTree unit : task.parse()) fixture.scanUnit(unit, Trees.instance(task));
        if (fixture.findings.size() != 3 || fixture.findings.stream().filter(f -> f.startsWith("UNRESOLVED")).count() != 1)
            throw new AssertionError("Strict gating/source pool/dynamic coverage parsing failed");
        if (!fixture.sourceRoots.equals(new TreeSet<>(Arrays.asList(42))))
            throw new AssertionError("Strict gating of source roots failed");
    }
}
