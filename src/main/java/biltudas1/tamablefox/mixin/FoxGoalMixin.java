package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import biltudas1.tamablefox.ai.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(Fox.class)
public abstract class FoxGoalMixin {

    @Inject(
        method = "registerGoals",
        at = @At("TAIL")
    )
    private void tamableFox$registerGoals(
            CallbackInfo ci
    ) {
        Fox fox = (Fox)(Object)this;

        GoalSelector goalSelector =
                ((MobAccessor) fox).getGoalSelector();

        goalSelector.removeAllGoals(goal ->
                goal.getClass().getSimpleName().equals("SleepGoal")
        );

        goalSelector.removeAllGoals(goal ->
                goal.getClass().getSimpleName().equals("PerchAndSearchGoal")
        );

        goalSelector.addGoal(
                4,
                new FollowOwnerGoal(fox, 1.2D)
        );
    }
}
