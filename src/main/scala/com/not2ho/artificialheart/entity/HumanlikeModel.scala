package com.not2ho.artificialheart.entity

import com.mojang.blaze3d.vertex.{PoseStack, VertexConsumer}
import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.entity.animations.HumanlikeAnimationDefinitions
import net.minecraft.client.model.{EntityModel, HierarchicalModel}
import net.minecraft.client.model.geom.{ModelLayerLocation, ModelPart, PartPose}
import net.minecraft.client.model.geom.builders.{CubeDeformation, CubeListBuilder, LayerDefinition, MeshDefinition, PartDefinition}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

import java.util
import java.util.{List, Map}

// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings// Exported for Minecraft version 1.17 or later
// with Mojang mappings
// Paste this class into your mod and generate all required imports// Paste this class into your mod and generate all
// required imports


object HumanlikeModel {
  // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into
  // this model's constructor
  val LAYER_LOCATION = new ModelLayerLocation( new ResourceLocation( ArtificialHeart.MOD_ID, "humanlike" ), "main" )


  
  
  def createBodyLayer : LayerDefinition = {
    val meshdefinition = new MeshDefinition
    val partdefinition = meshdefinition.getRoot
    val Head = partdefinition.addOrReplaceChild( "Head", CubeListBuilder.create.texOffs( 0, 0 )
      .addBox( -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation( 0.0F ) ).texOffs( 32, 0 )
      .addBox( -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation( 0.5F ) ), PartPose
                                                   .offsetAndRotation( 0.0F, 0.0F, 0.0F, -0.1047F, 0.0873F, 0.0F ) )
    val Body = partdefinition.addOrReplaceChild( "Body", CubeListBuilder.create.texOffs( 16, 16 )
      .addBox( -4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation( 0.0F ) ).texOffs( 16, 32 )
      .addBox( -4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation( 0.25F ) ),
                                                 PartPose.offset( 0.0F, 0.0F, 0.0F ) )
    val RightArm = partdefinition.addOrReplaceChild( "RightArm", CubeListBuilder.create.texOffs( 40, 16 )
      .addBox( -2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation( 0.0F ) ).texOffs( 40, 32 )
      .addBox( -2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation( 0.25F ) ), PartPose
                                                       .offsetAndRotation( -5.0F, 2.0F, 0.0F, -0.0436F, 0.0F, 0.0F ) )
    val LeftArm = partdefinition.addOrReplaceChild( "LeftArm", CubeListBuilder.create.texOffs( 32, 48 )
      .addBox( -1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation( 0.0F ) ).texOffs( 48, 48 )
      .addBox( -1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation( 0.25F ) ), PartPose
                                                      .offsetAndRotation( 5.0F, 2.0F, 0.0F, -0.096F, 0.0F, 0.0F ) )
    val RightLeg = partdefinition.addOrReplaceChild( "RightLeg", CubeListBuilder.create.texOffs( 0, 16 )
      .addBox( -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation( 0.0F ) ).texOffs( 0, 32 )
      .addBox( -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation( 0.25F ) ), PartPose
                                                       .offsetAndRotation( -1.9F, 12.0F, 0.0F, -0.0698F, 0.0F,
                                                                           0.0349F ) )
    val LeftLeg = partdefinition.addOrReplaceChild( "LeftLeg", CubeListBuilder.create.texOffs( 16, 48 )
      .addBox( -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation( 0.0F ) ).texOffs( 0, 48 )
      .addBox( -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation( 0.25F ) ), PartPose
                                                      .offsetAndRotation( 1.9F, 12.0F, 0.0F, -0.0873F, 0.0F,
                                                                          -0.0349F ) )
    LayerDefinition.create( meshdefinition, 64, 64 )
  }
}

class HumanlikeModel[T <: Entity]( proot : ModelPart ) extends HierarchicalModel[T] {
  var Head : ModelPart = proot.getChild( "Head" )
  var Body : ModelPart = proot.getChild( "Body" )
  var RightArm : ModelPart = proot.getChild( "RightArm" )
  var LeftArm : ModelPart = proot.getChild( "LeftArm" )
  var RightLeg : ModelPart = proot.getChild( "RightLeg" )
  var LeftLeg : ModelPart = proot.getChild( "LeftLeg" )

  override def setupAnim( entity : T, limbSwing : Float, limbSwingAmount : Float, ageInTicks : Float, netHeadYaw : Float, headPitch : Float ) : Unit = {
    this.root.getAllParts.forEach(p => p.resetPose())

    this.animateWalk( HumanlikeAnimationDefinitions.HUMANLIKE_WALK, limbSwing, limbSwingAmount, 1f, 1.5f )
    this.animate( entity.asInstanceOf[HumanlikeEntity].idleAnimationState, HumanlikeAnimationDefinitions.HUMANLIKE_IDLE, ageInTicks, 1f )
    this.animate( entity.asInstanceOf[HumanlikeEntity].attackAnimationState, HumanlikeAnimationDefinitions.HUMANLIKE_ATTACK, ageInTicks, 1f )
  }

  override def renderToBuffer( poseStack :PoseStack, vertexConsumer : VertexConsumer, packedLight : Int, packedOverlay : Int
                      , red: Float, green : Float, blue : Float, alpha : Float ) : Unit = {
    Head.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
    Body.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
    RightArm.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
    LeftArm.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
    RightLeg.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
    LeftLeg.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha )
  }

  override def root : ModelPart = proot
}