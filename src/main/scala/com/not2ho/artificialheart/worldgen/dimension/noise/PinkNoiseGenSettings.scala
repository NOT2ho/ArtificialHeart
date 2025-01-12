package com.not2ho.artificialheart.worldgen.dimension.noise

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks
import com.not2ho.artificialheart.worldgen.dimension.{PinkDimensions, PinkSurfaceRules}
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.{BootstapContext, SurfaceRuleData}
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.{DensityFunction, NoiseGeneratorSettings, NoiseSettings, SurfaceRules}
import net.minecraft.world.level.levelgen.synth.NormalNoise

import java.util


object PinkNoiseGenSettings {
  val STRAWBERRY_NOISE : ResourceKey[NoiseGeneratorSettings] = createKey( "strawberrydays" )

  def bootstrap( context : BootstapContext[NoiseGeneratorSettings] ) : Unit = {
    val densityFunctions = context.lookup( Registries.DENSITY_FUNCTION )
    val noiseParameters = context.lookup( Registries.NOISE )
    context.register( STRAWBERRY_NOISE, createNoise( densityFunctions, noiseParameters, true ) )
  }

  def createNoise( densityFunctions : HolderGetter[DensityFunction], noiseParameters : HolderGetter[NormalNoise
  .NoiseParameters], pinksurface : Boolean ) : NoiseGeneratorSettings = {
    val settings = NoiseSettings.create( -64, 384, 1, 2 )
    val surface = if (pinksurface) PinkSurfaceRules.strawberryDays
                  else SurfaceRuleData.overworld
    new NoiseGeneratorSettings( settings, Blocks.STONE.defaultBlockState, PinkBlocks.PINK_LIQUID_BLOCK.get().defaultBlockState,
                                PinkNoiseRouterData.strawberryDays( densityFunctions, noiseParameters ),
                                surface, util.List.of, PinkDimensions.SEA_LEVEL, false, true, true, true )
  }

  private def createKey( name : String ) = ResourceKey.create( Registries.NOISE_SETTINGS, new ResourceLocation(ArtificialHeart.MOD_ID, name) )
}