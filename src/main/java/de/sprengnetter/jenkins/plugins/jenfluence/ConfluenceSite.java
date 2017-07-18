package de.sprengnetter.jenkins.plugins.jenfluence;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.commons.validator.routines.UrlValidator;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.*;

/**
 * Representation of a configured site for confluence.
 *
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class ConfluenceSite extends AbstractDescribableImpl<ConfluenceSite> {

    private String username;

    private URL url;

    private String password;

    private AuthenticationType authenticationType;

    private Integer timeout;

    private ConfluenceSiteDescriptor confluenceSiteDescriptor = new ConfluenceSiteDescriptor();

    @DataBoundConstructor
    public ConfluenceSite(final String username, final String password, final URL url, final String authenticationType, final Integer timeout) {
        this.username = username;
        this.url = url;
        this.password = password;
        this.authenticationType = AuthenticationType.fromString(authenticationType);
        this.timeout = timeout;
    }

    @Override
    public Descriptor<ConfluenceSite> getDescriptor() {
        return confluenceSiteDescriptor;
    }

    public String getUserName() {
        return username;
    }

    @DataBoundSetter
    public void setUserName(final String userName) {
        this.username = userName;
    }

    public URL getUrl() {
        return url;
    }

    @DataBoundSetter
    public void setUrl(final URL url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    @DataBoundSetter
    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAuthenticationType() {
        return authenticationType.getType();
    }

    @DataBoundSetter
    public void setAuthenticationType(final String authenticationType) {
        this.authenticationType = AuthenticationType.fromString(authenticationType);
    }

    public Integer getTimeout() {
        return timeout;
    }

    @DataBoundSetter
    public void setTimeout(final Integer timeout) {
        this.timeout = timeout;
    }

    @Extension
    public static final class ConfluenceSiteDescriptor extends Descriptor<ConfluenceSite> {

        private String username;
        private String password;
        private String url;
        private AuthenticationType authenticationType;
        private Integer timeout;

        public ConfluenceSiteDescriptor() {
            super(ConfluenceSite.class);
            load();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Confluence Site";
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject json) throws FormException {
            username = json.getString("username");
            password = json.getString("password");
            url = json.getString("url");
            authenticationType = AuthenticationType.fromString(json.getString("authenticationType"));
            timeout = json.getInt("timeout");
            validate(username, password, url, timeout);
            save();
            return super.configure(req, json);
        }

        public FormValidation doTestConnection(@QueryParameter("username") final String username,
                                               @QueryParameter("password") final String password,
                                               @QueryParameter("url") final String url,
                                               @QueryParameter("timeout") final Integer timeout) {
            try {
                validate(username, password, url, timeout);
                URL confluenceUrl = new URL(url);
                if (!isReachable(confluenceUrl, timeout)) {
                    throw new IllegalStateException("Address " + confluenceUrl.toURI().toString() + " is not reachable");
                }
                return FormValidation.okWithMarkup("Success");
            } catch (MalformedURLException e) {
                return FormValidation.errorWithMarkup("The URL " + url + " is malformed");
            } catch (IllegalArgumentException e) {
                return FormValidation.warningWithMarkup(e.getMessage());
            } catch (URISyntaxException e) {
                return FormValidation.errorWithMarkup("URI build from URL " + url + " is malformed");
            } catch (IllegalStateException e) {
                return FormValidation.errorWithMarkup(e.getMessage());
            }
        }

        private void validate(final String username, final String password, final String url, final Integer timeout) {
            validateCredentials(username, password);
            validateUrl(url);
        }

        private void validateCredentials(final String username, final String password) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Please enter the username of the confluence user!");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Please enter the passoword of the confluence user!");
            }
        }

        private void validateUrl(final String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("Given URL is null or empty");
            }
            String[] supportedProtocols = new String[]{"http", "https"};
            UrlValidator urlValidator = new UrlValidator(supportedProtocols);
            if (!urlValidator.isValid(url)) {
                throw new IllegalArgumentException("URL " + url + " is invalid. Maybe you forgot the protocol (http/s)?");
            }
        }

        private boolean isReachable(final URL url, final Integer timeout) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(url.getHost(), 80), timeout * 1000);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getUrl() {
            return url;
        }

        public AuthenticationType getAuthenticationType() {
            return authenticationType;
        }

        public Integer getTimeout() {
            return timeout;
        }
    }
}
