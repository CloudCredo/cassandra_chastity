# Cassandra Chastity

An authentication and authorisation implementation for [Cassandra] [1] for use with [CloudFoundry] [2].

This Jar must be used in conjunction with [vcap_cassandra] [3] or with a service that set the CF_USER and CF_PASSWORD
system variables.

## Usage

1. The compiled jar must be included in $CASSANDRA_DEPLOYMENT/lib
2. The following $CASSANDRA_DEPLOYMENT/conf/cassandra.yaml properties set to:
     * authenticator: com.cloudcredo.cassandra.auth.CloudFoundryAuthenticator
     * authority: org.apache.cassandra.auth.AllowAllAuthority

A client located on the same box as the Cassandra daemon must set the system environment variables CF_USER and
CF_PASSWORD with the appropriate values.


## Privileges

By default this implementation will allow the following privileges:

### Standard Access
* READ
* WRITE
* FULL_ACCESS

### Schema management
* DESCRIBE
* CREATE
* ALTER
* DROP

### Data access
* UPDATE
* DELETE
* SELECT

Copyright (c) 2013 CloudCredo Ltd.

[1]: http://cassandra.apache.org                                                 "Cassandra"
[2]: http://www.cloudfoundry.org                                                 "Cloudfoundry"
[3]: https://github.com/CloudCredo/vcap_cassandra                                "vcap_cassandra"
