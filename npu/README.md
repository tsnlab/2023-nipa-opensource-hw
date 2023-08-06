# Instructions

Copy this directory under `chipyard/generators`.
To add dependency, modify `chipyard/build.sbt` as below:

```
lazy val chipyard = (project in file("generators/chipyard"))
.dependsOn(
...
npu,
...
)
...
...
lazy val npu = (project in file("generators/npu"))
  .dependsOn(rocketchip)
  .settings(libraryDependencies ++= rocketLibDeps.value)
  .settings(commonSettings)
```