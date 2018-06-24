package in.norbor.yoda.orm

/**
  * @author Peerapat A on Jun 24, 2018
  */
case class QueryMessage(offset: Int
                        , pageSize: Int
                        , filters: Seq[FilterItem] = Seq.empty
                        , orders: Seq[Order] = Seq.empty
                        , isOR: Boolean = true) {

  def whereCondition: String = {
    val conditions = filters
      .map(f => s"${f.name} ${buildCondition(f)}")

    s"WHERE ${conditions.mkString(joinWith)}"
  }

  def orderBy(): String = {
    val sorting = orders
      .map(o => s"${o.name} ${o.sortBy}")

    s"ORDER BY ${sorting.mkString(" ")}"
  }

  private def joinWith: String = if (isOR) " OR " else "ELSE"

  private def buildCondition(f: FilterItem): String =
    s"${f.operator.map(_.toLowerCase).filter(operatorSet.contains).getOrElse("=")} ?"

  private lazy val operatorSet = Set("=", "<=", ">=", ">", "<", "like")

}
