package me.blueishberry.bluesutilityaddons;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.blueishberry.bluesutilityaddons.config.BluesUtilityConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluesUtilityAddonsClient implements ClientModInitializer {

    private static final String category = "category.bluesutilityaddons";

    public static KeyBinding toggleSneakKey;
    public static KeyBinding helmetKey;
    public static KeyBinding chestplateKey;
    public static KeyBinding leggingsKey;
    public static KeyBinding bootsKey;

    private static String lastSender = null;
    private static final Pattern whisperPattern = Pattern.compile("^(.*) whispers to you:.*");


	@Override
	public void onInitializeClient() {

        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!
        // IMPORTANT: I KNOW THIS CODE IS SUPER DUPER MESSY!

        BluesUtilityConfig.load();

        // Initiate the keybindings

        toggleSneakKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bluesutilityaddons.togglesneak",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        helmetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bluesutilityaddons.helmet",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_1,
                category
        ));

        chestplateKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bluesutilityaddons.chestplate",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_2,
                category
        ));

        leggingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bluesutilityaddons.leggings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_3,
                category
        ));

        bootsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bluesutilityaddons.boots",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_4,
                category
        ));


        // Quick Equip crap
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player == null || client.interactionManager == null || !BluesUtilityConfig.INSTANCE.quickEquip) return;

            if(helmetKey.wasPressed()) {
                BluesQuickEquipHandler.toggleArmor(client, EquipmentSlot.HEAD);
            }

            if(chestplateKey.wasPressed()) {
                BluesQuickEquipHandler.toggleArmor(client, EquipmentSlot.CHEST);
            }

            if(leggingsKey.wasPressed()) {
                BluesQuickEquipHandler.toggleArmor(client, EquipmentSlot.LEGS);
            }

            if(bootsKey.wasPressed()) {
                BluesQuickEquipHandler.toggleArmor(client, EquipmentSlot.FEET);
            }
        });

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
            String text = message.getString();
            Matcher matcher = whisperPattern.matcher(text);
            if (matcher.find()) {
                lastSender = matcher.group(1);
            }
        });

        ClientCommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess) -> {
            commandDispatcher.register(ClientCommandManager.literal("r")
                    .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes(context -> {
                                if(!BluesUtilityConfig.INSTANCE.replier) {

                                    return 0;
                                }
                                if (lastSender == null) {
                                    MinecraftClient.getInstance().player.sendMessage(Text.literal("No one has whispered to you yet."), false);
                                    return 0;
                                }

                                String msg = StringArgumentType.getString(context, "message");
                                String command = "msg " + lastSender + " " + msg;
                                MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);

                                return 1;
                            })
                    )
            );
        }));
    }
}