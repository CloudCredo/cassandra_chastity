package com.cloudcredo.cassandra.auth;


import org.apache.cassandra.auth.AuthenticatedUser;
import org.apache.cassandra.auth.IAuthenticator;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.thrift.AuthenticationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: chris
 * @date: 10/01/2013
 */
public class CloudFoundryAuthenticatorTest {

    private static final String TEST_USER = "test user";
    private static final String TEST_PASSWORD = "test password";

    /**
     * As Java has no way to set environment variables and our unit requires two environment variables to have been set
     * we need this hack to get Java to think that an environment variable has been set
     */
    private static void setEnvironmentVariable(String key, String value) throws NoSuchFieldException, IllegalAccessException {

        Map<String, String> env = System.getenv();
        Class<?> cl = env.getClass();

        Field field = cl.getDeclaredField("m");
        field.setAccessible(true);

        Object obj = field.get(env);
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) obj;
        map.put(key, value);

    }

    private CloudFoundryAuthenticator unit;

    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        setEnvironmentVariable("CF_USER", TEST_USER);
        setEnvironmentVariable("CF_PASSWORD", TEST_PASSWORD);
        unit = new CloudFoundryAuthenticator();
    }


    @Test
    public void validateConfiguration() throws ConfigurationException {
        unit.validateConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void validateConfigurationShouldFail_no_username() throws NoSuchFieldException, IllegalAccessException, ConfigurationException {
        setEnvironmentVariable("CF_USER", "");
        unit = new CloudFoundryAuthenticator();
        unit.validateConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void validateConfigurationShouldFail_no_password() throws NoSuchFieldException, IllegalAccessException, ConfigurationException {
        setEnvironmentVariable("CF_PASSWORD", "");
        unit = new CloudFoundryAuthenticator();
        unit.validateConfiguration();
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_no_username() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials("", credentials);

        unit.authenticate(credentials);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_null_username() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials(null, credentials);

        unit.authenticate(credentials);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_no_password() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();
        addPasswordToCredentials("", credentials);
        unit.authenticate(credentials);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_null_password() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials(TEST_USER, credentials);
        addPasswordToCredentials(null, credentials);

        unit.authenticate(credentials);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_incorrect_password() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials(TEST_USER, credentials);
        addPasswordToCredentials("error", credentials);

        unit.authenticate(credentials);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateShouldFail_incorrect_username() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials("error", credentials);
        addPasswordToCredentials(TEST_PASSWORD, credentials);

        unit.authenticate(credentials);
    }

    @Test
    public void shouldAuthenticate() throws AuthenticationException {
        Map<CharSequence, CharSequence> credentials = new HashMap<CharSequence, CharSequence>();

        addUserNameToCredentials(TEST_USER, credentials);
        addPasswordToCredentials(TEST_PASSWORD, credentials);

        AuthenticatedUser actual = unit.authenticate(credentials);
        Assert.assertEquals(actual.username, TEST_USER);
    }


    @Test
    public void defaultUserShouldBeNull() {
        Assert.assertNull(unit.defaultUser());
    }

    private void addUserNameToCredentials(String testUser, Map<CharSequence, CharSequence> credentials) {
        CharSequence usernameKey = IAuthenticator.USERNAME_KEY;
        CharSequence usernameValue= testUser;
        credentials.put(usernameKey, usernameValue);
    }

    private void addPasswordToCredentials(String password, Map<CharSequence,CharSequence> credentials) {
        CharSequence passwordKey = IAuthenticator.PASSWORD_KEY;
        CharSequence passwordValue = password;
        credentials.put(passwordKey, passwordValue);
    }
}
