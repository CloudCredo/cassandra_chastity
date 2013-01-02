package com.cloudcredo.cassandra.auth;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.auth.*;
import java.util.Map;

public class CloudCredoAuthenticator implements IAuthenticator {

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

    boolean authenticated = false;

    //TODO, read the single user/ password from somewhere ...
    authenticated = username.equals("cloudfoundry_user") && password.equals("cloudfoundry_pass");

    if (!authenticated) throw new AuthenticationException(
        String.format("User cloud not be validated : %s", username));

    return new AuthenticatedUser(username.toString());
  }

  public void validateConfiguration() throws ConfigurationException {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}

