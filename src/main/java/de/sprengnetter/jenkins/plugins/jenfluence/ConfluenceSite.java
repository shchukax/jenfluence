package de.sprengnetter.jenkins.plugins.jenfluence;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import de.sprengnetter.jenkins.plugins.jenfluence.util.HttpUtil;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

/**
 * Representation of a configured site for confluence.
 *
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class ConfluenceSite extends AbstractDescribableImpl<ConfluenceSite> implements Serializable {

    private static final long serialVersionUID = -1895419369131803022L;

    private String username;

    private URL url;

    private String password;

    private AuthenticationType authenticationType;

    private Integer timeout;

    private Integer poolSize;

    private boolean trustAllCertificates;

    private ConfluenceSiteDescriptor confluenceSiteDescriptor = new ConfluenceSiteDescriptor();

    /**
     * Constructor that takes the values of this instance.
     *
     * @param username
     *        The username of the confluence user.
     * @param password
     *        The password of the confluence user.
     * @param url
     *        The base URL of Confluence.
     * @param authenticationType
     *        The desired type of authentication.
     * @param timeout
     *        The desired timeout for the connection.
     * @param poolSize
     *        The max connection pool size.
     */
    @DataBoundConstructor
    public ConfluenceSite(final String username, final String password, final URL url, final AuthenticationType authenticationType,
        final Integer timeout, final Integer poolSize, final boolean trustAllCertificates) {
        this.username = username;
        this.url = url;
        this.password = password;
        this.authenticationType = authenticationType;
        this.timeout = timeout;
        this.poolSize = poolSize;
        this.trustAllCertificates = trustAllCertificates;
    }

    @Override
    public Descriptor<ConfluenceSite> getDescriptor() {
        return confluenceSiteDescriptor;
    }

    /**
     * Returns the username of the Confluence user.
     *
     * @return The username.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Sets the username of the Confluence user.
     *
     * @param userName
     *        The value for username.
     */
    @DataBoundSetter
    public void setUserName(final String userName) {
        this.username = userName;
    }

    /**
     * Return the base URL of Confluence.
     *
     * @return The base URL of Confluence.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the base URL of Confluence.
     *
     * @param url
     *        The base URL of Confluence.
     */
    @DataBoundSetter
    public void setUrl(final URL url) {
        this.url = url;
    }

    /**
     * Returns the password of the Confluence user.
     *
     * @return The password of the Confluence user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the Confluence user.
     *
     * @param password
     *        The password of the Confluence user.
     */
    @DataBoundSetter
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Returns the type of authentication.
     *
     * @return The type of authentication.
     */
    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Sets the type of authentication.
     *
     * @param authenticationType
     *        The type of authentication.
     */
    @DataBoundSetter
    public void setAuthenticationType(final AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * Returns the timeout for the connections.
     *
     * @return The timeout for the connections.
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout for the connections.
     *
     * @param timeout
     *        The timeout for the connections.
     */
    @DataBoundSetter
    public void setTimeout(final Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the max pool size for the connection pool.
     *
     * @return The max pool size.
     */
    public Integer getPoolSize() {
        return poolSize;
    }

    /**
     * Sets the max pool size for the connection pool.
     *
     * @param poolSize
     *        The max pool size for the connection pool.
     */
    @DataBoundSetter
    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public boolean getTrustAllCertificates() {
        return trustAllCertificates;
    }

    @DataBoundSetter
    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }

    /**
     * Descriptor for {@link ConfluenceSite}.
     */
    @Extension
    public static final class ConfluenceSiteDescriptor extends Descriptor<ConfluenceSite> implements Serializable {

        private static final long serialVersionUID = 7773097811656159514L;

        private String username;
        private String password;
        private String url;
        private AuthenticationType authenticationType;
        private Integer timeout;
        private Integer poolSize;
        private boolean trustAllCertificates;

        /**
         * Constructor that initializes the view.
         */
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
            if (authenticationType.equals(AuthenticationType.OAUTH)) {
                throw new UnsupportedOperationException("OAuth is not a supported authentication method, yet");
            }
            timeout = json.getInt("timeout");
            poolSize = json.getInt("poolSize");
            trustAllCertificates = json.getBoolean("trustAllCertificates");
            validate(username, password, url, timeout);
            save();
            return super.configure(req, json);
        }

        /**
         * Tests the connection with the data from the view.
         *
         * @param username
         *        The username of the Confluence user.
         * @param password
         *        The password of the Confluence user.
         * @param url
         *        The base URL of Confluence.l
         * @param timeout
         *        The timeout for the connections.
         * @return FormValidation to show a success or an error on the view.
         */
        public FormValidation doTestConnection(@QueryParameter("username") final String username,
            @QueryParameter("password") final String password,
            @QueryParameter("url") final String url,
            @QueryParameter("timeout") final Integer timeout) {
            try {
                validate(username, password, url, timeout);
                URL confluenceUrl = new URL(url);
                if (!HttpUtil.isReachable(confluenceUrl, timeout)) {
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
            HttpUtil.validateUrl(url);
        }

        private void validateCredentials(final String username, final String password) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Please enter the username of the confluence user!");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Please enter the passoword of the confluence user!");
            }
        }


        /**
         * Returns the configured username of the Confluence user.
         *
         * @return The configured username of the Confluence user.
         */
        public String getUsername() {
            return username;
        }

        /**
         * Returns the configured password of the Confluence user.
         *
         * @return The configured password of the Confluence user.
         */
        public String getPassword() {
            return password;
        }

        public boolean getTrustAllCertificates() {
            return trustAllCertificates;
        }

        /**
         * Returns the configured URL of Confluence.
         *
         * @return The configured URL of Confluence.
         */
        public URL getUrl() {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException("I am already validated", e);
            }
        }

        /**
         * Returns the type of authentication.
         *
         * @return The type of authentication.
         */
        public AuthenticationType getAuthenticationType() {
            return authenticationType;
        }

        /**
         * Returns the configured timeout for connections.
         *
         * @return The configured timeout.
         */
        public Integer getTimeout() {
            return timeout;
        }

        /**
         * Returns the configured max size of the connection pool.
         *
         * @return The configured max size of the connection pool.
         */
        public Integer getPoolSize() {
            return poolSize;
        }
    }
}
