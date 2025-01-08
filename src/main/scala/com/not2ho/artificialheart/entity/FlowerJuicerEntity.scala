package com.not2ho.artificialheart.entity

import com.not2ho.artificialheart.entity.FlowerJuicerEntity.{BEAKER_ITEM, BEAKER_SLOT, FUEL_ITEM, FUEL_SLOT, INGREDIENT_SLOT}
import com.not2ho.artificialheart.{ArtificialHeart, recipe}
import com.not2ho.artificialheart.recipe.FlowerJuicerRecipe
import com.not2ho.artificialheart.screen.FlowerJuicerMenu
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import java.util.Optional
import scala.language.postfixOps


object FlowerJuicerEntity {
  private val INGREDIENT_SLOT = 1
  private val FUEL_SLOT = 2
  private val BEAKER_SLOT = 0
//  private val SLOTS_FOR_UP = Array[Int](1)
//  private val SLOTS_FOR_DOWN = Array[Int](0, 1)
//  private val SLOTS_FOR_SIDES = Array[Int](0, 2)
  val FUEL_ITEM = ArtificialHeart.MASHED_HEART
  val BEAKER_ITEM = ArtificialHeart.BEAKER

}


class FlowerJuicerEntity(pPos: BlockPos, pBlockState: BlockState) extends BlockEntity(BlockEntities.FLOWER_JUICER_BE.get(), pPos, pBlockState) with MenuProvider {
  final protected var data: ContainerData =  new ContainerData() {
    override def get(pIndex: Int): Int = pIndex match {
      case 0 => progress
      case 1 => maxProgress
      case _ => 0
    }

    override def set(pIndex: Int, pValue: Int): Unit = {
      pIndex match {
        case 0 => progress = pValue
        case 1 => maxProgress = pValue
      }
    }

    override def getCount = 2
  }
  final private val itemHandler = new ItemStackHandler(3) {
    override protected def onContentsChanged(slot: Int): Unit = {
      setChanged()
      if (!level.isClientSide) level.sendBlockUpdated(getBlockPos, getBlockState, getBlockState, 2)
    }
  }
  private var lazyItemHandler:LazyOptional[IItemHandler] = LazyOptional.empty
  private var progress = 0
  private var maxProgress = 78

//  def getRenderStack: ItemStack =
//    if (itemHandler.getStackInSlot(FlowerJuicerEntity.FUEL_SLOT).isEmpty) itemHandler.getStackInSlot(FlowerJuicerEntity.FUEL_SLOT)
//     else if (itemHandler.getStackInSlot(FlowerJuicerEntity.INGREDIENT_SLOT).isEmpty) itemHandler.getStackInSlot(FlowerJuicerEntity.INGREDIENT_SLOT)
//     else itemHandler.getStackInSlot(FlowerJuicerEntity.BEAKER_SLOT)

  @NotNull override def getCapability[T](@NotNull cap: Capability[T], @Nullable side: Direction): LazyOptional[T] = {
    if (cap eq ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast
    super.getCapability(cap, side)
  }

  override def onLoad(): Unit = {
    super.onLoad()
    lazyItemHandler = LazyOptional.of(() => itemHandler)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    lazyItemHandler.invalidate()
  }

  def drops(): Unit = {
    val inventory = new SimpleContainer(itemHandler.getSlots)
    for (i <- 0 until itemHandler.getSlots) {
      inventory.setItem(i, itemHandler.getStackInSlot(i))
    }
    Containers.dropContents(this.level, this.worldPosition, inventory)
  }

  private def increaseCraftingProgress(): Unit = {
    progress += 1
  }

  override def getDisplayName: Component = Component.translatable("block.artificialheart.flower_juicer")

  @Nullable override def createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player) = new FlowerJuicerMenu(pContainerId, pPlayerInventory, this, this.data)

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    pTag.put("inventory", itemHandler.serializeNBT)
    pTag.putInt("flower_juicer.progress", progress)
    super.saveAdditional(pTag)
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    itemHandler.deserializeNBT(pTag.getCompound("inventory"))
    progress = pTag.getInt("flower_juicer.progress")
  }

//  private def isJuicable(pItems: NonNullList[ItemStack]): Boolean = {
//    val ingredientSlot: ItemStack = pItems.get(INGREDIENT_SLOT)
//    val fuelSlot: ItemStack = pItems.get(FUEL_SLOT)
//    val beakerSlot: ItemStack = pItems.get(BEAKER_SLOT)
//    if fuelSlot.getItem() == FUEL_ITEM && beakerSlot.getItem() == BEAKER_ITEM then
//      true
//    else
//      false
//  }

    def tick(pLevel: Level, pPos: BlockPos, pState: BlockState): Unit = {
      if (hasRecipe) {
        increaseCraftingProgress()
        BlockEntity.setChanged(pLevel, pPos, pState)
        if (hasProgressFinished) {
          juicing()
          resetProgress()
        }
      }
      else resetProgress()
    }




  private def resetProgress(): Unit = {
    progress = 0
  }

  private def juicing(): Unit = {
    val recipe:Optional[FlowerJuicerRecipe] = getCurrentRecipe
    val result: ItemStack = recipe.get.getResultItem(null)
    this.itemHandler.extractItem(INGREDIENT_SLOT, 1, false)
    this.itemHandler.extractItem(FUEL_SLOT, 1, false)
    this.itemHandler.setStackInSlot(BEAKER_SLOT, new ItemStack(result.getItem, 1))
  }

  private def hasRecipe: Boolean = {
    val recipe: Optional[FlowerJuicerRecipe] = getCurrentRecipe
    if (recipe.isEmpty) return false
    val result = recipe.get.getResultItem(getLevel.registryAccess)

    canInsertItemIntoBeakerslot(ArtificialHeart.BEAKER.get())
  }

  private def getCurrentRecipe = {
    val inventory = new SimpleContainer(this.itemHandler.getSlots)
    for (i <- 0 until this.itemHandler.getSlots)
      inventory.setItem(i, this.itemHandler.getStackInSlot(i))

    this.level.getRecipeManager.getRecipeFor(FlowerJuicerRecipe.Type.INSTANCE, inventory, level)
  }

  private def canInsertItemIntoBeakerslot(item: Item) = this.itemHandler.getStackInSlot(FlowerJuicerEntity.BEAKER_SLOT).is(item)

  //private def canInsertAmountIntoBeakerslot(count: Int) = true //if this.itemHandler.getStackInSlot(FlowerJuicerEntity.BEAKER_SLOT).getCount == 0 then true else false

  private def hasProgressFinished = progress >= maxProgress

  @Nullable override def getUpdatePacket: Packet[ClientGamePacketListener] = ClientboundBlockEntityDataPacket.create(this)

  override def getUpdateTag: CompoundTag = saveWithoutMetadata
}