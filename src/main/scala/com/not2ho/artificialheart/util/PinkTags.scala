package com.not2ho.artificialheart.util

import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block


object PinkTags {
  object Blocks {
    val NEEDS_HEART_TOOL: TagKey[Block] = tag("needs_heart_tool")

    private def tag(name: String) = BlockTags.create(new ResourceLocation(ArtificialHeart.MOD_ID, name))
  }

  object Items {
    private def tag(name: String) = ItemTags.create(new ResourceLocation(ArtificialHeart.MOD_ID, name))
  }
}