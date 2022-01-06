# polygon-api

This project implements all Polygon API methods described [here](https://docs.google.com/document/d/1mb6CDWpbLQsi7F5UjAdwXdbCpyvSgWSXTJVHl52zZUQ/edit#heading=h.7qf3ungxcp7r). If there are any errors in the implementation of some methods, contact me, and I'll fix them.

### Building

Just call `mvn package`, it will build `polygon-api-VERSION.jar` file for you.

### Usage

The main class is `PolygonSession`. It implements all API methods. Constructor takes two parameters – `key` and `secret`. You can get them in your profile settings on Polygon.

All entity classes, for example, `Problem`, `ProblemPackage`, etc., are in the `ru.perveevm.polygon.api.entities` package.

To add it into your pom.xml use:

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.perveevm</groupId>
    <artifactId>polygon-api</artifactId>
    <version>v0.5.2</version>
</dependency>
```

### Advanced functionality

There is also a developing part of this project – `PolygonUserSession`. It implements some methods that are not supported by API but can be very useful in some situations. For example, it supports an ability to create, delete and commit problems, build packages. This part of the project is in developement and there may be some bugs in it. The functionality is gradually increasing. This class requires user `login` and `password` in Polygon.

To add it into your pom.xml use:

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.perveevm</groupId>
    <artifactId>polygon-api</artifactId>
    <version>v0.6</version>
</dependency>
```

### Dependencies

All dependencies that were used you can find in `pom.xml` config file.
