package io.getquill.sources.cassandra

import io.getquill.ast.StatelessTransformer
import io.getquill.ast._

object ExpandMappedInfix extends StatelessTransformer {

  override def apply(q: Ast) =
    q match {
      case Map(q: Infix, x, p) if (x == p) =>
        q
      case q @ Map(Infix(parts, params), x, p) =>
        params.zipWithIndex
          .collect {
            case (q: Query, i) => (q, i)
          } match {
            case List((q, i)) =>
              Infix(parts, params.updated(i, Map(q, x, p)))
            case other =>
              super.apply(q)
          }
      case other =>
        super.apply(q)
    }
}
