package com.not2ho.artificialheart.util

import it.unimi.dsi.fastutil.floats.Float2FloatFunction
import java.util.function.Function


object ToScalaFloatFunction {
  val IDENTITY : ToScalaFloatFunction[Float] = createUnlimited( ( p216474 : Float ) => {
    p216474

  }
                                                                )

  def createUnlimited( pWrapped : Float2FloatFunction ) : ToScalaFloatFunction[Float] = new ToScalaFloatFunction[Float]() {
    override def apply( p216483 : Float ) : Float = return pWrapped.apply( p216483 )

    override def minValue : Float = return Float.NegativeInfinity

    override def maxValue : Float = return Float.PositiveInfinity
  }
}

trait ToScalaFloatFunction[C] {
  def apply( pObject : C ) : Float

  def minValue : Float

  def maxValue : Float

  def comap[C2]( pConverter : Function[C2, C] ) : ToScalaFloatFunction[C2] = {
    val tofloatfunction = this
    new ToScalaFloatFunction[C2]() {
      override def apply( p216496 : C2 ) : Float = return tofloatfunction.apply( pConverter.apply( p216496 ) )

      override def minValue : Float = return tofloatfunction.minValue

      override def maxValue : Float = return tofloatfunction.maxValue
    }
  }
}