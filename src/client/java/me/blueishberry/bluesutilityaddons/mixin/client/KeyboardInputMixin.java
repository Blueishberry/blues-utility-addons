package me.blueishberry.bluesutilityaddons.mixin.client;

import me.blueishberry.bluesutilityaddons.BluesUtilityAddonsClient;
import me.blueishberry.bluesutilityaddons.config.BluesUtilityConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {

    @Unique
    private boolean isToggledSneaking = false;
    @Unique
    private boolean wasToggleKeyPressed = false;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(BluesUtilityConfig.INSTANCE.toggleSneak) {
            MinecraftClient client = MinecraftClient.getInstance();

            if(client.player == null) return;

            boolean isToggleKeyPressed = BluesUtilityAddonsClient.toggleSneakKey.isPressed();



            if(isToggleKeyPressed && !wasToggleKeyPressed) {
                isToggledSneaking = !isToggledSneaking;

                if(isToggledSneaking) {
                    client.player.setSneaking(true);
                } else {
                    client.player.setSneaking(false);
                }
            }

            PlayerInput current = client.player.input.playerInput;
            if (current == null) current = PlayerInput.DEFAULT;

            boolean sneaking = isToggledSneaking || client.options.sneakKey.isPressed();
            client.player.input.playerInput = new PlayerInput(
                    current.forward(),
                    current.backward(),
                    current.left(),
                    current.right(),
                    current.jump(),
                    sneaking,
                    current.sprint()
            );

            wasToggleKeyPressed = isToggleKeyPressed;
        }
    }
}
