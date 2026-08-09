package biltudas1.tamablefox.goal;

import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import biltudas1.tamablefox.state.GuardPositionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

public class ReturnToGuardGoal extends Goal {

    private final Fox fox;

    public ReturnToGuardGoal(Fox fox) {
        this.fox = fox;
    }

    @Override
    public boolean canUse() {

        if (
            ((FoxStateAccessor) fox)
                .tamableFox$getState()
            != FoxState.GUARD
        ) {
            return false;
        }

        if (fox.getTarget() != null) {
            return false;
        }

        BlockPos guardPos =
            ((GuardPositionAccessor) fox)
                .tamableFox$getGuardPos();

        if (guardPos == null) {
            return false;
        }

        if (fox.isSitting()) {
            return false;
        }

        return !fox.blockPosition()
            .closerThan(guardPos, 1.5D);
    }

    @Override
    public void start() {

        BlockPos guardPos =
            ((GuardPositionAccessor) fox)
                .tamableFox$getGuardPos();

        if (guardPos == null) {
            return;
        }

        fox.getNavigation().moveTo(
            guardPos.getX() + 0.5D,
            guardPos.getY(),
            guardPos.getZ() + 0.5D,
            1.2D
        );
    }

    @Override
    public void tick() {

        BlockPos guardPos =
            ((GuardPositionAccessor) fox)
                .tamableFox$getGuardPos();

        if (guardPos == null) {
            return;
        }

        if (
            fox.blockPosition()
                .closerThan(
                    guardPos,
                    1.5D
                )
        ) {

            fox.getNavigation().stop();

            fox.setTarget(null);
            
            fox.setDeltaMovement(
                0.0D,
                0.0D,
                0.0D
            );

            fox.setSitting(true);
        }
    }

    @Override
    public boolean canContinueToUse() {

        if (
            ((FoxStateAccessor) fox)
                .tamableFox$getState()
            != FoxState.GUARD
        ) {
            return false;
        }

        if (fox.getTarget() != null) {
            return false;
        }

        return !fox.isSitting();
    }

    @Override
    public void stop() {

        fox.getNavigation().stop();
    }
}