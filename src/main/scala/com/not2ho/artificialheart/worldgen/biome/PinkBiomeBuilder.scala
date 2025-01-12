package com.not2ho.artificialheart.worldgen.biome

import com.mojang.datafixers.util.{Pair}

import java.util
import java.util.function.Consumer
import net.minecraft.SharedConstants
import net.minecraft.core.registries.Registries
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.data.worldgen.TerrainProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.util.CubicSpline
import net.minecraft.util.ToFloatFunction
import net.minecraft.util.VisibleForDebug
import net.minecraft.world.level.biome.{Biome, Biomes, Climate}
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.DensityFunctions.Spline
import net.minecraft.world.level.levelgen.NoiseRouterData
import Climate.{Parameter as CP, ParameterPoint as CPPoint}
import com.not2ho.artificialheart.util.InstanceOfPatternMatchingFuntionalizer.doIf

import java.util.List

def bootstrap(context: Nothing): Unit = {
  val entries: util.List[Pair[Climate.ParameterPoint, ResourceKey[Biome]]] = new util.ArrayList[Pair[Climate.ParameterPoint, ResourceKey[Biome]]]()
  new PinkBiomeBuilder().addBiomes(entries.add)
}





/***************
 DOTGERED FROM OverworldBiomeBuilder
 ***************/

object PinkBiomeBuilder {
  private val VALLEY_SIZE = 0.05F
  private val LOW_START = 0.26666668F
  val HIGH_START = 0.4F
  private val HIGH_END = 0.93333334F
  private val PEAK_SIZE = 0.1F
  val PEAK_START = 0.56666666F
  private val PEAK_END = 0.7666667F
  val NEAR_INLAND_START: Float = -0.11F
  val MID_INLAND_START = 0.03F
  val FAR_INLAND_START = 0.3F
  val EROSION_INDEX_1_START: Float = -0.78F
  val EROSION_INDEX_2_START: Float = -0.375F
  private val EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225F
  private val DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9F

  def isDeepDarkRegion(pErosionFunction: DensityFunction, pDepthFunction: DensityFunction, pFunctionContext: DensityFunction.FunctionContext): Boolean = pErosionFunction.compute(pFunctionContext) < -0.225F.toDouble && pDepthFunction.compute(pFunctionContext) > 0.9F.toDouble

  def getDebugStringForPeaksAndValleys(pPeaksAndValleysData: Double): String = if (pPeaksAndValleysData < NoiseRouterData.peaksAndValleys(0.05F).toDouble) "Valley"
  else if (pPeaksAndValleysData < NoiseRouterData.peaksAndValleys(0.26666668F).toDouble) "Low"
  else if (pPeaksAndValleysData < NoiseRouterData.peaksAndValleys(0.4F).toDouble) "Mid"
  else if (pPeaksAndValleysData < NoiseRouterData.peaksAndValleys(0.56666666F).toDouble) "High"
  else "Peak"

  private def getDebugStringForNoiseValue(pDepth: Double, pValues: Array[CP]): String = {
    val d0 = Climate.quantizeCoord(pDepth.toFloat).toDouble
    for (i <- pValues.indices) {
      if (d0 < pValues(i).max.toDouble) return "" + i
    }
    "?"
  }
}

final class PinkBiomeBuilder {
  type CP = Climate.Parameter

