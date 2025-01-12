package com.not2ho.artificialheart.worldgen.dimension

import com.google.common.collect.ImmutableList
import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks
import com.not2ho.artificialheart.worldgen.biome.PinkBiomes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.{Noises, SurfaceRules, VerticalAnchor}
import net.minecraft.world.level.levelgen.SurfaceRules.*

object PinkSurfaceRules {
  private val BEDROCK = makeStateRule( Blocks.BEDROCK )
  private val DEEPSLATE = makeStateRule( Blocks.DEEPSLATE )
  private val DIRT = makeStateRule( Blocks.DIRT )
  private val PINK_GRASS_BLOCK = makeStateRule(PinkBlocks.PINK_GRASS_BLOCK.get() )
  private val PINKWATER = makeStateRule( PinkBlocks.PINK_LIQUID_BLOCK.get() )
  private val STONE = makeStateRule( Blocks.STONE )
  private val PINK_SAND = makeStateRule( PinkBlocks.PINK_SAND.get() )
  private def makeStateRule( block : Block ) = state( block.defaultBlockState )

  private def makeStateRule( blockState : BlockState ) = state( blockState )

  def strawberryDays : SurfaceRules.RuleSource = {
    val atOrAboveSeaLevel = yBlockCheck( VerticalAnchor.absolute(PinkDimensions.SEA_LEVEL - 1 ), 0 )
    val aboveSeaLevel = yBlockCheck( VerticalAnchor.absolute( PinkDimensions.SEA_LEVEL ), 0 )
    val notUnderWater = waterBlockCheck( -1, 0 )
    val isPinkArea = isBiome( PinkBiomes.PINK_AREA)
    val notUnderDeepWater = waterStartCheck( -6, -1 )
    val grassRule = sequence( ifTrue( notUnderWater, PINK_GRASS_BLOCK ), DIRT )
    val sandRule = sequence( ifTrue( not( notUnderWater ), PINK_SAND ),
                             PINK_SAND )
//    val isSandy = isBiome(  )
//    val isStony = isBiome( )
    val surfaceRule = sequence(
      ifTrue( isPinkArea, ifTrue(
        noiseCondition(Noises.CALCITE, -0.0125, 0.0125 ), STONE))
//      , ifTrue( isPinkArea, ifTrue(
//        surfaceNoiseAbove( 2.25 ), MUD ) )
//      , ifTrue( isBiome(  ), ifTrue( surfaceNoiseAbove( 1.35 ), sandRule ) )
//      , ifTrue( isSandy, sandRule )
//      , ifTrue( isStony, sequence(ifTrue( steep, STONE ), ifTrue(
//                                  noiseCondition( Noises.CALCITE, -0.0125, 0.0125 ),
//                                  STONE ) ) )
  )
    val underFloorRule = sequence( surfaceRule, DIRT )
    val floorRule = sequence( surfaceRule, grassRule )
    val aboveSurface = sequence( ifTrue( ON_FLOOR, sequence( ifTrue( isPinkArea, ifTrue(
      atOrAboveSeaLevel, ifTrue( not( aboveSeaLevel ), ifTrue( noiseCondition( Noises.SWAMP, 0.0 ), PINKWATER ) ) ) ),
                                                             ifTrue( notUnderWater,
                                                                     floorRule ) ) ),
                                 ifTrue( notUnderDeepWater, sequence( ifTrue( UNDER_FLOOR,
                                                                              underFloorRule ) ) ), ifTrue( ON_FLOOR, sandRule ) )
    val rules = ImmutableList.builder[RuleSource]
    rules.add( ifTrue( verticalGradient( "bedrock_floor", VerticalAnchor.bottom, VerticalAnchor.aboveBottom( 5 ) ),
                       BEDROCK ) )
    rules.add( ifTrue( abovePreliminarySurface, aboveSurface ) )
    rules.add( ifTrue( verticalGradient( "deepslate", VerticalAnchor.absolute( 0 ), VerticalAnchor.absolute( 8 ) ),
                       DEEPSLATE ) )
    sequence( rules.build.toArray((n: Int) => new Array[RuleSource](n)): _* )
  }

  private def surfaceNoiseAbove( threshold : Double ) = noiseCondition( Noises.SURFACE, threshold / 8.25, Double.MaxValue )
}