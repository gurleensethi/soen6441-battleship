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
public final class Thermosiphon_Factory implements Factory<Thermosiphon> {
  private final Provider<ElectricHeater> heaterProvider;

  public Thermosiphon_Factory(Provider<ElectricHeater> heaterProvider) {
    this.heaterProvider = heaterProvider;
  }

  @Override
  public Thermosiphon get() {
    return new Thermosiphon(heaterProvider.get());
  }

  public static Thermosiphon_Factory create(Provider<ElectricHeater> heaterProvider) {
    return new Thermosiphon_Factory(heaterProvider);
  }

  public static Thermosiphon newInstance(Object heater) {
    return new Thermosiphon((ElectricHeater) heater);
  }
}
