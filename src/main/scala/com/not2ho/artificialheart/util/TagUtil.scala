package com.not2ho.artificialheart.util

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.armortrim.TrimMaterial
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraftforge.registries.ForgeRegistries


/**
 * A class containing some simple methods for making tags.
 *
 * @author bageldotjpg
 */
object TagUtil {
  def blockTag(modid: String, name: String): TagKey[Block] = TagKey.create(Registries.BLOCK, new ResourceLocation(modid, name))

  def itemTag(modid: String, name: String): TagKey[Item] = TagKey.create(Registries.ITEM, new ResourceLocation(modid, name))

  def entityTypeTag(modid: String, name: String): TagKey[EntityType[_]] = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(modid, name))

  def enchantmentTag(modid: String, name: String): TagKey[Enchantment] = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(modid, name))

  def potionTag(modid: String, name: String): TagKey[Potion] = TagKey.create(Registries.POTION, new ResourceLocation(modid, name))

  def blockEntityTypeTag(modid: String, name: String): TagKey[BlockEntityType[_]] = TagKey.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation(modid, name))

  def mobEffectTag(modid: String, name: String): TagKey[MobEffect] = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(modid, name))

  def damageTypeTag(modid: String, name: String): TagKey[DamageType] = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modid, name))

  def trimMaterialTag(modid: String, name: String): TagKey[TrimMaterial] = TagKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(modid, name))

  def biomeTag(modid: String, name: String): TagKey[Biome] = TagKey.create(Registries.BIOME, new ResourceLocation(modid, name))

  def isTagged(biome: Biome, tagKey: TagKey[Biome]): Boolean = ForgeRegistries.BIOMES.tags.getTag(tagKey).contains(biome)

  def dimensionTag(modid: String, name: String): TagKey[Level] = TagKey.create(Registries.DIMENSION, new ResourceLocation(modid, name))

  def dimensionTypeTag(modid: String, name: String): TagKey[DimensionType] = TagKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(modid, name))

  def configuredFeatureTag(modid: String, name: String): TagKey[ConfiguredFeature[_, _]] = TagKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(modid, name))

  def placedFeatureTag(modid: String, name: String): TagKey[PlacedFeature] = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(modid, name))

  def structureTag(modid: String, name: String): TagKey[Structure] = TagKey.create(Registries.STRUCTURE, new ResourceLocation(modid, name))

  def configuredCarverTag(modid: String, name: String): TagKey[ConfiguredWorldCarver[_]] = TagKey.create(Registries.CONFIGURED_CARVER, new ResourceLocation(modid, name))
}