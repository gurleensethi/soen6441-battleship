package com.soen6441.battleship;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CoffeeMaker_Factory implements Factory<CoffeeMaker> {
  private final Provider<ElectricHeater> heaterProvider;

  private final Provider<Thermosiphon> thermosiphonProvider;

  public CoffeeMaker_Factory(Provider<ElectricHeater> heaterProvider,
      Provider<Thermosiphon> thermosiphonProvider) {
    this.heaterProvider = heaterProvider;
    this.thermosiphonProvider = thermosiphonProvider;
  }

  @Override
  public CoffeeMaker get() {
    return new CoffeeMaker(heaterProvider.get(), thermosiphonProvider.get());
  }

  public static CoffeeMaker_Factory create(Provider<ElectricHeater> heaterProvider,
      Provider<Thermosiphon> thermosiphonProvider) {
    return new CoffeeMaker_Factory(heaterProvider, thermosiphonProvider);
  }

  public static CoffeeMaker newInstance(Object heater, Object thermosiphon) {
    return new CoffeeMaker((ElectricHeater) heater, (Thermosiphon) thermosiphon);
  }
}
