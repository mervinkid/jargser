# Jargser

![Java](https://img.shields.io/badge/java-1.8-orange.svg?style=flat)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg?style=flat)
![Release](https://img.shields.io/badge/release-0.2.0-blue.svg?style=flat)
![License MIT](https://img.shields.io/badge/license-MIT-lightgray.svg?style=flat&maxAge=2592000)

Jargser is a smart command line argument parser for java.

## Installation

Download [the latest JAR](http://central.maven.org/maven2/me/mervinz/jargser/0.2.0/jargser-0.2.0.jar) or using **Maven**:

```xml
<dependency>
  <groupId>me.mervinz</groupId>
  <artifactId>jargser</artifactId>
  <version>0.2.0</version>
</dependency>
```

or **Gradle**:
    
```groovy
compile group: 'me.mervinz', name: 'jargser', version: '0.2.0'
```

## Get start

### Init application information

- setAppName(String appName): ArgumentParser
- setAppVerion(String appVersion): ArgumentParser

### Setup commands

- addCommand(String command, String desc): ArgumentParser

### Setup options

- addOption(String s, String flag, String desc): ArgumentParser
- addOption(String s, String flag, String desc, String defaultValue): ArgumentParser

### Parse command line arguments

- parse(String args[]): void

### Generate and display usage information

- usage(): String
- printUsage(): void

### Get parsed results

- hasParsedOption(String flag): bool
- parsedOption(String flag): String
- parsedCommand(): String
- parsedComamndValue(): String

## Demo

```java
package me.mervinz.demo;

import me.mervinz.jargser.ArgumentParser;

public class Demo {
    public static void main(String[] args) {
        // Init parser
        ArgumentParser parser = new ArgumentParser();
        parser.setAppName("demo");
        parser.setAppVersion("1.0.0");
        // Setup commands
        parser.addCommand("install", "Install application.");
        parser.addCommand("help", "Display help content.");
        // Setup options
        parser.addOption("c", "config", "Config file path.");
        parser.addOption("v", "verbose", "Show more output.");
        // Parse arguments
        parser.parse(args);
        
        // Get results
        
        // Command
        switch (parse.parsedCommand()) {
        case "install":
            // Install... 
            break;
        default:
            parser.printUsage();
            System.exit(1);
        }
        
        // Option
        
        // Check option
        if (parser.hasParsedOption("verbose")) {
            // Found flag 'verbose'.
        }
        
        // Get option value
        String config = parser.parsedOption("config");
        // ...
    }
}
```

The usage that Jargser generated below:

```
demo 1.0.0

Usage:
  demo <command> [options]
  
Commands:
  install                     Install application
  help                        Display help content
  
Options:
  -c  --config                Config file path
  -v  --verbose               Show more output
```

***

## Dependencies

For detail see `pom.xml`.


## Contributing

1. Fork it.
2. Create your feature branch. (`$ git checkout feature/my-feature-branch`)
3. Commit your changes. (`$ git commit -am 'What feature I just added.'`)
4. Push to the branch. (`$ git push origin feature/my-feature-branch`)
5. Create a new Pull Request

## Authors

- Mervin | [Website](https://mervinz.me) | [Github](https://github.com/mofei2816) | [Mail](mailto:mofei2816@gmail.com)

## License

The MIT License (MIT). For detail see [LICENSE](LICENSE).

