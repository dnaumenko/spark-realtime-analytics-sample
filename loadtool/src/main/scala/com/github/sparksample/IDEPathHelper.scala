package com.github.sparksample

import java.nio.file.Path

import io.gatling.commons.util.PathHelper._

object IDEPathHelper {

	val gatlingConfUrl: Path = getClass.getClassLoader.getResource("gatling.conf").toURI
	val projectRootDir: Path = gatlingConfUrl.ancestor(3)

	val srcDir: Path = projectRootDir / "src" / "test" / "scala"
	val resourcesDir: Path = projectRootDir / "src" / "test" / "resources"
	val targetDir: Path = projectRootDir / "scala-2.11"
	val binariesDir: Path = targetDir / "classes"

	val dataDir: Path = resourcesDir / "data"
	val bodiesDir: Path = resourcesDir / "bodies"

	val resultsDir: Path = targetDir / "gatling"
}