  final private val FULL_RANGE = CP.span(-1.0F, 1.0F)
  final private val temperatures = Array[CP](CP.span(-1.0F, -0.45F), CP.span(-0.45F, -0.15F), CP.span(-0.15F, 0.2F), CP.span(0.2F, 0.55F), CP.span(0.55F, 1.0F))
  final private val humidities = Array[CP](CP.span(-1.0F, -0.35F), CP.span(-0.35F, -0.1F), CP.span(-0.1F, 0.1F), CP.span(0.1F, 0.3F), CP.span(0.3F, 1.0F))
  final private val erosions = Array[CP](CP.span(-1.0F, -0.78F), CP.span(-0.78F, -0.375F), CP.span(-0.375F, -0.2225F), CP.span(-0.2225F, 0.05F), CP.span(0.05F, 0.45F), CP.span(0.45F, 0.55F), CP.span(0.55F, 1.0F))
  final private val FROZEN_RANGE = this.temperatures(0)
  final private val UNFROZEN_RANGE = CP.span(this.temperatures(1), this.temperatures(4))
  final private val mushroomFieldsContinentalness = CP.span(-1.2F, -1.05F)
  final private val deepOceanContinentalness = CP.span(-1.05F, -0.455F)
  final private val oceanContinentalness = CP.span(-0.455F, -0.19F)
  final private val coastContinentalness = CP.span(-0.19F, -0.11F)
  final private val inlandContinentalness = CP.span(-0.11F, 0.55F)
  final private val nearInlandContinentalness = CP.span(-0.11F, 0.03F)
  final private val midInlandContinentalness = CP.span(0.03F, 0.3F)
  final private val farInlandContinentalness = CP.span(0.3F, 1.0F)
  final private val OCEANS = Array[Array[ResourceKey[Biome]]](Array(
    Biomes.DEEP_FROZEN_OCEAN
    , Biomes.DEEP_COLD_OCEAN
    , Biomes.DEEP_OCEAN
    , Biomes.DEEP_LUKEWARM_OCEAN
    , Biomes.WARM_OCEAN)
  , Array(Biomes.FROZEN_OCEAN
    , Biomes.COLD_OCEAN
    , Biomes.OCEAN
    , Biomes.LUKEWARM_OCEAN
    , Biomes.WARM_OCEAN))
  final private val MIDDLE_BIOMES = Array[Array[ResourceKey[Biome]]](
    Array(
      Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_TAIGA
      , Biomes.TAIGA
      )
    , Array(
      Biomes.PLAINS
      , Biomes.PLAINS
      , Biomes.FOREST
      , Biomes.TAIGA
      , Biomes.OLD_GROWTH_SPRUCE_TAIGA
      ), Array(
      Biomes.FLOWER_FOREST
      , Biomes.PLAINS
      , Biomes.FOREST
      , Biomes.BIRCH_FOREST
      , Biomes.DARK_FOREST
      ), Array(
      Biomes.SAVANNA
      , Biomes.SAVANNA
      , Biomes.FOREST
      , Biomes.JUNGLE
      , Biomes.JUNGLE)
    , Array(
      Biomes.DESERT
      , Biomes.DESERT
      , Biomes.DESERT
      , Biomes.DESERT
      , Biomes.DESERT)
    )
  final private val MIDDLE_BIOMES_VARIANT = Array[Array[ResourceKey[Biome]]](
    Array(Biomes.ICE_SPIKES
          , null
          , Biomes.SNOWY_TAIGA
          , PinkBiomes.PINK_AREA
          , null)
          , Array(null
          , PinkBiomes.PINK_AREA
          , null
          , null
          , Biomes.OLD_GROWTH_PINE_TAIGA)
    , Array(Biomes.SUNFLOWER_PLAINS
          , null
          , null
          , Biomes.OLD_GROWTH_BIRCH_FOREST
          , PinkBiomes.PINK_AREA)
          , Array(null
          , null
          , Biomes.PLAINS
          , Biomes.SPARSE_JUNGLE
          , Biomes.BAMBOO_JUNGLE)
    , Array(null
          , null
          , null
          , null
          , null))
  final private val PLATEAU_BIOMES = Array[Array[ResourceKey[Biome]]](
    Array(
        Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_PLAINS
      , Biomes.SNOWY_TAIGA
      , Biomes.SNOWY_TAIGA)
    , Array(
      Biomes.MEADOW
      , Biomes.MEADOW
      , Biomes.FOREST
      , Biomes.TAIGA
      , Biomes.OLD_GROWTH_SPRUCE_TAIGA)
    , Array(
      Biomes.MEADOW
      , Biomes.MEADOW
      , Biomes.MEADOW
      , Biomes.MEADOW
      , Biomes.DARK_FOREST)
    , Array(
      Biomes.SAVANNA_PLATEAU
      , Biomes.SAVANNA_PLATEAU
      , Biomes.FOREST
      , Biomes.FOREST
      , Biomes.JUNGLE)
    , Array(
      Biomes.BADLANDS
      , Biomes.BADLANDS
      , Biomes.BADLANDS
      , Biomes.WOODED_BADLANDS
      , Biomes.WOODED_BADLANDS))
  final private val PLATEAU_BIOMES_VARIANT = Array[Array[ResourceKey[Biome]]](
    Array(
      Biomes.ICE_SPIKES
      , null
      , null
      , null
      , null)
    , Array(Biomes.CHERRY_GROVE
      , null
      , Biomes.MEADOW
      , Biomes.MEADOW
      , Biomes.OLD_GROWTH_PINE_TAIGA)
    , Array(Biomes.CHERRY_GROVE
      , Biomes.CHERRY_GROVE
      , Biomes.FOREST
      , Biomes.BIRCH_FOREST
      , null)
    , Array(null
      , null
      , null
      , null
      , null)
    , Array(Biomes.ERODED_BADLANDS
      , Biomes.ERODED_BADLANDS
      , null
      , null
      , null))
  final private val SHATTERED_BIOMES = Array[Array[ResourceKey[Biome]]](
    Array(
      Biomes.WINDSWEPT_GRAVELLY_HILLS
    , Biomes.WINDSWEPT_GRAVELLY_HILLS
    , Biomes.WINDSWEPT_HILLS
    , Biomes.WINDSWEPT_FOREST
    , Biomes.WINDSWEPT_FOREST)
  , Array(
      Biomes.WINDSWEPT_GRAVELLY_HILLS
    , Biomes.WINDSWEPT_GRAVELLY_HILLS
    , Biomes.WINDSWEPT_HILLS
    , Biomes.WINDSWEPT_FOREST
    , Biomes.WINDSWEPT_FOREST)
, Array(
      Biomes.WINDSWEPT_HILLS
    , Biomes.WINDSWEPT_HILLS
    , Biomes.WINDSWEPT_HILLS
    , Biomes.WINDSWEPT_FOREST
    , Biomes.WINDSWEPT_FOREST)
, Array(null
      , null
      , null
      , null
      , null)
, Array(null
      , null
      , null
      , null
      , null)
    )

