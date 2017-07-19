package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.steps.Step;

import java.io.Serializable;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public abstract class AbstractStep extends Step implements Serializable {

    private ConfluenceSite site;

    public AbstractStep() {
        ConfluenceSite.ConfluenceSiteDescriptor siteDescriptor = (ConfluenceSite.ConfluenceSiteDescriptor)
                Jenkins.getInstance().getDescriptor(ConfluenceSite.class);

        this.site = new ConfluenceSite(
                siteDescriptor.getUsername(),
                siteDescriptor.getPassword(),
                siteDescriptor.getUrl(),
                siteDescriptor.getAuthenticationType(),
                siteDescriptor.getTimeout(),
                siteDescriptor.getPoolSize()
        );
    }

    public ConfluenceSite getSite() {
        return site;
    }
}
