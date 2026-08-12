package biltudas1.tamablefox.state;

import java.util.UUID;

public interface TrustedPlayerAccessor {

    UUID tamableFox$getTrustedPlayer();

    void tamableFox$setTrustedPlayer(
        UUID uuid
    );
}