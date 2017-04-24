package com.github.sparksample

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

	val props = new GatlingPropertiesBuilder
	props.dataDirectory(IDEPathHelper.dataDir.toString)
	props.resultsDirectory(IDEPathHelper.resultsDir.toString)
	props.bodiesDirectory(IDEPathHelper.bodiesDir.toString)
	props.binariesDirectory(IDEPathHelper.binariesDir.toString)

	Gatling.fromMap(props.build)
}
