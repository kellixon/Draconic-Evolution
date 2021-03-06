package com.brandon3055.draconicevolution.network.ccnetwork;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import com.brandon3055.brandonscore.handlers.HandHelper;
import com.brandon3055.brandonscore.lib.ChatHelper;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.api.itemconfig.IConfigurableItem;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.items.tools.Magnet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class ServerPacketHandler implements ICustomPacketHandler.IServerPacketHandler {
    @Override
    public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer handler) {
        switch (packet.getType()) {
            case 1:
                toggleDislocators(sender);
                break;
            case 2:
                changeToolProfile(sender, packet.readBoolean());
                break;
        }
    }

    private void toggleDislocators(EntityPlayer player) {
        List<ItemStack> dislocators = new ArrayList<>();

        for (ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isEmpty() && stack.getItem() == DEFeatures.magnet) {
                dislocators.add(stack);
            }
        }

        for (ItemStack stack : player.inventory.offHandInventory) {
            if (!stack.isEmpty() && stack.getItem() == DEFeatures.magnet) {
                dislocators.add(stack);
            }
        }

        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == DEFeatures.magnet) {
                    dislocators.add(stack);
                }
            }
        }

        for (ItemStack dislocator : dislocators) {
            Magnet.toggleEnabled(dislocator);
            boolean enabled = Magnet.isEnabled(dislocator);
            ChatHelper.indexedTrans(player, "chat.item_dislocator_" + (enabled ? "activate" : "deactivate") + ".msg", -30553055);
        }
    }

    private void changeToolProfile(EntityPlayer player, boolean armor) {
        if (armor) {
            int i = 0;
            NonNullList<ItemStack> armorInventory = player.inventory.armorInventory;
            for (int i1 = armorInventory.size() - 1; i1 >= 0; i1--) {
                ItemStack stack = armorInventory.get(i1);
                if (!stack.isEmpty() && stack.getItem() instanceof IConfigurableItem) {
                    ToolConfigHelper.incrementProfile(stack);
                    int newProfile = ToolConfigHelper.getProfile(stack);
                    String name = ToolConfigHelper.getProfileName(stack, newProfile);
                    ChatHelper.indexedTrans(player, new TextComponentTranslation("config.de.armor_profile_" + i + ".msg").getFormattedText() + " " + name, -30553045 + i);
                }
                i++;
            }
        }
        else {
            ItemStack stack = HandHelper.getMainFirst(player);
            if (!stack.isEmpty() && stack.getItem() instanceof IConfigurableItem) {
                ToolConfigHelper.incrementProfile(stack);
            }
        }
    }
}