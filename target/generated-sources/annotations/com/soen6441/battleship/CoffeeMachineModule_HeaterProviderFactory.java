package com.soen6441.battleship;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CoffeeMachineModule_HeaterProviderFactory implements Factory<Heater> {
  private final CoffeeMachineModule module;

  public CoffeeMachineModule_HeaterProviderFactory(CoffeeMachineModule module) {
    this.module = module;
  }

  @Override
  public Heater get() {
    return heaterProvider(module);
  }

  public static CoffeeMachineModule_HeaterProviderFactory create(CoffeeMachineModule module) {
    return new CoffeeMachineModule_HeaterProviderFactory(module);
  }

  public static Heater heaterProvider(CoffeeMachineModule instance) {
    return Preconditions.checkNotNull(instance.heaterProvider(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
