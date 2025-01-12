package com.not2ho.artificialheart.block

import com.not2ho.artificialheart.worldgen.dimension.{PinkDimensions, PinkPortals}
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.portal.PortalInfo
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.common.util.ITeleporter

import javax.annotation.Nullable



class PinkPortalBlock(pProperties: BlockBehaviour.Properties) extends Block(pProperties) {
  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand,
                   pHit: BlockHitResult): InteractionResult = if (pPlayer.canChangeDimensions) {
    handlePinkPortal(pPlayer, pPos)
    InteractionResult.SUCCESS
  }
  else InteractionResult.CONSUME

  private def handlePinkPortal(player: Entity, pPos: BlockPos): Unit = {
    player.level match
      case sl : ServerLevel =>
        val minecraftserver = sl.getServer
        val resourcekey = player.level.dimension
        resourcekey match
          case PinkDimensions.STRAWBERRYDAYS_LEVEL_KEY => Level.OVERWORLD
          case _ => PinkDimensions.STRAWBERRYDAYS_LEVEL_KEY
        val portalDimension = minecraftserver.getLevel( resourcekey )
        if (portalDimension != null && !player.isPassenger)
          resourcekey match
            case PinkDimensions.STRAWBERRYDAYS_LEVEL_KEY => player.changeDimension( portalDimension, new PinkPortals( pPos, true ) )
            case _ => player.changeDimension( portalDimension, new PinkPortals( pPos, false ) )
  }
}

