import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;
import java.nio.file.*;
import java.util.*;

/** Offline entry-point audit. Does not load/save accounts or start the world. */
public final class NpcDialogueAudit {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.freeToPlayWorld = false;
        ServerSettings.membershipRequirementMode = 1;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        Set<Integer> spawned = new TreeSet<Integer>();
        try (DataInputStream input = new DataInputStream(new FileInputStream("data/npcs/Npc spawn.dat"))) {
            input.readUnsignedByte();
            int count = input.readUnsignedShort();
            for (int i = 0; i < count; i++) {
                int id = input.readUnsignedShort() - count - i;
                input.readUnsignedByte(); input.readUnsignedByte();
                input.readUnsignedShort(); input.readUnsignedShort(); input.readUnsignedByte();
                spawned.add(id);
            }
        }
        Path output = Paths.get(args.length == 0 ? "qa-output/npc-dialogue-audit.tsv" : args[0]);
        Files.createDirectories(output.toAbsolutePath().getParent());
        int handled = 0, missing = 0, errors = 0;
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output))) {
            writer.println("npc_id\tname\tspawned\ttalk_slots\tentry_status\tshop_fallback\tdetail");
            for (int id = 0; id < World.getNpcDefinitions().length; id++) {
                NpcDefinition definition = World.getNpcDefinitions()[id];
                if (definition == null) continue;
                boolean talks = false;
                String slots = "";
                for (int slot = 0; slot < 5; slot++) {
                    String action = definition.getAction(slot);
                    if ("Talk-to".equalsIgnoreCase(action) || "Talk to".equalsIgnoreCase(action)) {
                        talks = true; slots += (slots.isEmpty() ? "" : ",") + (slot + 1);
                    }
                }
                if (!talks) continue;
                Player player = player();
                Npc npc = new Npc(id);
                npc.setPosition(new Position(3208, 3215, 0));
                player.setInteractionTarget(npc);
                player.setInteractionTargetId(id);
                String status, detail = "";
                try {
                    if (DialogueManager.startDialogue(player, id)) { status = "handled"; handled++; }
                    else { status = "missing"; missing++; }
                } catch (Throwable failure) {
                    status = "error"; errors++;
                    detail = failure.getClass().getSimpleName() + ": " + failure.getMessage()
                            + (failure.getStackTrace().length == 0 ? "" : " at " + failure.getStackTrace()[0]);
                }
                writer.println(id + "\t" + definition.getName() + "\t" + spawned.contains(id)
                        + "\t" + slots + "\t" + status + "\t" + (definition.getShopId() >= 0)
                        + "\t" + detail.replace('\n', ' ').replace('\t', ' '));
            }
        }
        System.out.println("Dialogue entry audit: handled=" + handled + " missing=" + missing
                + " errors=" + errors + " report=" + output);
        System.exit(0);
    }

    static Player player() throws Exception {
        Player player = new Player(null);
        player.setEncodedIndex(32769);
        player.setUsername("dialogue_audit");
        player.setSize(1);
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        SocketChannel transport = SocketChannel.open(); transport.close();
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setQuestState(0, 1);
        player.setPosition(new Position(3207, 3215, 0));
        return player;
    }
}
