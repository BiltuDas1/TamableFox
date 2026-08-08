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
    private boolean tamableFox$healthApplied = false;

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
            && !tamableFox$healthApplied
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

                tamableFox$healthApplied = true;
            }
        }
    }
}