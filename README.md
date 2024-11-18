# OpenGrape is a Java-based Open Graph parser

The [Open Graph Protocol](https://opengraphprotocol.org/) is a metadata standard that enables services, 
like social media applications, to fetch previews of webpages, such as thumbnail images and summaries.

This Orbit Earth OpenGrape is a friendly fork of the excellent [Kientux OpenGrape](https://github.com/kientux/opengrape), 
which is a Java port of the Swift [OpenGraph](https://github.com/satoshi-takano/OpenGraph).

# Installation
Unlike Kientux OpenGrape, this version is not hosted in a public Maven repository, but must be built and
installed locally using `mvn install`.

```
<dependency>
    <groupId>social.orbitearth</groupId>
    <artifactId>opengrape</artifactId>
    <version>[1.0.0,)</version>
</dependency>
```

# Usage

```java
String url = "https://opengraphprotocol.org/";

try {
    OpenGrape og = OpenGrape.fetch(url);
    // or OpenGrape.fetch(url, "my custom user-agent")
    // or OpenGrape.fetch(url, 3) with a 3 second timeout
    String title = og.getValue(OpenGrapeMetadata.TITLE);
    System.out.println(title);
} catch (IOException | OpenGrapeResponseException e) {
    e.printStackTrace();
}
```


# Versions
* **1.0.0** Initial fork
* **1.0.1** Added Slf4j logging
* **1.0.2** New user agent that works more broadly
  * Java 17