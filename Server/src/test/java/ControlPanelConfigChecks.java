import com.rs2.ConfigFile;
import com.rs2.Server;
import com.rs2.ServerSettings;
import com.rs2.launcher.ConfigEditorPanel;
import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Regression checks for settings absent from older server configs. */
public final class ControlPanelConfigChecks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static Object field(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }

    private static String read(Path path) throws Exception {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    private static void verify() throws Exception {
        Path config = Paths.get("config/server.cfg");
        Files.createDirectories(config.getParent());
        ConfigFile.writeDefaultConfig();
        String defaults = read(config);
        check(defaults.contains("[2007_CONTENT];0"), "fresh config exposes 2007 content");
        check(defaults.contains("[CLUE_MERCHANT];0;1;50"), "fresh config exposes clue merchant");
        check(defaults.contains("[WILDERNESS_SLAYER];0"), "fresh config exposes Wilderness Slayer");

        ConfigFile.applyConfigEntry("2007_CONTENT", new String[]{"1"});
        ConfigFile.applyConfigEntry("CLUE_MERCHANT", new String[]{"1", "2", "125"});
        ConfigFile.applyConfigEntry("WILDERNESS_SLAYER", new String[]{"1"});
        check(ServerSettings.content2007Enabled, "2007 content setting is accepted");
        check(ServerSettings.clueMerchantEnabled && ServerSettings.clueMerchantClueLevel == 2
            && ServerSettings.clueMerchantPriceCoins == 125, "clue merchant parameters are applied");
        check(ServerSettings.wildernessSlayerEnabled, "Wilderness Slayer setting is accepted");
        ConfigFile.applyConfigEntry("2007_CONTENT", new String[]{"0"});
        ConfigFile.applyConfigEntry("CLUE_MERCHANT", new String[]{"0", "1", "50"});
        ConfigFile.applyConfigEntry("WILDERNESS_SLAYER", new String[]{"0"});
        check(!ServerSettings.content2007Enabled && !ServerSettings.clueMerchantEnabled
            && !ServerSettings.wildernessSlayerEnabled, "all three settings can be disabled");

        String legacy = "// Keep this comment\n[P2P_WORLD];1\n[XP_RATE];2.0;0\n";
        Files.write(config, legacy.getBytes(StandardCharsets.UTF_8));
        ConfigEditorPanel panel = new ConfigEditorPanel(new Font("Dialog", Font.PLAIN, 12));
        Object serverPanel = field(panel, "serverPanel");
        Map<String, JTextField> fields = (Map<String, JTextField>)field(serverPanel, "valueFields");
        check("0".equals(fields.get("2007_CONTENT").getText()), "older configs show disabled default");
        check("0;1;50".equals(fields.get("CLUE_MERCHANT").getText()), "older configs show clue defaults");
        check("0".equals(fields.get("WILDERNESS_SLAYER").getText()), "older configs show Slayer default");
        check(legacy.equals(read(config)), "opening the panel does not rewrite config");
        fields.get("2007_CONTENT").setText("1");
        fields.get("CLUE_MERCHANT").setText("1;3;500");
        fields.get("WILDERNESS_SLAYER").setText("1");
        // Exercise persistence without starting or reconfiguring a live server.
        Server.serverStatus = 1;
        Method save = serverPanel.getClass().getDeclaredMethod("saveSettings", boolean.class);
        save.setAccessible(true);
        check(Boolean.TRUE.equals(save.invoke(serverPanel, false)), "server panel saves");
        check(legacy.equals(read(Paths.get("config/server.cfg.bak"))), "backup preserves original");
        String saved = read(config);
        check(saved.startsWith(legacy), "existing comments and settings survive");
        check(saved.contains("[2007_CONTENT];1") && saved.contains("[CLUE_MERCHANT];1;3;500")
            && saved.contains("[WILDERNESS_SLAYER];1"), "missing settings persist on save");
        Server.loadConfig();
        check(ServerSettings.content2007Enabled && ServerSettings.clueMerchantEnabled
            && ServerSettings.clueMerchantClueLevel == 3 && ServerSettings.clueMerchantPriceCoins == 500
            && ServerSettings.wildernessSlayerEnabled, "saved settings load into server");

        Method reload = serverPanel.getClass().getDeclaredMethod("reloadSettings");
        reload.setAccessible(true);
        reload.invoke(serverPanel);
        check("1;3;500".equals(fields.get("CLUE_MERCHANT").getText()), "reload preserves explicit values");
        check(Boolean.TRUE.equals(save.invoke(serverPanel, false)), "second save succeeds");
        check(saved.equals(read(config)), "repeated saves do not duplicate entries");
        System.out.println("Control panel config checks passed.");
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                verify();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
