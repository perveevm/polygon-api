# polygon-api

This project implements all Polygon API methods described [here](https://codeforces.com/apiHelp). If there are any errors in the implementation of some methods, contact me, and I'll fix them.

### Building

Just call `mvn package`, it will build `codeforces-api-VERSION.jar` file for you.

### Usage

The main class is `CodeforcesSession`. It implements all API methods. Constructor takes two parameters – `key` and `secret`. You can get them in your profile settings on CodeForces.

All entity classes, for example, `BlogEntry`, `Contest`, etc., are in the `ru.perveevm.codeforces.api.entities` package.

### Dependencies

All dependencies that were used you can find in `pom.xml` config file.
