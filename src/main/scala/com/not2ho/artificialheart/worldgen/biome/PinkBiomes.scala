package com.not2ho.artificialheart.worldgen.biome

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.worldgen.PinkPlacedFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Musics
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.levelgen.GenerationStep


object PinkBiomes {
  val PINK_AREA: ResourceKey[Biome] = ResourceKey.create(Registries.BIOME, new ResourceLocation(ArtificialHeart.MOD_ID, "pink_area"))
  val KIRAKIRA_ZONE: ResourceKey[Biome] = ResourceKey.create( Registries.BIOME, new ResourceLocation( ArtificialHeart.MOD_ID, "kirakira_zone" ) )

  def bootstrap(context: BootstapContext[Biome]): Unit = {
    context.register(PINK_AREA, pinkArea(context))
    context.register( KIRAKIRA_ZONE, kirakiraZone( context ) )
  }

  def globalOverworldGeneration(builder: BiomeGenerationSettings.Builder): Unit = {
    BiomeDefaultFeatures.addDefaultCarversAndLakes(builder)
    BiomeDefaultFeatures.addDefaultCrystalFormations(builder)
    BiomeDefaultFeatures.addDefaultMonsterRoom(builder)
    BiomeDefaultFeatures.addDefaultUndergroundVariety(builder)
    BiomeDefaultFeatures.addDefaultSprings(builder)
    BiomeDefaultFeatures.addSurfaceFreezing(builder)
  }

  def pinkArea(context: BootstapContext[Biome]): Biome = {
    val spawnBuilder = new MobSpawnSettings.Builder
    spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4))
    BiomeDefaultFeatures.farmAnimals(spawnBuilder)
    BiomeDefaultFeatures.commonSpawns(spawnBuilder)
    val biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER))
    globalOverworldGeneration(biomeBuilder)
    BiomeDefaultFeatures.addForestFlowers(biomeBuilder)
    BiomeDefaultFeatures.addFerns(biomeBuilder)
    BiomeDefaultFeatures.addDefaultOres(biomeBuilder)
    biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS)
    biomeBuilder.addFeature( GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_CHERRY )
    biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PinkPlacedFeatures.PINK_TREE_PLACED_KEY)
    biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST)
    BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder)
    BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder)
    new Biome.BiomeBuilder()
      .hasPrecipitation(true)
      .downfall(0.8f).temperature(0.7f)
      .generationSettings(biomeBuilder.build)
      .mobSpawnSettings(spawnBuilder.build)
      .specialEffects((new BiomeSpecialEffects.Builder)
                        .waterColor(0xe2baed)
                        .waterFogColor(0xfae0e0)
                        .skyColor(0xe2baed)
                        .grassColorOverride(ArtificialHeart.subedGreen)
                        .foliageColorOverride(0xfae0e0)
                        .fogColor(0xfae0e0)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
//                        .backgroundMusic(Musics.createGameMusic(ModSounds.BAR_BRAWL.getHolder.get)).build)
                      .build).build
  }

  def kirakiraZone( context : BootstapContext[Biome] ) : Biome = {
    val spawnBuilder = new MobSpawnSettings.Builder
    spawnBuilder.addSpawn( MobCategory.CREATURE, new MobSpawnSettings.SpawnerData( EntityType.CAT, 5, 4, 4 ) )
    BiomeDefaultFeatures.farmAnimals( spawnBuilder )
    BiomeDefaultFeatures.commonSpawns( spawnBuilder )
    val biomeBuilder = new BiomeGenerationSettings.Builder( context.lookup( Registries.PLACED_FEATURE ), context.lookup( Registries.CONFIGURED_CARVER ) )
    globalOverworldGeneration( biomeBuilder )
    BiomeDefaultFeatures.addForestFlowers( biomeBuilder )
    BiomeDefaultFeatures.addSculk( biomeBuilder )
    BiomeDefaultFeatures.addRareBerryBushes(biomeBuilder)
    BiomeDefaultFeatures.addDefaultOres( biomeBuilder )
    BiomeDefaultFeatures.addBlueIce(biomeBuilder)
    biomeBuilder.addFeature( GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS )
    biomeBuilder.addFeature( GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_JUNGLE )
    biomeBuilder.addFeature( GenerationStep.Decoration.VEGETAL_DECORATION, PinkPlacedFeatures.PINK_TREE_PLACED_KEY )
    biomeBuilder.addFeature( GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST )
    BiomeDefaultFeatures.addDefaultMushrooms( biomeBuilder )
    BiomeDefaultFeatures.addDefaultExtraVegetation( biomeBuilder )
    new Biome.BiomeBuilder()
      .hasPrecipitation( false )
      .downfall( 2f )
      .temperature( 1f )
      .generationSettings( biomeBuilder.build )
      .mobSpawnSettings( spawnBuilder.build )
      .specialEffects( (new BiomeSpecialEffects.Builder)
                         .waterColor( 0xffffff )
                         .waterFogColor( 0xffffff )
                         .skyColor( 0xffffff )
                         .grassColorOverride(0xffffff)
                         .foliageColorOverride( 0xffffff )
                         .fogColor( 0xffffff )
                         .ambientMoodSound( AmbientMoodSettings.LEGACY_CAVE_SETTINGS )
                         .build
                                                                                                                                                                                           ).build
  }
}