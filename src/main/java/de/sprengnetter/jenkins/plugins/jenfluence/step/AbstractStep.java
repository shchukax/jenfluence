package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public abstract class AbstractStep extends Step implements Serializable {

    private ConfluenceSite site;

    public ConfluenceSite getSite() {
        return site;
    }

    @DataBoundSetter
    public void setSite(final ConfluenceSite site) {
        this.site = site;
    }
}
