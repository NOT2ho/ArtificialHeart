package com.not2ho.artificialheart.fluid


import com.mojang.blaze3d.shaders.FogShape
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.FluidType.Properties
import org.joml.Vector3f

import java.util.function.Consumer

class BaseFluidType(var stillTexture: ResourceLocation, var flowingTexture: ResourceLocation, var overlayTexture: ResourceLocation ,
                        var tintColor: Int , var fogColor: Vector3f , var properties: Properties ) extends FluidType(properties) {


    def getStillTexture: ResourceLocation = stillTexture

    def getFlowingTexture: ResourceLocation = flowingTexture

    def getTintColor: Int = tintColor

    def getOverlayTexture: ResourceLocation = overlayTexture

    def getFogColor: Vector3f = fogColor

    override def initializeClient(consumer: Consumer[IClientFluidTypeExtensions]) : Unit = {
        consumer.accept(new IClientFluidTypeExtensions() {

            override def getStillTexture: ResourceLocation = stillTexture
            
            override def getFlowingTexture: ResourceLocation = flowingTexture

            override def getOverlayTexture: ResourceLocation = overlayTexture

            override def getTintColor: Int = tintColor

            override def modifyFogColor(camera: Camera 
                                        , partialTick: Float 
                                        , level: ClientLevel 
                                        , renderDistance: Int
                                        , darkenWorldAmount: Float
                                        , fluidFogColor:Vector3f): Vector3f = fogColor
        

            override def modifyFogRender(camera: Camera , mode: FogRenderer.FogMode , renderDistance: Float, partialTick: Float,
                                         nearDistance: Float,  farDistance: Float,  shape: FogShape):Unit = {
                RenderSystem.setShaderFogStart(1f)
                RenderSystem.setShaderFogEnd(6f)
            }
        }
        )
    }
}