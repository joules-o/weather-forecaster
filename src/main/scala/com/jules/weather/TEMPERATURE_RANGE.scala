package com.jules.weather

import enumeratum._

sealed trait TEMPERATURE_RANGE extends EnumEntry

object TEMPERATURE_RANGE extends Enum[TEMPERATURE_RANGE] with CirceEnum[TEMPERATURE_RANGE] {
	val values: IndexedSeq[TEMPERATURE_RANGE] = findValues

	case object Cold extends TEMPERATURE_RANGE
	case object Moderate extends TEMPERATURE_RANGE
	case object Hot extends TEMPERATURE_RANGE

	final def fromTemperature: Int => TEMPERATURE_RANGE = {
		case temp if temp <= 45 => Cold
		case temp if temp <= 85 => Moderate
		case _ => Hot
	}

}