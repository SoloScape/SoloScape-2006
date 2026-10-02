import com.rs2.ServerSettings;
import com.rs2.cache.js5.Definitions;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.interaction.*;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.shop.ShopManager;
import com.rs2.net.packet.InterfaceBridge;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Inventories actual cache actions and reports bindings without claiming play-test coverage. */
public final class InteractionCoverageAudit {
    private static final Map<String, Integer> counts = new TreeMap<>();
    private static PrintWriter output;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        Path directory = Paths.get(args.length == 0 ? "../qa-output/interaction-audit" : args[0]);
        Files.createDirectories(directory);
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        ObjectDefinition.loadRevision443();
        ShopManager.loadShops();
        Interfaces.load();
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(directory.resolve("interactions.csv"), StandardCharsets.UTF_8))) {
            output = writer;
            writer.println("kind,id,name,option,action,route,status");
            for (int id : new TreeSet<>(Definitions.readGroup(9).keySet())) {
                NpcDefinition definition = NpcDefinition.forId(id);
                for (int slot = 0; slot < 5; slot++) {
                    String label = definition.getAction(slot);
                    if (label == null) continue;
                    row("npc", id, definition.getName(), slot + 1, label,
                            NpcActionRouter.resolve(definition, slot).name(), NpcActionRouter.coverage(definition, slot));
                }
            }
            for (ObjectDefinition definition : ObjectDefinition.definitionsById) {
                if (definition == null) continue;
                for (int slot = 0; slot < 5; slot++) {
                    String label = definition.getAction(slot);
                    if (label == null) continue;
                    InteractionType semantic = ObjectActionRouter.semanticRoute(definition, slot);
                    InteractionType legacy = ObjectActionRouter.legacyRoute(slot + 1);
                    row("object", definition.getObjectId(), definition.getName(), slot + 1, label,
                            String.valueOf(semantic == null ? legacy : semantic),
                            semantic != null ? "semantic" : legacy == null ? "missing-option-handler" : "review-legacy");
                }
            }
            for (int id : new TreeSet<>(Definitions.readGroup(10).keySet())) {
                ItemDefinition definition = ItemDefinition.forId(id);
                for (int slot = 0; slot < 5; slot++) {
                    String inventory = definition.getInventoryAction(slot);
                    if (inventory != null) row("inventory-item", id, definition.getName(), slot + 1, inventory,
                            "legacy-option-" + ItemActionRouter.semanticOption(definition, slot + 1),
                            ItemActionRouter.isSemantic(definition, slot) ? "semantic" : "review-legacy");
                    String ground = definition.getGroundAction(slot);
                    if (ground != null) row("ground-item", id, definition.getName(), slot + 1, ground,
                            "Take".equalsIgnoreCase(ground) && slot == 2 ? "pickup" : "legacy-option-" + (slot + 1),
                            "Take".equalsIgnoreCase(ground) && slot == 2 ? "semantic" : "review-legacy");
                }
            }
            for (int group : new TreeSet<>(Interfaces.groupIds())) {
                for (Interfaces.Component component : Interfaces.group(group).values()) {
                    // IF3 components need listener/operation parsing to prove clickability;
                    // keep them visible for review rather than pretending they are all buttons.
                    if (!component.modern && component.actionType == 0 && component.type != 2) continue;
                    int legacy = InterfaceBridge.toLegacyComponent(component.packedId);
                    row("widget", component.packedId, group + ":" + component.childId, component.actionType,
                            component.modern ? "IF3-review" : "action-type-" + component.actionType,
                            legacy == InterfaceBridge.UNMAPPED ? "native-or-unmapped" : "legacy-component-" + legacy,
                            legacy == InterfaceBridge.UNMAPPED ? "review-native-or-unmapped" : "bridge-mapped");
                }
            }
            for (Map.Entry<Integer, Integer> mapping : new TreeMap<>(InterfaceBridge.groupMappings()).entrySet()) {
                boolean exists = !Interfaces.group(mapping.getValue()).isEmpty();
                // GrandExchangeWidgets.java constructs these groups in the client at runtime.
                boolean generated = mapping.getValue() >= 500 && mapping.getValue() <= 504;
                row("interface-group", mapping.getKey(), "legacy-group", 0, "open",
                        "native-group-" + mapping.getValue(), exists ? "bridge-target-exists"
                                : generated ? "client-generated" : "missing-bridge-target");
            }
        }
        try (PrintWriter summary = new PrintWriter(Files.newBufferedWriter(directory.resolve("summary.md"), StandardCharsets.UTF_8))) {
            summary.println("# Revision 443 interaction inventory\n");
            summary.println("Source: the local loaded cache and current server bindings. Includes unspawned and unused content.\n");
            summary.println("This is an inventory of routes, not a count of working/broken gameplay. `semantic` means an action has a named gameplay owner; `bridge-mapped` means a widget can translate to a legacy component. Both still need end-to-end validation. Review rows may already work through specialised handlers.\n");
            summary.println("| Kind / status | Actions |\n| --- | ---: |");
            for (Map.Entry<String, Integer> count : counts.entrySet()) summary.println("| " + count.getKey() + " | " + count.getValue() + " |");
            summary.println("\nFilter interactions.csv by `missing-*` first, then `review-*`. Keep NPC-specific quest rules and position-dependent object behavior when adding bindings. Runtime `[packet-debug]` `unhandled` messages identify exercised gaps.\n");
            summary.println("Run tools/interaction-audit.ps1 -Check to regenerate this inventory and exercise the interaction regression checks.");
        }
        for (Map.Entry<String, Integer> count : counts.entrySet()) System.out.println(count.getKey() + "=" + count.getValue());
        System.out.println("Report: " + directory.toAbsolutePath());
        System.exit(0);
    }

    private static void row(String kind, int id, String name, int option, String action, String route, String status) {
        String key = kind + " / " + status;
        counts.put(key, counts.getOrDefault(key, 0) + 1);
        output.println(csv(kind) + "," + id + "," + csv(name) + "," + option + "," + csv(action)
                + "," + csv(route) + "," + csv(status));
    }

    private static String csv(String value) {
        return "\"" + String.valueOf(value).replace("\"", "\"\"") + "\"";
    }
}
