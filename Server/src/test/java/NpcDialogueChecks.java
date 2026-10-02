import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.dialogue.HistoricalNpcDialogues;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

/** Exercises restored quest dispatch, native/legacy packets and every imported choice. */
public final class NpcDialogueChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.freeToPlayWorld = false;
        QuestDefinition.loadDefinitions(); InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions(); NpcDefinition.loadDefinitions();
        require(InterfaceDefinition.interfaceCount == 0, "Fixture must reproduce absent legacy interfaces");
        Method available = com.rs2.model.quest.QuestManager.class
                .getDeclaredMethod("isQuestJournalButtonAvailable", int.class);
        available.setAccessible(true);
        int quests = 0;
        for (int id = 0; id < QuestDefinition.questCount; id++) {
            if (QuestDefinition.getQuestScript(id).getQuestId() < 0) continue;
            require((Boolean) available.invoke(null, id), "Implemented quest remains disabled: " + id);
            quests++;
        }
        Player restricted = NpcDialogueAudit.player();
        ServerSettings.membershipRequirementMode = 0;
        require(!restricted.getQuestManager().handleNpcDialogue(455, 1, 0, 455), "Members quest bypassed account restriction");
        require(DialogueManager.startDialogue(restricted, 278), "Free quest blocked for non-member");
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.freeToPlayWorld = true;
        require(!restricted.getQuestManager().handleNpcDialogue(455, 1, 0, 455), "Members quest bypassed world restriction");
        ServerSettings.freeToPlayWorld = false;
        for (boolean nativePackets : new boolean[] {false, true}) {
            try (Fixture fixture = new Fixture()) {
                Player player = fixture.player;
                require(DialogueManager.startDialogue(player, 278), "Cook not routed");
                require(fixture.text().contains("What am I to do?"), "Cook opening missing");
                advance(player, nativePackets);
                require(fixture.text().contains("What's wrong?"), "Cook choices missing");
                choose(player, 1, nativePackets);
                fixture.text();
                for (int i = 0; i < 3; i++) { advance(player, nativePackets); fixture.text(); }
                choose(player, 1, nativePackets); fixture.text();
                for (int i = 0; i < 3; i++) { advance(player, nativePackets); fixture.text(); }
                require(player.getQuestState(2) == 2, "Cook quest did not start");
                player.getInventoryManager().addItem(new ItemStack(1927));
                player.getInventoryManager().addItem(new ItemStack(1933));
                player.getInventoryManager().addItem(new ItemStack(1944));
                fixture.text();
                require(DialogueManager.startDialogue(player, 278), "Cook hand-in missing"); fixture.text();
                for (int i = 0; i < 20 && player.getQuestState(2) != 1; i++) {
                    advance(player, nativePackets); fixture.text();
                }
                require(player.getQuestState(2) == 1, "Cook quest not completed");
                require(!player.getInventoryManager().containsItem(1927)
                        && !player.getInventoryManager().containsItem(1933)
                        && !player.getInventoryManager().containsItem(1944), "Ingredients not consumed");
                require(player.getSkillManager().getExperience()[7] >= 300, "Cooking reward missing");
                require(DialogueManager.startDialogue(player, 278), "Cook post-quest dialogue missing");
                require(fixture.text().contains("How is the adventuring going"), "Wrong post-quest opening");
                advance(player, nativePackets); fixture.text();
                choose(player, 4, nativePackets);
                require(fixture.text().contains("Can I use your range?"), "Range branch missing");
                advance(player, nativePackets);
                require(fixture.text().contains("Go ahead - it's a very good range."), "Range permission missing");
            }
            for (int option : new int[] {2, 3, 4}) {
                try (Fixture fixture = new Fixture()) {
                    Player player = fixture.player;
                    DialogueManager.startDialogue(player, 278); fixture.text();
                    advance(player, nativePackets); fixture.text();
                    choose(player, option, nativePackets); fixture.text();
                    advance(player, nativePackets);
                    String reply = fixture.text();
                    require(reply.contains(option == 2 ? "Haha, very funny!" : option == 3 ? "No, I'm not." : "Err thank you."),
                            "Cook side branch missing: " + option);
                    require(player.getQuestState(2) == 0, "Side conversation started quest");
                    if (option == 3) {
                        advance(player, nativePackets); fixture.text();
                        choose(player, 2, nativePackets); fixture.text();
                        advance(player, nativePackets);
                        require(fixture.text().contains("terrible trouble."), "Day-off response missing");
                        advance(player, nativePackets); fixture.text();
                        advance(player, nativePackets);
                        require(fixture.text().contains("It's the Duke's birthday today"), "Side branch did not return to quest");
                    }
                }
            }
            try (Fixture fixture = new Fixture()) {
                DialogueManager.startDialogue(fixture.player, 741); fixture.text();
                advance(fixture.player, nativePackets); fixture.text();
                choose(fixture.player, 2, nativePackets); fixture.text();
                advance(fixture.player, nativePackets);
                require(fixture.text().contains("blacksmiths are prosperous"), "Duke money advice missing");
                require(fixture.player.getQuestState(14) == 0, "Money advice started Rune Mysteries");
            }
            try (Fixture fixture = new Fixture()) {
                require(DialogueManager.startDialogue(fixture.player, 300), "Sedridor pre-quest greeting missing");
                fixture.text(); advance(fixture.player, nativePackets); fixture.text();
                choose(fixture.player, 2, nativePackets);
                require(fixture.text().contains("That is indeed a good question."), "Sedridor history missing");
                require(fixture.player.getQuestState(14) == 0, "Small talk changed Rune Mysteries");
            }
            for (int option = 1; option <= 4; option++) {
                try (Fixture fixture = new Fixture()) {
                    Player player = fixture.player;
                    require(DialogueManager.startDialogue(player, 957), "Mubariz not routed"); fixture.text();
                    for (int i = 0; i < 4; i++) { advance(player, nativePackets); fixture.text(); }
                    require(player.getOpenInterfaceId() == 2480, "Mubariz menu missing");
                    choose(player, option, nativePackets);
                    String text = fixture.text();
                    String[] questions = {"What is this place?", "How do I challenge someone to a duel?",
                            "What kind of options are there?", "This place looks really old"};
                    require(text.contains(questions[option - 1]), "Wrong branch for option " + option);
                    for (int i = 0; i < 40 && !player.getDialogueManager().isDialogueInactive(); i++) {
                        advance(player, nativePackets); fixture.text();
                    }
                    require(player.getDialogueManager().isDialogueInactive(), "Mubariz branch stuck");
                    advance(player, nativePackets); fixture.text();
                    require(player.getOpenInterfaceId() == 0, "Terminal dialogue did not close");
                }
            }
        }
        int options = 0, pages = 0;
        Field head = DialogueManager.class.getDeclaredField("dialogueNpcId");
        head.setAccessible(true);
        for (String line : Files.readAllLines(Paths.get("data/content/npcDialogues2006.tsv"))) {
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] fields = line.split("\t", -1);
            int id = Integer.parseInt(fields[0]), node = Integer.parseInt(fields[1]);
            if ("OPTIONS".equals(fields[2])) {
                for (int option = 1; option <= fields.length - 4; option++) {
                    Player player = NpcDialogueAudit.player();
                    if (id == 278) player.setQuestState(2, 1);
                    require(HistoricalNpcDialogues.start(player, id), "Missing program " + id);
                    require(DialogueManager.continueDialogue(player, HistoricalNpcDialogues.ID_BASE + id, node, 0), "Menu rejected");
                    int before = player.getInventoryManager().getContainer().getFreeSlots();
                    require(player.getDialogueManager().getDialogueStep() + 1 == node, "Menu continuation lost");
                    choose(player, option, true);
                    require(player.getDialogueManager().getDialogueId() == HistoricalNpcDialogues.ID_BASE + id
                            || player.getDialogueManager().getDialogueId() == -1, "Choice lost program identity");
                    require(player.getInventoryManager().getContainer().getFreeSlots() == before, "Flavour choice changed inventory");
                    options++;
                }
            } else if (Arrays.asList("NPC", "PLAYER", "STATEMENT").contains(fields[2])) {
                Player player = NpcDialogueAudit.player();
                require(DialogueManager.continueDialogue(player, HistoricalNpcDialogues.ID_BASE + id, node, 0), "Page rejected");
                require(player.getOpenInterfaceId() >= 0, "Page did not open");
                require(head.getInt(player.getDialogueManager()) == id, "Program ID replaced NPC portrait identity");
                pages++;
            }
        }
        System.out.println("NPC dialogue checks passed: " + quests + " quest routes, "
                + HistoricalNpcDialogues.npcIds().size() + " programs, " + options + " choices, " + pages
                + " pages, native/legacy Continue and selection, Cook start/hand-in/rewards.");
        System.exit(0);
    }

    private static void choose(Player player, int option, boolean nativePackets) {
        int count = player.getOpenInterfaceId() == 2459 ? 2 : player.getOpenInterfaceId() == 2469 ? 3
                : player.getOpenInterfaceId() == 2480 ? 4 : 5;
        int legacy = new int[] {0, 0, 2461, 2471, 2482, 2494}[count] + option - 1;
        if (nativePackets) widget(player, InterfaceBridge.translate(legacy));
        else require(player.getDialogueManager().handleOptionButton(legacy), "Legacy option rejected");
    }

    private static void advance(Player player, boolean nativePackets) {
        // Quest XP normally emits a Discord level-up event. Keep this offline
        // fixture from notifying the configured relay while awarding rewards.
        boolean reward = player.getDialogueManager().getDialogueId() == 278
                && player.getQuestState(2) == 9 && player.getDialogueManager().getDialogueStep() == 4;
        if (reward) player.isBot = true;
        try { advancePacket(player, nativePackets); }
        finally { if (reward) player.isBot = false; }
    }

    private static void advancePacket(Player player, boolean nativePackets) {
        if (!nativePackets) { new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null)); return; }
        int group = InterfaceBridge.translateGroup(player.getOpenInterfaceId());
        int child = group >= 64 && group <= 67 ? group - 61 : group >= 241 && group <= 244 ? group - 238
                : group == 210 ? 1 : group >= 211 && group <= 214 ? group - 209 : -1;
        require(child >= 0, "No Continue for group " + group);
        widget(player, group << 16 | child);
    }

    private static void widget(Player player, int packed) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.putShort((short)-1).putInt(packed).flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(payload)));
    }

    private static void require(boolean condition, String message) { if (!condition) throw new AssertionError(message); }

    private static final class Fixture implements AutoCloseable {
        final Player player;
        final Socket client = new Socket();
        final SocketChannel transport;
        Fixture() throws Exception {
            try (ServerSocketChannel listener = ServerSocketChannel.open()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress()); client.setSoTimeout(10);
                transport = listener.accept();
            }
            player = NpcDialogueAudit.player();
            Field socket = Player.class.getDeclaredField("socketChannel"); socket.setAccessible(true); socket.set(player, transport);
            player.setOutboundCipher(new IsaacCipher(new int[4]));
        }
        String text() throws Exception {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(); byte[] buffer = new byte[8192];
            try { int count; while ((count = client.getInputStream().read(buffer)) != -1) bytes.write(buffer, 0, count); }
            catch (SocketTimeoutException drained) { }
            return new String(bytes.toByteArray(), "ISO-8859-1");
        }
        public void close() throws Exception { transport.close(); client.close(); }
    }
}
