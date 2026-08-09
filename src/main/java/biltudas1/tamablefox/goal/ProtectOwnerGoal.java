package biltudas1.tamablefox.goal;

import java.util.List;

import biltudas1.tamablefox.mixin.FoxAccessor;
import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

public class ProtectOwnerGoal extends Goal {

    private final Fox fox;
    private final double range;

    public ProtectOwnerGoal(
            Fox fox,
            double range
    ) {
        this.fox = fox;
        this.range = range;
    }

    @Override
    public boolean canUse() {

        ServerPlayer owner = getTrustedPlayer();

        if (owner == null) {
            return false;
        }

        List<Mob> mobs = fox.level()
            .getEntitiesOfClass(
                Mob.class,
                owner.getBoundingBox()
                    .inflate(range)
            );

        for (Mob mob : mobs) {

            if (
                mob.isAlive() &&
                mob.getTarget() == owner
            ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void start() {

        ServerPlayer owner = getTrustedPlayer();

        if (owner == null) {
            return;
        }

        List<Mob> mobs = fox.level()
            .getEntitiesOfClass(
                Mob.class,
                owner.getBoundingBox()
                    .inflate(range)
            );

        for (Mob mob : mobs) {

            if (
                mob.isAlive() &&
                mob.getTarget() == owner
            ) {

                ((FoxStateAccessor) fox)
                    .tamableFox$setState(
                        FoxState.FOLLOW
                    );

                fox.setSitting(false);

                fox.setTarget(mob);

                break;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {

        return fox.getTarget() != null
            && fox.getTarget().isAlive();
    }

    private ServerPlayer getTrustedPlayer() {

      if (!(fox.level() instanceof ServerLevel level))
        return null;

      return ((FoxAccessor) fox)
        .invokeGetTrustedEntities()
        .map(ref -> ref.getEntity(level, LivingEntity.class))
        .filter(entity -> entity instanceof ServerPlayer)
        .map(entity -> (ServerPlayer) entity)
        .findFirst()
        .orElse(null);
    }
}