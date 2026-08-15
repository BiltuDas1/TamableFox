package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.util.FoxUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NeutralMobTargetMixin {

    @Shadow
    protected LivingEntity target;

    @Inject(
        method = "canUse",
        at = @At("RETURN"),
        cancellable = true
    )
    private void tamableFox$ignoreTamedFox(
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (
            !(((TargetGoalAccessor) this).tamableFox$getMob() instanceof Wolf)
            && !(((TargetGoalAccessor) this).tamableFox$getMob() instanceof PolarBear)
        ) {
            return;
        }

        if (
            cir.getReturnValue()
            && target instanceof Fox fox
            && FoxUtil.isTamedFox(fox)
        ) {
            // Only suppress proactive nearest-target goals. HurtByTargetGoal
            // still assigns the fox as a normal retaliation target.
            cir.setReturnValue(false);
        }
    }
}
