//
// Depot library - a Java relational persistence library
// http://code.google.com/p/depot/source/browse/trunk/LICENSE

package com.samskivert.depot;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import com.samskivert.depot.tools.GenRecord;

@Mojo(name="genrecord", defaultPhase=LifecyclePhase.PROCESS_CLASSES,
      requiresDependencyResolution=ResolutionScope.COMPILE)
public class GenRecordMojo extends AbstractMojo {

    @Parameter(defaultValue="**/*Record.java")
    public List<String> includes;

    @Parameter
    public List<String> excludes;

    @Parameter(defaultValue="4")
    public int indentWidth;

    public void execute () throws MojoExecutionException {
        // create the classloader we'll use to load FooRecord classes
        Build build = _project.getBuild();
        List<URL> entries = Lists.newArrayList();
        List<String> cp = processTests() ? _testClasspath : _compileClasspath;
        for (String entry : cp) addEntry(entries, entry);
        addEntry(entries, build.getOutputDirectory());
        if (processTests()) {
            addEntry(entries, build.getTestOutputDirectory());
        }

        ClassLoader cloader = URLClassLoader.newInstance(
            entries.toArray(new URL[entries.size()]),
            Thread.currentThread().getContextClassLoader());

        // create our record generator
        GenRecord genner = new GenRecord(cloader) {
            protected void logInfo (String msg) {
                getLog().info(msg);
            }
            protected void logWarn (String msg, Exception e) {
                if (e == null) getLog().warn(msg);
                else getLog().warn(msg, e);
            }
            protected RuntimeException mkFail (String msg, Exception e) {
                return new RuntimeException(msg, e);
            }
        };
        genner.setIndentWidth(indentWidth);

        // now find all matching source files and (re)generate them
        try {
            File sourceDir = new File(processTests() ? build.getTestSourceDirectory() :
                                      build.getSourceDirectory());
            String includes = Joiner.on(",").join(this.includes);
            String excludes = (this.excludes == null) ? null :
                Joiner.on(",").join(this.excludes);
            List<?> files = FileUtils.getFiles(sourceDir, includes, excludes);
            for (Object fobj : files) genner.processRecord((File)fobj);

        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to resolve includes/excludes", ioe);
        }
    }

    // overridden by GenTestRecordMojo
    protected boolean processTests () {
        return false;
    }

    protected void addEntry (List<URL> entries, String entry) {
        try {
            entries.add(new File(entry).toURI().toURL());
        } catch (MalformedURLException mue) {
            getLog().warn("Malformed classpath entry: " + entry, mue);
        }
    }

    @Parameter(property="project")
    private MavenProject _project;

    @Parameter(property="project.compileClasspathElements", required=true, readonly=true)
    private List<String> _compileClasspath;

    @Parameter(property="project.testClasspathElements", required=true, readonly=true)
    private List<String> _testClasspath;
}
