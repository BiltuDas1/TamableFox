package biltudas1.tamablefox.state;

import net.minecraft.core.BlockPos;

public interface GuardPositionAccessor {

    BlockPos tamableFox$getGuardPos();

    void tamableFox$setGuardPos(
        BlockPos pos
    );
}