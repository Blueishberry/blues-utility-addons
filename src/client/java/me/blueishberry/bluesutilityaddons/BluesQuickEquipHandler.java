package me.blueishberry.bluesutilityaddons;

import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BluesQuickEquipHandler {
    public static void toggleArmor(net.minecraft.client.MinecraftClient client, EquipmentSlot slot) {
        assert client.player != null;
        ItemStack equippedStack = client.player.getEquippedStack(slot);


        if(!equippedStack.isEmpty()) {
            if(isInventoryFull(client)) {
                client.player.sendMessage(Text.literal("Your inventory is full!").formatted(Formatting.RED), true);
                return;
            }
            for(int i = 9; i <= 44; i++) {
                if (client.player.getInventory().getStack(i).isEmpty()) {
                    client.interactionManager.clickSlot(0, getEquipmentSlotForArmor(slot), 0, SlotActionType.PICKUP, client.player);
                    client.interactionManager.clickSlot(0, i, 0, SlotActionType.PICKUP, client.player);
                    return;
                }
            }
        } else {
            for(int i = 9; i <= 44; i++) {
                ItemStack stack = client.player.getInventory().getStack(i);

                if(isArmorForSlot(stack, slot)) {
                    client.interactionManager.clickSlot(0, i, 0, SlotActionType.PICKUP, client.player);
                    client.interactionManager.clickSlot(0, getEquipmentSlotForArmor(slot), 0, SlotActionType.PICKUP, client.player);
                    return;
                }
            }
        }
    }

    private static boolean isInventoryFull(net.minecraft.client.MinecraftClient client) {
        assert client.player != null;
        for(int i = 9; i <= 35; i++) {
            if (client.player.getInventory().getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }



    private static boolean isArmorForSlot(ItemStack stack, EquipmentSlot slot) {
        if (stack == null || stack.isEmpty()) return false;

        var player = MinecraftClient.getInstance().player;
        if (player != null && player.getPreferredEquipmentSlot(stack) == slot) {
            return true;
        }

        if (stack.getItem() instanceof EquipmentSlotProvider provider) {
            return provider.getPreferredEquipmentSlot(player, stack) == slot;
        }

        return false;
    }


    private static int getEquipmentSlotForArmor(EquipmentSlot slot) {
        return switch(slot) {
            case HEAD -> 5; // Helmet slot
            case CHEST -> 6; // Chestplate slot
            case LEGS -> 7; // Leggings slot
            case FEET -> 8; // Boots slot
            default -> -1; // Invalid slot
        };
    }
}
