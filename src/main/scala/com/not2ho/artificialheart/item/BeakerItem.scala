package com.not2ho.artificialheart.item

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.item.PinkItems.BEAKER
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.PotionUtils.*
import net.minecraft.world.item.{Item, ItemStack, Items, PotionItem}
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

import scala.jdk.CollectionConverters.*

class BeakerItem(pProperties: Properties) extends PotionItem(pProperties) {
  override def finishUsingItem(pStack: ItemStack, pLevel: Level, pEntityLiving: LivingEntity): ItemStack = {
    val player = if (pEntityLiving.isInstanceOf[Player]) pEntityLiving.asInstanceOf[Player]
    else null
    if (player.isInstanceOf[ServerPlayer]) CriteriaTriggers.CONSUME_ITEM.trigger(player.asInstanceOf[ServerPlayer], pStack)
    if (!pLevel.isClientSide) {
      for (mobeffectinstance <- getMobEffects(pStack).asScala) {
        if (mobeffectinstance.getEffect.isInstantenous) mobeffectinstance.getEffect.applyInstantenousEffect(player, player, pEntityLiving, mobeffectinstance.getAmplifier, 1.0D)
        else pEntityLiving.addEffect(new MobEffectInstance(mobeffectinstance))
      }
    }
    if (player != null) {
      player.awardStat(Stats.ITEM_USED.get(this))
      if (!player.getAbilities.instabuild) pStack.shrink(1)
    }
    if (player == null || !player.getAbilities.instabuild) {
      if (pStack.isEmpty) return new ItemStack(BEAKER.get())
      if (player != null) player.getInventory.add(new ItemStack(BEAKER.get()))
    }
    pEntityLiving.gameEvent(GameEvent.DRINK)
    pStack
  }


}

