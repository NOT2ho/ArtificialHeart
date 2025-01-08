package com.not2ho.artificialheart.worldgen.tree

import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.foliageplacers.{FoliagePlacer, FoliagePlacerType}
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject


object FoliagePlacers {
  val FOLIAGE_PLACERS: DeferredRegister[FoliagePlacerType[_]] = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ArtificialHeart.MOD_ID)
  val PINK_TREE_FOLIAGE_PLACER: RegistryObject[FoliagePlacerType[PinkTreeFoliagePlacer]] = FOLIAGE_PLACERS.register("pink_tree_foliage_placer", () => new FoliagePlacerType(PinkTreeFoliagePlacer.CODEC))

  def register(eventBus: IEventBus): Unit = {
    FOLIAGE_PLACERS.register(eventBus)
  }
}