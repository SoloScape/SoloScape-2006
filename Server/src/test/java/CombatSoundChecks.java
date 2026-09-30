import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Entity;
import com.rs2.model.combat.*;
import com.rs2.model.combat.attack.BaseCombatAttack;
import com.rs2.model.combat.hit.HitDefinition;
import com.rs2.model.combat.hit.HitType;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.AudioIds443;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.channels.*;

/** Verifies combat sounds through real revision 443 outbound packets. */
public final class CombatSoundChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        for (WeaponInterfaceDefinition weapon : new WeaponInterfaceDefinition[] {
                WeaponInterfaceDefinition.DAGGER, WeaponInterfaceDefinition.SLASH_SWORD,
                WeaponInterfaceDefinition.STAFF}) {
            for (AttackStyleDefinition style : weapon.getAttackStyles()) {
                int expected = weapon == WeaponInterfaceDefinition.STAFF ? 394
                        : weapon == WeaponInterfaceDefinition.DAGGER
                        ? (style.getAttackBonusType() == AttackBonusType.SLASH ? 401 : 403)
                        : (style.getAttackBonusType() == AttackBonusType.STAB ? 398 : 396);
                require(weapon.getAttackSoundId(style) == expected, "Wrong weapon/style sound");
                require(AudioIds443.sound(expected) >= 0, "Weapon sound unavailable in 443");
            }
        }
        try (Peer attacker = new Peer(); Peer target = new Peer()) {
            for (int gender : new int[] {0, 1}) {
                target.player.setGender(gender);
                for (int armour : new int[] {-1, 1117, 1103}) {
                    target.player.getEquipmentManager().getContainer().setItem(4,
                            armour < 0 ? null : new com.rs2.model.item.ItemStack(armour));
                    int block = armour < 0 ? 820 : armour == 1117 ? 410 : 414;
                    require(target.player.getBlockSoundId() == block, "Wrong armour block sound");
                    for (int damage : new int[] {0, 5}) {
                        target.player.getUpdateState().reset();
                        HitDefinition hit = new HitDefinition(WeaponInterfaceDefinition.UNARMED.getAttackStyles()[0],
                                HitType.NORMAL, damage);
                        new CombatAction(attacker.player, target.player, hit).applyHitUpdate();
                        int expected = damage > 0 ? (gender == 1 ? 73 : 69) : block;
                        sound(attacker.drain(), expected, 0, "PvP attacker hit/block");
                        sound(target.drain(), expected, 0, "PvP defender hit/block");
                    }
                }
                new DeathAnimationTask(0, 2304, target.player, attacker.player).execute();
                sound(attacker.drain(), gender == 1 ? 71 : 70, 0, "PvP death heard by attacker");
                sound(target.drain(), gender == 1 ? 71 : 70, 0, "Player death");
            }
            for (int id : new int[] {1, 41, 81, 86, 87, 100}) {
                Npc npc = new Npc(id);
                NpcDefinition def = npc.getDefinition();
                System.out.println(def.getName() + ": attack=" + npc.getAttackSoundId()
                        + " hit=" + npc.getHitSoundId() + " death=" + npc.getDeathSoundId());
                require(AudioIds443.sound(npc.getAttackSoundId()) >= 0, "Missing NPC attack: " + id);
                require(AudioIds443.sound(npc.getHitSoundId()) >= 0, "Missing NPC hit: " + id);
                require(AudioIds443.sound(npc.getDeathSoundId()) >= 0, "Missing NPC death: " + id);
                if (id == 1) {
                    require(npc.getHitSoundId() == 72 && npc.getDeathSoundId() == 70,
                            "Male NPC uses female hurt/death sounds");
                }
                attack(npc, target.player, -1);
                sound(target.drain(), npc.getAttackSoundId(), 0, "NPC attack timing");
                attack(attacker.player, npc, 403);
                sound(attacker.drain(), 403, 0, "Player attack timing");
                new CombatAction(attacker.player, npc, new HitDefinition(
                        WeaponInterfaceDefinition.UNARMED.getAttackStyles()[0], HitType.NORMAL, 5)).applyHitUpdate();
                sound(attacker.drain(), npc.getHitSoundId(), 0, "NPC hit");
                new DeathAnimationTask(0, def.getDeathAnimationId(), npc, attacker.player).execute();
                sound(attacker.drain(), npc.getDeathSoundId(), 0, "NPC death timing");
            }
            for (Entity victim : new Entity[] {target.player, new Npc(81)}) {
                for (boolean splash : new boolean[] {false, true}) {
                    victim.getUpdateState().reset();
                    HitDefinition spell = new HitDefinition(ServerSettings.MAGIC_ATTACK_STYLE,
                            HitType.NORMAL, splash ? 0 : 5)
                            .setImpactSoundId(1001)
                            .setGraphic(new com.rs2.model.animation.GraphicEffect(splash ? 85 : 92, 100));
                    new CombatAction(attacker.player, victim, spell).applyHitUpdate();
                    byte[] heard = attacker.drain();
                    if (splash) {
                        require(heard.length == 0, "Splash played a hurt/block/successful impact sound");
                    } else {
                        require(heard.length == 12, "Successful spell lost hurt or impact sound");
                        sound(java.util.Arrays.copyOfRange(heard, 0, 6), victim.isPlayer()
                                ? target.player.getHitSoundId() : ((Npc)victim).getHitSoundId(), 0, "Spell hurt");
                        sound(java.util.Arrays.copyOfRange(heard, 6, 12), 1001, 0, "Spell impact");
                    }
                    if (victim.isPlayer()) {
                        byte[] defender = target.drain();
                        if (splash) {
                            require(defender.length == 0, "Defender heard a successful splash impact");
                        } else {
                            require(defender.length == 12, "Defender lost successful spell audio");
                            sound(java.util.Arrays.copyOfRange(defender, 0, 6), target.player.getHitSoundId(), 0, "Defender spell hurt");
                            sound(java.util.Arrays.copyOfRange(defender, 6, 12), 1001, 0, "Defender spell impact");
                        }
                    }
                }
            }
        }
        System.out.println("Combat sound checks passed (weapon styles, armour, genders, PvP hit/block/death, six NPCs, timing, spell success/splash).");
        System.exit(0);
    }

    private static void attack(Entity attacker, Entity target, int soundId) {
        BaseCombatAttack attack = new BaseCombatAttack(attacker, target) {
            public CombatType getCombatType() { return CombatType.MELEE; }
            public int getAttackRange() { return 1; }
            public void prepare() { }
        };
        attack.setAnimationId(422);
        attack.setAttackSoundId(soundId);
        attack.execute(null);
    }

    private static void sound(byte[] bytes, int legacyId, int delay, String context) {
        int mapped = AudioIds443.sound(legacyId);
        require(mapped >= 0, context + ": asset missing");
        require(bytes.length == 6, context + ": expected one sound packet, got " + bytes.length + " bytes");
        require((bytes[1] & 255) == (mapped >> 8) && (bytes[2] & 255) == (mapped & 255)
                && bytes[3] == 1 && (bytes[4] & 255) == (delay >> 8)
                && (bytes[5] & 255) == (delay & 255), context + ": wrong ID/loop count/delay");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static final class Peer implements AutoCloseable {
        final Player player;
        final Socket client = new Socket();
        final ServerSocketChannel listener = ServerSocketChannel.open();
        final SocketChannel transport;
        Peer() throws Exception {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(30);
            transport = listener.accept();
            player = new Player(null);
            player.setIndex(1);
            player.setEncodedIndex(32769);
            Field socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);
            socket.set(player, transport);
            player.setOutboundCipher(new IsaacCipher(new int[4]));
            player.getAttributes().put("canTakeDamage", true);
        }
        byte[] drain() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[4096];
            try {
                int count;
                while ((count = client.getInputStream().read(bytes)) >= 0) out.write(bytes, 0, count);
            } catch (SocketTimeoutException done) { }
            return out.toByteArray();
        }
        public void close() throws Exception {
            transport.close(); client.close(); listener.close();
        }
    }
}