  def spawnTarget: util.List[CPPoint] = {
    val climate$parameter = CP.point(0.0F)
    val f = 0.16F
    util.List.of(new CPPoint(this.FULL_RANGE, this.FULL_RANGE, CP.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, CP.span(-1.0F, -0.16F), 0L), new CPPoint(this.FULL_RANGE, this.FULL_RANGE, CP.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, CP.span(0.16F, 1.0F), 0L))
  }

  def addBiomes(pKey: Consumer[Pair[CPPoint, ResourceKey[Biome]]]): Unit = {
    if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) this.addDebugBiomes(pKey)
    else {
      this.addOffCoastBiomes(pKey)
      this.addInlandBiomes(pKey)
      this.addUndergroundBiomes(pKey)
    }
  }

  private def addDebugBiomes(pKey: Consumer[Pair[CPPoint, ResourceKey[Biome]]]): Unit = {
    val holderlookup$provider = VanillaRegistries.createLookup
    val holdergetter = holderlookup$provider.lookupOrThrow(Registries.DENSITY_FUNCTION)
    val densityfunctions$spline$coordinate = new Spline.Coordinate(holdergetter.getOrThrow(NoiseRouterData.CONTINENTS))
    val densityfunctions$spline$coordinate1 = new Spline.Coordinate(holdergetter.getOrThrow(NoiseRouterData.EROSION))
    val densityfunctions$spline$coordinate2 = new Spline.Coordinate(holdergetter.getOrThrow(NoiseRouterData.RIDGES_FOLDED))
    pKey.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, CP.point(0.0F), this.FULL_RANGE, 0.01F), Biomes.PLAINS))
    val cubicspline = TerrainProvider.buildErosionOffsetSpline(densityfunctions$spline$coordinate1, densityfunctions$spline$coordinate2, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, ToFloatFunction.IDENTITY)

    doIf[CubicSpline.Multipoint[_, _]](cubicspline, multipoint => {
      var resourcekey = Biomes.DESERT
      for (f <- multipoint.locations) {
        pKey.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, CP.point(f), CP.point(0.0F), this.FULL_RANGE, 0.0F), resourcekey))
        resourcekey = if (resourcekey eq Biomes.DESERT) Biomes.BADLANDS
        else Biomes.DESERT
      }
    })

    val cubicspline1 = TerrainProvider.overworldOffset(densityfunctions$spline$coordinate, densityfunctions$spline$coordinate1, densityfunctions$spline$coordinate2, false)
    doIf[CubicSpline.Multipoint[_, _]](cubicspline1, multipoint => {
      for (f1 <- multipoint.locations) {
        pKey.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, CP.point(f1), this.FULL_RANGE, CP.point(0.0F), this.FULL_RANGE, 0.0F), Biomes.SNOWY_TAIGA))
      }
    })
  }


  private def addOffCoastBiomes(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]]): Unit = {
    this.addSurfaceBiome(pConsumer, this.FULL_RANGE, this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS)
    for (i <- 0 until this.temperatures.length) {
      val climate$parameter = this.temperatures(i)
      this.addSurfaceBiome(pConsumer, climate$parameter, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS(0)(i))
      this.addSurfaceBiome(pConsumer, climate$parameter, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS(1)(i))
    }
  }

  private def addInlandBiomes(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]]): Unit = {
    this.addMidSlice(pConsumer, CP.span(-1.0F, -0.93333334F))
    this.addHighSlice(pConsumer, CP.span(-0.93333334F, -0.7666667F))
    this.addPeaks(pConsumer, CP.span(-0.7666667F, -0.56666666F))
    this.addHighSlice(pConsumer, CP.span(-0.56666666F, -0.4F))
    this.addMidSlice(pConsumer, CP.span(-0.4F, -0.26666668F))
    this.addLowSlice(pConsumer, CP.span(-0.26666668F, -0.05F))
    this.addValleys(pConsumer, CP.span(-0.05F, 0.05F))
    this.addLowSlice(pConsumer, CP.span(0.05F, 0.26666668F))
    this.addMidSlice(pConsumer, CP.span(0.26666668F, 0.4F))
    this.addHighSlice(pConsumer, CP.span(0.4F, 0.56666666F))
    this.addPeaks(pConsumer, CP.span(0.56666666F, 0.7666667F))
    this.addHighSlice(pConsumer, CP.span(0.7666667F, 0.93333334F))
    this.addMidSlice(pConsumer, CP.span(0.93333334F, 1.0F))
  }

  private def addPeaks(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pParam: CP): Unit = {
    for (i <- this.temperatures.indices) {
      val climate$parameter = this.temperatures(i)
      for (j <- this.humidities.indices) {
        val climate$parameter1 = this.humidities(j)
        val resourcekey = this.pickMiddleBiome(i, j, pParam)
        val resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(i, j, pParam)
        val resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, pParam)
        val resourcekey3 = this.pickPlateauBiome(i, j, pParam)
        val resourcekey4 = this.pickShatteredBiome(i, j, pParam)
        val resourcekey5 = this.maybePickWindsweptSavannaBiome(i, j, pParam, resourcekey4)
        val resourcekey6 = this.pickPeakBiome(i, j, pParam)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(0), pParam, 0.0F, resourcekey6)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions(1), pParam, 0.0F, resourcekey2)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(1), pParam, 0.0F, resourcekey6)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), CP.span(this.erosions(2), this.erosions(3)), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(2), pParam, 0.0F, resourcekey3)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.midInlandContinentalness, this.erosions(3), pParam, 0.0F, resourcekey1)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions(3), pParam, 0.0F, resourcekey3)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(4), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey5)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey4)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, resourcekey)
      }
    }
  }

  private def addHighSlice(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pParam: CP): Unit = {
    for (i <- this.temperatures.indices) {
      val climate$parameter = this.temperatures(i)
      for (j <- this.humidities.indices) {
        val climate$parameter1 = this.humidities(j)
        val resourcekey = this.pickMiddleBiome(i, j, pParam)
        val resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(i, j, pParam)
        val resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, pParam)
        val resourcekey3 = this.pickPlateauBiome(i, j, pParam)
        val resourcekey4 = this.pickShatteredBiome(i, j, pParam)
        val resourcekey5 = this.maybePickWindsweptSavannaBiome(i, j, pParam, resourcekey)
        val resourcekey6 = this.pickSlopeBiome(i, j, pParam)
        val resourcekey7 = this.pickPeakBiome(i, j, pParam)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions(0), pParam, 0.0F, resourcekey6)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(0), pParam, 0.0F, resourcekey7)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions(1), pParam, 0.0F, resourcekey2)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(1), pParam, 0.0F, resourcekey6)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), CP.span(this.erosions(2), this.erosions(3)), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(2), pParam, 0.0F, resourcekey3)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.midInlandContinentalness, this.erosions(3), pParam, 0.0F, resourcekey1)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions(3), pParam, 0.0F, resourcekey3)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(4), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey5)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey4)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, resourcekey)
      }
    }
  }

  private def addMidSlice(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pParam: CP): Unit = {
    this.addSurfaceBiome(pConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, CP.span(this.erosions(0), this.erosions(2)), pParam, 0.0F, Biomes.STONY_SHORE)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(1), this.temperatures(2)), this.FULL_RANGE, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.SWAMP)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(3), this.temperatures(4)), this.FULL_RANGE, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.MANGROVE_SWAMP)
    for (i <- this.temperatures.indices) {
      val climate$parameter = this.temperatures(i)
      for (j <- this.humidities.indices) {
        val climate$parameter1 = this.humidities(j)
        val resourcekey = this.pickMiddleBiome(i, j, pParam)
        val resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(i, j, pParam)
        val resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, pParam)
        val resourcekey3 = this.pickShatteredBiome(i, j, pParam)
        val resourcekey4 = this.pickPlateauBiome(i, j, pParam)
        val resourcekey5 = this.pickBeachBiome(i, j)
        val resourcekey6 = this.maybePickWindsweptSavannaBiome(i, j, pParam, resourcekey)
        val resourcekey7 = this.pickShatteredCoastBiome(i, j, pParam)
        val resourcekey8 = this.pickSlopeBiome(i, j, pParam)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(0), pParam, 0.0F, resourcekey8)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions(1), pParam, 0.0F, resourcekey2)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions(1), pParam, 0.0F, if (i == 0) resourcekey8
        else resourcekey4)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions(2), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.midInlandContinentalness, this.erosions(2), pParam, 0.0F, resourcekey1)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions(2), pParam, 0.0F, resourcekey4)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions(3), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(3), pParam, 0.0F, resourcekey1)
        if (pParam.max < 0L) {
          this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(4), pParam, 0.0F, resourcekey5)
          this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(4), pParam, 0.0F, resourcekey)
        }
        else this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions(4), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(5), pParam, 0.0F, resourcekey7)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions(5), pParam, 0.0F, resourcekey6)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey3)
        if (pParam.max < 0L) this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(6), pParam, 0.0F, resourcekey5)
        else this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(6), pParam, 0.0F, resourcekey)
        if (i == 0) this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, resourcekey)
      }
    }
  }

  private def addLowSlice(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pParam: CP): Unit = {
    this.addSurfaceBiome(pConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, CP.span(this.erosions(0), this.erosions(2)), pParam, 0.0F, Biomes.STONY_SHORE)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(1), this.temperatures(2)), this.FULL_RANGE, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.SWAMP)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(3), this.temperatures(4)), this.FULL_RANGE, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.MANGROVE_SWAMP)
    for (i <- this.temperatures.indices) {
      val climate$parameter = this.temperatures(i)
      for (j <- this.humidities.indices) {
        val climate$parameter1 = this.humidities(j)
        val resourcekey: ResourceKey[Biome] = this.pickMiddleBiome(i, j, pParam)
        val resourcekey1: ResourceKey[Biome] = this.pickMiddleBiomeOrBadlandsIfHot(i, j, pParam)
        val resourcekey2: ResourceKey[Biome] = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, pParam)
        val resourcekey3: ResourceKey[Biome] = this.pickBeachBiome(i, j)
        val resourcekey4: ResourceKey[Biome] = this.maybePickWindsweptSavannaBiome(i, j, pParam, resourcekey)
        val resourcekey5: ResourceKey[Biome] = this.pickShatteredCoastBiome(i, j, pParam)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, resourcekey1)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, resourcekey2)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, CP.span(this.erosions(2), this.erosions(3)), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), CP.span(this.erosions(2), this.erosions(3)), pParam, 0.0F, resourcekey1)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, CP.span(this.erosions(3), this.erosions(4)), pParam, 0.0F, resourcekey3)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(4), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(5), pParam, 0.0F, resourcekey5)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions(5), pParam, 0.0F, resourcekey4)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions(5), pParam, 0.0F, resourcekey)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions(6), pParam, 0.0F, resourcekey3)
        if (i == 0) this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, resourcekey)
      }
    }
  }

  private def addValleys(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pParam: CP): Unit = {
    this.addSurfaceBiome(pConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, if (pParam.max < 0L) Biomes.STONY_SHORE
    else Biomes.FROZEN_RIVER)
    this.addSurfaceBiome(pConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, if (pParam.max < 0L) Biomes.STONY_SHORE
    else Biomes.RIVER)
    this.addSurfaceBiome(pConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, Biomes.FROZEN_RIVER)
    this.addSurfaceBiome(pConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, Biomes.RIVER)
    this.addSurfaceBiome(pConsumer, this.FROZEN_RANGE, this.FULL_RANGE, CP.span(this.coastContinentalness, this.farInlandContinentalness), CP.span(this.erosions(2), this.erosions(5)), pParam, 0.0F, Biomes.FROZEN_RIVER)
    this.addSurfaceBiome(pConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, CP.span(this.coastContinentalness, this.farInlandContinentalness), CP.span(this.erosions(2), this.erosions(5)), pParam, 0.0F, Biomes.RIVER)
    this.addSurfaceBiome(pConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions(6), pParam, 0.0F, Biomes.FROZEN_RIVER)
    this.addSurfaceBiome(pConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions(6), pParam, 0.0F, Biomes.RIVER)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(1), this.temperatures(2)), this.FULL_RANGE, CP.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.SWAMP)
    this.addSurfaceBiome(pConsumer, CP.span(this.temperatures(3), this.temperatures(4)), this.FULL_RANGE, CP.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.MANGROVE_SWAMP)
    this.addSurfaceBiome(pConsumer, this.FROZEN_RANGE, this.FULL_RANGE, CP.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions(6), pParam, 0.0F, Biomes.FROZEN_RIVER)
    for (i <- this.temperatures.indices) {
      val climate$parameter = this.temperatures(i)
      for (j <- this.humidities.indices) {
        val climate$parameter1 = this.humidities(j)
        val resourcekey = this.pickMiddleBiomeOrBadlandsIfHot(i, j, pParam)
        this.addSurfaceBiome(pConsumer, climate$parameter, climate$parameter1, CP.span(this.midInlandContinentalness, this.farInlandContinentalness), CP.span(this.erosions(0), this.erosions(1)), pParam, 0.0F, resourcekey)
      }
    }
  }

  private def addUndergroundBiomes(pConsume: Consumer[Pair[CPPoint, ResourceKey[Biome]]]): Unit = {
    this.addUndergroundBiome(pConsume, this.FULL_RANGE, this.FULL_RANGE, CP.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES)
    this.addUndergroundBiome(pConsume, this.FULL_RANGE, CP.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES)
    this.addBottomBiome(pConsume, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, CP.span(this.erosions(0), this.erosions(1)), this.FULL_RANGE, 0.0F, Biomes.DEEP_DARK)
  }

  private def pickMiddleBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pParam.max < 0L) this.MIDDLE_BIOMES(pTemperature)(pHumidity)
  else {
    val resourcekey = this.MIDDLE_BIOMES_VARIANT(pTemperature)(pHumidity)
    if (resourcekey == null) this.MIDDLE_BIOMES(pTemperature)(pHumidity)
    else resourcekey
  }

  private def pickMiddleBiomeOrBadlandsIfHot(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pTemperature == 4) this.pickBadlandsBiome(pHumidity, pParam)
  else this.pickMiddleBiome(pTemperature, pHumidity, pParam)

  private def pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pTemperature == 0) this.pickSlopeBiome(pTemperature, pHumidity, pParam)
  else this.pickMiddleBiomeOrBadlandsIfHot(pTemperature, pHumidity, pParam)

  private def maybePickWindsweptSavannaBiome(pTemperature: Int, pHumidity: Int, pParam: CP, pKey: ResourceKey[Biome]): ResourceKey[Biome] = if (pTemperature > 1 && pHumidity < 4 && pParam.max >= 0L) Biomes.WINDSWEPT_SAVANNA
  else pKey

  private def pickShatteredCoastBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = {
    val resourcekey = if (pParam.max >= 0L) this.pickMiddleBiome(pTemperature, pHumidity, pParam)
    else this.pickBeachBiome(pTemperature, pHumidity)
    this.maybePickWindsweptSavannaBiome(pTemperature, pHumidity, pParam, resourcekey)
  }

  private def pickBeachBiome(pTemperature: Int, pHumidity: Int): ResourceKey[Biome] = if (pTemperature == 0) Biomes.SNOWY_BEACH
  else if (pTemperature == 4) Biomes.DESERT
  else Biomes.BEACH

  private def pickBadlandsBiome(pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pHumidity < 2) if (pParam.max < 0L) Biomes.BADLANDS
  else Biomes.ERODED_BADLANDS
  else if (pHumidity < 3) Biomes.BADLANDS
  else Biomes.WOODED_BADLANDS

  private def pickPlateauBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = {
    if (pParam.max >= 0L) {
      val resourcekey = this.PLATEAU_BIOMES_VARIANT(pTemperature)(pHumidity)
      if (resourcekey != null) return resourcekey
    }
    this.PLATEAU_BIOMES(pTemperature)(pHumidity)
  }

  private def pickPeakBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pTemperature <= 2) if (pParam.max < 0L) Biomes.JAGGED_PEAKS
  else Biomes.FROZEN_PEAKS
  else if (pTemperature == 3) Biomes.STONY_PEAKS
  else this.pickBadlandsBiome(pHumidity, pParam)

  private def pickSlopeBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = if (pTemperature >= 3) this.pickPlateauBiome(pTemperature, pHumidity, pParam)
  else if (pHumidity <= 1) Biomes.SNOWY_SLOPES
  else Biomes.GROVE

  private def pickShatteredBiome(pTemperature: Int, pHumidity: Int, pParam: CP): ResourceKey[Biome] = {
    val resourcekey = this.SHATTERED_BIOMES(pTemperature)(pHumidity)
    if (resourcekey == null) this.pickMiddleBiome(pTemperature, pHumidity, pParam)
    else resourcekey
  }

  private def addSurfaceBiome(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pTemperature: CP, pHumidity: CP, pContinentalness: CP, pErosion: CP, pDepth: CP, pWeirdness: Float, pKey: ResourceKey[Biome]): Unit = {
    pConsumer.accept(Pair.of(Climate.parameters(pTemperature, pHumidity, pContinentalness, pErosion, CP.point(0.0F), pDepth, pWeirdness), pKey))
    pConsumer.accept(Pair.of(Climate.parameters(pTemperature, pHumidity, pContinentalness, pErosion, CP.point(1.0F), pDepth, pWeirdness), pKey))
  }

  private def addUndergroundBiome(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pTemperature: CP, pHumidity: CP, pContinentalness: CP, pErosion: CP, pDepth: CP, pWeirdness: Float, pKey: ResourceKey[Biome]): Unit = {
    pConsumer.accept(Pair.of(Climate.parameters(pTemperature, pHumidity, pContinentalness, pErosion, CP.span(0.2F, 0.9F), pDepth, pWeirdness), pKey))
  }

  private def addBottomBiome(pConsumer: Consumer[Pair[CPPoint, ResourceKey[Biome]]], pTemerature: CP, pHumidity: CP, pContinentalness: CP, pErosion: CP, pDepth: CP, pWeirdness: Float, pKey: ResourceKey[Biome]): Unit = {
    pConsumer.accept(Pair.of(Climate.parameters(pTemerature, pHumidity, pContinentalness, pErosion, CP.point(1.1F), pDepth, pWeirdness), pKey))
  }

  def getDebugStringForContinentalness(pContinentalness: Double): String = {
    val d0 = Climate.quantizeCoord(pContinentalness.toFloat).toDouble
    if (d0 < this.mushroomFieldsContinentalness.max.toDouble) "Mushroom fields"
    else if (d0 < this.deepOceanContinentalness.max.toDouble) "Deep ocean"
    else if (d0 < this.oceanContinentalness.max.toDouble) "Ocean"
    else if (d0 < this.coastContinentalness.max.toDouble) "Coast"
    else if (d0 < this.nearInlandContinentalness.max.toDouble) "Near inland"
    else if (d0 < this.midInlandContinentalness.max.toDouble) "Mid inland"
    else "Far inland"
  }

  def getDebugStringForErosion(pErosion: Double): String = PinkBiomeBuilder.getDebugStringForNoiseValue(pErosion, this.erosions)

  def getDebugStringForTemperature(pTemperature: Double): String = PinkBiomeBuilder.getDebugStringForNoiseValue(pTemperature, this.temperatures)

  def getDebugStringForHumidity(pHumidity: Double): String = PinkBiomeBuilder.getDebugStringForNoiseValue(pHumidity, this.humidities)

  @VisibleForDebug def getTemperatureThresholds: Array[CP] = this.temperatures

  @VisibleForDebug def getHumidityThresholds: Array[CP] = this.humidities

  @VisibleForDebug def getErosionThresholds: Array[CP] = this.erosions

  @VisibleForDebug def getContinentalnessThresholds: Array[CP] = Array[CP](this.mushroomFieldsContinentalness, this.deepOceanContinentalness, this.oceanContinentalness, this.coastContinentalness, this.nearInlandContinentalness, this.midInlandContinentalness, this.farInlandContinentalness)

  @VisibleForDebug def getPeaksAndValleysThresholds: Array[CP] = Array[CP](CP.span(-2.0F, NoiseRouterData.peaksAndValleys(0.05F)), CP.span(NoiseRouterData.peaksAndValleys(0.05F), NoiseRouterData.peaksAndValleys(0.26666668F)), CP.span(NoiseRouterData.peaksAndValleys(0.26666668F), NoiseRouterData.peaksAndValleys(0.4F)), CP.span(NoiseRouterData.peaksAndValleys(0.4F), NoiseRouterData.peaksAndValleys(0.56666666F)), CP.span(NoiseRouterData.peaksAndValleys(0.56666666F), 2.0F))

  @VisibleForDebug def getWeirdnessThresholds: Array[CP] = Array[CP](CP.span(-2.0F, 0.0F), CP.span(0.0F, 2.0F))
}