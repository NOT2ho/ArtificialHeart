package com.not2ho.artificialheart.entity.ai.HumanlikeAttackGoal

import com.not2ho.artificialheart.entity.HumanlikeEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal


class HumanlikeAttackGoal( pMob : PathfinderMob, pSpeedModifier : Double, pFollowingTargetEvenIfNotSeen : Boolean )
  extends MeleeAttackGoal(
    pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen ) {
  val entity = pMob.asInstanceOf[HumanlikeEntity]
  private var attackDelay = 40
  private var ticksUntilNextAttack = 40
  private var shouldCountTillNextAttack = false

  override def start( ) : Unit = {
    super.start()
    attackDelay = 40
    ticksUntilNextAttack = 40
  }

  override protected def checkAndPerformAttack( pEnemy : LivingEntity, pDistToEnemySqr : Double ) : Unit = {
    if (isEnemyWithinAttackDistance( pEnemy, pDistToEnemySqr )) {
      shouldCountTillNextAttack = true
      if (isTimeToStartAttackAnimation) entity.setAttacking( true )
      if (isTimeToAttack) {
        this.mob.getLookControl.setLookAt( pEnemy.getX, pEnemy.getEyeY, pEnemy.getZ )
        performAttack( pEnemy )
      }
    }
    else {
      resetAttackCooldown()
      shouldCountTillNextAttack = false
      entity.setAttacking( false )
      entity.attackAnimationTimeout = 0
    }
  }

  private def isEnemyWithinAttackDistance( pEnemy : LivingEntity, pDistToEnemySqr : Double ) = pDistToEnemySqr <= this.getAttackReachSqr( pEnemy )

  override protected def resetAttackCooldown( ) : Unit = {
    this.ticksUntilNextAttack = this.adjustedTickDelay( attackDelay * 2 )
  }

  override protected def isTimeToAttack : Boolean = this.ticksUntilNextAttack <= 0

  protected def isTimeToStartAttackAnimation : Boolean = this.ticksUntilNextAttack <= attackDelay

  override protected def getTicksUntilNextAttack : Int = this.ticksUntilNextAttack

  protected def performAttack( pEnemy : LivingEntity ) : Unit = {
    this.resetAttackCooldown()
    this.mob.swing( InteractionHand.MAIN_HAND )
    this.mob.doHurtTarget( pEnemy )
  }

  override def tick( ) : Unit = {
    super.tick()
    if (shouldCountTillNextAttack) this.ticksUntilNextAttack = Math.max( this.ticksUntilNextAttack - 1, 0 )
  }

  override def stop( ) : Unit = {
    entity.setAttacking( false )
    super.stop()
  }
}