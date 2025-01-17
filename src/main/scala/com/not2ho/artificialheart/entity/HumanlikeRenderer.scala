package com.not2ho.artificialheart.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation


class HumanlikeRenderer( pContext : EntityRendererProvider.Context ) extends MobRenderer[HumanlikeEntity, HumanlikeModel[HumanlikeEntity]](
  pContext, new HumanlikeModel[HumanlikeEntity]( pContext.bakeLayer( HumanlikeModel.LAYER_LOCATION ) ), 2f ) {
  override def getTextureLocation( pEntity : HumanlikeEntity ) = new ResourceLocation( ArtificialHeart.MOD_ID, "textures/entity/humanlike.png" )

  override def render( pEntity : HumanlikeEntity, pEntityYaw : Float, pPartialTicks : Float, pMatrixStack : PoseStack,
                       pBuffer : MultiBufferSource, pPackedLight : Int ) : Unit = {
    if (pEntity.isBaby) pMatrixStack.scale( 0.5f, 0.5f, 0.5f )
    super.render( pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight )
  }
}