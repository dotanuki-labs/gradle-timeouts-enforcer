# Gradle Timeouts Enforcer Plugin

[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) ![Language](https://img.shields.io/github/languages/top/dotanuki-labs/gradle-timeouts-enforcer?color=blue&logo=kotlin) [![Maintainability](https://api.codeclimate.com/v1/badges/13c4a2a26bfc9c631a22/maintainability)](https://codeclimate.com/github/dotanuki-labs/gradle-timeouts-enforcer/maintainability) ![Main](https://github.com/dotanuki-labs/gradle-timeouts-enforcer/workflows/Main/badge.svg) [![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?metadataUrl=https://plugins.gradle.org/m2/io/labs/dotanuki/timeouts-enforcer-plugin/maven-metadata.xml&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/io.labs.dotanuki.timeoutsenforcer) ![License](https://img.shields.io/github/license/dotanuki-labs/gradle-timeouts-enforcer.svg)
> Ensure that your Gradle build never runs forever, for whatever reason

## What is this?

> *Blog post to come, stay tunned*

A simple Gradle plugin that enforces timeouts for build executions, both at task execution level and global build level.

## How

**Step 01** : Add these two properties at your `<root-project>/gradle.properties` file

```
io.labs.dotanuki.enforcer.maxDurationPerTask=10.minutes
io.labs.dotanuki.enforcer.maxDurationPerBuild=2.hours
```

**Step 02** : Apply this plugin into your build at your root level Gradle build.

- For `<root-project>/build.gradle`

```groovy
plugin {
    id 'io.labs.dotanuki.timeoutsenforcer' version '<plugin_version>'
}

```

- For `<root-project>/build.gradle.kts`

```kotlin
plugin {
    id("io.labs.dotanuki.timeoutsenforcer") version "<plugin_version>"
}

```

**Step 03** : Profit 🚀

Check [releases](https://github.com/dotanuki-labs/gradle-timeouts-enforcer/releases) for the latest plugin version. More instructions also available in the [plugin page](https://plugins.gradle.org/plugin/io.labs.dotanuki.timeoutsenforcer)

## Providing timeouts

In the Gradle properties this plugin use, each value specified as `duration` must follow the `amount.unit` convention, where 

(a) supported `unit` include :

- **second**, **seconds** (upper/lower/camel/etc cases)
- **minute**, **minutes** (upper/lower/camel/etc cases)
- **hour**, **hours** (upper/lower/camel/etc cases)

(b) `amount` should be an **integer** value

Examples of valid specifications

- `1.hour`
- `10.SECONDS`
- `30.Minutes`

Examples of invalid specifications

- `1.5.hour`
- `30,5.minutes`

This plugin will parse and validate the values from provided and throw an error if they don't match the expected convention.

## Supported Gradle versions

This plugin supports only Gradle 5.x.y or newer since it is driven by `Task.timeout` [internal property](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html#getTimeout--).

## See it in action

To quick test this plugin in a real project, a `sample`is provided : 

```shell
$> cd sample
$> ./gradlew clean run
```

Sample will emulate a long task (**15 seconds** to complete `run`), but this task will fail after **10 seconds**, as defined in `sample/gradle.properties`.

```shell
➜  sample git:(master) ✗ ./gradlew clean run
Configuration on demand is an incubating feature.

> Task :run FAILED
FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':run'.
> Timeout has been exceeded

``` 

## Author

Coded by Ubiratan Soares (follow me on [Twitter](https://twitter.com/ubiratanfsoares))

## License

```
The MIT License (MIT)

Copyright (c) 2020 Dotanuki Labs

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```