package com.not2ho.artificialheart.entity

import com.not2ho.artificialheart.entity.*
import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks.FLOWER_JUICER
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object BlockEntities {
  val BLOCK_ENTITIES: DeferredRegister[BlockEntityType[_]] = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArtificialHeart.MOD_ID)
  val FLOWER_JUICER_BE: RegistryObject[BlockEntityType[FlowerJuicerEntity]] =
    BLOCK_ENTITIES.register("flower_juicer_be",
      () => BlockEntityType.Builder.of((pPos, pBlockState) => new FlowerJuicerEntity(pPos, pBlockState), FLOWER_JUICER.get).build(null)
    )

  def register(eventBus: IEventBus): Unit = {
    BLOCK_ENTITIES.register(eventBus)
  }
}