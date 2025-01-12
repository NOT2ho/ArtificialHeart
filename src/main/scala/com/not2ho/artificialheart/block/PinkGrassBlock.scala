package com.not2ho.artificialheart.block

import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.GrassBlock
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraftforge.common.IPlantable

import java.util


class PinkGrassBlock( pbb : BlockBehaviour.Properties ) extends GrassBlock( pbb ) {
  override def canSustainPlant( state : BlockState, world : BlockGetter, pos : BlockPos, facing : Direction, plantable : IPlantable)
  = true
}
