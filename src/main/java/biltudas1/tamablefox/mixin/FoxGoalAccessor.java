package biltudas1.tamablefox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

@Mixin(Fox.class)
public interface FoxGoalAccessor {

    @Accessor("landTargetGoal")
    Goal tamableFox$getLandTargetGoal();

    @Accessor("turtleEggTargetGoal")
    Goal tamableFox$getTurtleEggTargetGoal();

    @Accessor("fishTargetGoal")
    Goal tamableFox$getFishTargetGoal();
}