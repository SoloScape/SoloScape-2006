package com.rs2.model.quest.event;

import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.util.GameUtil;
import java.util.ArrayList;
import java.util.List;
import static com.rs2.model.quest.event.GublinchChristmasEvent.*;

// Dialogue supplied by the user from the 2006 Christmas event transcript.
// Reference: https://runescape.wiki/w/Transcript:2006_Christmas_event
// Each entry preserves one transcript speech or narration. Explicit body lines
// avoid incidental overflow pages; longer bodies still paginate without losing text.
// Factories keep authored text in method bodies for development HotSwap.
final class GublinchChristmasDialogue {
    private GublinchChristmasDialogue() {}
    static final int ITEM_SHARDS = 10081, ITEM_OTHER = 10082;
    static final int SEARCH_ENTRANCE = 10083, CAVE_EMPTY = 10084;

    // Steps below 100 are menus and handoff routing; each speech branch has a block
    // of 100 page steps. DialogueManager owns the page so movement cancellation and
    // stale Continue protection also apply to this event.
    static boolean handle(Player player, int id, int step, int option) {
        if (id != SHANTY_CLAWS && id != ITEM_SHARDS && id != ITEM_OTHER
                && id != SEARCH_ENTRANCE && id != CAVE_EMPTY && (id < 5046 || id > 5048)) return false;
        DialogueManager manager = player.getDialogueManager();
        if (step == 1) {
            if (id == ITEM_SHARDS) step = 1800;
            else if (id == ITEM_OTHER) step = 1900;
            else if (id == SEARCH_ENTRANCE) step = cagesRemaining(player) == 0 ? 2100 : 2000;
            else if (id == CAVE_EMPTY) step = 2200;
            else if (id >= 5046 && id <= 5048) step = 2300;
            else if (isComplete(player)) step = membersReward(player) && !treeClaimed(player) ? 70 : 1500;
            else if (cagesRemaining(player) == 0) {
                manager.showPlayerOneLineDialogue("I think I've finished.", 588);
                manager.setNextDialogueStep(60);
                return true;
            } else if (!started(player)) step = 100;
            else if (cagesRemaining(player) < 10) step = 400;
            else if (player.getInventoryManager().containsItem(SHARDS)) step = 300;
            else step = 200;
        }
        if (step == 60) step = player.getInventoryManager().getContainer().getFreeSlots() == 0
                && !player.ownsItem(REINDEER_HAT) ? 1100 : 1000;
        if (step == 70) {
            if (!membersReward(player)) step = 1300;
            else if (treeClaimed(player)) { manager.finishDialogue(); return true; }
            else step = player.getInventoryManager().getContainer().getFreeSlots() == 0 ? 1400 : 1200;
        }
        if (step == 80) {
            manager.setDialogueStep(80);
            manager.showFiveOptions("What are gublinch?", "Where are these gublinch?",
                    "What do I get for getting rid of these gublinch?",
                    "Can you just tell me in a plain and simple way what I have to do?", "Okay, thanks.");
            return true;
        }
        if (step == 81) {
            if (option < 1 || option > 5) { manager.finishDialogue(); return true; }
            step = option == 5 ? 900 : 400 + option * 100;
        }
        if (step == 90) {
            manager.setDialogueStep(90);
            manager.showThreeOptions("What will happen to the gublinch now?", "What will you do now?", "Okay, thanks.");
            return true;
        }
        if (step == 91) {
            if (option < 1 || option > 3) { manager.finishDialogue(); return true; }
            step = option == 3 ? 900 : 1500 + option * 100;
        }
        int branch = step / 100;
        Speech[] speeches;
        switch (branch) {
            case 1: speeches = intro();
                // HotSwap replaces method bodies but does not rerun static initializers.
                // Author this greeting here so body-line edits reach the running server.
                speeches[1] = new Speech(0, "'Raise the mainsail and hoist the anchor...' Eh! Oh, the\n"
                        + "names 'Shanty'; 'Shanty Claws' to a landlubber like ye.\n"
                        + "And oi've some terrible news for yer deck-swabbin' ears.");
                speeches[3] = new Speech(0, "'Splice the mainsail and shiver me timbers...' Ah yea, the\n"
                        + "news is of a disturbin' type so ye's best go ahead and\n"
                        + "prepare yerself, or the jelly in yer bones'll shrivel and\n"
                        + "yer'll end up a lifeless bag o' puss.");
                speeches[5] = new Speech(0, "It's the gublinch, ye see. The evil gublinch ain't too\n"
                        + "pleased, what with the recent snow. Right near their\n"
                        + "landlubbin' homes, it be. And these sweaty, smelly,\n"
                        + "warmth-loving critters, they's a tad sensitive to the cold.");
                speeches[6] = new Speech(0, "So they've childernapped the childerkins from hereabouts,\n"
                        + "forcin' 'em to stoke the fires in the gublinch caves!");
                speeches[7] = new Speech(1, "Childernapped the childerkins? Why, that sounds terrible!\n"
                        + "What can I do to help?");
                speeches[8] = new Speech(0, "You's be needin' to ice them gublinch. Freeze 'em good\n"
                        + "and proper, like. Then you's gonna have to get 'em into\n"
                        + "these 'ere cages so's we can ship 'em off someplace else!");
                speeches[10] = new Speech(0, "Ahhh, a hard-nosed dealer who talks straight, that's what\n"
                        + "I like. For yer 'ard effort, I'll be willin' ta compensate\n"
                        + "ye. How's ye fancy a festive hat with a fancy nose\n"
                        + "d\u00e9cor, what I got on a recent raid to the north?");
                speeches[18] = new Speech(0, "Aye, that's what I said, aye. And to give it ye, you'll\n"
                        + "need to be on a members' server. If yer interested,\n"
                        + "just start filling these 'ere cages with gublinch and, when\n"
                        + "they's filled, come see me for yer rewards.");
                speeches[19] = new Speech(1, "Hmmm, I'm not sure. I want to ask a few more\n"
                        + "questions first.");
                if (player.getEquipmentManager().containsItem(4202) || player.getEquipmentManager().containsItem(6465)) speeches[1] = charos()[0];
                break;
            case 2: speeches = repeat(); break;
            case 3: speeches = shardsTalk(); break;
            case 4: speeches = caged(); break;
            case 5: speeches = gublinch();
                speeches[1] = new Speech(0, "'Fire the port gun, stow the belayin' pin...let's haul\n"
                        + "anchor ye mornin'...' Ah... So yer back and asking about\n"
                        + "th' gublinch? They're foul goberlin-style beasties, what\n"
                        + "take pride in their stinky sweatyness. Though");
                speeches[2] = new Speech(0, "they's be little more than a pest. They're easy to get rid\n"
                        + "o' with them being so sensitive to th' chill. They's\n"
                        + "prefer more to live in them heated caverns and cook up a\n"
                        + "foul stink, than to do a second's worth o' work.");
                break;
            case 6: speeches = location(); break;
            case 7: speeches = rewards(); break;
            case 8: speeches = simple(); break;
            case 9: speeches = goodbye(); break;
            case 10: speeches = hat(); break;
            case 11: speeches = hatFull(); break;
            case 12: speeches = tree(); break;
            case 13: speeches = treeFree(); break;
            case 14: speeches = treeFull(); break;
            case 15: speeches = post(); break;
            case 16: speeches = future(); break;
            case 17: speeches = voyage(); break;
            case 18: speeches = useShards(); break;
            case 19: speeches = useOther(); break;
            case 20: speeches = notice(); break;
            case 21: speeches = emptySearch(); break;
            case 22: speeches = emptyCave(); break;
            case 23: speeches = child(); break;
            default: manager.finishDialogue(); return true;
        }
        List<Page> pages = pages(player, speeches);
        int page = step % 100;
        if (page >= pages.size()) {
            if (branch == 1) start(player);
            if (branch >= 1 && branch <= 8) return handle(player, id, 80, 0);
            if (branch == 10) return handle(player, id, 70, 0);
            if (branch >= 15 && branch <= 17) return handle(player, id, 90, 0);
            manager.finishDialogue();
            player.packetSender.closeInterfaces();
            return true;
        }
        Page current = pages.get(page);
        if (current.first && current.speech.kind == 2) {
            int item = branch == 10 ? REINDEER_HAT : branch == 12 ? WINTUMBER_TREE : -1;
            if (item != -1 && !giveReward(player, item)) {
                return handle(player, id, item == REINDEER_HAT ? 1100 : membersReward(player) ? 1400 : 1300, 0);
            }
        }
        if (id == SHANTY_CLAWS || id == ITEM_SHARDS || id == ITEM_OTHER) manager.setDialogueNpcId(SHANTY_CLAWS);
        if (current.speech.kind == 0) manager.showNpcDialogue(current.lines, 588);
        else if (current.speech.kind == 1) {
            if (current.lines.length == 1) manager.showPlayerOneLineDialogue(current.lines[0], 588);
            else manager.showPlayerTwoLineDialogue(current.lines[0], current.lines[1], 588);
        } else if (current.speech.text.startsWith("Shanty Claws shows") || current.speech.text.startsWith("Shanty Claws offers")) {
            manager.showTwoItemMessage(current.lines[0], current.lines.length > 1 ? current.lines[1] : "",
                    new ItemStack(-1, 1), new ItemStack(branch == 12 ? WINTUMBER_TREE : REINDEER_HAT));
            if (branch != 12) {
                player.packetSender.sendInterfaceModel(4957, 245, REINDEER_HAT);
                player.packetSender.sendInterfacePosition(4957, -10, 0);
            }
        } else manager.showStatement(current.lines);
        // Statement helpers finish non-tutorial dialogue. Restore our next page after rendering.
        manager.setDialogueStep(step);
        return true;
    }

