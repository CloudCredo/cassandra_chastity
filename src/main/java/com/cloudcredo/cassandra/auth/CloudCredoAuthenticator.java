package com.cloudcredo.cassandra.auth;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.auth.*;
import java.util.Map;

public class CloudCredoAuthenticator implements IAuthenticator {

  private String cf_username;
  private String cf_password;

  public CloudCredoAuthenticator() {
    cf_username = System.getenv("CF_USER");
    cf_password = System.getenv("CF_PASSWORD");
  }

  public AuthenticatedUser defaultUser() {
    return null;
  }

  public AuthenticatedUser authenticate(Map<? extends CharSequence, ? extends CharSequence> credentials) throws AuthenticationException {
    CharSequence username = credentials.get(USERNAME_KEY);
    if (username == null)
      throw new AuthenticationException("Authentication request was missing the required key '" + USERNAME_KEY + "'");

    CharSequence password = credentials.get(PASSWORD_KEY);
    if (password == null)
      throw new AuthenticationException("Authentication request was missing the required key '" + PASSWORD_KEY + "'");

    boolean authenticated = username.equals(cf_username) && password.equals(cf_password);

    if (!authenticated) throw new AuthenticationException(
        String.format("User cloud not be validated : %s", username));

    return new AuthenticatedUser(username.toString());
  }

  public void validateConfiguration() throws ConfigurationException {
    if (cf_username == null || cf_username.length() == 0)
      throw new ConfigurationException("CF_USER is null or empty");

    if (cf_password == null || cf_password.length() == 0)
      throw new ConfigurationException("CF_PASSWORD is null or empty");
  }
}

