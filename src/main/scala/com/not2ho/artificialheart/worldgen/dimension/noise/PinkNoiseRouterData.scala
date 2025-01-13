package com.not2ho.artificialheart.worldgen.dimension.noise

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.worldgen.dimension.PinkTerrainProvider
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.DensityFunctions.Spline
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseRouterData
import net.minecraft.world.level.levelgen.Noises
import net.minecraft.world.level.levelgen.synth.NormalNoise


object PinkNoiseRouterData {
  private val BLENDING_FACTOR = DensityFunctions.constant( 20.0 )
  private val BLENDING_JAGGEDNESS = DensityFunctions.zero
  private val AMPLIFIED = true
  private val Y = vanillaKey( "y" )
  private val SHIFT_X = vanillaKey( "shift_x" )
  private val SHIFT_Z = vanillaKey( "shift_z" )
  private val BASE_3D_NOISE_OVERWORLD = vanillaKey( "overworld/base_3d_noise" )
  private val SPAGHETTI_ROUGHNESS_FUNCTION = vanillaKey("overworld/caves/spaghetti_roughness_function" )
  private val ENTRANCES = vanillaKey( "overworld/caves/entrances" )
  private val NOODLE = vanillaKey( "overworld/caves/noodle" )
  private val PILLARS = vanillaKey( "overworld/caves/pillars" )
  private val SPAGHETTI_2D = vanillaKey( "overworld/caves/spaghetti_2d" )
  val OFFSET : ResourceKey[DensityFunction] = createKey( "strawberrydays/offset" )
  val FACTOR : ResourceKey[DensityFunction] = createKey( "strawberrydays/factor" )
  val DEPTH : ResourceKey[DensityFunction] = createKey( "strawberrydays/depth" )
  val JAGGEDNESS : ResourceKey[DensityFunction] = createKey( "strawberrydays/jaggedness" )
  val SLOPED_CHEESE : ResourceKey[DensityFunction] = createKey( "strawberrydays/sloped_cheese" )

  def bootstrap( context :  BootstapContext[DensityFunction]) : Unit = {
    val densityFunctions = context.lookup( Registries.DENSITY_FUNCTION )
    val continents = new Spline.Coordinate( densityFunctions.getOrThrow( NoiseRouterData
                                                                           .CONTINENTS ) )
    val erosion = new Spline.Coordinate( densityFunctions.getOrThrow( NoiseRouterData.EROSION ) )
    val ridgesFolded = new Spline.Coordinate( densityFunctions.getOrThrow( NoiseRouterData
                                                                             .RIDGES_FOLDED ) )
    val weirdness = new Spline.Coordinate( densityFunctions.getOrThrow( NoiseRouterData.RIDGES ) )
    val offset = context.register( OFFSET, splineWithBlending( DensityFunctions
                                                                 .add(DensityFunctions.constant( -0.50375f )
                                                                      , DensityFunctions.spline( PinkTerrainProvider
                                                                                                   .offset( continents, erosion, ridgesFolded, AMPLIFIED )
                                                                                                 )
                                                                      )
                                                               , DensityFunctions.blendOffset ) )
    val factor = context.register( FACTOR, splineWithBlending( DensityFunctions
                                                                 .spline(
                                                                   PinkTerrainProvider.factor( continents, erosion, weirdness, ridgesFolded, AMPLIFIED ) ), BLENDING_FACTOR ) )
    val depth = context.register( DEPTH, DensityFunctions.add( DensityFunctions
                                                                 .yClampedGradient( 0, 320, 1.5, -1.5 ), wrap( offset ) ) )
    val jaggedness = context.register( JAGGEDNESS, splineWithBlending(
      DensityFunctions.spline( PinkTerrainProvider.jaggedness( continents, erosion, weirdness, ridgesFolded, AMPLIFIED ) )
      , BLENDING_JAGGEDNESS ) )
    val jagged = DensityFunctions.mul( wrap( jaggedness ), DensityFunctions.noise( context.lookup(
      Registries.NOISE ).getOrThrow( Noises.JAGGED ), 1500.0, 0.0 ).halfNegative )
    context.register( SLOPED_CHEESE, DensityFunctions.add( noiseGradientDensity( wrap( factor ), DensityFunctions.add
      ( wrap( depth ), jagged ) ), wrap( densityFunctions.getOrThrow( BASE_3D_NOISE_OVERWORLD ) ) ) )
  }

