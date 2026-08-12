package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.state.TrustedPlayerAccessor;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.ItemStack;

@Mixin(Fox.class)
public abstract class FoxPickupMixin {

    @Inject(
        method = "canHoldItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tamableFox$canHoldItem(
        ItemStack itemStack,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Fox fox = (Fox)(Object)this;

        if (
            ((TrustedPlayerAccessor) fox)
                .tamableFox$getTrustedPlayer()
            != null
        ) {
            cir.setReturnValue(false);
        }
    }
}