package com.rs2.net.packet.handler;

import com.rs2.ServerSettings;
import com.rs2.model.GameplayHelper;
import com.rs2.model.gameplay.partyroom.PartyRoomManager;
import com.rs2.model.player.Player;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketHandler;

public final class CloseInterfacePacketHandler
implements PacketHandler {
    @Override
    public final void handle(Player player, IncomingPacket incomingPacket) {
        GameplayHelper.declineTrade(player);
        PartyRoomManager.returnStagedChestItems(player);
        if (player.interfaceAction == "flour") {
            GameplayHelper.handleFlourDoughButton(player, 53204);
        }
        if ((player.interfaceAction == "duel" || player.interfaceAction == "duel2") && !player.isInDuelArena()) {
            if (player.getDuelSession().getOpponent() != null && player.getDuelSession().getOpponent().isRegistered()) {
                player.getDuelSession().getOpponent().getDuelController().resetDuel(true);
                player.getDuelSession().getOpponent().packetSender.sendGameMessage("Other played declined the duel.");
            }
            player.getDuelController().resetDuel(true);
        }
        player.getAttributes().put("isBanking", Boolean.FALSE);
        player.getAttributes().put("isShopping", Boolean.FALSE);
        player.interfaceAction = "";
        if (player.getQuestState(0) != 1) {
            if (ServerSettings.clientBuild == 443 && player.getOpenInterfaceId() == 15106) {
                // The native X button already closed the viewport and kept
                // the tutorial chatbox. A global close/reopen would make it flicker.
                player.setOpenInterfaceId(6179);
                player.setInventoryOverlayInterfaceId(0);
                return;
            }
            if (ServerSettings.clientBuild == 443 && player.getQuestState(0) == 0) {
                // Native revision-443 character design (group 269) accepts via
                // clientscript opcode 3103. It does not send the legacy 3651
                // button event, so this close-modal packet is the first server
                // event that tells us character creation was accepted.
                player.setAppearanceUpdateRequired(true);
                player.getUpdateState().setUpdateRequired(true);
                // The native 443 Accept script has already closed character
                // design locally. Packet 140 is not an interface-close packet
                // in this client, so do not send closeInterface(3559) here.
                player.setQuestState(0, 2);
                player.getQuestManager().refreshQuestJournal();
                // refreshQuestJournal() has just opened legacy 6179 (native
                // group 214). Keep that server-side interface state intact so
                // the first movement click does not have to restore it.
                return;
            }
            if (ServerSettings.clientBuild == 443 && player.getQuestState(0) == 2) {
                // A duplicate close-modal can arrive after the stage transition.
                // Keep the tutorial instructions open instead of globally closing.
                player.getQuestManager().refreshQuestJournal();
                return;
            }
            player.packetSender.closeInterfaces();
        }
        if (player.getQuestState(0) == 1) {
            player.packetSender.setSidebarInterface(3, 3213);
        }
        player.setOpenInterfaceId(0);
        if (player.queuedLevelUpSkillIds.size() > 0) {
            player.getSkillManager().showLevelUpInterface((Integer)player.queuedLevelUpSkillIds.get(0));
            player.queuedLevelUpSkillIds.remove(0);
        }
    }
}
