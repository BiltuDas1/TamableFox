package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(Fox.class)
public abstract class FoxHealthMixin {

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
            && fox.getAttribute(
                Attributes.MAX_HEALTH
            ).getBaseValue() < 60.0D
        ) {

            if (
                fox.getAttribute(
                    Attributes.MAX_HEALTH
                ) != null
            ) {

                fox.getAttribute(
                    Attributes.MAX_HEALTH
                ).setBaseValue(60.0D);

                fox.setHealth(60.0F);
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