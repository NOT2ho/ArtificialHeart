package com.not2ho.artificialheart.block

import com.not2ho.artificialheart.ArtificialHeart.{BLOCKS,MOD_ID,ITEMS}
import com.not2ho.artificialheart.fluid.PinkLiquid
import com.not2ho.artificialheart.worldgen.tree.PinkTreeGrower
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, Blocks, DropExperienceBlock, LeavesBlock, LiquidBlock, RotatedPillarBlock, SaplingBlock, SoundType}
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object PinkBlocks {

  val PINK_LIQUID_BLOCK: RegistryObject[LiquidBlock] = BLOCKS.register("pink_liquid_block",
    () => new LiquidBlock(PinkLiquid.SOURCE_PINK_LIQUID, BlockBehaviour.Properties.copy(Blocks.WATER)))

  val PINK_LIQUID_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("pink_liquid_block"
    , () => new BlockItem(PINK_LIQUID_BLOCK.get(), new Item.Properties()))


  val HEART_ORE: RegistryObject[DropExperienceBlock] = BLOCKS.register("heart_ore"
    , () => new DropExperienceBlock
      (BlockBehaviour.Properties.of.strength(3f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE), UniformInt.of(2,5)))

  val HEART_ORE_ITEM: RegistryObject[Item] = ITEMS.register("heart_ore"
    , () => new BlockItem(HEART_ORE.get(), new Item.Properties()))


  val FLOWER_JUICER: RegistryObject[Block] = BLOCKS.register("flower_juicer"
    , () => new FlowerJuicer(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion))

  val FLOWER_JUICER_ITEM: RegistryObject[Item] = ITEMS.register("flower_juicer"
    , () => new BlockItem(FLOWER_JUICER.get(), new Item.Properties()))

  val DEEPSLATE_HEART_ORE: RegistryObject[DropExperienceBlock] = BLOCKS.register("deepslate_heart_ore"
    , () => new DropExperienceBlock
      (BlockBehaviour.Properties.of.strength(4f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE), UniformInt.of(3, 6)))

  val DEEPSLATE_HEART_ORE_ITEM: RegistryObject[Item] = ITEMS.register("deepslate_heart_ore"
    , () => new BlockItem(DEEPSLATE_HEART_ORE.get(), new Item.Properties()))

  val PINK_GRASS_BLOCK: RegistryObject[Block] = BLOCKS.register("pink_grass_block", () => new Block(BlockBehaviour.Properties.of()
    .destroyTime(2.0f)
    .explosionResistance(0.5f)))
  val HEART_BLOCK: RegistryObject[Block] = BLOCKS.register("heart_block", () => new Block(BlockBehaviour.Properties.of()
    .strength(2f)
    .requiresCorrectToolForDrops()
    .explosionResistance(0.5f)
    .sound(SoundType.PINK_PETALS)))

  val PINK_TREE_PLANK: RegistryObject[Block] = BLOCKS.register("pink_tree_plank", () => new Block(BlockBehaviour.Properties.of()
    .strength(2f)))

  val PINK_TREE_SAPLING: RegistryObject[Block] = BLOCKS.register("pink_tree_sapling"
    , () => new SaplingBlock(new PinkTreeGrower(), BlockBehaviour.Properties.copy(Blocks.CHERRY_SAPLING)))
  val PINK_TREE_LOG: RegistryObject[Block] = BLOCKS.register("pink_tree_log"
    , () => new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LOG)))

  val PINK_TREE_LEAVES: RegistryObject[Block] = BLOCKS.register("pink_tree_leaves"
    , () => new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES)) {
      override def isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = true

      override def getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 60

      override def getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 30
    })

  val PINK_TREE_SAPLING_ITEM: RegistryObject[Item] = ITEMS.register("pink_tree_sapling"
    , () => new BlockItem(PINK_TREE_SAPLING.get(), new Item.Properties()))

  val PINK_TREE_LOG_ITEM: RegistryObject[Item] = ITEMS.register("pink_tree_log"
    , () => new BlockItem(PINK_TREE_LOG.get(), new Item.Properties()))

  val PINK_TREE_LEAVES_ITEM: RegistryObject[Item] = ITEMS.register("pink_tree_leaves"
    , () => new BlockItem(PINK_TREE_LEAVES.get(), new Item.Properties()))

  val PINK_TREE_PLANK_ITEM: RegistryObject[Item] = ITEMS.register("pink_tree_plank"
    , () => new BlockItem(PINK_TREE_PLANK.get(), new Item.Properties()))

  val PINK_GRASS_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("pink_grass_block"
    , () => new BlockItem(PINK_GRASS_BLOCK.get(), new Item.Properties()))
  val HEART_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("heart_block"
    , () => new BlockItem(HEART_BLOCK.get(), new Item.Properties()))
}
