file:///C:/Users/i5-32/Desktop/artificialheart/src/main/scala/com/not2ho/artificialheart/fluid/basefluid.scala
### java.lang.AssertionError: assertion failed: MethodType(List(stillTexture, flowingTexture, overlayTexture, tintColor, fogColor, properties), List(dotty.tools.dotc.core.Types$PreviousErrorType@4d81230b, dotty.tools.dotc.core.Types$PreviousErrorType@41c26d10, dotty.tools.dotc.core.Types$PreviousErrorType@20c8bf47, TypeRef(ThisType(TypeRef(NoPrefix,module class scala)),class Int), dotty.tools.dotc.core.Types$PreviousErrorType@2e994066, dotty.tools.dotc.core.Types$PreviousErrorType@6a44b7c5), TypeRef(ThisType(TypeRef(NoPrefix,module class scala)),class Unit))

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 891
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
        def this (@@name: String) {
            this(name, 0)
        }
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
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.core.TypeErasure.dotty$tools$dotc$core$TypeErasure$$sigName(TypeErasure.scala:950)
	dotty.tools.dotc.core.TypeErasure.dotty$tools$dotc$core$TypeErasure$$sigName(TypeErasure.scala:951)
	dotty.tools.dotc.core.TypeErasure$.sigName(TypeErasure.scala:234)
	dotty.tools.dotc.core.Signature$.apply(Signature.scala:167)
	dotty.tools.dotc.core.Types$MethodOrPoly.computeSignature$2(Types.scala:3776)
	dotty.tools.dotc.core.Types$MethodOrPoly.signature(Types.scala:3801)
	dotty.tools.dotc.core.Denotations$SingleDenotation.signature(Denotations.scala:618)
	dotty.tools.dotc.core.Denotations$SingleDenotation.signature(Denotations.scala:608)
	dotty.tools.dotc.core.Symbols$Symbol.signature(Symbols.scala:192)
	dotty.tools.pc.SemanticdbSymbols$.addOverloadIdx$1(SemanticdbSymbols.scala:154)
	dotty.tools.pc.SemanticdbSymbols$.addDescriptor$1(SemanticdbSymbols.scala:175)
	dotty.tools.pc.SemanticdbSymbols$.addSymName(SemanticdbSymbols.scala:179)
	dotty.tools.pc.SemanticdbSymbols$.addOwner$1(SemanticdbSymbols.scala:134)
	dotty.tools.pc.SemanticdbSymbols$.addSymName(SemanticdbSymbols.scala:178)
	dotty.tools.pc.SemanticdbSymbols$.symbolName(SemanticdbSymbols.scala:117)
	dotty.tools.pc.utils.InteractiveEnrichments$.toSemanticdbSymbol$1(InteractiveEnrichments.scala:272)
	dotty.tools.pc.utils.InteractiveEnrichments$.symbolDocumentation(InteractiveEnrichments.scala:273)
	dotty.tools.pc.HoverProvider$.$anonfun$3(HoverProvider.scala:131)
	scala.collection.immutable.List.flatMap(List.scala:294)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:131)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: MethodType(List(stillTexture, flowingTexture, overlayTexture, tintColor, fogColor, properties), List(dotty.tools.dotc.core.Types$PreviousErrorType@4d81230b, dotty.tools.dotc.core.Types$PreviousErrorType@41c26d10, dotty.tools.dotc.core.Types$PreviousErrorType@20c8bf47, TypeRef(ThisType(TypeRef(NoPrefix,module class scala)),class Int), dotty.tools.dotc.core.Types$PreviousErrorType@2e994066, dotty.tools.dotc.core.Types$PreviousErrorType@6a44b7c5), TypeRef(ThisType(TypeRef(NoPrefix,module class scala)),class Unit))