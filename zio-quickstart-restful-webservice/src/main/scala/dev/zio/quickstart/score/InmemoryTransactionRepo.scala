package dev.zio.quickstart.score

import zio.*

import scala.collection.mutable

case class InmemoryTransactionRepo(map: Ref[Map[String, Transaction]]) extends TransactionRepo:
  def save_transaction(transaction: Transaction): UIO[String] =
    for
      id <- Random.nextUUID.map(_.toString)
      _  <- map.update(_ + (id -> transaction))
    yield id


  def transactions: UIO[List[Transaction]] =
    map.get.map(_.values.toList)

object InmemoryTransactionRepo {
  def layer: ZLayer[Any, Nothing, InmemoryTransactionRepo] =
    ZLayer.fromZIO(
      Ref.make(Map.empty[String, Transaction]).map(new InmemoryTransactionRepo(_))
    )
}
