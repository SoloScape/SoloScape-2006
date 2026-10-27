import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.RomeoAndJulietQuest;

/** Regression checks for issue #6. Run from Server with its libraries on the classpath. */
public final class RomeoAndJulietDialogueChecks {
    private static final int QUEST = 13;
    private static final int ROMEO = 639;
    private static final int JULIET = 637;
    private static final int LETTER = 755;
    private static final int POTION = 756;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.freeToPlayWorld = false;
        ServerSettings.membershipRequirementMode = 1;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();

        // Both NPCs must respond at every stage, not just when they advance the quest.
        for (int stage = 0; stage <= 8; stage++) {
            Player player = player(stage);
            require(DialogueManager.startDialogue(player, ROMEO),
                    "Romeo untalkable at quest stage " + stage);
            require(DialogueManager.startDialogue(player, JULIET),
                    "Juliet untalkable at quest stage " + stage);
            require(player.getQuestState(QUEST) == stage,
                    "Opening an optional conversation changed stage " + stage);
        }

        // The same must hold when a player still carries the item to hand over.
        Player letterCarrier = player(3);
        letterCarrier.getInventoryManager().addItem(new ItemStack(LETTER));
        require(DialogueManager.startDialogue(letterCarrier, ROMEO),
                "Romeo must accept Juliet's letter");
        require(DialogueManager.startDialogue(letterCarrier, JULIET),
                "Juliet must remind the player to deliver her letter");
        Player potionCarrier = player(7);
        potionCarrier.getInventoryManager().addItem(new ItemStack(POTION));
        require(DialogueManager.startDialogue(potionCarrier, JULIET),
                "Juliet must accept the Cadava potion");

        // All three introductory answers should work instead of a missing-dialogue stub.
        for (int option = 1; option <= 3; option++) {
            Player player = player(0);
            require(DialogueManager.startDialogue(player, ROMEO), "Romeo introduction");
            require(DialogueManager.continueDialogue(player, ROMEO, 2, 0), "Romeo choices");
            require(DialogueManager.continueDialogue(player, ROMEO, 3, option),
                    "Romeo introduction option " + option + " not handled");
            if (option == 1) {
                require(player.getDialogueManager().getDialogueStep() == 9,
                        "Seeing Juliet should continue to Romeo's request");
            } else if (option == 2) {
                require(player.getDialogueManager().isDialogueInactive(),
                        "Declining to help should finish the dialogue");
            }
            require(player.getQuestState(QUEST) == 0,
                    "Introductory option prematurely started the quest");
        }

        // Romeo's question menu loops back, without advancing the quest.
        for (int option = 1; option <= 3; option++) {
            Player player = player(2);
            require(DialogueManager.continueDialogue(player, ROMEO, 19, 0),
                    "Romeo question menu not handled");
            require(DialogueManager.continueDialogue(player, ROMEO, 20, option),
                    "Romeo question option " + option + " not handled");
            if (option == 3) {
                require(player.getDialogueManager().isDialogueInactive(), "Goodbye should exit");
            } else {
                int next = option == 1 ? 21 : 23;
                require(player.getDialogueManager().getDialogueStep() == next - 1,
                        "Romeo question jumps to the wrong branch");
                require(DialogueManager.continueDialogue(player, ROMEO, next, 0),
                        "Romeo answer not implemented");
                require(player.getDialogueManager().getDialogueStep() == 18,
                        "Romeo answer did not return to the question menu");
            }
            require(player.getQuestState(QUEST) == 2,
                    "Asking Romeo questions should not advance the quest");
        }

        // Father Lawrence stage's previously missing choices are all reachable.
        for (int option = 1; option <= 4; option++) {
            Player player = player(4);
            require(DialogueManager.startDialogue(player, ROMEO), "Romeo stage 4 menu");
            require(DialogueManager.continueDialogue(player, ROMEO, 2, option),
                    "Romeo stage 4 option " + option + " not handled");
            if (option == 1 || option == 3) {
                int next = option == 1 ? 32 : 33;
                require(player.getDialogueManager().getDialogueStep() == next - 1,
                        "Romeo stage 4 jumped to the wrong branch");
                require(DialogueManager.continueDialogue(player, ROMEO, next, 0),
                        "Romeo stage 4 answer not implemented");
                require(player.getDialogueManager().getDialogueStep() == 0,
                        "Romeo stage 4 answer did not return to the menu");
            }
            require(player.getQuestState(QUEST) == 4,
                    "Romeo stage 4 chat changed quest progress");
        }

        // Handing over Juliet's letter must keep Romeo available immediately.
        Player deliveringLetter = player(3);
        deliveringLetter.getInventoryManager().addItem(new ItemStack(LETTER));
        RomeoAndJulietQuest script = new RomeoAndJulietQuest(0);
        for (int step = 1; step <= 25; step++) {
            require(script.handleNpcDialogue(deliveringLetter, ROMEO, step, 0,
                    deliveringLetter.getQuestState(QUEST)), "Letter hand-off step " + step);
        }
        require(deliveringLetter.getQuestState(QUEST) == 4, "Letter did not advance to stage 4");
        require(!deliveringLetter.getInventoryManager().containsItem(LETTER),
                "Romeo did not take the letter");
        require(DialogueManager.startDialogue(deliveringLetter, ROMEO),
                "Romeo untalkable after accepting the letter");

        // Likewise Juliet must remain talkable after the potion hand-off.
        Player deliveringPotion = player(7);
        deliveringPotion.getInventoryManager().addItem(new ItemStack(POTION));
        for (int step = 1; step <= 14; step++) {
            require(script.handleNpcDialogue(deliveringPotion, JULIET, step, 0,
                    deliveringPotion.getQuestState(QUEST)), "Potion hand-off step " + step);
        }
        require(deliveringPotion.getQuestState(QUEST) == 8, "Potion did not advance to stage 8");
        require(!deliveringPotion.getInventoryManager().containsItem(POTION),
                "Juliet did not take the potion");
        require(DialogueManager.startDialogue(deliveringPotion, JULIET),
                "Juliet untalkable after accepting the potion");
        require(DialogueManager.startDialogue(deliveringPotion, ROMEO),
                "Romeo not available for quest completion");

        System.out.println("Romeo & Juliet dialogue checks passed");
    }

    private static Player player(int stage) throws Exception {
        Player player = NpcDialogueAudit.player();
        player.setQuestState(QUEST, stage);
        return player;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
