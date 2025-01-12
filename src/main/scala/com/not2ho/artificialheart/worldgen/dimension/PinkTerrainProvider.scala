package com.not2ho.artificialheart.worldgen.dimension

import net.minecraft.util.CubicSpline
import net.minecraft.util.Mth
import net.minecraft.util.ToFloatFunction
import net.minecraft.world.level.levelgen.NoiseRouterData
import java.lang.Float


object PinkTerrainProvider {
  private val DEEP_OCEAN_CONTINENTALNESS : Float  = -0.51F
  private val OCEAN_CONTINENTALNESS: Float  = -0.4F
  private val PLAINS_CONTINENTALNESS: Float  = 0.1F
  private val BEACH_CONTINENTALNESS: Float  = -0.15F
  private val NO_TRANSFORM = ToFloatFunction.IDENTITY
  private val AMPLIFIED_OFFSET= ToFloatFunction.createUnlimited(pAO => {if (pAO < 0.0F) pAO else pAO * 2.0F})
  private val AMPLIFIED_FACTOR = ToFloatFunction.createUnlimited(pAF => {1.25F - 6.25F / (pAF + 5.0F)})
  private val AMPLIFIED_JAGGEDNESS = ToFloatFunction.createUnlimited(pAJ => {pAJ * 2.0F})

  def offset[C, I <: ToFloatFunction[C]]( pContinents : I, pErosion : I, pRidgesFolded : I, pAmplified
  : Boolean ) : CubicSpline[C, I] = {
    val tofloatfunction : ToFloatFunction[Float] = if (pAmplified) AMPLIFIED_OFFSET
    else NO_TRANSFORM
    val cubicspline = buildErosionOffsetSpline( pErosion, pRidgesFolded, -0.15F, 0.0F, 0.0F,
                                                0.1F, 0.0F, -0.03F, false, false,
                                                tofloatfunction )
    val cubicspline1 = buildErosionOffsetSpline( pErosion, pRidgesFolded, -0.1F, 0.03F, 0.1F,
                                                 0.1F, 0.01F, -0.03F, false, false,
                                                 tofloatfunction )
    val cubicspline2 = buildErosionOffsetSpline( pErosion, pRidgesFolded, -0.1F, 0.03F, 0.1F,
                                                 0.7F, 0.01F, -0.03F, true, true,
                                                 tofloatfunction )
    val cubicspline3 = buildErosionOffsetSpline( pErosion, pRidgesFolded, -0.05F, 0.03F, 0.1F,
                                                 1.0F, 0.01F, 0.01F, true, true, tofloatfunction )
    CubicSpline.builder[C, I]( pContinents, tofloatfunction ).addPoint( -1.1F, 0.044F ).addPoint( -1.02F,
                                                                                                  -0.2222F
                                                                                                  ).addPoint
      ( -0.51F, -0.2222F ).addPoint( -0.44F, -0.12F ).addPoint( -0.18F, -0.12F ).addPoint( -0.16F,
                                                                                           cubicspline )
      .addPoint( -0.15F, cubicspline ).addPoint( -0.1F, cubicspline1 ).addPoint( 0.25F, cubicspline2 ).addPoint(
        1.0F, cubicspline3 ).build
  }

  def factor[C, I <: ToFloatFunction[C]]( pContinents : I, pErosion : I, pRidges : I, pRidgesFolded : I,
                                                   pAmplified : Boolean ) : CubicSpline[C, I] = {
    val tofloatfunction = if (pAmplified) AMPLIFIED_FACTOR
    else NO_TRANSFORM
    CubicSpline.builder[C, I]( pContinents, NO_TRANSFORM ).addPoint( -0.19F, 3.95F ).addPoint( -0.15F,
                                                                                               getErosionFactor( pErosion, pRidges, pRidgesFolded, 6.25F, true, NO_TRANSFORM ) ).addPoint( -0.1F, getErosionFactor( pErosion, pRidges, pRidgesFolded, 5.47F, true, tofloatfunction ) ).addPoint( 0.03F, getErosionFactor( pErosion, pRidges, pRidgesFolded, 5.08F, true, tofloatfunction ) ).addPoint( 0.06F, getErosionFactor( pErosion, pRidges, pRidgesFolded, 4.69F, false, tofloatfunction ) ).build
  }

