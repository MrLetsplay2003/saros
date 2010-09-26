package de.fu_berlin.inf.dpp.stf.conditions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;

public class ExistNoResource extends DefaultCondition {

    String resourcePath;

    ExistNoResource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getFailureMessage() {

        return null;
    }

    public boolean test() throws Exception {
        IPath path = new Path(resourcePath);
        IResource resource = ResourcesPlugin.getWorkspace().getRoot()
            .findMember(path);
        if (resource == null)
            return true;
        return false;
    }
}
