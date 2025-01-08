package com.not2ho.artificialheart.item

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.util.PinkTags
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.ForgeTier
import net.minecraftforge.common.TierSortingRegistry

import java.util


object PinkToolTiers {
  val HEART: Tier = TierSortingRegistry.registerTier(
    new ForgeTier(
      3, 300, 5f, 10f, 25, PinkTags.Blocks.NEEDS_HEART_TOOL
      , () => Ingredient.of(ArtificialHeart.HEART_INGOT.get))
    , new ResourceLocation(ArtificialHeart.MOD_ID, "heart")
    , util.List.of(Tiers.IRON), util.List.of(Tiers.DIAMOND))
}