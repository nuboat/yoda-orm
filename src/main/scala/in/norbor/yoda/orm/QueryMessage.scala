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

    if (conditions.isEmpty) "" else s"WHERE ${conditions.mkString(joinWith)}"
  }

  def orderBy: String = {
    val sorting = orders
      .map(o => s"${o.name} ${o.sortBy}")

    if (sorting.isEmpty) "" else s"ORDER BY ${sorting.mkString(" ")}"
  }

  private def joinWith: String = if (isOR) " OR " else "ELSE"

  private def buildCondition(f: FilterItem): String = {
    if (operatorSet(f.operator))
      throw new IllegalArgumentException(s"Does not support ${f.operator}")

    s"${f.operator} ?"
  }

  private lazy val operatorSet = Set("=", "<=", ">=", ">", "<", "LIKE")

}
