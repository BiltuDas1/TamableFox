package biltudas1.tamablefox.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.state.TrustedPlayerAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;

@Mixin(LivingEntity.class)
public abstract class FoxDamageMixin {

    @Inject(
        method = "hurtServer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tamableFox$preventDamage(
        ServerLevel level,
        DamageSource source,
        float damage,
        CallbackInfoReturnable<Boolean> cir
    ) {

        if (
            !((Object) this instanceof Fox fox)
        ) {
            return;
        }

        if (
            !(source.getEntity()
                instanceof Player player)
        ) {
            return;
        }

        UUID trustedPlayer =
            ((TrustedPlayerAccessor) fox)
                .tamableFox$getTrustedPlayer();

        if (
            trustedPlayer != null
            && trustedPlayer.equals(
                player.getUUID()
            )
        ) {

            cir.setReturnValue(false);
        }
    }
}