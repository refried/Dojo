package DojoCode

import Jama.Matrix

object NeighborhoodBased2 {
  val UNRATED: Double = 0
  val LAMBDA: Double = 1
  val GAMMA: Double = 1
}

class NeighborhoodBased2() {
  import NeighborhoodBased2._
  type User = Int
  type Item = Int

  // ratings: user x item
  val r = new Matrix(DataParser.parse())
  val numUsers = r.getRowDimension
  val numItems = r.getColumnDimension
  val users = 0 until numUsers
  val items = 0 until numItems

  def userRating(u: User)(i: Item) = r.get(u,i)

  // weights
  val w = calculateWeights

  def calculateMeans = {
    for {
      u <- users
      v <- users
    } yield {
      val commonItems = findCommonItems(u,v)
      def mean(values: Seq[Double]) = if (values.length > 0) values.sum / values.length else 0.0
      val ru = mean(commonItems.map(userRating(u)))
      val rv = mean(commonItems.map(userRating(v)))
      def error(user: User, mean: Double)(item: Item) = userRating(user)(item) - mean
      def squaredError(user: User, mean: Double)(item: Item) = math.pow(error(user, mean)(item), 2)
      val numerator = sum

      def denom(u: User, mean: Double) = {
        def squaredError(i: Item) = math.pow(r.get(u,i) - mean, 2)
        def sumSquaredError = commonItems.map(squaredError).sum
        math.sqrt(sumSquaredError)
      }
    }
  }

  def rated(u: User, i: Item): Boolean =
    r.get(u,i) != UNRATED

  def findCommonItems(u: User, v: User): Seq[Item] =
    items.filter(i => rated(u,i) && rated(v,i))

  def calculateWeights: Matrix = {
    val w = new Matrix(numUsers, numUsers, 0)

    w
  }

}