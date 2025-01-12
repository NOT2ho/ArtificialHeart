package com.not2ho.artificialheart.util

object InstanceOfPatternMatchingFuntionalizer {
  def doIf[C](value: Any, func: C => Unit): Unit = {
    value match
      case cValue: C => func(cValue)
  }
}