package com.not2ho.artificialheart.worldgen.tree


import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}


object TrunkPlacerTypes {
  val TRUNK_PLACER: DeferredRegister[TrunkPlacerType[_]] = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ArtificialHeart.MOD_ID)
  val PINK_TREE_TRUNK_PLACER: RegistryObject[TrunkPlacerType[PinkTreeTrunkPlacer]] = TRUNK_PLACER.register("pink_tree_trunk_placer", () => new TrunkPlacerType(PinkTreeTrunkPlacer.CODEC))

  def register(eventBus: IEventBus): Unit = {
    TRUNK_PLACER.register(eventBus)
  }
}