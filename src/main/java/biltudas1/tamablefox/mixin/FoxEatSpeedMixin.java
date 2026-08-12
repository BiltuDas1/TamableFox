package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

@Mixin(Fox.class)
public abstract class FoxEatSpeedMixin {

    @Shadow
    private int ticksSinceEaten;

    @Unique
    private boolean tamableFox$foodHealApplied = false;

    @Inject(
        method = "aiStep",
        at = @At("HEAD")
    )
    private void tamableFox$eatFaster(
        CallbackInfo ci
    ) {

        Fox fox = (Fox)(Object)this;

        ItemStack mouthItem =
            fox.getMainHandItem();

        if (
            mouthItem.isEmpty()
            || !mouthItem.has(
                DataComponents.FOOD
            )
        ) {

            tamableFox$foodHealApplied = false;
            return;
        }

        if (
            fox.getHealth()
            >= fox.getMaxHealth()
        ) {

            tamableFox$foodHealApplied = false;
            return;
        }

        if (
            this.ticksSinceEaten > 60
        ) {

            if (
                !tamableFox$foodHealApplied
            ) {

                FoodProperties food =
                    mouthItem.get(
                        DataComponents.FOOD
                    );

                if (food != null) {

                    int healAmount =
                        Math.max(
                            1,
                            (int)Math.ceil(
                                food.saturation()
                            )
                        );

                    fox.heal(
                        (float)healAmount
                    );
                }

                tamableFox$foodHealApplied = true;
            }

            this.ticksSinceEaten = 600;
        }
    }
}