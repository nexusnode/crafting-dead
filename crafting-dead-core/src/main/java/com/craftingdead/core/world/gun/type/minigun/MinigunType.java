package com.craftingdead.core.world.gun.type.minigun;

import java.util.function.Function;
import com.craftingdead.core.world.gun.type.TypedGun;
import com.craftingdead.core.world.gun.type.TypedGunClient;
import com.craftingdead.core.world.gun.type.simple.SimpleGunType;

public class MinigunType extends SimpleGunType {

  protected MinigunType(Builder<?> builder) {
    super(builder);
  }

  @Override
  protected <T extends TypedGun<?>> Function<T, TypedGunClient<? super T>> getClientFactory() {
    return MinigunClient::new;
  }

  public static Builder<?> builder() {
    return new Builder<>(MinigunType::new);
  }
}
