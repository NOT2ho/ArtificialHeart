package com.not2ho.artificialheart.entity.animations
import net.minecraft.client.animation.{AnimationChannel, AnimationDefinition, Keyframe, KeyframeAnimations}

/**
 * Made with Blockbench 4.11.2
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 **/
object HumanlikeAnimationDefinitions {
  val HUMANLIKE_WALK : AnimationDefinition = AnimationDefinition.Builder.withLength( 1.0F ).looping()
    .addAnimation( "Head", new AnimationChannel(
      AnimationChannel.Targets.ROTATION
      , new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 0.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.25F, KeyframeAnimations.degreeVec( 10.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.4583F, KeyframeAnimations.degreeVec( -2.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.7083F, KeyframeAnimations.degreeVec( 7.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 1.0F, KeyframeAnimations.degreeVec( -7.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "Head", new AnimationChannel(
      AnimationChannel.Targets.SCALE
      , new Keyframe( 0.0F, KeyframeAnimations.scaleVec( 1.0F, 1.0F, 1.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.25F, KeyframeAnimations.scaleVec( 1.3F, 0.8F, 1.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.4583F, KeyframeAnimations.scaleVec( 1.0F, 1.0F, 1.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.7083F, KeyframeAnimations.scaleVec( 1.3F, 0.8F, 1.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 1.0F, KeyframeAnimations.scaleVec( 1.0F, 1.0F, 1.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "RightArm", new AnimationChannel(
      AnimationChannel.Targets.ROTATION
      , new Keyframe( 0.0F, KeyframeAnimations.degreeVec( -35.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.4583F, KeyframeAnimations.degreeVec( 52.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 1.0F, KeyframeAnimations.degreeVec( -35.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "LeftArm", new AnimationChannel(
      AnimationChannel.Targets.ROTATION
      , new Keyframe( 0.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 0.4583F, KeyframeAnimations.degreeVec( -35.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
      , new Keyframe( 1.0F, KeyframeAnimations.degreeVec( 52.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation("RightLeg", new AnimationChannel(
      AnimationChannel.Targets.ROTATION
        , new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 42.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
        , new Keyframe( 0.4583F, KeyframeAnimations.degreeVec( -30.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
        , new Keyframe( 1.0F, KeyframeAnimations.degreeVec( 42.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation("LeftLeg", new AnimationChannel(
      AnimationChannel.Targets.ROTATION
        , new Keyframe( 0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
        , new Keyframe( 0.4583F, KeyframeAnimations.degreeVec( 42.5F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR )
        , new Keyframe( 1.0F, KeyframeAnimations.degreeVec( -30.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) ).build

  
    val HUMANLIKE_IDLE : AnimationDefinition = AnimationDefinition.Builder.withLength( 0.5F )
      .looping.addAnimation( "Head", new AnimationChannel(
        AnimationChannel.Targets.ROTATION,
        new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 0.0F, 0.0F, -5.0F ), AnimationChannel.Interpolations.LINEAR ),
        new Keyframe( 0.25F, KeyframeAnimations.degreeVec( 0.0F, 0.0F, 10.0F ), AnimationChannel.Interpolations.LINEAR ),
        new Keyframe( 0.5F, KeyframeAnimations.degreeVec( 0.0F, 0.0F, -5.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
      .addAnimation( "RightArm", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                              new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 2.5F, 0.0F, 0.0F ),
                                                           AnimationChannel.Interpolations.LINEAR ) ) )
      .addAnimation( "LeftArm", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                             new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 10.0F, 0.0F, 0.0F ),
                                                          AnimationChannel.Interpolations.LINEAR ) ) )
      .addAnimation( "RightLeg", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                              new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 0.0F, 0.0F, 0.0F ),
                                                           AnimationChannel.Interpolations.LINEAR ) ) )
      .addAnimation( "LeftLeg", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                             new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 5.0F, 0.0F, 0.0F ),
                                                          AnimationChannel.Interpolations.LINEAR ) ) ).build

  val HUMANLIKE_ATTACK : AnimationDefinition = AnimationDefinition.Builder.withLength( 0.5F ).addAnimation( "RightArm", new AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 45.0F, 0.0F, 0.0F ), AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "LeftArm", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                           new Keyframe( 0.0F, KeyframeAnimations.degreeVec( -217.5F, 0.0F, 0.0F ),
                                                        AnimationChannel.Interpolations.LINEAR ),
                                           new Keyframe( 0.2083F, KeyframeAnimations.degreeVec( -115.0F, 0.0F, 0.0F ),
                                                        AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "LeftArm", new AnimationChannel( AnimationChannel.Targets.SCALE,
                                           new Keyframe( 0.0F, KeyframeAnimations.scaleVec( 2.7F, 1.0F, 1.8F ),
                                                        AnimationChannel.Interpolations.LINEAR ),
                                           new Keyframe( 0.2083F, KeyframeAnimations.scaleVec( 1.0F, 1.0F, 1.0F ),
                                                        AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "RightLeg", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                            new Keyframe( 0.0F, KeyframeAnimations.degreeVec( -50.0F, 0.0F, 0.0F ),
                                                         AnimationChannel.Interpolations.LINEAR ) ) )
    .addAnimation( "LeftLeg", new AnimationChannel( AnimationChannel.Targets.ROTATION,
                                           new Keyframe( 0.0F, KeyframeAnimations.degreeVec( 30.0F, 0.0F, 0.0F ),
                                                        AnimationChannel.Interpolations.LINEAR ) ) ).build

}



  