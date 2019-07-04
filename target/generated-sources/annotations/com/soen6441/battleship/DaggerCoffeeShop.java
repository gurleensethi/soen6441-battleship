package com.soen6441.battleship;

import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
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
final class DaggerCoffeeShop implements CoffeeShop {
  private Provider<ElectricHeater> electricHeaterProvider;

  private Provider<Heater> heaterProvider;

  private DaggerCoffeeShop(CoffeeMachineModule coffeeMachineModuleParam) {

    initialize(coffeeMachineModuleParam);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static CoffeeShop create() {
    return new Builder().build();
  }

  private Thermosiphon getThermosiphon() {
    return new Thermosiphon(heaterProvider.get());}

  @SuppressWarnings("unchecked")
  private void initialize(final CoffeeMachineModule coffeeMachineModuleParam) {
    this.electricHeaterProvider = DoubleCheck.provider(ElectricHeater_Factory.create());
    this.heaterProvider = DoubleCheck.provider(CoffeeMachineModule_HeaterProviderFactory.create(coffeeMachineModuleParam));
  }

  @Override
  public CoffeeMaker maker() {
    return new CoffeeMaker(electricHeaterProvider.get(), getThermosiphon());}

  static final class Builder {
    private CoffeeMachineModule coffeeMachineModule;

    private Builder() {
    }

    public Builder coffeeMachineModule(CoffeeMachineModule coffeeMachineModule) {
      this.coffeeMachineModule = Preconditions.checkNotNull(coffeeMachineModule);
      return this;
    }

    public CoffeeShop build() {
      if (coffeeMachineModule == null) {
        this.coffeeMachineModule = new CoffeeMachineModule();
      }
      return new DaggerCoffeeShop(coffeeMachineModule);
    }
  }
}