  private def splineWithBlending( p224454: DensityFunction, p224455: DensityFunction ) = {
    val densityfunction = DensityFunctions.lerp( DensityFunctions.blendAlpha, p224455, p224454 )
    DensityFunctions.flatCache( DensityFunctions.cache2d( densityfunction ) )
  }

  def strawberryDays( densityFunctions : HolderGetter[DensityFunction], noiseParameters : HolderGetter[NormalNoise
  .NoiseParameters] ) : NoiseRouter = {
    val aquiferBarrier = DensityFunctions.noise( noiseParameters.getOrThrow( Noises.AQUIFER_BARRIER
                                                                             ), 0.5 )
    val aquiferFluidLevelFloodedness = DensityFunctions.noise( noiseParameters.getOrThrow( Noises
                                                                                             .AQUIFER_FLUID_LEVEL_FLOODEDNESS ), 0.67 )
    val aquiferFluidLevelSpread = DensityFunctions.noise( noiseParameters.getOrThrow( Noises
                                                                                        .AQUIFER_FLUID_LEVEL_SPREAD ), 1.0 / 1.4 )
    val aquiferLava = DensityFunctions.noise( noiseParameters.getOrThrow( Noises.AQUIFER_LAVA ) )
    val shiftX = getFunction( densityFunctions, SHIFT_X )
    val shiftZ = getFunction( densityFunctions, SHIFT_Z )
    val temperature = DensityFunctions.shiftedNoise2d( shiftX, shiftZ, 0.25, noiseParameters
      .getOrThrow( Noises.TEMPERATURE ) )
    val vegetation = DensityFunctions.shiftedNoise2d( shiftX, shiftZ, 0.25, noiseParameters
      .getOrThrow( Noises.VEGETATION ) )
    val factor = getFunction( densityFunctions, FACTOR )
    val depth = getFunction( densityFunctions, DEPTH )
    val initialDensityWithoutJaggedness = noiseGradientDensity( DensityFunctions.cache2d( factor ),
                                                                depth )
    val slopedCheese = getFunction( densityFunctions, SLOPED_CHEESE )
    val densityfunction12 = DensityFunctions.min( slopedCheese, DensityFunctions.mul(
      DensityFunctions.constant( 5.0 ), getFunction( densityFunctions, ENTRANCES ) ) )
    val densityfunction13 = DensityFunctions.rangeChoice( slopedCheese, -1000000.0, 1.5625,
                                                          densityfunction12, underground(
        densityFunctions, noiseParameters, slopedCheese ) )
    val finalDensity = DensityFunctions.min( postProcess( slidePinks( densityfunction13 ) ),
                                             getFunction( densityFunctions, NOODLE ) )
    val y = getFunction( densityFunctions, Y )
    val j = -60
    val k = 50
    val veinToggle = yLimitedInterpolatable( y, DensityFunctions.noise( noiseParameters.getOrThrow(
      Noises.ORE_VEININESS ), 1.5, 1.5 ), j, k, 0 )
    val oreVeinA = yLimitedInterpolatable( y, DensityFunctions.noise( noiseParameters.getOrThrow(
      Noises.ORE_VEIN_A ), 4.0, 4.0 ), j, k, 0 ).abs
    val oreVeinB = yLimitedInterpolatable( y, DensityFunctions.noise( noiseParameters.getOrThrow(
      Noises.ORE_VEIN_B ), 4.0, 4.0 ), j, k, 0 ).abs
    val veinRidged = DensityFunctions.add( DensityFunctions.constant( -0.08f ), DensityFunctions
      .max( oreVeinA, oreVeinB ) )
    val veinGap = DensityFunctions.noise( noiseParameters.getOrThrow( Noises.ORE_GAP ) )
    new NoiseRouter( aquiferBarrier, aquiferFluidLevelFloodedness, aquiferFluidLevelSpread, aquiferLava,
                     temperature, vegetation, getFunction( densityFunctions, NoiseRouterData.CONTINENTS ),
                     getFunction( densityFunctions, NoiseRouterData.EROSION ), depth, getFunction(
        densityFunctions, NoiseRouterData.RIDGES ), initialDensityWithoutJaggedness, finalDensity, veinToggle,
                     veinRidged, veinGap )
  }

