# Depot Maven Plugin

A simple Mojo for running [Depot]'s code generation phase in a Maven build.

To add this to your build, add the following to your `pom.xml`:

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>com.samskivert</groupId>
        <artifactId>depot-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <id>genrecord</id>
            <phase>process-classes</phase>
            <goals>
              <goal>genrecord</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

By default, it searches your source directory for all files matching `*Record.java`. You can change
this behavior like so:

```xml
        <!-- ... -->
          <execution>
            <!-- ... -->
            <configuration>
              <!-- include something instead of *Record.java -->
              <includes>
                <include>**/*Barzle.java</include>
              </includes>
              <!-- or exclude one or more files -->
              <excludes>
                <exclude>**/NotActuallyARecord.java</exclude>
              </excludes>
            </configuration>
          </execution>
        <!-- ... -->
```

## Discuss

Questions and comments can be directed to the [OOO Google Group].

## License

Nexus is released under the BSD License. See the [LICENSE] file for details.

[Depot]: https://code.google.com/p/depot
[OOO Google Group]: http://groups.google.com/group/ooo-libs
[LICENSE]: https://github.com/samskivert/depot-maven-plugin/blob/master/LICENSE
