package com.not2ho.artificialheart.worldgen.dimension

import com.mojang.datafixers.util.Pair
import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.worldgen.biome.PinkBiomes
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.{Level, LevelReader}
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import com.not2ho.artificialheart.util.InstanceOfPatternMatchingFuntionalizer
import com.not2ho.artificialheart.worldgen.dimension.noise.PinkNoiseGenSettings

import java.util
import java.util.{List, OptionalLong}


object PinkDimensions {
  val STRAWBERRYDAYS_KEY: ResourceKey[LevelStem] = ResourceKey.create(Registries.LEVEL_STEM, new ResourceLocation
    (ArtificialHeart.MOD_ID, "strawberrydays"))
  val STRAWBERRYDAYS_LEVEL_KEY: ResourceKey[Level] = ResourceKey.create(Registries.DIMENSION, new ResourceLocation
    (ArtificialHeart.MOD_ID, "strawberrydays"))
  val STRAWBERRY_DAYS_TYPE: ResourceKey[DimensionType] = ResourceKey.create(Registries.DIMENSION_TYPE, new
      ResourceLocation(ArtificialHeart.MOD_ID, "strawberrydays_type"))
  val SEA_LEVEL = 70

  def bootstrapType(context: BootstapContext[DimensionType]): Unit = {
    context.register(STRAWBERRY_DAYS_TYPE, new DimensionType(OptionalLong.empty(), // fixedTime
                                                        true, // hasSkylight
                                                        false, // hasCeiling
                                                        false, // ultraWarm
                                                        true, // natural
                                                        1.0, // coordinateScale
                                                        true, // bedWorks
                                                        false, // respawnAnchorWorks
                                                        0, // minY
                                                        256, // height
                                                        256, // logicalHeight
                                                        BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                                                        BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                                                        1.0f, // ambientLight
                                                        new DimensionType.MonsterSettings(false, false, ConstantInt
                                                          .of(0), 0)
                                                        )
                     )
  }

  def bootstrapStem(context: BootstapContext[LevelStem]): Unit = {
    val biomeRegistry = context.lookup(Registries.BIOME)
    val dimTypes = context.lookup(Registries.DIMENSION_TYPE)
    val noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS)
    val wrappedChunkGenerator = new NoiseBasedChunkGenerator(new FixedBiomeSource
      (biomeRegistry.getOrThrow(PinkBiomes.PINK_AREA)), noiseGenSettings.getOrThrow(PinkNoiseGenSettings.STRAWBERRY_NOISE))
    val noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(MultiNoiseBiomeSource
                                                                  .createFromList(
                                                                    new Climate.ParameterList(
                                                                      util.List.of(
                                                                        Pair.of(
                                                                          Climate.parameters(0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.0F, 0.0F)
                                                                          , biomeRegistry.getOrThrow(PinkBiomes.PINK_AREA)
                                                                          )
                                                                        , Pair.of(Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F)
                                                                                  , biomeRegistry.getOrThrow(PinkBiomes.PINK_AREA)
                                                                                  )
                                                                        , Pair.of(Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F)
                                                                                  , biomeRegistry.getOrThrow( PinkBiomes.KIRAKIRA_ZONE )
                                                                                  )
                                                                        , Pair.of(Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F)
                                                                                  , biomeRegistry.getOrThrow( PinkBiomes.KIRAKIRA_ZONE )
                                                                                  )
                                                                        )
                                                                      )
                                                                    ),
                                                                noiseGenSettings.getOrThrow(PinkNoiseGenSettings.STRAWBERRY_NOISE)
                                                                )
    val stem = new LevelStem(dimTypes.getOrThrow(PinkDimensions.STRAWBERRY_DAYS_TYPE), noiseBasedChunkGenerator)
    context.register(STRAWBERRYDAYS_KEY, stem)
  }

  def getSeaLevel( reader : LevelReader ) : Int = {
    reader match
      case serverLevel: ServerLevel =>
        val chunkProvider = serverLevel.getChunkSource
        return chunkProvider.getGenerator.getSeaLevel

      case _ =>
        InstanceOfPatternMatchingFuntionalizer.doIf[Level](
          reader, level => if level.dimension eq STRAWBERRYDAYS_LEVEL_KEY then return SEA_LEVEL)
    reader.getSeaLevel
  }
}
