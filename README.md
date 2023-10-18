# polygon-api [![Maven Central](https://img.shields.io/maven-central/v/ru.perveevm/polygon-api.svg)](https://search.maven.org/artifact/ru.perveevm/polygon-api) [![javadoc](https://javadoc.io/badge2/ru.perveevm/polygon-api/javadoc.svg)](https://javadoc.io/doc/ru.perveevm/polygon-api)

This project implements all Polygon API methods described [here](https://docs.google.com/document/d/1mb6CDWpbLQsi7F5UjAdwXdbCpyvSgWSXTJVHl52zZUQ/edit#heading=h.7qf3ungxcp7r).

### Building

Use `mvn package` if you need a `.jar` file.

### Usage

The main class is `PolygonSession`. It implements all API methods. Constructor takes two parameters – `key` and `secret`. You can get them in your profile settings on Polygon.

All entity classes, for example, `Problem`, `ProblemPackage`, etc., are in the `ru.perveevm.polygon.api.entities` package.

To add it into your pom.xml use:

```xml
<dependency>
    <groupId>ru.perveevm</groupId>
    <artifactId>polygon-api</artifactId>
    <version>Put the version you want here</version>
</dependency>
```

### Advanced functionality

There is also a developing part of this project – `PolygonUserSession`. It implements some methods that are not supported by API but can be very useful in some situations. For example, it supports an ability to create, delete and commit problems, build packages. This part of the project is in development and there may be some bugs in it. The functionality is gradually increasing. This class requires user `login` and `password` in Polygon. Some of the methods are deprecated and will be removed due to Polygon API enhancements.
