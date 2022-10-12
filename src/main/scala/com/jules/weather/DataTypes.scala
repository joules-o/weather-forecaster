package com.jules.weather

import sttp.model.StatusCode

object DataTypes {
	// IO Types
	case class CoordinatesIn(lat: Float, long: Float) {
		override def toString: String = s"$lat,$long"
	}
	case class ForecastOut(forecast: String, temp: TEMPERATURE_RANGE)
	case class ErrorOut(statusCode: StatusCode, message: String)

	// Internal Types
	case class GridResponseProperties(forecast: String)
	case class GridResponse(properties: GridResponseProperties)
	case class TimePeriodWeatherInfo(number: Int, name: String, temperature: Int, shortForecast: String)
	case class ForecastResponseProperties(periods: List[TimePeriodWeatherInfo])
	case class ForecastResponse(properties: ForecastResponseProperties)
	case class NwsError(title: String, detail: String)
}
