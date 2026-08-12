package biltudas1.tamablefox.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import biltudas1.tamablefox.TamableFox;
import biltudas1.tamablefox.state.FoxState;
import biltudas1.tamablefox.state.FoxStateAccessor;
import biltudas1.tamablefox.state.GuardPositionAccessor;
import biltudas1.tamablefox.state.TrustedPlayerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@Mixin(Fox.class)
public class FoxStateMixin implements FoxStateAccessor, GuardPositionAccessor, TrustedPlayerAccessor {

    @Unique
    private FoxState tamableFox$state = FoxState.FOLLOW;

    @Unique
    private BlockPos tamableFox$guardPos;

    @Unique
    private UUID tamableFox$trustedPlayer;

    @Override
    public UUID tamableFox$getTrustedPlayer() {
        return tamableFox$trustedPlayer;
    }

    @Override
    public void tamableFox$setTrustedPlayer(
        UUID uuid
    ) {
        tamableFox$trustedPlayer = uuid;
    }

    @Override
    public FoxState tamableFox$getState() {
        return tamableFox$state;
    }

    @Override
    public void tamableFox$setState(FoxState state) {
        this.tamableFox$state = state;
    }

    @Override
    public BlockPos tamableFox$getGuardPos() {
        return tamableFox$guardPos;
    }

    @Override
    public void tamableFox$setGuardPos(
            BlockPos pos
    ) {
        tamableFox$guardPos = pos;
    }

    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void debugGuardState(CallbackInfo ci) {
        Fox fox = (Fox)(Object)this;

        if (tamableFox$state == FoxState.GUARD && !fox.isSitting()) {
            TamableFox.LOGGER.info(
                "GUARD fox is standing! sitting={}, sleeping={}",
                fox.isSitting(),
                fox.isSleeping()
            );
        }
    }

    @Inject(
        method = "setSitting",
        at = @At("HEAD")
    )
    private void debugSetSitting(
            boolean value,
            CallbackInfo ci
    ) {
        TamableFox.LOGGER.warn(
            "setSitting({}) state={}",
            value,
            tamableFox$state,
            new RuntimeException("Stack Trace")
        );

    }

    
    @Inject(
        method = "clearStates",
        at = @At("HEAD"),
        cancellable = true
    )
    private void preventClearStatesWhileGuard(
            CallbackInfo ci
    ) {
        if (((FoxStateAccessor)this)
                .tamableFox$getState() == FoxState.GUARD) {

            TamableFox.LOGGER.info(
                "Blocked clearStates() while GUARD"
            );

            ci.cancel();
        }
    }

    @Inject(
        method = "addAdditionalSaveData",
        at = @At("TAIL")
    )
    private void saveFoxState(
            ValueOutput output,
            CallbackInfo ci
    ) {
        output.putString(
            "TamableFoxState",
            tamableFox$state.name()
        );

        if (tamableFox$guardPos != null) {

            output.putInt(
                "TamableFoxGuardX",
                tamableFox$guardPos.getX()
            );

            output.putInt(
                "TamableFoxGuardY",
                tamableFox$guardPos.getY()
            );

            output.putInt(
                "TamableFoxGuardZ",
                tamableFox$guardPos.getZ()
            );
        }


        if (
            tamableFox$trustedPlayer != null
        ) {

            output.putString(
                "TamableFoxTrustedPlayer",
                tamableFox$trustedPlayer.toString()
            );
        }

        TamableFox.LOGGER.info(
            "Saved fox state {}",
            tamableFox$state
        );
    }

    @Inject(
        method = "readAdditionalSaveData",
        at = @At("TAIL")
    )
    private void loadFoxState(
            ValueInput input,
            CallbackInfo ci
    ) {
        input.getString("TamableFoxState")
            .ifPresent(stateName -> {

                try {

                    tamableFox$state =
                        FoxState.valueOf(stateName);

                    TamableFox.LOGGER.info(
                        "Loaded fox state {}",
                        tamableFox$state
                    );

                } catch (
                    IllegalArgumentException e
                ) {

                    tamableFox$state =
                        FoxState.FOLLOW;
                }
            });

        input.getString("TamableFoxTrustedPlayer")
            .ifPresent(uuidString -> {

                tamableFox$trustedPlayer =
                    UUID.fromString(
                        uuidString
                    );
            });

        if (
            input.getInt("TamableFoxGuardX").isPresent()
            && input.getInt("TamableFoxGuardY").isPresent()
            && input.getInt("TamableFoxGuardZ").isPresent()
        ) {

            int x =
                input.getInt("TamableFoxGuardX").get();

            int y =
                input.getInt("TamableFoxGuardY").get();

            int z =
                input.getInt("TamableFoxGuardZ").get();

            tamableFox$guardPos =
                new BlockPos(x, y, z);

            TamableFox.LOGGER.info(
                "Loaded guard position {}",
                tamableFox$guardPos
            );
        }
    }
}