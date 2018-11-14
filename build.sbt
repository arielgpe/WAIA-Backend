name := "waia"
 
version := "1.0" 
      
lazy val `waia` = (project in file(".")).enablePlugins(PlayScala, PlayEbean)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.h2database" % "h2" % "1.4.192",
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      