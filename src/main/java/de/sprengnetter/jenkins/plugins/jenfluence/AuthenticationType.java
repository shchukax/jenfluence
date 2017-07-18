package de.sprengnetter.jenkins.plugins.jenfluence;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public enum AuthenticationType {
    BASIC("basic"),
    OAUTH("oauth");

    private String type;

    AuthenticationType(final String type) {
        this.type = type;
    }

    public static AuthenticationType fromString(final String value) {
        switch (value.toLowerCase()) {
            case "basic":
                return BASIC;
            case "oauth":
                return OAUTH;
            default:
                throw new IllegalArgumentException("AuthenticationType " + value + " is not supported");
        }
    }

    public String getType() {
        return type;
    }
}
