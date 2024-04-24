package dev.zio.quickstart.score

import scala.io.Source
import zio.*
import zio.http.*
import zio.json.*

object ScoreApp:
  def apply(): Http[TransactionRepo, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      // POST /score -d '{"from": "39", "to": "101", "sum": 10.6}'
      case req @ (Method.POST -> Root / "score") =>
        (for {
          u <- req.body.asString.map(_.fromJson[Transaction])
          r <- u match
            case Left(e) =>
              ZIO
                .debug(s"Failed to parse the input: $e")
                .as(
                  Response.text(e).withStatus(Status.BadRequest)
                )
            case Right(u) =>
              val isBlacklisted = Source
                .fromResource("blacklist.txt")
                .getLines()
                .toList
                .exists(line => line == u.from || line == u.to)

              if (isBlacklisted) {
                ZIO.succeed(Response.json("""{"success": false}"""))
              } else {
                TransactionRepo
                  .save_transaction(u)
                  .map(id => Response.json("""{"success": true}"""))
              }
        } yield r).orDie

      // GET /score
      case Method.GET -> Root / "score" =>
        TransactionRepo.transactions
          .map(response => Response.json(response.toJson))
          .orDie
    }
