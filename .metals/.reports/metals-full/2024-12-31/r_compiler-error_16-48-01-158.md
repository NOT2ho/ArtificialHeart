file:///C:/Users/i5-32/Desktop/artificialheart/src/main/scala/com/not2ho/artificialheart/fluid/basefluid.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Select(Select(Select(Ident(_root_),scala),Predef),???) is not assigned

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 920
uri: file:///C:/Users/i5-32/Desktop/artificialheart/src/main/scala/com/not2ho/artificialheart/fluid/basefluid.scala
text:
```scala
package com.not2ho.artificialheart


import com.mojang.blaze3d.shaders.FogShape
import com.mojang.blaze3d.systems.RenderSystem
import org.joml.Vector3f
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType

class BaseFluidType extends FluidType {
    var stillTexture: ResourceLocation
    var flowingTexture: ResourceLocation
    var overlayTexture: ResourceLocation
    var tintColor: Int
    var fogColor: Vector3f

    val BaseFluidType= (stillTexture: ResourceLocation, flowingTexture: ResourceLocation, overlayTexture: ResourceLocation ,
                        tintColor: Int , fogColor: Vector3f , properties: Properties ) => {
        def this() = this([Properties.statement@@])
        this.stillTexture = stillTexture
        this.flowingTexture = flowingTexture
        this.overlayTexture = overlayTexture
        this.tintColor = tintColor
        this.fogColor = fogColor
    }

    def getStillTexture(): ResourceLocation = stillTexture
    

    def getFlowingTexture(): ResourceLocation =flowingTexture
    

    def getTintColor(): Int = tintColor
    

    def getOverlayTexture(): ResourceLocation = overlayTexture

    def getFogColor(): Vector3f = fogColor

    override def initializeClient(consumer: Consumer[IClientFluidTypeExtensions]):void = 
        {
        consumer.accept(new IClientFluidTypeExtensions() {

            override def getStillTexture(): ResourceLocation = stillTexture
            
            override def getFlowingTexture(): ResourceLocation = flowingTexture

            override def getOverlayTexture(): ResourceLocation = overlayTexture

            override def getTintColor(): Int = tintColor

            override def modifyFogColor(camera: Camera , partialTick: float , level: ClientLevel ,
                                                     renderDistance: Int, darkenWorldAmount: float, fluidFogColor: Vector3f):Vector3f = fogColor
        

            def modifyFogRender(camera: Camera , mode: FogRenderer.FogMode , renderDistance: float, partialTick: float,
                                         nearDistance: float,  farDistance: float,  shape: FogShape):void = {
                RenderSystem.setShaderFogStart(1f)
                RenderSystem.setShaderFogEnd(6f)
            }
        })
    }
}
```



#### Error stacktrace:

```
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:74)
	dotty.tools.dotc.util.Signatures$.$anonfun$29(Signatures.scala:686)
	scala.collection.immutable.List.map(List.scala:247)
	dotty.tools.dotc.util.Signatures$.bestAlternative(Signatures.scala:686)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:225)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:101)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:47)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:422)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Select(Select(Select(Ident(_root_),scala),Predef),???) is not assigned