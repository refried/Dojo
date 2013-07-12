package DojoCode

import Jama.Matrix

object util {
  implicit class Pow(val d: Double) extends AnyVal {
    def **(exp: Double) = math.pow(d,exp)
  }

  def mean(values: Seq[Double]) =
    if (values.length > 0) values.sum / values.length else 0.0
}

object NeighborhoodBased2 extends App {
  val UNRATED: Double = 0
  val LAMBDA: Double = 1
  val GAMMA: Double = 1

  import util._
  type User = Int
  type Item = Int

  // ratings: user x item
  val r = new Matrix(DataParser.parse())
  val numUsers = r.getRowDimension
  val numItems = r.getColumnDimension
  val users = 0 until numUsers
  val items = 0 until numItems

  def userRating(u: User)(i: Item) = r.get(u,i)


  // calculate weights
  val w = Array.tabulate(numUsers,numUsers){
    case (u: User, v: User) =>
      val commonItems = findCommonItems(u,v)

      if (commonItems.length > 2) {

        def meanRatingOverCommonItems(u: User) = meanRating(commonItems, u)

        def error(user: User)(item: Item) = userRating(user)(item) - meanRatingOverCommonItems(user)

        val numerator: Double = {
          def crossError(u: User, v: User)(i: Item) = error(u)(i) * error(v)(i)
          commonItems.map(crossError(u,v)).sum
        }

        val denominator = {
          def squaredError(user: User)(item: Item) = error(user)(item) ** 2
          def sumSquaredError(u: User) = commonItems.map(squaredError(u)).sum
          def denom(u: User) = math.sqrt(sumSquaredError(u))
          denom(u) * denom(v)
        }

        if (denominator > 0)
          numerator / denominator
        else 0

      }
      else 0
  }

  // calculate predictions
  val p = Array.tabulate(numUsers, numItems) {
    case (u: User, i: Item) =>
      val usersHavingRatedI = users.filter(rated(_,i)).filterNot(u==)

      val numerator: Double = {
        def numeratorTerm(v: User) = w(u)(v) * { userRating(v)(i) - meanRatingOverAllItems(v)}
        usersHavingRatedI.map(numeratorTerm).sum
      }
      val denominator: Double = usersHavingRatedI.map(v => math.abs(w(u)(v))).sum

      numerator / denominator
  }

  def rated(u: User, i: Item): Boolean =
    r.get(u,i) != UNRATED

  def findCommonItems(u: User, v: User): Seq[Item] =
    items.filter(i => rated(u,i) && rated(v,i))

  def meanRatingOverAllItems(u: User) =
    meanRating(items, u)

  def meanRating(items: Seq[Item], u: User) =
    mean(items.filter(rated(u,_)).map(userRating(u)))

}