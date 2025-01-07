file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/fluid/pinkfluid.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(DeferredRegister) is not assigned

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 827
uri: file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/fluid/pinkfluid.scala
text:

```scala
package com.not2ho.artificialheart

import com.not2ho.artificialheart
import com.mojang.math.Vector3f
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraftforge.common.SoundAction
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

class Pinkfluid {
  val WATER_STILL_RL: ResourceLocation = new ResourceLocation("block/water_still")
  val WATER_FLOWING_RL: ResourceLocation = new ResourceLocation("block/water_flow")
  val PINK_OVERLAY_RL: ResourceLocation = new ResourceLocation(ArtificialHeart.MOD_ID, "misc/in_pink")

  val DeferredRegister
  [@@ FluidType > FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ArtificialHeart.MOD_ID)
  val RegistryObject < FluidType > PINK_WATER_FLUID_TYPE = register("pink_liquid",
    FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
      SoundEvents.HONEY_DRINK))

  val register /*: RegistryObject<FluidType>*/ = (name: String, properties: FluidType.Properties) => {
    FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, PINK_OVERLAY_RL,
      0xA1ff0066, new Vector3f(255f / 255f, 0f / 255f, 102f / 255f), properties))
  }

  val register = (eventBus: IEventBus) => {
    FLUID_TYPES.register(eventBus)
  }
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

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(DeferredRegister) is not assigned