  def jaggedness[C, I <: ToFloatFunction[C]]( pContinents : I, pErosion : I, pRidges : I, pRidgesFolded : I,
                                                       pAmplified : Boolean ) : CubicSpline[C, I] = {
    val tofloatfunction = if (pAmplified) AMPLIFIED_JAGGEDNESS
    else NO_TRANSFORM
    val f = 0.65F
    CubicSpline.builder[C, I]( pContinents, tofloatfunction ).addPoint( -0.11F, 0.0F ).addPoint( 0.03F,
                                                                                                 buildErosionJaggednessSpline( pErosion, pRidges, pRidgesFolded, 1.0F, 0.5F, 0.0F, 0.0F, tofloatfunction ) ).addPoint( 0.65F, buildErosionJaggednessSpline( pErosion, pRidges, pRidgesFolded, 1.0F, 1.0F, 1.0F, 0.0F, tofloatfunction ) ).build
  }

  private def buildErosionJaggednessSpline[C, I <: ToFloatFunction[C]](
                                                                        pErosion : I,
                                                                        pRidges : I,
                                                                        pRidgesFolded : I,
                                                                        p236617 : Float,
                                                                        p236618 : Float,
                                                                        p236619: Float,
                                                                        p236620 : Float,
                                                                        pTransform
                                                                        : ToFloatFunction[Float]
                                                                      ) = {
    val f = -0.5775F
    val cubicspline = buildRidgeJaggednessSpline( pRidges, pRidgesFolded, p236617, p236618,
                                                  pTransform )
    val cubicspline1 = buildRidgeJaggednessSpline( pRidges, pRidgesFolded, p236619, p236620,
                                                   pTransform )
    CubicSpline.builder[C, I]( pErosion, pTransform ).addPoint( -1.0F, cubicspline ).addPoint( -0.78F,
                                                                                               cubicspline1
                                                                                               ).addPoint( -0.5775F, cubicspline1 ).addPoint( -0.375F, 0.0F ).build
  }

  private def buildRidgeJaggednessSpline[C, I <: ToFloatFunction[C]]( pRidges : I, pRidgesFolded : I, p236610
  : Float, p236611 : Float, pTransform : ToFloatFunction[Float] ) = {
    val f = NoiseRouterData.peaksAndValleys( 0.4F )
    val f1 = NoiseRouterData.peaksAndValleys( 0.56666666F )
    val f2 = (f + f1) / 2.0F
    val builder = CubicSpline.builder( pRidgesFolded, pTransform )
    builder.addPoint( f, 0.0F )
    if (p236611 > 0.0F) builder.addPoint( f2, buildWeirdnessJaggednessSpline( pRidges, p236611, pTransform ) )
    else builder.addPoint( f2, 0.0F )
    if (p236610 > 0.0F) builder.addPoint( 1.0F, buildWeirdnessJaggednessSpline( pRidges, p236610, pTransform ) )
    else builder.addPoint( 1.0F, 0.0F )
    builder.build
  }

  private def buildWeirdnessJaggednessSpline[C, I <: ToFloatFunction[C]]( pRidges : I, p236588 : Float, pTransform
  : ToFloatFunction[Float] ) = {
    val f = 0.63F * p236588
    val f1 = 0.3F * p236588
    CubicSpline.builder[C, I]( pRidges, pTransform ).addPoint( -0.01F, f ).addPoint( 0.01F, f1 ).build
  }

