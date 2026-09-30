import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.combat.CombatAction;
import com.rs2.model.combat.CombatManager;
import com.rs2.model.combat.WeaponInterfaceDefinition;
import com.rs2.model.combat.hit.HitDefinition;
import com.rs2.model.combat.hit.HitType;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import java.nio.ByteBuffer;

/** Native combat-tab toggles drive the real incoming-hit retaliation path. */
public final class AutoRetaliateChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        Interfaces.load();
        ItemDefinition.loadRevision443();
        NpcDefinition.loadDefinitions();
        QuestDefinition.loadDefinitions();
        Player defender = player("defender");
        int buttons = 0;
        for (int group = 75; group <= 93; group++) {
            for (Interfaces.Component component : Interfaces.group(group).values()) {
                if (component.actionType != 4
                        || !new String(component.getData(), "ISO-8859-1").contains("Auto Retaliate")) continue;
                buttons++;
                for (int opcode : new int[] {ClientPackets.INTERFACE_BUTTON,
                        ClientPackets.INTERFACE_OPERATIONS[0]}) {
                    defender.setAutoRetaliate(false);
                    click(defender, component.packedId, opcode, 0);
                    require(defender.isAutoRetaliate(), "Toggle did not enable: " + group);
                    click(defender, component.packedId, opcode, 0);
                    require(!defender.isAutoRetaliate(), "Toggle did not disable: " + group);
                    click(defender, component.packedId, opcode, 1);
                    if (opcode != ClientPackets.INTERFACE_BUTTON)
                        require(!defender.isAutoRetaliate(), "Invalid parameter toggled setting");
                }
            }
        }
        require(buttons == 19, "Missing combat tabs");
        Npc npc = new Npc(1);
        npc.setActive(true);
        npc.setPosition(new Position(3223, 3222, 0));
        Player attacker = player("attacker");
        for (Entity source : new Entity[] {npc, attacker}) {
            for (boolean enabled : new boolean[] {false, true}) {
                CombatManager.stopCombat(defender);
                defender.setAutoRetaliate(enabled);
                incoming(source, defender, HitType.NORMAL, false);
                require(defender.getCombatTarget() == (enabled ? source : null),
                        "Incoming attack did not respect toggle");
            }
            CombatManager.stopCombat(defender);
            defender.setAutoRetaliate(true);
            incoming(source, defender, HitType.POISON, false);
            incoming(source, defender, HitType.NORMAL, true);
            require(defender.getCombatTarget() == null, "Passive damage triggered retaliation");
            defender.setCombatTarget(attacker);
            incoming(source, defender, HitType.NORMAL, false);
            require(defender.getCombatTarget() == attacker, "Retaliation replaced existing target");
        }
        CombatManager.stopCombat(defender);
        System.out.println("Auto-retaliate checks passed (19 tabs, both packet forms, NPC/PvP hits, off, passive damage, existing target).");
        System.exit(0);
    }

    private static Player player(String name) {
        Player player = new Player(null);
        player.isBot = true;
        player.setEncodedIndex(32768);
        player.setUsername(name);
        player.setQuestState(0, 1);
        player.setPosition(new Position(3222, 3222, 0));
        player.setCurrentHitpoints(10);
        for (int bonus = 0; bonus < 12; bonus++) player.setCombatBonus(bonus, 0);
        player.getAttributes().put("canTakeDamage", true);
        return player;
    }

    private static void incoming(Entity attacker, Player defender, HitType type, boolean passive) {
        HitDefinition hit = new HitDefinition(WeaponInterfaceDefinition.UNARMED.getAttackStyles()[0],
                type, 0).setAlwaysHits(passive).setBlockAnimationEnabled(false);
        new CombatAction(attacker, defender, hit).applyHit();
    }

    private static void click(Player player, int widget, int opcode, int parameter) {
        ByteBuffer payload = ByteBuffer.allocate(opcode == ClientPackets.INTERFACE_BUTTON ? 4 : 6);
        payload.putInt(widget);
        if (opcode != ClientPackets.INTERFACE_BUTTON) payload.putShort((short) parameter);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(opcode, payload.remaining(), PacketBuffer.wrapReader(payload)));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
