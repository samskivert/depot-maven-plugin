# Depot Maven Plugin

A simple Mojo for running [Depot]'s code generation phase in a Maven build.

To add this to your build, add the following to your `pom.xml`:

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>com.samskivert</groupId>
        <artifactId>depot-maven-plugin</artifactId>
        <version>1.0.2</version>
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

The plugin has to run after the `compile` phase because it operates on the compiled `*Record.class`
files. The above configuration runs it during the `process-classes` phase (which immediately
follows `compile`). Thus to trigger it, you need to invoke either `mvn package` (which includes the
process-classes phase), or `mvn process-classes` directly (which is not a phase one normally runs
on the command line).

## Includes/Excludes

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

## Indent width

You can configure the number of spaces per indent level in the generated code like so:

```xml
        <!-- ... -->
          <execution>
            <!-- ... -->
            <configuration>
              <indentWidth>2</indentWidth>
            </configuration>
          </execution>
        <!-- ... -->
```

## Discuss

Questions and comments can be directed to the [OOO Google Group].

## License

Depot Maven Plugin is released under the BSD License. See the [LICENSE] file for details.

[Depot]: https://code.google.com/p/depot
[OOO Google Group]: http://groups.google.com/group/ooo-libs
[LICENSE]: https://github.com/samskivert/depot-maven-plugin/blob/master/LICENSE
