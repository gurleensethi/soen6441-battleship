package com.soen6441.battleship;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ElectricHeater_Factory implements Factory<ElectricHeater> {
  private static final ElectricHeater_Factory INSTANCE = new ElectricHeater_Factory();

  @Override
  public ElectricHeater get() {
    return new ElectricHeater();
  }

  public static ElectricHeater_Factory create() {
    return INSTANCE;
  }

  public static ElectricHeater newInstance() {
    return new ElectricHeater();
  }
}
