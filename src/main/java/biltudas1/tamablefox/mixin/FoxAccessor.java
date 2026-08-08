package biltudas1.tamablefox.mixin;

import java.util.stream.Stream;

import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Fox.class)
public interface FoxAccessor {

  @Invoker("getTrustedEntities")
  Stream<EntityReference<LivingEntity>> invokeGetTrustedEntities();
}
