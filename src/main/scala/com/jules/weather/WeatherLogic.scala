package com.jules.weather

import cats.data.EitherT
import cats.effect._
import cats.implicits._
import com.jules.weather.DataTypes._
import io.circe.generic.auto._
import sttp.capabilities.fs2.Fs2Streams
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class WeatherLogic[F[_] : Async](nwsClient: NwsClient[F]) {

	// Endpoint Spec

	final val weather: String = "weather"

	val weatherInput: EndpointInput[CoordinatesIn] = query[Float]("lat")
		.and(query[Float]("long"))
		.map(input => CoordinatesIn(input._1, input._2))(coordinates => (coordinates.lat, coordinates.long))
	val errorOutput: EndpointOutput[(ErrorOut, StatusCode)] = jsonBody[ErrorOut].and(statusCode)
	val forecastOutput: EndpointOutput[ForecastOut] = jsonBody[ForecastOut]

	def weatherEndpoint:
	ServerEndpoint[Fs2Streams[F], F] = endpoint
		.in(weather)
		.in(weatherInput)
		.errorOut(errorOutput)
		.out(forecastOutput)
		.serverLogic((weatherForecastLogic _).andThen(determineStatusCode))

	private def determineStatusCode: F[Either[ErrorOut, ForecastOut]] => F[Either[(ErrorOut, StatusCode), ForecastOut]] =
		_.map(_.leftMap(e => (e, e.statusCode)))

	// Endpoint Logic

	def weatherForecastLogic(coordinates: CoordinatesIn): F[Either[ErrorOut, ForecastOut]] =
		(for {
			validatedCoords <- EitherT.fromEither[F](validateCoordinates(coordinates))
			forecastGrid <- nwsClient.getForecastGrid(validatedCoords)
			forecast <- nwsClient.getForecastForGridArea(forecastGrid.properties.forecast)
			todaysWeather = forecast.properties.periods.head
			temperatureRange = TEMPERATURE_RANGE.fromTemperature(todaysWeather.temperature)
		} yield ForecastOut(todaysWeather.shortForecast, temperatureRange)).value

	def validateCoordinates: CoordinatesIn => Either[ErrorOut, CoordinatesIn] = coordinates =>
		coordinates.asRight[ErrorOut]
			.ensureOr(_ => ErrorOut(StatusCode.BadRequest, "Latitude must be between -90 and 90"))(c => -90 <= c.lat && c.lat <= 90)
			.ensureOr(_ => ErrorOut(StatusCode.BadRequest, "Longitude must be between -180 and 80"))(c => -180 <= c.long && c.long <= 180)

}
