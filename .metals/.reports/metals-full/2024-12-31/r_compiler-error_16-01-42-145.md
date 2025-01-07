file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/block/liquidblocks.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(RegistryObject) is not assigned

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1524
uri: file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/block/liquidblocks.scala
text:

```scala
package com.not2ho.artificialheart

import com.not2ho.artificialheart
import com.not2ho.artificialheart.Pinkliquid
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.{BlockItem, CreativeModeTab, CreativeModeTabs, Item}
import net.minecraft.world.level.block.{Block, Blocks, LiquidBlock}
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, FMLCommonSetupEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}
import org.apache.logging.log4j.LogManager

import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraftforge.fluids.ForgeFlowingFluid


class LiquidBlocks {
  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, ArtificialHeart.MOD_ID)
  val RegistryObject
  [@@ LiquidBlock
  ] SOAP_WATER_BLOCK = BLOCKS.register("pink_liquid",
    () -> new LiquidBlock(Pinkliquid.SOURCE_PINK_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)))

  BLOCKS.register(modEventBus)

}
```



#### Error stacktrace:

```
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:74)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:208)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:104)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:47)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:422)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(RegistryObject) is not assigned