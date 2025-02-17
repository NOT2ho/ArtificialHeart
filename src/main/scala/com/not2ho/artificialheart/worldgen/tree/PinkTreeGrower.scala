package com.not2ho.artificialheart.worldgen.tree

import com.not2ho.artificialheart.worldgen.PinkConfiguredFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import org.jetbrains.annotations.Nullable


class PinkTreeGrower extends AbstractTreeGrower {
  @Nullable
  override protected def getConfiguredFeature(pRandom: RandomSource, pHasFlowers: Boolean)
  : ResourceKey[ConfiguredFeature[_, _]] = PinkConfiguredFeatures.PINK_TREE_KEY
}