package com.not2ho.artificialheart.register

import com.not2ho.artificialheart.ArtificialHeart
import PinkDatapackBuiltinEntriesProvider.BUILDER
import com.not2ho.artificialheart.worldgen.biome.PinkBiomes
import com.not2ho.artificialheart.worldgen.dimension.PinkDimensions
import com.not2ho.artificialheart.worldgen.dimension.noise.{PinkNoiseGenSettings, PinkNoiseRouterData}
import com.not2ho.artificialheart.worldgen.{PinkConfiguredFeatures, PinkPlacedFeatures}
import net.minecraft.core.HolderLookup.Provider
import net.minecraft.core.registries.Registries
import net.minecraft.core.{HolderLookup, RegistrySetBuilder}
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider

import java.util.Set
import java.util.concurrent.CompletableFuture


object PinkDatapackBuiltinEntriesProvider {
  val BUILDER: RegistrySetBuilder = new RegistrySetBuilder()
    .add(Registries.CONFIGURED_FEATURE, PinkConfiguredFeatures.bootstrap)
    .add(Registries.PLACED_FEATURE, PinkPlacedFeatures.bootstrap)
    .add(Registries.BIOME, PinkBiomes.bootstrap)
    .add(Registries.LEVEL_STEM, PinkDimensions.bootstrapStem)
    .add(Registries.DENSITY_FUNCTION, PinkNoiseRouterData.bootstrap )
    .add(Registries.NOISE_SETTINGS, PinkNoiseGenSettings.bootstrap )
    .add( Registries.DIMENSION_TYPE, PinkDimensions.bootstrapType )
}

class PinkDatapackBuiltinEntriesProvider(output: PackOutput, provider: CompletableFuture[HolderLookup.Provider])
  extends DatapackBuiltinEntriesProvider(output, provider, BUILDER, Set.of(ArtificialHeart.MOD_ID)) {
}