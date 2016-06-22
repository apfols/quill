package io.getquill.sources.sql.norm

import io.getquill.sources.sql.mirrorSource._
import io.getquill.sources.sql.mirrorSource
import io.getquill.sources.sql.SqlSpec

class ExpandNestedQueriesSpec extends SqlSpec {

  "keeps the initial table alias" in {
    val q = quote {
      (for {
        a <- qr1
        b <- qr2
      } yield b).take(10)
    }

    mirrorSource.run(q).sql mustEqual
      "SELECT x.s, x.i, x.l, x.o FROM (SELECT x.s, x.i, x.l, x.o FROM TestEntity a, TestEntity2 x) x LIMIT 10"
  }

  "partial select" in {
    val q = quote {
      (for {
        a <- qr1
        b <- qr2
      } yield b.i).take(10)
    }
    mirrorSource.run(q).sql mustEqual
      "SELECT x.* FROM (SELECT b.i FROM TestEntity a, TestEntity2 b) x LIMIT 10"
  }
}
