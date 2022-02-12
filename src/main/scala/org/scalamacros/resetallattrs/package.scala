package org.scalamacros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.{Context => BlackboxContext}
import scala.reflect.macros.whitebox.{Context => WhiteboxContext}

package object resetallattrs {
  implicit class ResetAllAttrs(val c: BlackboxContext) {
    def resetAllAttrs[T](tree: T): Any = macro Macros.impl
  }
}

package resetallattrs {
  object Macros {
    def impl(c: WhiteboxContext)(tree: c.Tree): c.Tree = {
      import c.universe._
      val q"$_.ResetAllAttrs($context).resetAllAttrs[${t: Type}](...$_)" = c.macroApplication
      val internalHelper = tq"_root_.scala.reflect.internal.resetallattrs.Helper"
      val internalUniverse = tq"scala.reflect.internal.SymbolTable"
      q"new $internalHelper($context.universe.asInstanceOf[$internalUniverse]).impl($tree).asInstanceOf[$t]"
    }
  }
}
