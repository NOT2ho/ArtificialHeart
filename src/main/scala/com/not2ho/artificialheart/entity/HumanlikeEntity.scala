package com.not2ho.artificialheart.entity

import com.not2ho.artificialheart.entity.ai.HumanlikeAttackGoal.HumanlikeAttackGoal
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity.createLivingAttributes
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import org.jetbrains.annotations.Nullable

import java.lang.Boolean

object HumanlikeEntity {
  private val ATTACKING =
    SynchedEntityData.defineId( classOf[HumanlikeEntity],EntityDataSerializers.BOOLEAN )

  def createAttributes : AttributeSupplier.Builder = createLivingAttributes.add( Attributes.MAX_HEALTH, 20D ).add( Attributes.FOLLOW_RANGE, 24D ).add(
    Attributes.MOVEMENT_SPEED, 0.5D ).add( Attributes.ARMOR_TOUGHNESS, 0.1f ).add( Attributes.ATTACK_KNOCKBACK,
                                                                                    0.5f ).add( Attributes.ATTACK_DAMAGE, 2f )
}

class HumanlikeEntity( pEntityType : EntityType[_ <: Animal], pLevel : Level ) extends Animal( pEntityType, pLevel ) {
  final val idleAnimationState = new AnimationState
  private var idleAnimationTimeout = 0
  final val attackAnimationState = new AnimationState
  var attackAnimationTimeout = 0

  override def tick( ) : Unit = {
    super.tick()
    if (this.level.isClientSide) setupAnimationStates()
  }

  private def setupAnimationStates( ) : Unit = {
    if (this.idleAnimationTimeout <= 0) {
      this.idleAnimationTimeout = this.random.nextInt( 40 ) + 40
      this.idleAnimationState.start( this.tickCount )
    }
    else this.idleAnimationTimeout -= 1
    if (this.isAttacking && attackAnimationTimeout <= 0) {
      attackAnimationTimeout = 40

      attackAnimationState.start( this.tickCount )
    }
    else this.attackAnimationTimeout -= 1
    if (!this.isAttacking) attackAnimationState.stop()
  }

  override protected def updateWalkAnimation( pPartialTick : Float ) : Unit = {
    var f = .0
    if (this.getPose eq Pose.STANDING) f = Math.min( pPartialTick * 6F, 1f )
    else f = 0f
    this.walkAnimation.update( f.toFloat, 0.2f )
  }

  def setAttacking( attacking : Boolean ) : Unit = {
    this.entityData.set( HumanlikeEntity.ATTACKING, attacking )
  }

  def isAttacking : Boolean = this.entityData.get( HumanlikeEntity.ATTACKING )

  override protected def defineSynchedData( ) : Unit = {
    super.defineSynchedData()
//    entityData.define( HumanlikeEntity.ATTACKING, false )
  }

  override protected def registerGoals( ) : Unit = {
    this.goalSelector.addGoal( 0, new FloatGoal( this ) )
    this.goalSelector.addGoal( 1, new HumanlikeAttackGoal( this, 1.0D, true ) )
    this.goalSelector.addGoal( 1, new BreedGoal( this, 4D ) )
    this.goalSelector.addGoal( 2, new TemptGoal( this, 1.2D, Ingredient.of( Items.COBBLESTONE ), false ) )
    this.goalSelector.addGoal( 3, new FollowParentGoal( this, 1.1D ) )
    this.goalSelector.addGoal( 4, new WaterAvoidingRandomStrollGoal( this, 1.1D ) )
    this.goalSelector.addGoal( 5, new LookAtPlayerGoal( this, classOf[Player], 3f ) )
    this.goalSelector.addGoal( 6, new RandomLookAroundGoal( this ) )
    this.targetSelector.addGoal( 1, new HurtByTargetGoal( this ) )
  }

  @Nullable override def getBreedOffspring( pLevel : ServerLevel, pOtherParent : AgeableMob ) : AgeableMob = PinkEntities.HUMANLIKE.get.create( pLevel )

  override def isFood( pStack : ItemStack ) = pStack.is( Items.COBBLESTONE )

  @Nullable override protected def getAmbientSound : SoundEvent = SoundEvents.SNIFFER_IDLE

  @Nullable override protected def getHurtSound( pDamageSource : DamageSource ) : SoundEvent = SoundEvents.SNIFFER_SNIFFING

  @Nullable override protected def getDeathSound : SoundEvent = SoundEvents.SNIFFER_HAPPY
}