package biltudas1.tamablefox.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.TamableFox;
import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import biltudas1.tamablefox.state.GuardPositionAccessor;

@Mixin(Mob.class)
public abstract class FoxInteractMixin {

    @Inject(
        method = "mobInteract",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tamableFox$mobInteract(
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir
    ) {

        if (!((Object) this instanceof Fox fox)) {
            return;
        }

        boolean trusted = ((FoxAccessor) fox)
                .invokeGetTrustedEntities()
                .map(EntityReference::getUUID)
                .anyMatch(uuid -> uuid.equals(player.getUUID()));

        if (!trusted) {
            return;
        }

        if (!player.isShiftKeyDown()) {

            FoxStateAccessor stateAccessor = (FoxStateAccessor) fox;

            if (stateAccessor.tamableFox$getState() == FoxState.FOLLOW) {

                stateAccessor.tamableFox$setState(
                    FoxState.GUARD
                );

                ((GuardPositionAccessor) fox)
                    .tamableFox$setGuardPos(
                        fox.blockPosition()
                    );

                fox.setSitting(true); // visual only
                fox.getNavigation().stop();
                fox.setTarget(null);

            } else {

                stateAccessor.tamableFox$setState(FoxState.FOLLOW);

                fox.setSitting(false); // visual only
            }

            TamableFox.LOGGER.info(
                "Fox state changed to {}",
                ((FoxStateAccessor) fox).tamableFox$getState()
            );

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}