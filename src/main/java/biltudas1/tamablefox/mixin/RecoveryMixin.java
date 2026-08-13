package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(Fox.class)
public abstract class RecoveryMixin {

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void tamableFox$recoveryTick(
        CallbackInfo ci
    ) {

        Fox fox = (Fox)(Object)this;

        boolean trusted =
            ((FoxAccessor) fox)
                .invokeGetTrustedEntities()
                .findAny()
                .isPresent();

        if (!trusted) {
            return;
        }

        FoxStateAccessor stateAccessor =
            (FoxStateAccessor)fox;

        /*
         * Enter recovery mode
         */
        if (
            stateAccessor.tamableFox$getState()
                != FoxState.RECOVERING
            &&
            fox.getHealth()
                <= fox.getMaxHealth() * 0.25F
        ) {

            stateAccessor.tamableFox$setPreviousState(
                stateAccessor.tamableFox$getState()
            );

            stateAccessor.tamableFox$setState(
                FoxState.RECOVERING
            );

            fox.setTarget(null);

            fox.getNavigation().stop();


            ((FoxAccessor)fox)
                .invokeSetSleeping(true);

            fox.addEffect(
                new MobEffectInstance(
                    MobEffects.REGENERATION,
                    Integer.MAX_VALUE,
                    1,
                    false,
                    true,
                    true
                )
            );
        }

        /*
         * Recovery behavior
         */
        if (
            stateAccessor.tamableFox$getState()
                == FoxState.RECOVERING
        ) {

            fox.setTarget(null);

            fox.getNavigation().stop();

            ((FoxAccessor)fox)
                .invokeSetSleeping(true);

            if (
                !fox.hasEffect(
                    MobEffects.REGENERATION
                )
            ) {

                fox.addEffect(
                    new MobEffectInstance(
                        MobEffects.REGENERATION,
                        Integer.MAX_VALUE,
                        1,
                        false,
                        true,
                        true
                    )
                );
            }

            /*
             * Leave recovery mode
             */
            if (
                fox.getHealth()
                    >= fox.getMaxHealth() * 0.75F
            ) {

                fox.removeEffect(
                    MobEffects.REGENERATION
                );

                ((FoxAccessor)fox)
                    .invokeSetSleeping(false);

                stateAccessor.tamableFox$setState(
                    stateAccessor
                        .tamableFox$getPreviousState()
                );
            }
        }
    }
}