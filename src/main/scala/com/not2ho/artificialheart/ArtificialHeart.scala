package com.not2ho.artificialheart

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.not2ho.artificialheart.ArtificialHeart.{LOGGER, MOD_ID}
import com.not2ho.artificialheart.block.PinkBlocks
import com.not2ho.artificialheart.block.PinkBlocks.{PINK_GRASS_BLOCK, PINK_GRASS_BLOCK_ITEM}
import com.not2ho.artificialheart.entity.BlockEntities
import com.not2ho.artificialheart.fluid.{PinkFluid, PinkLiquid}
import com.not2ho.artificialheart.recipe.PinkRecipe
import com.not2ho.artificialheart.register.PinkDatapackBuiltinEntriesProvider
import com.not2ho.artificialheart.screen.{FlowerJuicerScreen, MenuTypes}
import com.not2ho.artificialheart.worldgen.biome.PinkBiomes.PINK_AREA
import com.not2ho.artificialheart.worldgen.tree.{FoliagePlacerTypes, TrunkPlacerTypes}
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.{BiomeColors, ItemBlockRenderTypes, RenderType}
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.{CreativeModeTab, CreativeModeTabs, Item, ItemStack}
import net.minecraft.world.level.{FoliageColor, GrassColor}
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.common.{BiomeManager, MinecraftForge}
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, FMLCommonSetupEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}
import org.apache.logging.log4j.LogManager

import java.util.concurrent.CompletableFuture


@Mod(ArtificialHeart.MOD_ID)
object ArtificialHeart {
  final val MOD_ID = "artificialheart"
  val addedPink = 0xb03911
  val subedGreen = 0x48b518

  val LOGGER = LogManager.getLogger

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
    modEventBus.addListener(this.blockColorHandlerEvent)
    modEventBus.addListener(this.itemColorHandlerEvent)
    if (FMLEnvironment.dist.isClient) {
      modEventBus.addListener(this.onClientSetup)
    }

    //modEventBus.addListener(this.dataSetup)
  }

  private def commonSetup(event: FMLCommonSetupEvent): Unit = {
    val _: CompletableFuture[Void] =
      event.enqueueWork(() => BiomeManager.addAdditionalOverworldBiomes(PINK_AREA))
  }

  private def addCreative(event: BuildCreativeModeTabContentsEvent): Unit = {
  }


  @SubscribeEvent
  def onServerStarting(event: ServerStartingEvent): Unit = {
  }

  def blockColorHandlerEvent( event : RegisterColorHandlersEvent.Block ) : Unit = {
    event.register( ( state, world, pos, tintIndex ) => {
      if (world != null && pos != null) {
        0xf8a4bf.min(BiomeColors.getAverageGrassColor( world, pos ) - subedGreen + addedPink)
      }
      else {
        GrassColor.getDefaultColor - subedGreen + addedPink
      }

    }, PinkBlocks.PINK_GRASS_BLOCK.get())
  }

  def itemColorHandlerEvent( event : RegisterColorHandlersEvent.Item ) : Unit = {
    event.register( ( stack, tintIndex ) => {
      0xf8a4bf
    }, PINK_GRASS_BLOCK.get()
                    )
  }

  def onClientSetup(event: FMLClientSetupEvent): Unit = {
    ItemBlockRenderTypes.setRenderLayer(PinkLiquid.SOURCE_PINK_LIQUID.get(), RenderType.translucent)
    ItemBlockRenderTypes.setRenderLayer(PinkLiquid.FLOWING_PINK_LIQUID.get(), RenderType.translucent)
    MenuScreens.register(MenuTypes.FLOWER_JUICER_MENU.get(), new FlowerJuicerScreen(_, _, _))
  }

  def registerBlockColors( event: RegisterColorHandlersEvent.Block  ): Unit = {
    event.register( null )
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

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object DataSetupEvents {
  @SubscribeEvent
  def dataSetup(event: GatherDataEvent): Unit = {
    val generator = event.getGenerator
    val output = generator.getPackOutput
    val provider = event.getLookupProvider
    val helper = event.getExistingFileHelper
    val server = event.includeServer
    val datapackEntries = new PinkDatapackBuiltinEntriesProvider(output, provider)
    generator.addProvider(server, datapackEntries)
    datapackEntries.getRegistryProvider()
    //    val client = event.includeClient
  }
}
