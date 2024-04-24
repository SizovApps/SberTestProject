package dev.zio.quickstart.score

import zio.*

trait TransactionRepo:
  def save_transaction(transaction: Transaction): Task[String]

  def transactions: Task[List[Transaction]]

object TransactionRepo:
  def save_transaction(transaction: Transaction): ZIO[TransactionRepo, Throwable, String] =
    ZIO.serviceWithZIO[TransactionRepo](_.save_transaction(transaction))

  def transactions: ZIO[TransactionRepo, Throwable, List[Transaction]] =
    ZIO.serviceWithZIO[TransactionRepo](_.transactions)
