//
// Depot library - a Java relational persistence library
// https://github.com/threerings/depot/blob/master/LICENSE

package com.samskivert.depot;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.*;

@Mojo(name="gentestrecord", defaultPhase=LifecyclePhase.PROCESS_TEST_CLASSES,
      requiresDependencyResolution=ResolutionScope.TEST)
public class GenTestRecordMojo extends GenRecordMojo {

    @Override protected boolean processTests () {
        return true;
    }
}
