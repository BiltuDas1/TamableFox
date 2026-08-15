package biltudas1.tamablefox.util;

import biltudas1.tamablefox.mixin.FoxAccessor;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxUtil {

    public static boolean isTamedFox(
        Fox fox
    ) {

        return ((FoxAccessor) fox)
            .invokeGetTrustedEntities()
            .findAny()
            .isPresent();
    }
}