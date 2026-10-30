import com.rs2.cache.js5.Interfaces;
import java.util.Map;

/** Inspect the stock revision-443 Castle Wars catapult aiming interface. */
public final class CastleWarsCatapultWidgetCacheChecks {
    public static void main(String[] args) throws Exception {
        Interfaces.load();
        Map<Integer, Interfaces.Component> group = Interfaces.group(54);
        if (group.isEmpty()) {
            throw new AssertionError("Missing native 443 catapult group 54");
        }
        System.out.println("Catapult group 54 (" + group.size() + " children)");
        for (Map.Entry<Integer, Interfaces.Component> entry : group.entrySet()) {
            Interfaces.Component c = entry.getValue();
            StringBuilder strings = new StringBuilder();
            StringBuilder word = new StringBuilder();
            for (byte b : c.getData()) {
                int n = b & 0xff;
                if (n >= 32 && n <= 126) {
                    word.append((char) n);
                } else {
                    if (word.length() >= 5) strings.append(" [").append(word).append("]");
                    word.setLength(0);
                }
            }
            if (word.length() >= 5) strings.append(" [").append(word).append("]");
            System.out.println("54:" + c.childId + " type=" + c.type
                    + " action=" + c.actionType + " xy=(" + c.x + "," + c.y
                    + ") size=" + c.width + "x" + c.height
                    + " parent=" + c.parentId + " strings=" + strings);
        }
    }
}