  private def postProcess( function : DensityFunction ) = {
    val blendedFunction = DensityFunctions.blendDensity( function )
    DensityFunctions.mul( DensityFunctions.interpolated( blendedFunction ), DensityFunctions.constant( 0.64 )
                          ).squeeze
  }

  private def slidePinks( function : DensityFunction ) = slide( function, 0, 256, 80, 64, -0.198125, 0, 24, 0.253333 )

  private def slide( function : DensityFunction, minY : Int, height : Int, topSliderLowerOffset : Int,
                     topSlideUpperOffset : Int, topSlideTarget : Double, bottomSlideLowerOffset : Int,
                     bottomSlideUpperOffset : Int, bottomSlideTarget : Double ) = {
    val topSlideFactor = DensityFunctions.yClampedGradient( minY + height - topSliderLowerOffset,
                                                            minY + height - topSlideUpperOffset,
                                                            1.0, 0.0 )
    val bottomSlideFactor = DensityFunctions.yClampedGradient( minY + bottomSlideLowerOffset, minY
                                                                                              +
                                                                                              bottomSlideUpperOffset, 0.0, 1.0 )
    DensityFunctions.lerp( bottomSlideFactor, bottomSlideTarget, DensityFunctions.lerp( topSlideFactor,
                                                                                        topSlideTarget,
                                                                                        function ) )
  }

  private def getFunction( registry : HolderGetter[DensityFunction], key : ResourceKey[DensityFunction] ) = wrap( registry.getOrThrow( key ) )

  private def wrap( holder : Holder.Reference[DensityFunction] ) = new DensityFunctions.HolderHolder( holder )

  private def yLimitedInterpolatable( p209472 : DensityFunction, p209473 : DensityFunction, p209474 : Int, p209475
  : Int, p209476 : Int ) = DensityFunctions.interpolated( DensityFunctions.rangeChoice( p209472, p209474, p209475 + 1, p209473,
                                                                                        DensityFunctions.constant( p209476 ) ) )

  private def noiseGradientDensity( p212272 : DensityFunction, p212273 : DensityFunction ) = {
    val densityfunction = DensityFunctions.mul( p212273, p212272 )
    DensityFunctions.mul( DensityFunctions.constant( 4.0 ), densityfunction.quarterNegative )
  }

  private def underground( densityFunctions : HolderGetter[DensityFunction], noiseParameters
  : HolderGetter[NormalNoise.NoiseParameters], slopedCheese : DensityFunction ) = {
    val spaghetti2d = getFunction( densityFunctions, SPAGHETTI_2D )
    val spaghettiRoughness = getFunction( densityFunctions, SPAGHETTI_ROUGHNESS_FUNCTION )
    val caveLayer = DensityFunctions.noise( noiseParameters.getOrThrow( Noises.CAVE_LAYER ), 8.0 )
    val densityfunction3 = DensityFunctions.mul( DensityFunctions.constant( 4.0 ), caveLayer.square )
    val caveCheese = DensityFunctions.noise( noiseParameters.getOrThrow( Noises.CAVE_CHEESE ), 1.0
                                                                                               / 1.5 )
    val densityfunction5 = DensityFunctions.add( DensityFunctions.add( DensityFunctions.constant(
      0.27 ), caveCheese ).clamp( -1.0, 1.0 ), DensityFunctions.add( DensityFunctions.constant( 1.5 ),
                                                                     DensityFunctions.mul( DensityFunctions
                                                                                             .constant( -0.64 ),
                                                                                           slopedCheese ) ).clamp(
      0.0, 0.5 ) )
    val densityfunction6 = DensityFunctions.add( densityfunction3, densityfunction5 )
    val caves = DensityFunctions.min( DensityFunctions.min( densityfunction6, getFunction(
      densityFunctions, ENTRANCES ) ), DensityFunctions.add( spaghetti2d, spaghettiRoughness ) )
    val pillars = getFunction( densityFunctions, PILLARS )
    val pillarsThreshold = DensityFunctions.rangeChoice( pillars, -1000000.0, 0.03,
                                                         DensityFunctions.constant( -1000000.0 ),
                                                         pillars )
    DensityFunctions.max( caves, pillarsThreshold )
  }

  private def vanillaKey( name : String ) = ResourceKey.create( Registries.DENSITY_FUNCTION, new ResourceLocation( name ) )

  private def createKey( name : String ) = ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(ArtificialHeart.MOD_ID, name) )
}