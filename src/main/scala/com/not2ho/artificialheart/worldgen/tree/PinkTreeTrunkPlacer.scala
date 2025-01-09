package com.not2ho.artificialheart.worldgen.tree

import com.google.common.collect.ImmutableList
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer.setDirtAt
import net.minecraft.world.level.levelgen.feature.trunkplacers.{FancyTrunkPlacer, TrunkPlacer, TrunkPlacerType}

import java.util
import java.util.function.{BiConsumer, Function}


object PinkTreeTrunkPlacer {
  val CODEC: Codec[PinkTreeTrunkPlacer]
  = RecordCodecBuilder.create((instance: RecordCodecBuilder.Instance[PinkTreeTrunkPlacer])
  => instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((tp: PinkTreeTrunkPlacer) => tp.getBaseHeight())
        ,Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((tp: PinkTreeTrunkPlacer) => tp.getHeightRandA())
        ,Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((tp: PinkTreeTrunkPlacer) => tp.getHeightRandB())
      )

      .apply(instance, new PinkTreeTrunkPlacer(_,_,_)))
}

class PinkTreeTrunkPlacer(pBaseHeight: Int, pHeightRandA: Int, pHeightRandB: Int) extends FancyTrunkPlacer(pBaseHeight, pHeightRandA, pHeightRandB) {
  private def getBaseHeight(): Int = pBaseHeight
  private def getHeightRandA(): Int = pHeightRandA
  private def getHeightRandB(): Int = pHeightRandB
//
  override protected def `type`: TrunkPlacerType[_] = TrunkPlacerTypes.PINK_TREE_TRUNK_PLACER.get
//
//  override def placeTrunk(pLevel: LevelSimulatedReader, pBlockSetter: BiConsumer[BlockPos, BlockState], pRandom: RandomSource, pFreeTreeHeight: Int, pPos: BlockPos, pConfig: TreeConfiguration): util.List[FoliagePlacer.FoliageAttachment] = {
//    setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below, pConfig)
//    val height = pFreeTreeHeight + pRandom.nextInt(heightRandA, heightRandA + 3) + pRandom.nextInt(heightRandB - 1, heightRandB + 1)
//    for (i <- 0 until height) {
//      placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i), pConfig)
//      if (i % 2 == 0 && pRandom.nextBoolean) {
//        if (pRandom.nextFloat > 0.25f) for (x <- 0 until 4) {
//          pBlockSetter.accept(pPos.above(i).relative(Direction.NORTH, x), Function.identity.apply(pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)).asInstanceOf[BlockState])
//        }
//        if (pRandom.nextFloat > 0.25f) for (x <- 0 until 4) {
//          pBlockSetter.accept(pPos.above(i).relative(Direction.SOUTH, x), Function.identity.apply(pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)).asInstanceOf[BlockState])
//        }
//        if (pRandom.nextFloat > 0.25f) for (x <- 0 until 4) {
//          pBlockSetter.accept(pPos.above(i).relative(Direction.EAST, x), Function.identity.apply(pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)).asInstanceOf[BlockState])
//        }
//        if (pRandom.nextFloat > 0.25f) for (x <- 0 until 4) {
//          pBlockSetter.accept(pPos.above(i).relative(Direction.WEST, x), Function.identity.apply(pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)).asInstanceOf[BlockState])
//        }
//      }
//    }
//    ImmutableList.of(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false))
//  }
}