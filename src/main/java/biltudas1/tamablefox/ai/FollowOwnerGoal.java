package biltudas1.tamablefox.ai;

import java.util.EnumSet;

import biltudas1.tamablefox.mixin.FoxAccessor;
import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

public class FollowOwnerGoal extends Goal {

  private final Fox fox;
  private final double speed;

  public FollowOwnerGoal(Fox fox, double speed) {
    this.fox = fox;
    this.speed = speed;

    this.setFlags(EnumSet.of(
      Goal.Flag.MOVE,
      Goal.Flag.LOOK
    ));
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

  private boolean isFollowing() {
      return ((FoxStateAccessor) fox)
          .tamableFox$getState()
          == FoxState.FOLLOW;
  }

  private boolean tryTeleportNearPlayer(
          ServerPlayer player
  ) {

      for (int i = 0; i < 20; i++) {

          int offsetX =
                  fox.getRandom().nextInt(7) - 3;

          int offsetZ =
                  fox.getRandom().nextInt(7) - 3;

          int x =
                  player.blockPosition().getX()
                  + offsetX;

          int z =
                  player.blockPosition().getZ()
                  + offsetZ;

          int y =
                  player.blockPosition().getY();

          BlockPos pos =
                  new BlockPos(x, y, z);

          BlockPos groundPos =
                  pos.below();

          if (
                  !fox.level()
                          .getBlockState(groundPos)
                          .isSolidRender()
          ) {
              continue;
          }

          if (
                  !fox.level()
                          .getBlockState(pos)
                          .isAir()
          ) {
              continue;
          }

          if (
                  !fox.level()
                          .getBlockState(pos.above())
                          .isAir()
          ) {
              continue;
          }

          fox.teleportTo(
                  x + 0.5D,
                  y,
                  z + 0.5D
          );

          fox.fallDistance = 0;

          return true;
      }

      return false;
  }

  @Override
  public boolean canUse() {

    if (!this.isFollowing()) {
      return false;
    }

    ServerPlayer player = getTrustedPlayer();

    if (player == null)
      return false;

    return fox.distanceToSqr(player) > 12 * 12;
  }

  @Override
  public boolean canContinueToUse() {

    if (!this.isFollowing()) {
      return false;
    }

    ServerPlayer player = getTrustedPlayer();

    if (player == null)
      return false;

    return fox.distanceToSqr(player) > 5 * 5;
  }

  @Override
  public void tick() {

    if (!this.isFollowing()) {
        fox.getNavigation().stop();
        return;
    }

    ServerPlayer player = getTrustedPlayer();

    if (player == null)
        return;

    if (
        !fox.isLeashed()
        && fox.distanceToSqr(player) > 40 * 40
    ) {

        tryTeleportNearPlayer(player);
        return;
    }

    fox.getLookControl().setLookAt(player);

    double distanceSq = fox.distanceToSqr(player);

    double moveSpeed;

    if (distanceSq > 20 * 20) {
        moveSpeed = speed * 3.0D;
    } else {
        moveSpeed = speed;
    }

    fox.getNavigation().moveTo(player, moveSpeed);
  }

  @Override
  public void stop() {
    fox.getNavigation().stop();
  }
}