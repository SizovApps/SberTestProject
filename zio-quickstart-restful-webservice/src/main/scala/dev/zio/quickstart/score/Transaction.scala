package dev.zio.quickstart.score

import zio.json.*

import java.util.UUID

case class Transaction(from: String, to: String, sum: Double)

object Transaction:
  given JsonEncoder[Transaction] =
    DeriveJsonEncoder.gen[Transaction]
  given JsonDecoder[Transaction] =
    DeriveJsonDecoder.gen[Transaction]
