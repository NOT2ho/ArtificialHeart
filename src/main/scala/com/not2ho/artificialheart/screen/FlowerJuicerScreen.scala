package com.not2ho.artificialheart.screen

import com.mojang.blaze3d.systems.RenderSystem
import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory


object FlowerJuicerScreen {
  private val TEXTURE = new ResourceLocation(ArtificialHeart.MOD_ID, "textures/gui/flower_juicer_gui.png")
}

class FlowerJuicerScreen(pMenu: FlowerJuicerMenu, pPlayerInventory: Inventory, pTitle: Component) extends AbstractContainerScreen[FlowerJuicerMenu](pMenu, pPlayerInventory, pTitle) {

  override protected def init(): Unit = {
    super.init()
  }

  override protected def renderBg(guiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int): Unit = {
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader)
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
    RenderSystem.setShaderTexture(0, FlowerJuicerScreen.TEXTURE)
    val x = (width - imageWidth) / 2
    val y = (height - imageHeight) / 2
    guiGraphics.blit(FlowerJuicerScreen.TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
    renderProgressArrow(guiGraphics, x, y)
  }

  private def renderProgressArrow(guiGraphics: GuiGraphics, x: Int, y: Int): Unit = {
    if (menu.isCrafting) guiGraphics.blit(FlowerJuicerScreen.TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress)
  }

  override def render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float): Unit = {
    renderBackground(guiGraphics)
    super.render(guiGraphics, mouseX, mouseY, delta)
    renderTooltip(guiGraphics, mouseX, mouseY)
  }
}