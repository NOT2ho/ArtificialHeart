package com.not2ho.artificialheart.worldgen.dimension

import com.not2ho.artificialheart.block.PinkPortalBlock
import com.not2ho.artificialheart.util.InstanceOfPatternMatchingFuntionalizer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids
import net.minecraftforge.common.util.ITeleporter

import java.util.function.Function
import scala.annotation.targetName
import scala.jdk.CollectionConverters.*

object PinkPortals {
  var thisPos: BlockPos = BlockPos.ZERO
  var insideDimension = true
}

class PinkPortals( pos: BlockPos, insideDim: Boolean) extends ITeleporter {
//  PinkPortals.thisPos = pos
//  PinkPortals.insideDimension = insideDim
//
//
//  def placeEntity(entity: Entity, currentWorld: ServerLevel, destinationWorld: ServerLevel, yaw: Float,
//                           repositionEntity: Function[Boolean, Entity]): Entity = {
//    val fEntity = repositionEntity.apply(false)
//    var y = 61
//    if (!PinkPortals.insideDimension) y = PinkPortals.thisPos.getY
//    var destinationPos = new BlockPos(PinkPortals.thisPos.getX, y, PinkPortals.thisPos.getZ)
//    var tries = 0
//    while (
//      checkCondition(destinationWorld, destinationPos)
//      && checkCondition(destinationWorld, destinationPos.above)
//      && (tries < 25)
//    ) {
//      destinationPos = destinationPos.above(2)
//      tries += 1
//    }
//    fEntity.setPos(destinationPos.getX, destinationPos.getY, destinationPos.getZ)
//    if (PinkPortals.insideDimension) {
//      var doSetBlock = true
//      for (checkPos <- BlockPos.betweenClosed(destinationPos.below(10).west(10), destinationPos.above(10).east(10)).asScala) {
//        InstanceOfPatternMatchingFuntionalizer.doIf[PinkPortalBlock] (destinationWorld.getBlockState(checkPos).getBlock, _ => doSetBlock = false)
//      }
////      if (doSetBlock) destinationWorld.setBlock(destinationPos, ModBlocks.MOD_PORTAL.get.defaultBlockState, 3)
//    }
//    fEntity
//  }
//
//  private def checkCondition(destinationWorld: ServerLevel, destinationPos: BlockPos) : Boolean = {
//    val state = destinationWorld.getBlockState(destinationPos)
//    (state.getBlock ne Blocks.AIR) && !state.canBeReplaced(Fluids.WATER)
//  }
}

