package com.github.sparksample

import java.nio.file.{Path, Paths}


object IDEPathHelper {
	implicit class RichPath(val path: Path) extends AnyVal {
		def /(pathString: String) = path.resolve(pathString)

		def /(other: Path) = path.resolve(other)
	}

	val gatlingConfUrl: Path = Paths.get(getClass.getClassLoader.getResource("gatling.conf").toURI)
	val projectRootDir: Path = gatlingConfUrl.getParent.getParent.getParent

	val srcDir: Path = projectRootDir / "src" / "test" / "scala"
	val targetDir: Path = projectRootDir / "scala-2.10"
	val binariesDir: Path = targetDir / "classes"
	val resultsDir: Path = targetDir / "gatling"

	val resourcesDir: Path = projectRootDir / "src" / "test" / "resources"
	val dataDir: Path = resourcesDir / "data"
	val bodiesDir: Path = resourcesDir / "bodies"
}

