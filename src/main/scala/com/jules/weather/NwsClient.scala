package com.jules.weather

import cats.data.EitherT
import cats.effect.Async
import cats.implicits._
import com.jules.weather.DataTypes._
import io.circe.generic.auto._
import io.circe.{Error => CError}
import sttp.client3.circe.asJsonEither
import sttp.client3.{ResponseException, basicRequest, _}
import sttp.model.StatusCode

class NwsClient[F[_] : Async](backend: SttpBackend[F, Any]) {

	final val pointsPath = "https://api.weather.gov/points/"

	// TODO: error handling
	def getForecastGrid(coordinates: CoordinatesIn): EitherT[F, ErrorOut, GridResponse] =
		EitherT(basicRequest
			.get(uri"https://api.weather.gov/points/$coordinates")
			.response(asJsonEither[NwsError, GridResponse]
				.mapLeft(handleClientErrors)
			)
			.send(backend)
			.map(_.body))
			logging.DefaultLog

	def getForecastForGridArea(forecastUri: String): EitherT[F, ErrorOut, ForecastResponse] =
		EitherT(basicRequest
			.get(uri"$forecastUri")
			.response(asJsonEither[NwsError, ForecastResponse]
				.mapLeft(handleClientErrors)
			)
			.send(backend)
			.map(_.body))

	private final val pointOutsideUsError: String = "Data Unavailable For Requested Point"
	private def handleClientErrors: ResponseException[NwsError, CError] => ErrorOut = {
		case e: HttpError[NwsError] if (e.body.title == pointOutsideUsError) =>
			ErrorOut(StatusCode.BadRequest, "Can't forecast for locations outside the US.")
		case _ => ErrorOut(StatusCode.InternalServerError, "Oops, something went wrong.")
	}
}