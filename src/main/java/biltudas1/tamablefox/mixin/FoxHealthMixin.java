package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(Fox.class)
public abstract class FoxHealthMixin {

    @Unique
    private boolean tamableFox$huntGoalsRemoved = false;

    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void tamableFox$applyHealth(
            CallbackInfo ci
    ) {
        Fox fox = (Fox)(Object)this;

        boolean trusted =
            ((FoxAccessor) fox)
                .invokeGetTrustedEntities()
                .findAny()
                .isPresent();

        if (
            trusted
            && !tamableFox$huntGoalsRemoved
        ) {

            FoxGoalAccessor goals =
                (FoxGoalAccessor) fox;

            ((MobAccessor) fox)
                .getTargetSelector()
                .removeGoal(
                    goals.tamableFox$getLandTargetGoal()
                );

            ((MobAccessor) fox)
                .getTargetSelector()
                .removeGoal(
                    goals.tamableFox$getTurtleEggTargetGoal()
                );

            ((MobAccessor) fox)
                .getTargetSelector()
                .removeGoal(
                    goals.tamableFox$getFishTargetGoal()
                );

            tamableFox$huntGoalsRemoved = true;
        }

        if (
            trusted
            && fox.getAttribute(
                Attributes.MAX_HEALTH
            ).getBaseValue() < 80.0D
        ) {

            if (
                fox.getAttribute(
                    Attributes.MAX_HEALTH
                ) != null
            ) {

                fox.getAttribute(
                    Attributes.MAX_HEALTH
                ).setBaseValue(80.0D);

                fox.setHealth(80.0F);
            }

            if (
                fox.getAttribute(
                    Attributes.ATTACK_DAMAGE
                ) != null
            ) {

                fox.getAttribute(
                    Attributes.ATTACK_DAMAGE
                ).setBaseValue(6.0D);
            }
        }
    }
}