package com.not2ho.artificialheart.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraftforge.common.ToolAction
import org.checkerframework.checker.units.qual.A
import org.jetbrains.annotations.Nullable


class TreePillar(pProperties: BlockBehaviour.Properties) extends RotatedPillarBlock(pProperties) {
  override def isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = true

  override def getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

  override def getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

  @Nullable override def getToolModifiedState(state: BlockState, context: UseOnContext, toolAction: ToolAction, simulate: Boolean): BlockState = {
    if (context.getItemInHand.getItem.isInstanceOf[AxeItem]) {
//      if (state.is(ModBlocks.PINE_LOG.get)) return ModBlocks.STRIPPED_PINE_LOG.get.defaultBlockState.setValue(AXIS, state.getValue(AXIS))
//      if (state.is(ModBlocks.PINE_WOOD.get)) return ModBlocks.STRIPPED_PINE_WOOD.get.defaultBlockState.setValue(AXIS, state.getValue(AXIS))
    }
    super.getToolModifiedState(state, context, toolAction, simulate)
  }
}