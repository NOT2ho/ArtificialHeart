package com.not2ho.artificialheart.screen

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks.FLOWER_JUICER
import com.not2ho.artificialheart.entity.FlowerJuicerEntity
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.inventory.AbstractContainerMenu.checkContainerSize
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.items.SlotItemHandler

import scala.language.postfixOps


object FlowerJuicerMenu {
  private val HOTBAR_SLOT_COUNT = 9
  private val PLAYER_INVENTORY_ROW_COUNT = 3
  private val PLAYER_INVENTORY_COLUMN_COUNT = 9
  private val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
  private val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
  private val VANILLA_FIRST_SLOT_INDEX = 0
  private val TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT
  private val TE_INVENTORY_SLOT_COUNT = 3

}

class FlowerJuicerMenu(pContainerId: Int, inv: Inventory, entity: BlockEntity, private var data: ContainerData) extends AbstractContainerMenu(MenuTypes.FLOWER_JUICER_MENU.get(), pContainerId) {
  checkContainerSize(inv, 3)
  blockEntity = entity.asInstanceOf[FlowerJuicerEntity]
  this.level = inv.player.level
  addPlayerInventory(inv)
  addPlayerHotbar(inv)
  this.data = data
  blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler => {
    this.addSlot(new SlotItemHandler(iItemHandler, 1, 80, 11))
    this.addSlot(new SlotItemHandler(iItemHandler, 2, 50, 20))
    this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 59))

  })
  addDataSlots(data)
  final var blockEntity: FlowerJuicerEntity = _
  final private var level: Level = _

  def this(pContainerId: Int, inv: Inventory, extraData: FriendlyByteBuf) = {
    this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos), new SimpleContainerData(3))
  }

  def isCrafting: Boolean = data.get(0) > 0

  def getScaledProgress: Int = {
    val progress = this.data.get(0)
    val maxProgress = this.data.get(1)

    val progressArrowSize = 26 // This is the height in pixels of your arrow

    if (maxProgress != 0 && progress != 0) progress * progressArrowSize / maxProgress
    else 0
  }

  override def quickMoveStack(playerIn: Player, pIndex: Int): ItemStack = {
    val sourceSlot = slots.get(pIndex)
    if (sourceSlot == null || !sourceSlot.hasItem) return ItemStack.EMPTY //EMPTY_ITEM
    val sourceStack = sourceSlot.getItem
    val copyOfSourceStack = sourceStack.copy
    if (pIndex < FlowerJuicerMenu.VANILLA_FIRST_SLOT_INDEX + FlowerJuicerMenu.VANILLA_SLOT_COUNT) {
      if (!moveItemStackTo(sourceStack, FlowerJuicerMenu.TE_INVENTORY_FIRST_SLOT_INDEX, FlowerJuicerMenu.TE_INVENTORY_FIRST_SLOT_INDEX + FlowerJuicerMenu.TE_INVENTORY_SLOT_COUNT, false)) return ItemStack.EMPTY // EMPTY_ITEM
    }
    else if (pIndex < FlowerJuicerMenu.TE_INVENTORY_FIRST_SLOT_INDEX + FlowerJuicerMenu.TE_INVENTORY_SLOT_COUNT) {
      if (!moveItemStackTo(sourceStack, FlowerJuicerMenu.VANILLA_FIRST_SLOT_INDEX, FlowerJuicerMenu.VANILLA_FIRST_SLOT_INDEX + FlowerJuicerMenu.VANILLA_SLOT_COUNT, false)) return ItemStack.EMPTY
    }
    else {
      System.out.println("Invalid slotIndex:" + pIndex)
      return ItemStack.EMPTY
    }
    if (sourceStack.getCount == 0) sourceSlot.set(ItemStack.EMPTY)
    else sourceSlot.setChanged()
    sourceSlot.onTake(playerIn, sourceStack)
    copyOfSourceStack
  }

  override def stillValid(pPlayer: Player): Boolean = AbstractContainerMenu.stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos),
    pPlayer,
    FLOWER_JUICER.get)

  private def addPlayerInventory(playerInventory: Inventory): Unit = {
    for (i <- 0 until 3) {
      for (l <- 0 until 9) {
        addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18))
      }
    }
  }

  private def addPlayerHotbar(playerInventory: Inventory): Unit = {
    for (i <- 0 until 9) {
      this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142))
    }
  }
}