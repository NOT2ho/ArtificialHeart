package com.not2ho.artificialheart

import com.not2ho.artificialheart.block.FlowerJuicer
import com.not2ho.artificialheart.block.PinkBlocks.{PINK_GRASS_BLOCK, PINK_GRASS_BLOCK_ITEM}
import com.not2ho.artificialheart.entity.BlockEntities
import com.not2ho.artificialheart.fluid.{PinkFluid, PinkLiquid}
import com.not2ho.artificialheart.item.{BeakerItem, PinkItems, PinkToolTiers}
import com.not2ho.artificialheart.recipe.PinkRecipe
import com.not2ho.artificialheart.screen.{FlowerJuicerScreen, MenuTypes}
import com.not2ho.artificialheart.util.PinkDatapackBuiltinEntriesProvider
import com.not2ho.artificialheart.worldgen.tree.{FoliagePlacerTypes, PinkTreeGrower, TrunkPlacerTypes}
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.core.registries.{BuiltInRegistries, Registries}
import net.minecraft.world.item.{BlockItem, BucketItem, CreativeModeTab, CreativeModeTabs, Item, ItemStack, Items, PotionItem, SwordItem}
import net.minecraft.world.level.block.{Block, Blocks, DropExperienceBlock, LeavesBlock, LiquidBlock, RotatedPillarBlock, SaplingBlock, SoundType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.BlockGetter
import net.minecraftforge.data.event.GatherDataEvent
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

  val CREATIVE_MODE_TABS: DeferredRegister[CreativeModeTab] = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID)
  val PINK_TAB: RegistryObject[CreativeModeTab] = CREATIVE_MODE_TABS.register("pink_tab", () => CreativeModeTab.builder()
    .withTabsBefore(CreativeModeTabs.COMBAT)
    .icon(() => PINK_GRASS_BLOCK_ITEM.get().getDefaultInstance)
      .displayItems((parameters, output) => {
      val stacks =ITEMS.getEntries.stream.map(reg => new ItemStack(reg.get)).toList
      output.acceptAll(stacks)
    }).build)

//    .displayItems((parameters, output) => {
//      output.accept(PINK_GRASS_BLOCK_ITEM.get())
//      output.accept(PINK_TREE_LOG_ITEM.get())
//      output.accept(PINK_TREE_LEAVES_ITEM.get())
//      output.accept(PINK_TREE_SAPLING_ITEM.get())
//      output.accept(HEART_BLOCK_ITEM.get())
//      output.accept(HEART_ORE_ITEM.get())
//      output.accept(DEEPSLATE_HEART_ORE_ITEM.get())
//      output.accept(RAW_HEART.get())
//      output.accept(HEART_INGOT.get())
//      output.accept(PINK_LIQUID_BUCKET.get())
//      output.accept(MASHED_HEART.get())
//      output.accept(CUTTER.get())
//      output.accept(CLEAVER.get())
//      output.accept(BEAKER.get())
//      output.accept(POPPY_JUICE.get())
//      output.accept(DANDELION_JUICE.get())
//      output.accept(ALLIUM_JUICE.get())
//      output.accept(AZURE_BLUET_JUICE.get())
//      output.accept(BLUE_ORCHID_JUICE.get())
//      output.accept(CORNFLOWER_JUICE.get())
//      output.accept(LILAC_JUICE.get())
//      output.accept(LILY_OF_THE_VALLEY_JUICE.get())
//      output.accept(OXEYE_DAISY_JUICE.get())
//      output.accept(SUNFLOWER_JUICE.get())
//      output.accept(TULIP_JUICE.get())
//      output.accept(WITHER_ROSE_JUICE.get())
//      output.accept(PINK_TREE_PLANK_ITEM.get())



  {
    val modEventBus = FMLJavaModLoadingContext.get().getModEventBus

    BLOCKS.register(modEventBus)
    FoliagePlacerTypes.register(modEventBus)
    TrunkPlacerTypes.register(modEventBus)
    BlockEntities.register(modEventBus)
    PinkRecipe.register(modEventBus)
    MenuTypes.register(modEventBus)
    PinkLiquid.register(modEventBus)

    PinkFluid.register(modEventBus)

    ITEMS.register(modEventBus)
    CREATIVE_MODE_TABS.register(modEventBus)

    modEventBus.addListener(this.commonSetup)
    MinecraftForge.EVENT_BUS.register(this)
    //MinecraftForge.EVENT_BUS.register(ClientModEvents)
    modEventBus.addListener(this.addCreative)
    ModLoadingContext.get.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
    modEventBus.addListener(this.dataSetup)

    if (FMLEnvironment.dist.isClient) {
      modEventBus.addListener(this.onClientSetup)
    }
  }

  private def commonSetup(event: FMLCommonSetupEvent): Unit = {
  }

  private def addCreative(event: BuildCreativeModeTabContentsEvent): Unit = {
  }

  private def dataSetup(event: GatherDataEvent): Unit = {
    val generator = event.getGenerator
    val output = generator.getPackOutput
    var provider = event.getLookupProvider
    val helper = event.getExistingFileHelper
    val server = event.includeServer
    val datapackEntries = new PinkDatapackBuiltinEntriesProvider(output, provider)
    generator.addProvider(server, datapackEntries)
    datapackEntries.getRegistryProvider()
//    val client = event.includeClient
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
