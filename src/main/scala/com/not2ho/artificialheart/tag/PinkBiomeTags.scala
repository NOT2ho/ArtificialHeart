package com.not2ho.artificialheart.tag
import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome


object PinkBiomeTags {
  val HAS_MAPLE_HUT: TagKey[Biome] = biomeTag("has_structure/maple_hut")
  val HAS_SNAIL: TagKey[Biome] = biomeTag("has_animal/snail")
  val HAS_TURKEY: TagKey[Biome] = biomeTag("has_animal/turkey")
  val HAS_MAPLE_TREE: TagKey[Biome] = biomeTag("has_feature/maple_tree")
  val HAS_YELLOW_MAPLE_TREE: TagKey[Biome] = biomeTag("has_feature/spotted_maple_tree/yellow")
  val HAS_ORANGE_MAPLE_TREE: TagKey[Biome] = biomeTag("has_feature/spotted_maple_tree/orange")
  val HAS_RED_MAPLE_TREE: TagKey[Biome] = biomeTag("has_feature/spotted_maple_tree/red")

  private def biomeTag(name: String) = TagKey.create(Registries.BIOME, new ResourceLocation(ArtificialHeart.MOD_ID, name))
}