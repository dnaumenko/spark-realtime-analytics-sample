package com.github.sparksample

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

import IDEPathHelper._

object Engine extends App {
	val props = new GatlingPropertiesBuilder
	props.disableCompiler()
	props.dataDirectory(dataDir.toString)
	props.resultsDirectory(resultsDir.toString)
//	props.bodiesDirectory(bodiesDir.toString)
	props.binariesDirectory(binariesDir.toString)
//	props.simulationClass(classOf[SimpleServerSimulation].toString)

	Gatling.fromMap(props.build)
}
