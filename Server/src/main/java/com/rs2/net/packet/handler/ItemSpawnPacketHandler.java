package com.rs2.net.packet.handler;

import com.rs2.ServerSettings;
import com.rs2.model.item.ItemStack;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.GrandExchangeManager;
import com.rs2.model.player.Player;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketHandler;
import com.rs2.util.GameUtil;

public final class ItemSpawnPacketHandler
implements PacketHandler {
    @Override
    public final void handle(Player player, IncomingPacket incomingPacket) {
        if (player.isActionLocked()) {
            return;
        }
        switch (incomingPacket.getOpcode()) {
            case 19: {
                Object value;
                int reader = incomingPacket.getReader().readSignedShort();
                // Only the command's dedicated picker can grant free test items.
                if (player.getOpenInterfaceId() == 19103) {
                    if (ItemDefinition.isDefined(reader)) {
                        String name = ItemDefinition.forId(reader).getName();
                        if (name != null && !name.trim().isEmpty() && !name.equalsIgnoreCase("null")
                                && !name.equals("# + id")) {
                            player.getInventoryManager().addItem(new ItemStack(reader, 1));
                        }
                    }
                    return;
                }
                if (reader < 0 || reader > 11883) {
                    return;
                }
                value = new ItemStack(reader, 1);
                if (reader == 995 || player.getOpenInterfaceId() != 18890 || !GrandExchangeManager.isExchangeableItem(reader)) break;
                if (((ItemStack)value).getDefinition().isMembersOnly() && reader != 7999 && reader != 8000) {
                    if (player.isMember()) {
                        if (ServerSettings.freeToPlayWorld) {
                            player.packetSender.sendGameMessage("You need to be in members world to access members content.");
                            return;
                        }
                    } else {
                        player.packetSender.sendGameMessage("You need a members account to access members content.");
                        return;
                    }
                }
                player.selectedGrandExchangeItemId = reader;
                player.selectedGrandExchangeQuantity = 1;
                player.selectedGrandExchangeUnitPrice = GrandExchangeManager.getGuidePrice(reader);
                String examine = ((ItemStack)value).getDefinition().getDescription();
                value = player;
                ((Player)value).packetSender.sendInterfaceItemModel(18938, player.selectedGrandExchangeItemId);
                ((Player)value).packetSender.sendInterfaceText(examine == null ? "" : examine, 18918);
                value = player;
                ((Player)value).packetSender.sendInterfaceText(GameUtil.formatNumber(GrandExchangeManager.getGuidePrice(reader)), 18919);
                value = player;
                ((Player)value).packetSender.sendInterfaceText("" + player.selectedGrandExchangeQuantity, 18920);
                value = player;
                ((Player)value).packetSender.sendInterfaceText(String.valueOf(GameUtil.formatNumber(player.selectedGrandExchangeUnitPrice)) + " coins", 18921);
                value = player;
                ((Player)value).packetSender.sendInterfaceText(String.valueOf(GameUtil.formatNumber(player.selectedGrandExchangeUnitPrice * player.selectedGrandExchangeQuantity)) + " coins", 18922);
            }
        }
    }
}

