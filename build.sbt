name := "acumen"

version := "10-devel"

scalaVersion := "2.9.3"

theMainClass := "acumen.Main"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.9.3",
  "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
)

resolvers += "tuxfamily" at "http://download.tuxfamily.org/arakhne/maven/"

libraryDependencies ++= Seq (
  "javax" % "j3d-core" % "1.5.2",
  "javax" % "j3d-core-utils" % "1.5.2",
  "javax" % "vecmath" % "1.5.2",
  "com.fifesoft" % "rsyntaxtextarea" % "2.0.2",
  "com.fifesoft" % "autocomplete" % "2.0.2",
  "org.apache.xmlgraphics" % "batik-dom" % "1.7",
  "org.apache.xmlgraphics" % "batik-svggen" % "1.7",
  "org.apache.xmlgraphics" % "batik-transcoder" % "1.7"
)

resolvers ++= Seq(
   "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
   "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq( 
   "org.jfree" % "jfreechart" % "1.0.14",
   "org.jfree" % "jcommon" % "1.0.17"
)

// Add resources in project root directory to class path
unmanagedResources in Compile ++= Seq("AUTHORS").map(new File(_))

// FIXME: Is this necessary
retrieveManaged := true

// SCCT
// seq(ScctPlugin.instrumentSettings : _*)

//
// Exclude files that start with XXX from the jar file
//

mappings in (Compile,packageBin) ~= { (ms: Seq[(File, String)]) =>
  ms filter { case (file, toPath) =>
    !toPath.contains("/XXX")
  }
}

//
// enable proguard
//

seq(ProguardPlugin.proguardSettings :_*)

proguardDefaultArgs := Seq("-dontwarn", "-dontobfuscate")

// don't shrink as proguard gets it wrong in some cases
proguardDefaultArgs += "-dontshrink"

// make sure all target specific Java 3d dependencies are included
proguardOptions += "-keep class javax.media.j3d.**"

// temporary hack to get proguard working with enclosure code
proguardOptions ++= Seq("-keep class org.jfree.resources.**",
                        "-keep class org.jfree.chart.resources.**",
                        "-keep class org.fife.**")

// for faster jar creation (but larger file)
proguardDefaultArgs += "-dontoptimize"

// Do not include any signature files from other jars, they cause
// nothing but problems.
makeInJarFilter ~= {
  (makeInJarFilter) => {
    (file) => makeInJarFilter(file) + ",!**/*.RSA,!**/*.SF,!**/*.DSA"
    }
  }


// modify package(-bin) jar file name
artifactPath in (Compile, packageBin) <<= (crossTarget, moduleName, version) {
  (path, name, ver) => path / (name + "-" + ver + ".pre.jar")
}

// modify proguard jar file name
minJarPath <<= (crossTarget, moduleName, version) {
  (path, name, ver) => path / (name + "-" + ver + ".jar")
}

//
// set main based on theMainClass setting
//

mainClass in Compile <<= theMainClass map { m => Some(m) }

proguardOptions <<= (proguardOptions, theMainClass) {
  (prev, main) => prev :+ (keepMain(main))
}

fork in run := true

javaOptions in run += "-Xmx1G"

// disable parallel test Execution for now due to a possible race
// condition that needs to be tracked down and causes a hang
parallelExecution in Test := false

logBuffered in Test := false