  private def getErosionFactor[C, I <: ToFloatFunction[C]]( pErosion : I, pRidges : I, pRidgesFolded : I, p236626
  : Float, p236627 : Boolean, pTransform : ToFloatFunction[Float] ) = {
    val cubicspline = CubicSpline.builder[C, I]( pRidges, pTransform ).addPoint( -0.2F, 6.3F )
      .addPoint( 0.2F, p236626 ).build
    val builder = CubicSpline.builder[C, I]( pErosion, pTransform ).addPoint( -0.6F,
                                                                              cubicspline
                                                                              ).addPoint(
      -0.5F, CubicSpline.builder[C, I]( pRidges, pTransform ).addPoint( -0.05F, 6.3F ).addPoint( 0.05F, 2.67F )
        .build ).addPoint( -0.35F, cubicspline ).addPoint( -0.25F, cubicspline ).addPoint( -0.1F, CubicSpline
      .builder[C, I]( pRidges, pTransform ).addPoint( -0.05F, 2.67F ).addPoint( 0.05F, 6.3F ).build ).addPoint(
      0.03F, cubicspline )
    if (p236627) {
      val cubicspline1 = CubicSpline.builder[C, I]( pRidges, pTransform ).addPoint( 0.0F, p236626
                                                                                    ).addPoint(
        0.1F, 0.625F ).build
      val cubicspline2 = CubicSpline.builder[C, I]( pRidgesFolded, pTransform ).addPoint( -0.9F
                                                                                          ,
                                                                                          p236626
                                                                                          )
        .addPoint( -0.69F, cubicspline1 ).build
      builder.addPoint( 0.35F, p236626 ).addPoint( 0.45F, cubicspline2 ).addPoint( 0.55F, cubicspline2 ).addPoint(
        0.62F, p236626 )
    }
    else {
      val cubicspline3 = CubicSpline.builder[C, I]( pRidgesFolded, pTransform ).addPoint( -0.7F
                                                                                          ,
                                                                                          cubicspline ).addPoint( -0.15F, 1.37F ).build
      val cubicspline4 = CubicSpline.builder[C, I]( pRidgesFolded, pTransform ).addPoint( 0.45F,
                                                                                          cubicspline ).addPoint( 0.7F, 1.56F ).build
      builder
        .addPoint( 0.05F, cubicspline4 )
        .addPoint( 0.4F, cubicspline4 )
        .addPoint( 0.45F, cubicspline3 )
        .addPoint( 0.55F, cubicspline3 )
        .addPoint( 0.58F, p236626 )
    }
    builder.build
  }

  private def calculateSlope( pY1 : Float, pY2 : Float, pX1 : Float, pX2 : Float ) = (pY2 - pY1) / (pX2 - pX1)

  private def buildMountainRidgeSplineWithPoints[C, I <: ToFloatFunction[C]]( pRidgesFolded : I, p236592 : Float,
                                                                              p236593 : Boolean, pTransform
                                                                              : ToFloatFunction[Float] ) = {
    val builder = CubicSpline.builder( pRidgesFolded, pTransform )
    val f = -0.7F
    val f1 = -1.0F
    val f2 = mountainContinentalness( -1.0F, p236592, -0.7F )
    val f3 = 1.0F
    val f4 = mountainContinentalness( 1.0F, p236592, -0.7F )
    val f5 = calculateMountainRidgeZeroContinentalnessPoint( p236592 )
    val f6 = -0.65F
    if (-0.65F < f5 && f5 < 1.0F) {
      val f14 = mountainContinentalness( -0.65F, p236592, -0.7F )
      val f8 = -0.75F
      val f9 = mountainContinentalness( -0.75F, p236592, -0.7F )
      val f10 = calculateSlope( f2, f9, -1.0F, -0.75F )
      builder.addPoint( -1.0F, f2, f10 )
      builder.addPoint( -0.75F, f9 )
      builder.addPoint( -0.65F, f14 )
      val f11 = mountainContinentalness( f5, p236592, -0.7F )
      val f12 = calculateSlope( f11, f4, f5, 1.0F )
      val f13 = 0.01F
      builder.addPoint( f5 - 0.01F, f11 )
      builder.addPoint( f5, f11, f12 )
      builder.addPoint( 1.0F, f4, f12 )
    }
    else {
      val f7 = calculateSlope( f2, f4, -1.0F, 1.0F )
      if (p236593) {
        builder.addPoint( -1.0F, Math.max( 0.2F, f2 ) )
        builder.addPoint( 0.0F, Mth.lerp( 0.5F, f2, f4 ), f7 )
      }
      else builder.addPoint( -1.0F, f2, f7 )
      builder.addPoint( 1.0F, f4, f7 )
    }
    builder.build
  }

  private def mountainContinentalness( p236569 : Float, p236570 : Float, p236571 : Float ) = {
    val f = 1.17F
    val f1 = 0.46082947F
    val f2 = 1.0F - (1.0F - p236570) * 0.5F
    val f3 = 0.5F * (1.0F - p236570)
    val f4 = (p236569 + 1.17F) * 0.46082947F
    val f5 = f4 * f2 - f3
    if (p236569 < p236571) Math.max( f5, -0.2222F )
    else Math.max( f5, 0.0F )
  }

