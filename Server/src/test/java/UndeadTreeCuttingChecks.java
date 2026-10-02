import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.combat.CombatAction;
import com.rs2.model.combat.CombatManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.woodcutting.UndeadTreeCutting;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;

/** Runs real chopping cycles against cache definitions without starting a server. */
public final class UndeadTreeCuttingChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.woodcuttingEnabled = true;
        ServerSettings.freeToPlayWorld = false;
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.xpRate = 1;
        ServerSettings.progressiveXpMode = 0;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        Npc tree = new Npc(5208);
        tree.setSize(tree.getDefinition().getSize());
        tree.setPosition(new Position(3100, 3340, 0));
        Player p = player();
        p.getInventoryManager().getContainer().setItem(0, new ItemStack(1359));
        blocked(p, tree, "ordinary axe");
        p.getInventoryManager().getContainer().setItem(0, new ItemStack(10491));
        p.getSkillManager().getCurrentLevels()[18] = 17;
        blocked(p, tree, "17 Slayer");
        p.getSkillManager().getCurrentLevels()[18] = 18;
        p.getSkillManager().getCurrentLevels()[8] = 34;
        blocked(p, tree, "34 Woodcutting");
        p.getSkillManager().getCurrentLevels()[8] = 35;
        ServerSettings.freeToPlayWorld = true;
        blocked(p, tree, "free world");
        ServerSettings.freeToPlayWorld = false;
        ServerSettings.woodcuttingEnabled = false;
        blocked(p, tree, "disabled skill");
        ServerSettings.woodcuttingEnabled = true;
        for (int i = 1; i < 28; i++) p.getInventoryManager().getContainer().setItem(i, new ItemStack(1511));
        blocked(p, tree, "full inventory");
        p = player();
        p.getEquipmentManager().getContainer().setItem(3, new ItemStack(10491));
        // A better ordinary axe must never override the blessed axe on undead trees.
        p.getInventoryManager().getContainer().setItem(0, new ItemStack(1359));
        UndeadTreeCutting.start(p, tree);
        require(p.getActiveCycleEvent() instanceof UndeadTreeCutting, "equipped axe did not start");
        CycleEventContainer cycle = new CycleEventContainer(p, p.getActiveCycleEvent(), 4);
        double xp = p.getSkillManager().getExperience()[8];
        for (int i = 0; i < 1000 && !p.getInventoryManager().containsItem(10490); i++) cycle.execute();
        require(p.getInventoryManager().getContainer().getItemAmount(10490) == 1, "no single twig reward");
        require(p.getSkillManager().getExperience()[8] == xp + 5, "incorrect XP reward");
        require(cycle.isActive() && !tree.isDead() && tree.getNpcId() == 5208, "tree depleted");
        p.getEquipmentManager().getContainer().setItem(3, null);
        cycle.execute();
        require(!cycle.isActive(), "axe removal did not cancel");
        p = player();
        p.getInventoryManager().getContainer().setItem(0, new ItemStack(10491));
        UndeadTreeCutting.start(p, tree);
        cycle = new CycleEventContainer(p, p.getActiveCycleEvent(), 4);
        p.nextActionSequence();
        cycle.execute();
        require(!cycle.isActive() && !p.getInventoryManager().containsItem(10490), "cancelled action rewarded");
        p = player();
        p.getInventoryManager().getContainer().setItem(0, new ItemStack(10491));
        UndeadTreeCutting.start(p, tree);
        require(p.getActiveCycleEvent() instanceof UndeadTreeCutting, "carried axe did not start");
        cycle = new CycleEventContainer(p, p.getActiveCycleEvent(), 4);
        p.setPosition(new Position(3090, 3340, 0));
        cycle.execute();
        require(!cycle.isActive() && !p.getInventoryManager().containsItem(10490), "distant action rewarded");
        proximityAttackChecks();
        System.out.println("Undead tree requirements, reward, persistence and cancellation checks passed.");
        System.exit(0);
    }

    private static Player player() throws Exception {
        Player p = new Player(null);
        p.setSize(1);
        java.lang.reflect.Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        java.nio.channels.SocketChannel transport = java.nio.channels.SocketChannel.open();
        transport.close();
        socket.set(p, transport);
        p.setOutboundCipher(new IsaacCipher(new int[4]));
        p.setQuestState(0, 1);
        p.setPosition(new Position(3099, 3340, 0));
        p.getSkillManager().getCurrentLevels()[8] = 35;
        p.getSkillManager().getCurrentLevels()[18] = 18;
        p.getSkillManager().getExperience()[8] = 25000;
        return p;
    }

    @SuppressWarnings("unchecked")
    private static void proximityAttackChecks() throws Exception {
        CombatManager.initialize();
        java.lang.reflect.Field pending = CombatManager.class.getDeclaredField("pendingActions");
        pending.setAccessible(true);
        java.util.List<CombatAction> hits = (java.util.List<CombatAction>) pending.get(CombatManager.getInstance());
        Player passerby = player();
        passerby.getSkillManager().getCurrentLevels()[8] = 1;
        passerby.getSkillManager().getCurrentLevels()[18] = 1;
        passerby.getSkillManager().getExperience()[3] = 14000000;
        passerby.setCurrentHitpoints(99);
        // No axe, chopping event or Animal Magnetism progress is needed.
        World.getPlayers()[1] = passerby;
        World.getPlayers()[2] = player();
        World.getPlayers()[2].setCurrentHitpoints(10);
        try {
            for (int id : new int[]{5207, 5208}) {
                Npc tree = new Npc(id);
                tree.setSize(tree.getDefinition().getSize());
                tree.setPosition(new Position(3100, 3340, 0));
                int totalDamage = 0;
                for (int swipe = 0; swipe < 40; swipe++) {
                    tree.process();
                    require(hits.size() == 1, "ambient swipe missing or hit multiple players: " + id);
                    CombatAction hit = hits.remove(0);
                    require(hit.getTarget() == passerby && hit.getDamage() >= 0 && hit.getDamage() <= 3,
                            "incorrect ambient target or damage");
                    int hp = passerby.getCurrentHitpoints();
                    hit.applyHit();
                    require(passerby.getCurrentHitpoints() == hp - hit.getDamage(), "ambient damage was not applied");
                    totalDamage += hit.getDamage();
                    passerby.setCurrentHitpoints(99);
                    require(tree.getUpdateState().getAnimationId() == 73, "missing tree swipe animation");
                    require(tree.getCombatTarget() == null && passerby.getCombatTarget() == null,
                            "ambient swipe started combat");
                    for (int tick = 0; tick < 3; tick++) tree.process();
                    require(hits.isEmpty(), "swipe cooldown ignored");
                }
                require(totalDamage > 0, "ambient swipes never caused damage");
                tree.setActive(false);
                tree.process();
                require(hits.isEmpty(), "inactive tree attacked");
                tree.setActive(true);
                tree.setDead(true);
                tree.process();
                require(hits.isEmpty(), "dead tree attacked");
            }
            World.getPlayers()[2] = null;
            Npc tree = new Npc(5208);
            tree.setSize(1);
            tree.setPosition(new Position(3100, 3340, 0));
            for (Position position : new Position[]{new Position(3098, 3340, 0),
                    new Position(3099, 3340, 1), new Position(3100, 3340, 0)}) {
                passerby.setPosition(position);
                tree.process();
                require(hits.isEmpty(), "out-of-reach, other-plane or overlapping player attacked");
            }
            passerby.setPosition(new Position(3099, 3340, 0));
            passerby.setDead(true);
            tree.process();
            require(hits.isEmpty(), "dead player attacked");
            passerby.setDead(false);
            tree = new Npc(1226);
            tree.setSize(1);
            tree.setPosition(new Position(3100, 3340, 0));
            tree.process();
            require(hits.isEmpty(), "unrelated NPC gained ambient attack");
            require(passerby.getActiveCycleEvent() == null
                    && !passerby.getInventoryManager().containsItem(10490), "ambient swipe started chopping");
        } finally {
            World.getPlayers()[1] = null;
            World.getPlayers()[2] = null;
            hits.clear();
        }
        System.out.println("Undead tree ambient damage, range, cooldown and lifecycle checks passed.");
    }

    private static void blocked(Player p, Npc tree, String reason) {
        UndeadTreeCutting.start(p, tree);
        require(!(p.getActiveCycleEvent() instanceof UndeadTreeCutting), "allowed " + reason);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
