package com.not2ho.artificialheart.worldgen.dimension

import PinkPortalShape.{MAX_SIDE, test}

import java.util.Optional
import java.util.function.Predicate
import javax.annotation.Nullable
import net.minecraft.BlockUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.level.{BlockGetter, LevelAccessor}
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.NetherPortalBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.portal.{PortalInfo, PortalShape}
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

import scala.annotation.tailrec



object PinkPortalShape {
  private val MIN_WIDTH = 2
  val MAX_SIDE = 21
  private val MIN_HEIGHT = 3

  def test( pState : BlockState, pBlockGetter : BlockGetter, pBlockPos : BlockPos ) : Boolean = {
    pState.isPortalFrame( pBlockGetter, pBlockPos )
  }

  private val SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F
  private val SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0D

  def findEmptyPortalShape( pLevel : LevelAccessor, pBottomLeft : BlockPos, pAxis : Direction.Axis ) : Optional[PinkPortalShape] = {
    findPortalShape( pLevel, pBottomLeft, ( portalShape : PinkPortalShape ) => portalShape.isValid && portalShape.numPortalBlocks == 0, pAxis
                     )
  }

  def findPortalShape(
                       pLevel : LevelAccessor, pBottomLeft : BlockPos, pPredicate : Predicate[PinkPortalShape], pAxis : Direction.Axis
                     ) : Optional[PinkPortalShape] = {
    val optional = Optional.of( new PinkPortalShape( pLevel, pBottomLeft, pAxis ) ).filter( pPredicate )
    if (optional.isPresent) optional
    else {
      val direction$axis = if (pAxis eq Direction.Axis.X) Direction.Axis.Z
      else Direction.Axis.X
      Optional.of( new PinkPortalShape( pLevel, pBottomLeft, direction$axis ) ).filter( pPredicate )
    }
  }

  def getRelativePosition(
                           pFoundRectangle : BlockUtil.FoundRectangle, pAxis : Direction.Axis, pPos : Vec3, pEntityDimensions : EntityDimensions
                         ) : Vec3 = {
    val d0 = pFoundRectangle.axis1Size.toDouble - pEntityDimensions.width.toDouble
    val d1 = pFoundRectangle.axis2Size.toDouble - pEntityDimensions.height.toDouble
    val blockpos = pFoundRectangle.minCorner
    var d2 = .0
    if (d0 > 0.0D) {
      val f = blockpos.get( pAxis ).toFloat + pEntityDimensions.width / 2.0F
      d2 = Mth.clamp( Mth.inverseLerp( pPos.get( pAxis ) - f.toDouble, 0.0D, d0 ), 0.0D, 1.0D )
    }
    else d2 = 0.5D
    var d4 = .0
    if (d1 > 0.0D) {
      val direction$axis = Direction.Axis.Y
      d4 = Mth.clamp( Mth.inverseLerp( pPos.get( direction$axis ) - blockpos.get( direction$axis ).toDouble, 0.0D, d1 ), 0.0D, 1.0D )
    }
    else d4 = 0.0D
    val direction$axis1 = if (pAxis eq Direction.Axis.X) Direction.Axis.Z
    else Direction.Axis.X
    val d3 = pPos.get( direction$axis1 ) - (blockpos.get( direction$axis1 ).toDouble + 0.5D)
    new Vec3( d2, d4, d3 )
  }

  def createPortalInfo(
                        pLevel : ServerLevel, pPortalPos : BlockUtil.FoundRectangle, pAxis : Direction.Axis, pRelativePos : Vec3, pEntity : Entity, pVelocity : Vec3, pYRot : Float, pXRot : Float
                      ) : PortalInfo = {
    val blockpos = pPortalPos.minCorner
    val blockstate = pLevel.getBlockState( blockpos )
    val direction$axis = blockstate.getOptionalValue( BlockStateProperties.HORIZONTAL_AXIS ).orElse
    Direction.Axis.X
    val d0 = pPortalPos.axis1Size.toDouble
    val d1 = pPortalPos.axis2Size.toDouble
    val entitydimensions = pEntity.getDimensions( pEntity.getPose )
    val i = if (pAxis eq direction$axis) 0
    else 90
    val vec3 = if (pAxis eq direction$axis) pVelocity
    else new Vec3( pVelocity.z, pVelocity.y, -pVelocity.x )
    val d2 = entitydimensions.width.toDouble / 2.0D + (d0 - entitydimensions.width.toDouble) * pRelativePos.x
    val d3 = (d1 - entitydimensions.height.toDouble) * pRelativePos.y
    val d4 = 0.5D + pRelativePos.z
    val flag = direction$axis eq Direction.Axis.X
    val vec31 = new Vec3( blockpos.getX.toDouble + (
                                                   if (flag) d2
                                                   else d4), blockpos.getY.toDouble + d3, blockpos.getZ.toDouble + (
                                                                                                                   if (flag) d4
                                                                                                                   else d2)
                          )
    val vec32 = findCollisionFreePosition( vec31, pLevel, pEntity, entitydimensions )
    new PortalInfo( vec32, vec3, pYRot + i.toFloat, pXRot )
  }

