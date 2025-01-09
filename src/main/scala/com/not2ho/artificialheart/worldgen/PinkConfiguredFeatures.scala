package com.not2ho.artificialheart.worldgen

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.worldgen.tree.{PinkTreeFoliagePlacer, PinkTreeTrunkPlacer}
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature}
import net.minecraft.world.level.levelgen.feature.configurations.{FeatureConfiguration, OreConfiguration, TreeConfiguration}
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import net.minecraft.world.level.levelgen.structure.templatesystem.{BlockMatchTest, RuleTest, TagMatchTest}
import net.minecraftforge.data.event.GatherDataEvent

import java.util


object PinkConfiguredFeatures {
  val OVERWORLD_HEART_ORE_KEY: ResourceKey[ConfiguredFeature[_, _]] = registerKey("heart_ore")
  val PINK_TREE_KEY: ResourceKey[ConfiguredFeature[_, _]] = registerKey("pink_tree")

  def bootstrap(context: BootstapContext[ConfiguredFeature[_, _]]): Unit = {
    val stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
    val deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
    val overworldHeartOres = util.List.of(OreConfiguration.target(stoneReplaceable, ArtificialHeart.HEART_ORE.get.defaultBlockState), OreConfiguration.target(deepslateReplaceables, ArtificialHeart.DEEPSLATE_HEART_ORE.get.defaultBlockState))
    register(context, OVERWORLD_HEART_ORE_KEY, Feature.ORE, new OreConfiguration(overworldHeartOres, 9))
    register(context, PINK_TREE_KEY, Feature.TREE
      , new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ArtificialHeart.PINK_TREE_LOG.get)
      , new PinkTreeTrunkPlacer(8, 40, 3)
      , BlockStateProvider.simple(ArtificialHeart.PINK_TREE_LEAVES.get)
      , new PinkTreeFoliagePlacer(ConstantInt.of(8), ConstantInt.of(6), 7)
      , new TwoLayersFeatureSize(1, 0, 2)).build)
  }

  def registerKey(name: String): ResourceKey[ConfiguredFeature[_, _]] = ResourceKey.create(Registries.CONFIGURED_FEATURE
    , new ResourceLocation(ArtificialHeart.MOD_ID, name))

  private def register[FC <: FeatureConfiguration, F <: Feature[FC]](context: BootstapContext[ConfiguredFeature[_, _]]
                                                                     , key: ResourceKey[ConfiguredFeature[_, _]]
                                                                     , feature: F, configuration: FC): Unit = {
    context.register(key, new ConfiguredFeature[FC, F](feature, configuration))
  }
}