  private def calculateMountainRidgeZeroContinentalnessPoint( p236567 : Float ) = {
    val f = 1.17F
    val f1 = 0.46082947F
    val f2 = 1.0F - (1.0F - p236567) * 0.5F
    val f3 = 0.5F * (1.0F - p236567)
    f3 / (0.46082947F * f2) - 1.17F
  }

  def buildErosionOffsetSpline[C, I <: ToFloatFunction[C]]( pErosion : I, pRidgesFolded : I, p236598 : Float, p236599
  : Float, p236600 : Float, p236601 : Float, p236602 : Float, p236603 : Float, p236604 : Boolean, p236605 : Boolean,
                                                            pTransform : ToFloatFunction[Float] ) : CubicSpline[C, I]
  = {
    val f = 0.6F
    val f1 = 0.5F
    val f2 = 0.5F
    val cubicspline = buildMountainRidgeSplineWithPoints( pRidgesFolded, Mth.lerp( p236601, 0.6F,
                                                                                   1.5F ),
                                                          p236605, pTransform )
    val cubicspline1 = buildMountainRidgeSplineWithPoints( pRidgesFolded, Mth.lerp( p236601,
                                                                                    0.6F, 1.0F ),
                                                           p236605, pTransform )
    val cubicspline2 = buildMountainRidgeSplineWithPoints( pRidgesFolded, p236601, p236605,
                                                           pTransform )
    val cubicspline3 = ridgeSpline( pRidgesFolded, p236598 - 0.15F, 0.5F * p236601, Mth.lerp(
      0.5F, 0.5F, 0.5F ) * p236601, 0.5F * p236601, 0.6F * p236601, 0.5F, pTransform )
    val cubicspline4 = ridgeSpline( pRidgesFolded, p236598, p236602 * p236601, p236599 * p236601,
                                    0.5F * p236601, 0.6F * p236601, 0.5F, pTransform )
    val cubicspline5 = ridgeSpline( pRidgesFolded, p236598, p236602, p236602, p236599, p236600,
                                    0.5F, pTransform )
    val cubicspline6 = ridgeSpline( pRidgesFolded, p236598, p236602, p236602, p236599, p236600,
                                    0.5F, pTransform )
    val cubicspline7 = CubicSpline.builder[C, I]( pRidgesFolded, pTransform ).addPoint( -1.0F,
                                                                                        p236598 )
      .addPoint( -0.4F, cubicspline5 ).addPoint( 0.0F, p236600 + 0.07F ).build
    val cubicspline8 = ridgeSpline( pRidgesFolded, -0.02F, p236603, p236603, p236599, p236600,
                                    0.0F, pTransform )
    val builder = CubicSpline.builder[C, I]( pErosion, pTransform ).addPoint( -0.85F,
                                                                              cubicspline
                                                                              ).addPoint(
      -0.7F, cubicspline1 ).addPoint( -0.4F, cubicspline2 ).addPoint( -0.35F, cubicspline3 ).addPoint( -0.1F,
                                                                                                       cubicspline4 ).addPoint( 0.2F, cubicspline5 )
    if (p236604) {
      builder.addPoint( 0.4F, cubicspline6 ).addPoint( 0.45F, cubicspline7 ).addPoint( 0.55F, cubicspline7 )
        .addPoint( 0.58F, cubicspline6 )
    }
    builder.addPoint( 0.7F, cubicspline8 )
    builder.build
  }

  private def ridgeSpline[C, I <: ToFloatFunction[C]]( pRidgesFolded : I, p236579 : Float, p236580 : Float, p236581
  : Float, p236582 : Float, p236583 : Float, p236584 : Float, pTransform : ToFloatFunction[Float] ) = {
    val f = Math.max( 0.5F * (p236580 - p236579), p236584 )
    val f1 = 5.0F * (p236581 - p236580)
    CubicSpline.builder[C, I]( pRidgesFolded, pTransform ).addPoint( -1.0F, p236579, f ).addPoint( -0.4F,
                                                                                                   p236580,
                                                                                                   Math.min(
                                                                                                     f, f1 )
                                                                                                   )
      .addPoint( 0.0F, p236581, f1 ).addPoint( 0.4F, p236582, 2.0F * (p236582 - p236581) ).addPoint( 1.0F, p236583,
                                                                                                     0.7F * (p236583
                                                                                                             -
                                                                                                             p236582)
                                                                                                     ).build
  }
}