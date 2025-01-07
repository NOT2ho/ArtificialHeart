file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/item/buckets.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(RegistryObject) is not assigned

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 652
uri: file:///C:/Users/i5-32/Desktop/mod/src/main/scala/com/not2ho/artificialheart/item/buckets.scala
text:
```scala
package com.not2ho.artificialheart

import com.not2ho.artificialheart.Pinkliquid
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.Items
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

class Buckets {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, ArtificialHeart.MOD_ID)

  val RegistryObject[@@Item] PINK_WATER_BUCKET = ITEMS.register("pink_liquid_bucket",
            () -> new BucketItem(Pinkliquid.SOURCE_PINK_WATER))

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