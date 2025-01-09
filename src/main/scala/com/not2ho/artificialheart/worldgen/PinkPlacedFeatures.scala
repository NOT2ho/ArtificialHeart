package com.not2ho.artificialheart.worldgen

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks.PINK_TREE_SAPLING
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

import java.util


object PinkPlacedFeatures {
  val HEART_ORE_PLACED_KEY: ResourceKey[PlacedFeature] = registerKey("heart_ore_placed")
  val PINK_TREE_PLACED_KEY: ResourceKey[PlacedFeature] = registerKey("pink_tree_placed")

  def bootstrap(context: BootstapContext[PlacedFeature]): Unit = {
    val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
    register(context, HEART_ORE_PLACED_KEY, configuredFeatures.getOrThrow(PinkConfiguredFeatures.OVERWORLD_HEART_ORE_KEY), PinkOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(3))))
    register(context, PINK_TREE_PLACED_KEY, configuredFeatures.getOrThrow(PinkConfiguredFeatures.PINK_TREE_KEY), VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),PINK_TREE_SAPLING.get))
  }

  private def registerKey(name: String) = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ArtificialHeart.MOD_ID, name))

  private def register(context: BootstapContext[PlacedFeature], key: ResourceKey[PlacedFeature], configuration: Holder[ConfiguredFeature[_, _]], modifiers: util.List[PlacementModifier]): Unit = {
    context.register(key, new PlacedFeature(configuration, util.List.copyOf(modifiers)))
  }
}