package biltudas1.tamablefox.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import biltudas1.tamablefox.util.FoxUtil;

@Mixin(Animal.class)
public abstract class FoxBerryMixin {

    @Inject(
        method = "mobInteract",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tamableFox$berryHealing(
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResult> cir
    ) {

        if (!((Object) this instanceof Fox fox)) {
            return;
        }

        if (!FoxUtil.isTamedFox(fox)) {
            return;
        }

        ItemStack itemStack =
            player.getItemInHand(hand);

        boolean berry =
            itemStack.is(Items.SWEET_BERRIES)
            || itemStack.is(Items.GLOW_BERRIES);

        if (!berry) {
            return;
        }

        if (
            fox.getHealth()
            >= fox.getMaxHealth()
        ) {
            return;
        }

        fox.heal(1.0F);

        itemStack.shrink(1);

        cir.setReturnValue(
            InteractionResult.SUCCESS
        );
    }
}
