package com.not2ho.artificialheart

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.config.ModConfigEvent
import net.minecraftforge.registries.ForgeRegistries

import scala.jdk.CollectionConverters.*

@Mod.EventBusSubscriber(modid = ArtificialHeart.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object Config {
  private val BUILDER: ForgeConfigSpec.Builder = new ForgeConfigSpec.Builder()


  val SPEC: ForgeConfigSpec = BUILDER.build

  @SubscribeEvent
  def onLoad(event: ModConfigEvent): Unit = {
  }
}
