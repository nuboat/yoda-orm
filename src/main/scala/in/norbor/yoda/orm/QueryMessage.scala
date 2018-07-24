package in.norbor.yoda.orm

/**
  * @author Peerapat A on Jun 24, 2018
  */
case class QueryMessage(offset: Int
                        , pageSize: Int
                        , dateFilter: DateFilterItem
                        , filters: Seq[FilterItem]
                        , orders: Seq[Order]
                        , isOR: Boolean) {

  def whereCondition: String = {
    val conditions = filters
      .map(f => s"${f.name} ${buildCondition(f)}")

    conditions.mkString(joinWith)
  }

  def orderBy: String = {
    val sorting = orders
      .map(o => s"${o.name} ${o.sortBy}")

    if (sorting.isEmpty) "" else s"ORDER BY ${sorting.mkString(" ")}"
  }

  private def joinWith: String = if (isOR) " OR " else " AND "

  private def buildCondition(f: FilterItem): String = {
    if (!operatorSet.contains(f.operator))
      throw new IllegalArgumentException(s"Does not support ${f.operator}")

    s"${f.operator} ?"
  }

  private lazy val operatorSet = Set("=", "<=", ">=", ">", "<", "<>", "LIKE")

}
