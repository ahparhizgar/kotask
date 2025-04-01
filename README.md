![build status](https://github.com/ahparhizgar/kotask/actions/workflows/build.yml/badge.svg)

# Kotask

![Logo](/images/logo.png)

This is a Kotlin Multiplatform To Do List project written in kotlin.

## About this Project

A multiplatform (jvm and android) project with a shared module that uses Decompose.

## Useful gradle tasks

```shell
# run desktop app
gradle app-desktop:run

# run the jvm tests and validate screenshots
gradle jvmTest -Proborazzi.test.verify=true

# record screenshots
gradle  jvmTest -Proborazzi.test.record=true
```
