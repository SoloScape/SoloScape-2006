import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.TutorialQuest;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.ButtonClickPacketHandler;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.QuestJournalPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Regression checks for revision-443 Tutorial Island interface lessons. */
public final class TutorialClassicFlowAuditChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        require(ServerSettings.tutorialSkipPromptEnabled,
                "Tutorial Island skip prompt is disabled by default");

        Npc instructor = new Npc(944);
        instructor.setIndex(0);
        instructor.setPosition(new Position(3105, 9508, 0));
        World.getNpcs()[0] = instructor;
        Npc chicken = new Npc(951);
        chicken.setIndex(1);
        chicken.setPosition(new Position(3140, 3087, 0));
        World.getNpcs()[1] = chicken;

        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        TutorialQuest tutorial = new TutorialQuest(0);
        InterfaceActionPacketHandler interfaces = new InterfaceActionPacketHandler();
        QuestJournalPacketHandler sidebar = new QuestJournalPacketHandler();
        ButtonClickPacketHandler buttons = new ButtonClickPacketHandler();

        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(100);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                player.setIndex(1);
                player.setEncodedIndex(32768);
                socketField.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3073, 3090, 0));
                player.setRunEnergyPercent(100);

                player.setQuestState(0, 2);
                tutorial.handleNpcDialogue(player, 945, 1, 0, 2);
                require(drain(client).contains("Would you like to skip tutorial?"),
                        "RuneScape Guide did not offer the tutorial skip");
                tutorial.handleNpcDialogue(player, 945, 2, 2, 2);
                require(Boolean.TRUE.equals(player.getAttributes().get("tutorialSkipDeclined")),
                        "Declining the skip was not remembered");
                drain(client);
                tutorial.handleNpcDialogue(player, 945, 1, 0, 2);
                require(!drain(client).contains("Would you like to skip tutorial?"),
                        "RuneScape Guide repeated the skip prompt after it was declined");

                player.setQuestState(0, 22);
                tutorial.refreshQuestJournal(player, 22);
                String emoteIntro = drain(client);
                require(emoteIntro.contains("Emotes.")
                                && emoteIntro.contains("flashing")
                                && emoteIntro.contains("emotes"),
                        "Stage 22 did not teach the Emotes tab");
                sidebar.handle(player, packet(ClientPackets.SIDEBAR_ACK, new byte[] {12}));
                drain(client);
                require(player.getQuestState(0) == 23,
                        "Opening Emotes did not advance to stage 23");
                require(!player.isTutorialRunUnlocked(),
                        "Run unlocked before the emote/settings lessons");

                clickInterface(interfaces, player, (464 << 16) | 1);
                drain(client);
                require(player.isTutorialEmoteCompleted(),
                        "Performing an emote did not complete the Emotes lesson");
                require(!player.isTutorialRunUnlocked(),
                        "Run unlocked before opening player settings");
                sidebar.handle(player, packet(ClientPackets.SIDEBAR_ACK, new byte[] {11}));
                String runLesson = drain(client);
                require(player.isTutorialRunSettingsOpened(),
                        "Opening player settings did not unlock the Run lesson");
                require(player.isTutorialRunUnlocked(),
                        "Run remained locked after player settings were opened");
                require(runLesson.contains("running shoe") || runLesson.contains("run button"),
                        "Run-button instruction was not displayed");
                buttons.handleButton(player, 153);
                drain(client);
                require(player.getQuestState(0) == 24 && player.getMovementQueue().isRunning(),
                        "Run button did not complete the historical running lesson");

                player.setQuestState(0, 42);
                player.getInventoryManager().getContainer().clear();
                player.getEquipmentManager().getContainer().clear();
                player.getInventoryManager().addItem(new ItemStack(1205, 1));
                player.getEquipmentManager().equipFromInventorySlot(0);
                drain(client);
                require(player.getQuestState(0) == 42
                                && player.getEquipmentManager().getItemIdAtSlot(3) != 1205,
                        "Dagger could bypass the Equipment Stats lesson");

                clickInterface(interfaces, player, (387 << 16) | 24);
                drain(client);
                require(player.isTutorialEquipmentStatsOpened(),
                        "Equipment Stats button did not complete its Tutorial sub-step");
                require(player.getOpenInterfaceId() == 15106,
                        "Equipment Stats window did not open");
                player.getEquipmentManager().equipFromInventorySlot(0);
                drain(client);
                require(player.getQuestState(0) == 43
                                && player.getEquipmentManager().getItemIdAtSlot(3) == 1205,
                        "Dagger did not equip after the Equipment Stats lesson");

                player.setQuestState(0, 65);
                tutorial.refreshQuestJournal(player, 65);
                String magicLesson = drain(client);
                require(magicLesson.contains("second in from the"),
                        "Wind Strike instruction still points to the wrong 443 spell slot");

                player.setQuestState(0, 68);
                tutorial.refreshQuestJournal(player, 68);
                String welcome = drain(client);
                require(welcome.contains("Lumbridge Guide or one of the Tutors")
                                && welcome.contains("Home spell."),
                        "Lumbridge arrival message does not match the 443-era guidance");
                require(player.getQuestState(0) == 1,
                        "Completion check did not finish Tutorial Island");
            }
        }
        System.out.println("Classic Tutorial flow audit checks passed (emotes, run, equipment stats, magic, arrival). ");
        System.exit(0);
    }

    private static void clickInterface(InterfaceActionPacketHandler handler,
            Player player, int packedWidgetId) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt(packedWidgetId);
        payload.flip();
        handler.handle(player, new IncomingPacket(
                ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(payload)));
    }

    private static IncomingPacket packet(int opcode, byte[] payload) {
        return new IncomingPacket(opcode, payload.length,
                PacketBuffer.wrapReader(ByteBuffer.wrap(payload)));
    }

    private static String drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
        } catch (SocketTimeoutException drained) { }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
