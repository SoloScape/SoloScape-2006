package com.rs2.model.dialogue;

import com.rs2.model.player.Player;
import com.rs2.util.GameUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** Dialogue-only graphs; quest, clue, shop and travel handlers retain priority. */
public final class HistoricalNpcDialogues {
    public static final int ID_BASE = 50000;
    private static final Map<Integer, Program> PROGRAMS = load();

    private HistoricalNpcDialogues() { }

    public static Set<Integer> npcIds() {
        return Collections.unmodifiableSet(PROGRAMS.keySet());
    }

    public static boolean start(Player player, int npcId) {
        if (npcId == 278 && player.getQuestState(2) != 1) return false;
        if (npcId == 300 && player.getQuestState(14) != 0) return false;
        Program program = PROGRAMS.get(npcId);
        if (program == null) return false;
        DialogueManager manager = player.getDialogueManager();
        manager.setDialogueId(ID_BASE + npcId);
        manager.setDialogueNpcId(npcId);
        manager.setDialogueType(0);
        return display(player, program, program.start, 0);
    }

    public static boolean continueDialogue(Player player, int dialogueId, int step, int option) {
        Program program = PROGRAMS.get(dialogueId - ID_BASE);
        if (program == null) return false;
        player.getDialogueManager().setDialogueNpcId(dialogueId - ID_BASE);
        return display(player, program, step, option);
    }

    private static boolean display(Player player, Program program, int step, int option) {
        DialogueManager manager = player.getDialogueManager();
        // Bound control-only jumps so malformed data cannot stall the game tick.
        for (int hops = 0; hops < 64; hops++) {
            Node node = program.nodes.get(step);
            if (node == null || "END".equals(node.kind)) {
                player.getPacketSender().closeInterfaces();
                manager.resetDialogueState();
                return true;
            }
            if ("JUMP".equals(node.kind)) { step = node.next[0]; continue; }
            if ("RANDOM".equals(node.kind)) {
                step = node.next[GameUtil.randomInclusive(node.next.length - 1)]; continue;
            }
            if ("OPTIONS".equals(node.kind)) {
                if (option >= 1 && option <= node.next.length) {
                    step = node.next[option - 1]; option = 0; continue;
                }
                manager.showOptions(node.text);
                manager.setNextDialogueStep(step);
                return true;
            }
            if ("NPC".equals(node.kind)) manager.showNpcDialogue(node.text, 591);
            else if ("PLAYER".equals(node.kind)) {
                switch (node.text.length) {
                    case 1: manager.showPlayerOneLineDialogue(node.text[0], 591); break;
                    case 2: manager.showPlayerTwoLineDialogue(node.text[0], node.text[1], 591); break;
                    case 3: manager.showPlayerThreeLineDialogue(node.text[0], node.text[1], node.text[2], 591); break;
                    case 4: manager.showPlayerFourLineDialogue(node.text[0], node.text[1], node.text[2], node.text[3], 591); break;
                    default: throw new IllegalStateException("Invalid dialogue page");
                }
            } else manager.showStatement(node.text);
            int next = node.next[0];
            if ("END".equals(program.nodes.get(next).kind)) manager.finishDialogue();
            else manager.setNextDialogueStep(next);
            return true;
        }
        throw new IllegalStateException("Dialogue control loop for NPC " + program.npcId);
    }

    private static Map<Integer, Program> load() {
        Map<Integer, Program> programs = new LinkedHashMap<Integer, Program>();
        try {
            int lineNumber = 0;
            for (String line : Files.readAllLines(Paths.get("data/content/npcDialogues2006.tsv"), StandardCharsets.UTF_8)) {
                lineNumber++;
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] fields = line.split("\t", -1);
                if (fields.length < 5) throw new IllegalArgumentException("Invalid row " + lineNumber);
                int npcId = Integer.parseInt(fields[0]);
                int nodeId = Integer.parseInt(fields[1]);
                if (npcId < 0 || npcId >= 6433) throw new IllegalArgumentException("Invalid NPC " + npcId);
                Program program = programs.get(npcId);
                if (program == null) { program = new Program(npcId); programs.put(npcId, program); }
                String[] targets = fields[3].isEmpty() ? new String[0] : fields[3].split(",");
                int[] next = new int[targets.length];
                for (int i = 0; i < next.length; i++) next[i] = Integer.parseInt(targets[i]);
                if ("START".equals(fields[2])) {
                    if (program.start >= 0 || nodeId != -1 || next.length != 1)
                        throw new IllegalArgumentException("Invalid start for NPC " + npcId);
                    program.start = next[0]; continue;
                }
                String[] text = Arrays.copyOfRange(fields, 4, fields.length);
                if (program.nodes.put(nodeId, new Node(fields[2], text, next)) != null)
                    throw new IllegalArgumentException("Duplicate node for NPC " + npcId);
            }
            for (Program program : programs.values()) program.validate();
        } catch (IOException | IllegalArgumentException failure) {
            throw new IllegalStateException("Unable to load historical NPC dialogues", failure);
        }
        return Collections.unmodifiableMap(programs);
    }

    private static final class Program {
        final int npcId;
        int start = -1;
        final Map<Integer, Node> nodes = new LinkedHashMap<Integer, Node>();
        Program(int npcId) { this.npcId = npcId; }
        void validate() {
            if (!nodes.containsKey(start)) throw new IllegalArgumentException("Missing start for NPC " + npcId);
            for (Node node : nodes.values()) {
                for (int next : node.next) if (!nodes.containsKey(next))
                    throw new IllegalArgumentException("Missing target for NPC " + npcId);
                boolean page = "NPC".equals(node.kind) || "PLAYER".equals(node.kind) || "STATEMENT".equals(node.kind);
                boolean options = "OPTIONS".equals(node.kind);
                boolean jump = "JUMP".equals(node.kind);
                boolean random = "RANDOM".equals(node.kind);
                boolean end = "END".equals(node.kind);
                if (!(page || options || jump || random || end)
                        || (page && (node.text.length < 1 || node.text.length > 4 || node.next.length != 1))
                        || (options && (node.text.length < 2 || node.text.length > 5 || node.next.length != node.text.length))
                        || (jump && node.next.length != 1) || (random && node.next.length < 1)
                        || (end && node.next.length != 0))
                    throw new IllegalArgumentException("Invalid node for NPC " + npcId);
            }
        }
    }

    private static final class Node {
        final String kind;
        final String[] text;
        final int[] next;
        Node(String kind, String[] text, int[] next) { this.kind = kind; this.text = text; this.next = next; }
    }
}
