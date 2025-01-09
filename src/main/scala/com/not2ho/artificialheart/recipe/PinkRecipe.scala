package com.not2ho.artificialheart.recipe

import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}


object PinkRecipe {
  val SERIALIZERS: DeferredRegister[RecipeSerializer[_]] = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArtificialHeart.MOD_ID)
  val FLOWER_JUICER_SERIALIZER: RegistryObject[RecipeSerializer[FlowerJuicerRecipe]] = SERIALIZERS.register("flower_juicer", () => FlowerJuicerRecipe.Serializer.INSTANCE)

  def register(eventBus: IEventBus): Unit = {
    SERIALIZERS.register(eventBus)
  }
}