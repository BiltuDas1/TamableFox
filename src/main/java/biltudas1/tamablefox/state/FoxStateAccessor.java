package biltudas1.tamablefox.state;

public interface FoxStateAccessor {

  FoxState tamableFox$getState();
  void tamableFox$setState(FoxState state);

  FoxState tamableFox$getPreviousState();
  void tamableFox$setPreviousState(FoxState state);
}
