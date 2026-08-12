package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(LivingEntity.class)
public abstract class FoxEnemyMixin {

    @Inject(
        method = "canBeSeenAsEnemy",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tamableFox$canBeSeenAsEnemy(
        CallbackInfoReturnable<Boolean> cir
    ) {

        if (
            !((Object)this instanceof Fox fox)
        ) {
            return;
        }

        if (
            ((FoxStateAccessor)fox)
                .tamableFox$getState()
                == FoxState.RECOVERING
        ) {

            cir.setReturnValue(false);
        }
    }
}