  private def findCollisionFreePosition(
                                         pPos : Vec3, pLevel : ServerLevel, pEntity : Entity, pDimensions : EntityDimensions
                                       ) = {
    if (!(pDimensions.width > 4.0F) && !(pDimensions.height > 4.0F)) {
      val d0 = pDimensions.height.toDouble / 2.0D
      val vec3 = pPos.add( 0.0D, d0, 0.0D )
      val voxelshape = Shapes.create( AABB.ofSize( vec3, pDimensions.width.toDouble, 0.0D, pDimensions.width.toDouble
                                                   ).expandTowards( 0.0D, 1.0D, 0.0D ).inflate( 1.0E-6D )
                                      )
      val optional = pLevel.findFreePosition( pEntity, voxelshape, vec3, pDimensions.width.toDouble, pDimensions.height.toDouble, pDimensions.width.toDouble )
      val optional1 = optional.map( ( pVec3 : Vec3 ) => pVec3.subtract( 0.0D, d0, 0.0D ) )
      optional1.orElse( pPos )
    }
    else pPos
  }
}

  class PinkPortalShape(private val level: LevelAccessor, pBottomLeft: BlockPos, private val axis: Direction.Axis) {
    this.rightDir = if (axis eq Direction.Axis.X) Direction.WEST
    else Direction.SOUTH
    this.bottomLeft = this.calculateBottomLeft( pBottomLeft )
    if (this.bottomLeft == null) {
      this.bottomLeft = pBottomLeft
      this.width = 1
      this.height = 1
    }
    else {
      this.width = this.calculateWidth
      if (this.width > 0) this.height = this.calculateHeight
    }
    final private var rightDir : Direction = null
    private var numPortalBlocks = 0
    @Nullable private var bottomLeft : BlockPos = null
    private var height = 0
    final private var width = 0

    private def isEmpty( pState : BlockState ) = pState.isAir || pState.is( BlockTags.FIRE ) || pState.is( Blocks.NETHER_PORTAL )

    @Nullable private def calculateBottomLeft(pPos: BlockPos) = {
      var mPos = pPos
      val i = Math.max( this.level.getMinBuildHeight, pPos.getY - MAX_SIDE )
      while (mPos.getY > i && isEmpty(this.level.getBlockState(mPos.below)))
        mPos = mPos.below
      val direction = this.rightDir.getOpposite
      val j = this.getDistanceUntilEdgeAboveFrame(pPos, direction) - 1
      if (j < 0) null
      else mPos.relative(direction, j)
    }
  

    private def calculateWidth = {
      val i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir)
      if (i >= 2 && i <= MAX_SIDE) i
      else 0
    }

    @tailrec final def recursiveGetDistanceUntilEdgeAboveFrame( bool: Boolean, i : Int, bm: BlockPos.MutableBlockPos, pPos: BlockPos, pDirection: Direction) : Int = {
      i match
        case 0 => 0
        case j if j < i =>
          bm.set( pPos ).move( pDirection, i )
          val blockstate = this.level.getBlockState(bm)
          val isFrame = test(blockstate, this.level, bm)
          if (!isEmpty(blockstate) && isFrame) return i
          val blockstate1 = this.level.getBlockState( bm.move( Direction.DOWN ) )
          if (!test( blockstate1, this.level, bm )) return 0
          recursiveGetDistanceUntilEdgeAboveFrame( isFrame, i + 1 , bm, pPos, pDirection)
    }

    private def getDistanceUntilEdgeAboveFrame(pPos: BlockPos, pDirection: Direction): Int = {
      val blockpos$mutableblockpos = new BlockPos.MutableBlockPos
      recursiveGetDistanceUntilEdgeAboveFrame( true, MAX_SIDE, blockpos$mutableblockpos, pPos, pDirection )
    }
  
    private def calculateHeight = {
      val blockpos$mutableblockpos = new BlockPos.MutableBlockPos
      val i = this.getDistanceUntilTop(blockpos$mutableblockpos)
      if (i >= 3 && i <= MAX_SIDE && this.hasTopFrame( blockpos$mutableblockpos, i )) i
      else 0
    }
  
    private def hasTopFrame(pPos: BlockPos.MutableBlockPos, pDistanceToTop: Int): Boolean = {
      for (i <- 0 until this.width) {
        val blockpos$mutableblockpos = pPos.set(this.bottomLeft).move(Direction.UP,
                                                                      pDistanceToTop).move
          (this.rightDir, i)
        if (!test(this.level.getBlockState(blockpos$mutableblockpos), this.level,
                                        blockpos$mutableblockpos)) return false
      }
      true
    }
  
    private def getDistanceUntilTop(pPos: BlockPos.MutableBlockPos): Int = {
      for (i <- 0 until MAX_SIDE) {
        pPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1)
        if (!test(this.level.getBlockState(pPos), this.level, pPos)) return i
        pPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width)
        if (!test(this.level.getBlockState(pPos), this.level, pPos)) return i
        for (j <- 0 until this.width) {
          pPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j)
          val blockstate = this.level.getBlockState(pPos)
          if (!isEmpty(blockstate)) return i
          if (blockstate.is(Blocks.NETHER_PORTAL)) this.numPortalBlocks += 1
        }
      }
      MAX_SIDE
    }
  
    def isValid: Boolean = this.bottomLeft != null && this.width >= 2 && this.width <= MAX_SIDE && this.height >= 3 && this.height <= MAX_SIDE
  
    def createPortalBlocks(): Unit = {
      val blockstate = Blocks.NETHER_PORTAL.defaultBlockState.setValue(NetherPortalBlock.AXIS, this.axis)
      BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft
        .relative(Direction.UP, this.height - 1)
        .relative(this.rightDir, this.width - 1)).forEach((pBlockState: BlockPos) => this.level.setBlock(pBlockState, blockstate, 18)
      )
    }
  
    def isComplete: Boolean = this.isValid && this.numPortalBlocks == this.width * this.height
  }
