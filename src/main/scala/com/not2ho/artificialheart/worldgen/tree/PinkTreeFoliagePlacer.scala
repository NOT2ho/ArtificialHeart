package com.not2ho.artificialheart.worldgen.tree

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.{AcaciaFoliagePlacer, FoliagePlacer, FoliagePlacerType}

object PinkTreeFoliagePlacer {
  val CODEC: Codec[PinkTreeFoliagePlacer] =
    RecordCodecBuilder.create(
      (instance: RecordCodecBuilder.Instance[PinkTreeFoliagePlacer])
      => instance.group(
        IntProvider.codec(0, 16).fieldOf("radius").forGetter((fp: PinkTreeFoliagePlacer) => fp.getRadius()),
        IntProvider.codec(0, 16).fieldOf("offset").forGetter((fp: PinkTreeFoliagePlacer) => fp.getOffset()))
        .and(Codec.intRange(0, 16).fieldOf("height").forGetter((fp: PinkTreeFoliagePlacer) => fp.height))
        .apply(instance, new PinkTreeFoliagePlacer(_, _, _))
    )
}

class PinkTreeFoliagePlacer(pRadius: IntProvider, pOffset: IntProvider, private val height: Int) extends AcaciaFoliagePlacer(pRadius, pOffset) {
  private def getRadius(): IntProvider = pRadius
  private def getOffset(): IntProvider = pOffset
  
  override protected def `type`: FoliagePlacerType[_] = FoliagePlacerTypes.PINK_TREE_FOLIAGE_PLACER.get

//  override protected def createFoliage(pLevel: LevelSimulatedReader, pBlockSetter: FoliagePlacer.FoliageSetter, pRandom: RandomSource, pConfig: TreeConfiguration, pMaxFreeTreeHeight: Int, pAttachment: FoliagePlacer.FoliageAttachment, pFoliageHeight: Int, pFoliageRadius: Int, pOffset: Int): Unit = {
//    this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos.above(0), 2, 2, pAttachment.doubleTrunk)
//    this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos.above(1), 2, 2, pAttachment.doubleTrunk)
//    this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos.above(2), 2, 2, pAttachment.doubleTrunk)
//  }
//
//  override def foliageHeight(pRandom: RandomSource, pHeight: Int, pConfig: TreeConfiguration): Int = this.height

 // override protected def shouldSkipLocation(pRandom: RandomSource, pLocalX: Int, pLocalY: Int, pLocalZ: Int, pRange: Int, pLarge: Boolean) = false
}