    private static final class Page {
        final Speech speech;
        final String[] lines;
        final boolean first;
        Page(Speech speech, String[] lines, boolean first) { this.speech = speech; this.lines = lines; this.first = first; }
    }

    private static List<Page> pages(Player player, Speech[] speeches) {
        List<Page> pages = new ArrayList<Page>();
        String name = GameUtil.formatDisplayName(player.getUsername());
        for (Speech speech : speeches) {
            String text = speech.text.replace("[player name]", name).replace("<name>", name)
                    .replace("[lad/lass]", player.getGender() == 1 ? "lass" : "lad")
                    .replace("<number>", Integer.toString(cagesRemaining(player)));
            List<String> lines = new ArrayList<String>();
            if (text.contains("\n")) {
                // Authored body lines take precedence over automatic word wrapping.
                for (String line : text.split("\n")) lines.add(line);
            } else {
                String line = "";
                for (String word : text.split(" ")) {
                    if (!line.isEmpty() && line.length() + word.length() + 1 > 55) { lines.add(line); line = ""; }
                    line += (line.isEmpty() ? "" : " ") + word;
                }
                if (!line.isEmpty()) lines.add(line);
            }
            int capacity = speech.kind == 1 ? 2 : speech.kind == 2
                    && (speech.text.startsWith("Shanty Claws shows") || speech.text.startsWith("Shanty Claws offers")) ? 2 : 4;
            for (int i = 0; i < lines.size(); i += capacity) {
                pages.add(new Page(speech, lines.subList(i, Math.min(i + capacity, lines.size())).toArray(new String[0]), i == 0));
            }
        }
        return pages;
    }
    static final class Speech {
        final int kind; // 0: NPC, 1: player, 2: narration
        final String text;
        Speech(int kind, String text) { this.kind = kind; this.text = text; }
    }
    private static Speech[] intro() {
        return new Speech[] {
            new Speech(1, "What are you doing here?"),
            null, // Greeting is authored in handle() so edits survive development HotSwap.
            new Speech(1, "Well, I don't really swab the deck with... Oh, never\n"
                    + "mind, please do go on!"),
            null, // Authored body lines are supplied in handle() for development HotSwap.
            new Speech(1, "Er, I think I'll manage. Please continue; your story's\n"
                    + "'enthralling'."),
            null, // Authored body lines are supplied in handle() for development HotSwap.
            null, // Authored body lines are supplied in handle() for development HotSwap.
            null, // Authored body lines are supplied in handle() for development HotSwap.
            null, // Authored body lines are supplied in handle() for development HotSwap.
            new Speech(1, "What's in it for me?"),
            null, // Authored body lines are supplied in handle() for development HotSwap.
            new Speech(1, "Let's see it then."),
            new Speech(0, "Right you are!"),
            new Speech(2, "Shanty Claws shows you a strange looking hat with an\n"
                    + "accompanying nose decoration."),
            new Speech(1, "Is there anything else to sweeten the deal?"),
            new Speech(0, "Perhaps I can also tempt ye with a festive wintumber\n"
                    + "tree for yer player-owned house? it glitters fancifully\n"
                    + "in front of yer very eyes, though to plant it ye's\n"
                    + "needing to be on one them thar member's worlds and"),
            new Speech(0, "have a spot in yer garden in which plant it."),
            new Speech(1, "Eh? So I can plant this tree in the garden of my\n"
                    + "player-owned house?"),
            null, // Authored body lines are supplied in handle() for development HotSwap.
            null, // Authored body lines are supplied in handle() for development HotSwap.
        };
    }
    private static Speech[] charos() {
        return new Speech[] {
            new Speech(0, "'Raise the mainsail and hoist the anchor...' Eh!\n"
                    + "A fellow werewolf! I say; I never expected that, friend! Oh, the\n"
                    + "name's 'Shanty'; 'Shanty Claws' to a landlubber like ye.\n"
                    + "And oi've some terrible news for yer deck-swabbin' ears."),
        };
    }
    private static Speech[] gublinch() {
        return new Speech[] {
            new Speech(1, "What are gublinch?"),
            null, // Authored body lines are supplied in handle() for development HotSwap.
            null, // Authored body lines are supplied in handle() for development HotSwap.
        };
    }
    private static Speech[] location() {
        return new Speech[] {
            new Speech(1, "Where are these gublinch?"),
            new Speech(0, "'...haul the anchor, rig the mainsail...' Eh? Ah, th'\n"
                    + "gublinch have gone and nested themselves in, near the\n"
                    + "belching mountain."),
            new Speech(1, "D'you mean the volcano?"),
            new Speech(0, "Aye, [lad/lass], volcano is what I be meanin'. The\n"
                    + "south foot o' the same. They's be 'avin' a cavern\n"
                    + "there, but arm yerself wi' the chilly snowball ammo\n"
                    + "afore ye goes venturin' inside."),
        };
    }
    private static Speech[] rewards() {
        return new Speech[] {
            new Speech(1, "What do I get for getting rid of these gublinch?"),
            new Speech(0, "'...a brig's too good, so keelhaul him in the mornin'...'\n"
                    + "Ah, direct, ye are, and to the point. Well, me swarthy\n"
                    + "deck-swabber, perhaps I can tempt ye with a festive hat with\n"
                    + "a big nose d\u00e9cor, what I got on a recent raid in the"),
            new Speech(0, "north?"),
            new Speech(1, "Let's see it then."),
            new Speech(0, "Right you are!"),
            new Speech(2, "Shanty Claws shows you a strange-looking hat with an\n"
                    + "accompanying nose decoration."),
            new Speech(1, "Is there anything else to sweeten the deal?"),
            new Speech(0, "Perhaps I can also tempt ye with a festive wintumber\n"
                    + "tree for yer player-owned house? It glitters fancifully\n"
                    + "in front of yer very eyes, though to plant it ye's\n"
                    + "needing to be on one them thar members' worlds and"),
            new Speech(0, "have a spot in yer garden in which to plant it."),
            new Speech(1, "Eh? So I can plant this tree in the garden of my\n"
                    + "player-owned house?"),
            new Speech(0, "Aye, that's what I said, aye. And to give it ye, you'll\n"
                    + "need to be on a members' server. If yer interested,\n"
                    + "just start filling these 'ere cages and, when ten are\n"
                    + "filled, come to me for yer rewards."),
        };
    }
    private static Speech[] simple() {
        return new Speech[] {
            new Speech(1, "Can you just tell me in a plain and simple way what I\n"
                    + "have to do?"),
            new Speech(0, "Certainly, [player name]. Local children have been\n"
                    + "kidnapped by creatures called gublinch. The gublinch\n"
                    + "cave is on the south side of the volcano. The gublinch\n"
                    + "are sensitive to cold, so use snowballs to freeze 'em."),
            new Speech(0, "Ye'll find snowdrifts near the entrance to their cave.\n"
                    + "When ye's frozen a gublinch, bring it back 'ere an'\n"
                    + "place it in one o' these cages."),
            new Speech(0, "In return for filling all ten cages, I'll give ye a\n"
                    + "reindeer hat and a members' only wintumber tree that\n"
                    + "you can plant in th' garden o' yer player-owned house."),
        };
    }
    private static Speech[] goodbye() {
        return new Speech[] {
            new Speech(1, "Okay, thanks."),
        };
    }
    private static Speech[] repeat() {
        return new Speech[] {
            new Speech(1, "Hello again. What should I do?"),
            new Speech(0, "'Swab the decks and raise the top mast...' Ye be\n"
                    + "needin' to snow them gublinch. Freeze 'em good and\n"
                    + "proper, like. Then yer gonna have to get 'em into these\n"
                    + "'ere cages so's we can ship 'em off someplace else!"),
        };
    }
    private static Speech[] shardsTalk() {
        return new Speech[] {
            new Speech(1, "Hello again. What should I do?"),
            new Speech(0, "'Swab the decks and raise the top mast...' Oh, sorry\n"
                    + "there, ye be wantin' to know what to do, but yer\n"
                    + "already going great guns, shipmate! Keep up the good\n"
                    + "work, you old sea-dog, and get them gublinch shards"),
            new Speech(0, "into them thar cages!"),
        };
    }
    private static Speech[] caged() {
        return new Speech[] {
            new Speech(1, "Hello again. What should I do?"),
            new Speech(0, "'Swab the decks and raise the top mast...' Ye be\n"
                    + "wantin' to know what to do, but ye's already going\n"
                    + "great guns, shipmate! Keep up the good work, you old\n"
                    + "sea-dog, and get them gublinch shards into them thar\n"
                    + "cages! Ye be needin' ta freeze those gublinch wi'\n"
                    + "snowballs and get them in th' cages. You've got\n"
                    + "<number> gublinch left to cage, so you have!"),
        };
    }
    private static Speech[] useShards() {
        return new Speech[] {
            new Speech(0, "'...bring me home to a dry-dock and a bottle of\n"
                    + "hock...' Hey there, ye need to put them thar shards\n"
                    + "into a cage so's to restrain the gublinch!"),
        };
    }
    private static Speech[] useOther() {
        return new Speech[] {
            new Speech(0, "'...skimming the sea with a sailor's heart, lofty and\n"
                    + "high...' Hey, whad'ya think yer doin! Go shove that in\n"
                    + "someone else's face!"),
        };
    }
    private static Speech[] hatFull() {
        return new Speech[] {
            new Speech(0, "'...to exotic places with fair winds blowing in our\n"
                    + "faces...' Aarghhh! Me first mate, <name> - ye's done a\n"
                    + "grand job, [lad/lass], and stuffed them evil gublinch\n"
                    + "in them cages. Now, this salty sea-wolf would offer ye\n"
                    + "something to make yer eyes light up...if ye had the\n"
                    + "space to take it. Yer loaded up like a mule, though -\n"
                    + "so come back when ye has some space, ye load-loving\n"
                    + "landlubber."),
            new Speech(1, "Okay, so you want me to return when I have a spare\n"
                    + "inventory slot. Okay, will do!"),
        };
    }
    private static Speech[] hat() {
        return new Speech[] {
            new Speech(0, "'...to exotic places with fair winds blowing in our faces...'\n"
                    + "Aarghhh! Me first mate, [player name] - ye's done a grand job,\n"
                    + "[lad/lass], and stuffed them evil gublinch in them cages. Now,\n"
                    + "this salty sea-wolf can offer ye something to make yer"),
            new Speech(0, "eyes light up like ye's seen a treasure chest, brimful\n"
                    + "o' the bootiest booty ye ever did see."),
            new Speech(2, "Shanty Claws shows you a reindeer hat with a matching\n"
                    + "flashing nose."),
            new Speech(0, "If ye should happen to lose this, ye can pester Diango\n"
                    + "fer another. I've left him plenty of supplies so I\n"
                    + "have."),
            new Speech(1, "Oooh, thanks very much."),
        };
    }
    private static Speech[] treeFree() {
        return new Speech[] {
            new Speech(0, "'...polish ye cutlasses to make 'em rust-less...'\n"
                    + "Aarrghh, <name>. Now this salty old sea-wolf can make\n"
                    + "ye a prize, but ye's be needin' to take a trip to a\n"
                    + "members' server for it."),
        };
    }
    private static Speech[] treeFull() {
        return new Speech[] {
            new Speech(0, "'...polish ye cutlasses to make 'em rust-less...' Aarrghh,\n"
                    + "<name>. Now this salty old sea-wolf can make ye a prize, but ye's\n"
                    + "all loaded down like a caravel wi' a tall ship's booty. Come back\n"
                    + "when ye's not haulin' and low in the water with salvage."),
            new Speech(1, "Okay, so you want me to come back when I have free\n"
                    + "inventory space?"),
            new Speech(0, "Aye, shipmate, aye!"),
        };
    }
    private static Speech[] tree() {
        return new Speech[] {
            new Speech(0, "'...polish ye cutlasses to make 'em rust-less...'\n"
                    + "Aarrghh, <name>. Now this salty old sea-wolf can make\n"
                    + "ye a prize that'll make yer landlubbin' heart beat full\n"
                    + "bore."),
            new Speech(0, "Ye'll be needing a patch o' dirt for yer dry-docked\n"
                    + "ship for this, though."),
            new Speech(1, "Dry-docked ship? Oh, you mean a player-owned house?\n"
                    + "Okay, so I need to plant it in the garden?"),
            new Speech(0, "Aye, that's what ol' Shanty says. Let it fill yer heart\n"
                    + "wi' festive cheer."),
            new Speech(2, "Shanty Claws offers you a wintumber tree for your\n"
                    + "player-owned house."),
            new Speech(0, "Be off wi' thee now, then, [lad/lass]. Have some fun!\n"
                    + "Shanty wishes ye festive cheer."),
            new Speech(1, "Thanks very much, Shanty Claws."),
        };
    }
    private static Speech[] post() {
        return new Speech[] {
            new Speech(0, "'...and we'll sink another drink to our sailor ways...'\n"
                    + "Eh? Arrrrgh. If it ain't me first mate, <name>, again.\n"
                    + "Ye have me heartiest thanks fer sortin' them thar\n"
                    + "gublinch, mate. If ye needs another festive hat or\n"
                    + "tree, get yerself along to Diango - I've left supplies\n"
                    + "wi' him, so I have."),
        };
    }
    private static Speech[] future() {
        return new Speech[] {
            new Speech(1, "What will happen to the gublinch now?"),
            new Speech(0, "'...skimming the sea with a sailor's heart, lofty and\n"
                    + "high...' Arrgh, you be here again then, shipmate...and\n"
                    + "well you can ask about them thar gublinch. But a-fear ye\n"
                    + "not - they's be disposed of well, mark my words. And a"),
            new Speech(0, "sweaty, swarthy bunch o' critters they'll be in their\n"
                    + "new home, far from here!"),
        };
    }
    private static Speech[] voyage() {
        return new Speech[] {
            new Speech(1, "What will you do now?"),
            new Speech(0, "I'll probably go on a long sea voyage and compose some\n"
                    + "more shanties! Shanty by name, shanty by nature!"),
            new Speech(0, "'...rounding the cape, with good hope in our heart, that\n"
                    + "we'll find treasure for our chests...' Arggh me swarthy\n"
                    + "landlubber, I's be sailin' away afore long, matey, an'\n"
                    + "striking up a tune for me shipmates; with a wholesome"),
            new Speech(0, "shanty coursing the rigging, old Shanty Claws will make\n"
                    + "his way. Wringing a salty sea song from the adventurers\n"
                    + "we'll survive, the people we'll meet and the treasure\n"
                    + "we'll find."),
        };
    }
    private static Speech[] child() {
        return new Speech[] {
            new Speech(0, "Thanks so much for saving us from those stinky\n"
                    + "gublinch. They were awful! I hope Shanty gave you\n"
                    + "somefing cool for your efforts."),
        };
    }
    private static Speech[] notice() {
        return new Speech[] {
            new Speech(2, "You search the strange structure and find a notice\n"
                    + "attached."),
            new Speech(2, "*DANGER: GUBLINCH* This 'ere notice be warnin' all\n"
                    + "landlubbers o' these 'ere gublinch, what lie in wait in\n"
                    + "their stinky cavern below. Any swashbucklin' sailor or\n"
                    + "cutlass-wieldin' pirate wi' an eye fer treasure should\n"
                    + "seek out Shanty Claws (sailor), Musa Point, On the\n"
                    + "Dock. Thankee kindly."),
        };
    }
    private static Speech[] emptyCave() {
        return new Speech[] {
            new Speech(2, "The cavern is empty of gublinch now; there's no need to\n"
                    + "go down there."),
        };
    }
    private static Speech[] emptySearch() {
        return new Speech[] {
            new Speech(2, "You search the strange structure but find nothing."),
        };
    }
    static final String[] SHANTIES = {
        "'Sailing on the sea...happy as can be...'",
        "'...to exotic places...fair winds in our faces...'",
        "'...and we'll sink a drink to our sailor ways...'",
        "'...polish ye cutlass's makes 'em rust-less...'",
        "'...a brig's too good, so keelhaul him in the mornin'...'",
    };
}
