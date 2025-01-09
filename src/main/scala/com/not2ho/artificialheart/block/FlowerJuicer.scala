package com.not2ho.artificialheart.block

import com.not2ho.artificialheart.entity
import com.not2ho.artificialheart.entity.{BlockEntities, FlowerJuicerEntity}
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{BaseEntityBlock, Block, RenderShape}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.{CollisionContext, VoxelShape}
import net.minecraftforge.network.NetworkHooks
import org.jetbrains.annotations.Nullable



class FlowerJuicer(pProperties: BlockBehaviour.Properties) extends BaseEntityBlock(pProperties) {
  override def onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean): Unit = {
    if (pState.getBlock ne pNewState.getBlock) {
      val blockEntity = pLevel.getBlockEntity(pPos)
      blockEntity match
        case entity: FlowerJuicerEntity => entity.drops()
        case _ =>
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  val SHAPE: VoxelShape  = Block.box(0, 0, 0, 16, 15, 16);

  def getShape(pState: BlockState, pLevel: Level, pPos: BlockPos, pContext: CollisionContext):VoxelShape = SHAPE

  override def getRenderShape(pState: BlockState) = RenderShape.MODEL

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    if (!pLevel.isClientSide) {
      val entity = pLevel.getBlockEntity(pPos)
      entity match
        case fEntity: FlowerJuicerEntity => NetworkHooks.openScreen(pPlayer.asInstanceOf[ServerPlayer], fEntity, pPos)
          InteractionResult.CONSUME
        case _ => throw new IllegalStateException("Our Container provider is missing!")
    }
      InteractionResult.sidedSuccess(pLevel.isClientSide)
  }

  @Nullable override def newBlockEntity(pPos: BlockPos, pState: BlockState) = new FlowerJuicerEntity(pPos, pState)

  @Nullable override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if (pLevel.isClientSide) return null
    createTickerHelper(pBlockEntityType, BlockEntities.FLOWER_JUICER_BE.get(), (npLevel, npPos, npState, npBlockEntity) => npBlockEntity.tick(npLevel, npPos, npState))
  }
}