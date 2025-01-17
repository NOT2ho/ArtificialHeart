package com.not2ho.artificialheart.entity

import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object PinkEntities {
  val ENTITY_TYPES : DeferredRegister[EntityType[_]] = DeferredRegister.create( ForgeRegistries.ENTITY_TYPES,
                                                                                ArtificialHeart.MOD_ID )
  val HUMANLIKE : RegistryObject[EntityType[HumanlikeEntity]]
  = ENTITY_TYPES.register( "humanlike", ( ) => EntityType.Builder.of[HumanlikeEntity](
    new HumanlikeEntity(_, _), MobCategory.CREATURE )
      .sized( 2.5f, 2.5f )
      .build("humanlike") )



  def register( eventBus : IEventBus ) : Unit = {
    ENTITY_TYPES.register( eventBus )
  }
}

