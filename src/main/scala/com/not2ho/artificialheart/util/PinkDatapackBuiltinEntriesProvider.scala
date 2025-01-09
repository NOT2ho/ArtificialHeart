package com.not2ho.artificialheart.util


import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.util.PinkDatapackBuiltinEntriesProvider.BUILDER
import com.not2ho.artificialheart.worldgen.{PinkConfiguredFeatures, PinkPlacedFeatures}
import net.minecraft.core.HolderLookup.Provider
import net.minecraft.core.{HolderLookup, RegistrySetBuilder}
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider

import java.util
import java.util.concurrent.CompletableFuture


object PinkDatapackBuiltinEntriesProvider {
  val BUILDER: RegistrySetBuilder = new RegistrySetBuilder()
    .add(Registries.CONFIGURED_FEATURE, PinkConfiguredFeatures.bootstrap)
    .add(Registries.PLACED_FEATURE, PinkPlacedFeatures.bootstrap)
}

class PinkDatapackBuiltinEntriesProvider(output: PackOutput, provider: CompletableFuture[HolderLookup.Provider])
  extends DatapackBuiltinEntriesProvider(output, provider, BUILDER, util.Set.of(ArtificialHeart.MOD_ID)) {
}