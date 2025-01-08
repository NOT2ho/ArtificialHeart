package com.not2ho.artificialheart

import com.not2ho.artificialheart.block.FlowerJuicer
import com.not2ho.artificialheart.entity.BlockEntities
import com.not2ho.artificialheart.fluid.{PinkFluid, PinkLiquid}
import com.not2ho.artificialheart.item.{BeakerItem, PinkToolTiers}
import com.not2ho.artificialheart.recipe.PinkRecipe
import com.not2ho.artificialheart.screen.{FlowerJuicerScreen, MenuTypes}
import com.not2ho.artificialheart.worldgen.tree.{Features, FoliagePlacers, PinkTreeGrower, TrunkPlacerTypes}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.{BlockItem, BucketItem, CreativeModeTab, CreativeModeTabs, Item, Items, PotionItem, SwordItem}
import net.minecraft.world.level.block.{Block, Blocks, DropExperienceBlock, LeavesBlock, LiquidBlock, RotatedPillarBlock, SaplingBlock, SoundType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.BlockGetter
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, FMLCommonSetupEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}
import org.apache.logging.log4j.LogManager


@Mod(ArtificialHeart.MOD_ID)
object ArtificialHeart {
  final val MOD_ID = "artificialheart"

  private val LOGGER = LogManager.getLogger

  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID)
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID)

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

  val MASHED_HEART: RegistryObject[Item] = ITEMS.register("mashed_heart", () => new Item(new Item.Properties()))

  val BEAKER: RegistryObject[Item] = ITEMS.register("beaker", () => new Item(new Item.Properties()
  .stacksTo(1)
  ))

  val POPPY_JUICE: RegistryObject[Item] = ITEMS.register("poppy_juice", ()
  => new BeakerItem(new Item.Properties()
    .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val DANDELION_JUICE: RegistryObject[Item] = ITEMS.register("dandelion_juice", () => new BeakerItem(new Item.Properties()
    .stacksTo(1)
    .food(new FoodProperties.Builder().alwaysEat()
    .build())))
  val CUTTER: RegistryObject[Item] = ITEMS.register("cutter", () => new SwordItem(PinkToolTiers.HEART, 1, 10, new Item.Properties()))
  val CLEAVER: RegistryObject[Item] = ITEMS.register("cleaver", () => new SwordItem(PinkToolTiers.HEART, 10, 0.1, new Item.Properties()))

  val RAW_HEART: RegistryObject[Item] = ITEMS.register("raw_heart"
    , () => new Item(new Item.Properties()))

  val HEART_INGOT: RegistryObject[Item] = ITEMS.register("heart_ingot"
    , () => new Item(new Item.Properties()))

  val DEEPSLATE_HEART_ORE: RegistryObject[DropExperienceBlock] = BLOCKS.register("deepslate_heart_ore"
    , () => new DropExperienceBlock
      (BlockBehaviour.Properties.of.strength(4f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE), UniformInt.of(3,6)))

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


  val PINK_GRASS_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("pink_grass_block"
    , () => new BlockItem(PINK_GRASS_BLOCK.get(), new Item.Properties()))
  val HEART_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("heart_block"
    , () => new BlockItem(HEART_BLOCK.get(), new Item.Properties()))
  val CREATIVE_MODE_TABS: DeferredRegister[CreativeModeTab] = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID)

  val PINK_LIQUID_BUCKET: RegistryObject[Item] = ITEMS.register("pink_liquid_bucket",
    () => new BucketItem(PinkLiquid.SOURCE_PINK_LIQUID, new Item.Properties().craftRemainder(Items.BUCKET)
      .stacksTo(1)))

  val PINK_TAB: RegistryObject[CreativeModeTab] = CREATIVE_MODE_TABS.register("pink_tab", () => CreativeModeTab.builder()
    .withTabsBefore(CreativeModeTabs.COMBAT)
    .icon(() => PINK_GRASS_BLOCK_ITEM.get().getDefaultInstance)
    .displayItems((parameters, output) => {
      output.accept(PINK_GRASS_BLOCK_ITEM.get())
    }).build()
  )

  {
    val modEventBus = FMLJavaModLoadingContext.get().getModEventBus

    BLOCKS.register(modEventBus)

    BlockEntities.register(modEventBus)
    PinkRecipe.register(modEventBus)
    MenuTypes.register(modEventBus)
    PinkLiquid.register(modEventBus)

    TrunkPlacerTypes.register(modEventBus)
    FoliagePlacers.register(modEventBus)
    PinkFluid.register(modEventBus)

    ITEMS.register(modEventBus)
    CREATIVE_MODE_TABS.register(modEventBus)

    modEventBus.addListener(this.commonSetup)
    MinecraftForge.EVENT_BUS.register(this)
    //MinecraftForge.EVENT_BUS.register(ClientModEvents)

    modEventBus.addListener(this.addCreative)
    ModLoadingContext.get.registerConfig(ModConfig.Type.COMMON, Config.SPEC)

    if (FMLEnvironment.dist.isClient) {
      modEventBus.addListener(this.onClientSetup)
    }
  }

  private def commonSetup(event: FMLCommonSetupEvent): Unit = {
  }

  private def addCreative(event: BuildCreativeModeTabContentsEvent): Unit = {
    if (event.getTabKey == CreativeModeTabs.BUILDING_BLOCKS)
      event.accept(PINK_GRASS_BLOCK)
  }

  @SubscribeEvent
  def onServerStarting(event: ServerStartingEvent): Unit = {
  }

  def onClientSetup(event: FMLClientSetupEvent): Unit = {
    ItemBlockRenderTypes.setRenderLayer(PinkLiquid.SOURCE_PINK_LIQUID.get(), RenderType.translucent)
    ItemBlockRenderTypes.setRenderLayer(PinkLiquid.FLOWING_PINK_LIQUID.get(), RenderType.translucent)
    MenuScreens.register(MenuTypes.FLOWER_JUICER_MENU.get(), new FlowerJuicerScreen(_, _, _))
  }

  /*@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Array(Dist.CLIENT))
  object ClientModEvents {
    @SubscribeEvent
    def onClientSetup(event: FMLClientSetupEvent): Unit = {
      ItemBlockRenderTypes.setRenderLayer(PinkLiquid.SOURCE_PINK_LIQUID.get(), RenderType.translucent)
      ItemBlockRenderTypes.setRenderLayer(PinkLiquid.FLOWING_PINK_LIQUID.get(), RenderType.translucent)
      MenuScreens.register(BeakerRegister.FLOWER_JUICER_MENU.get(), new FlowerJuicerScreen(_,_,_))
    }
  }*/
}
