package com.not2ho.artificialheart.worldgen

import net.minecraft.world.level.levelgen.placement.*

import java.util


object PinkOrePlacement {
  def orePlacement(pModifier: PlacementModifier, pModifier2: PlacementModifier): util.List[PlacementModifier] = util.List.of(pModifier, InSquarePlacement.spread, pModifier2, BiomeFilter.biome)

  def commonOrePlacement(pCount: Int, pHeightRange: PlacementModifier): util.List[PlacementModifier] = orePlacement(CountPlacement.of(pCount), pHeightRange)

  def rareOrePlacement(pChance: Int, pHeightRange: PlacementModifier): util.List[PlacementModifier] = orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange)
}