package com.cloudcredo.cassandra.auth;

import org.apache.cassandra.config.ConfigurationException;

import java.util.EnumSet;
import java.util.List;
import org.apache.cassandra.auth.*;

public class CloudCredoAuthorizer implements IAuthority {
  public EnumSet<Permission> authorize(AuthenticatedUser user, List<Object> resource) {
    return EnumSet.allOf(Permission.class);
  }

  public void validateConfiguration() throws ConfigurationException {
  }
}

