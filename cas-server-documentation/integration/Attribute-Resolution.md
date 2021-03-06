---
layout: default
title: CAS - Attribute Resolution
---

# Attribute Resolution

Attribute resolution strategies are controlled by 
the [Person Directory project](https://github.com/apereo/person-directory). 
The Person Directory dependency is automatically bundled with the CAS server. Therefor, 
declaring an additional dependency will not be required. 
This Person Directory project supports both LDAP and JDBC attribute resolution, 
caching, attribute aggregation from multiple attribute sources, etc.

<div class="alert alert-info"><strong>Default Caching Policy</strong><p>By default, 
attributes are cached to the length of the SSO session. 
This means that while the underlying component provided by Person Directory may have 
a different caching model, attributes by default and from 
a CAS perspective will not be refreshed and retrieved again on subsequent requests 
as long as the SSO session exists.</p></div>


## Components

A Person Directory `IPersonAttributeDao` attribute source noted by an `attributeRepository` bean 
is defined and configured to describe the global set of attributes to be fetched 
for each authenticated principal. That global set of attributes is then filtered by the 
service manager according to service-specific attribute release rules. 

Note that by default, CAS auto-creates attribute repository sources that are appropriate for LDAP and JDBC sources.
If you need something more, you will need to resort to more elaborate measures of defining the bean configuration directly
on your own. 

To see the relevant list of CAS properties, please [review this guide](../installation/Configuration-Properties.html).

### Person Directory

| Component         					| Description 
|-----------------------------------+--------------------------------------------------------------------------------+
| `MergingPersonAttributeDaoImpl`| Designed to query multiple `IPersonAttributeDaos` in order and merge the results into a single result set. Merging strategies may be configured via instances of `IAttributeMerger`.
| `CachingPersonAttributeDaoImpl`| Provides the ability to cache results of executed inner DAOs.
| `CascadingPersonAttributeDao`| Designed to query multiple `IPersonAttributeDaos` in order and merge the results into a single result set. As each `IPersonAttributesAttributeDao` is queried the attributes from the first `IPersonAttributes` in the result set are used as the query for the next `IPersonAttributesAttributeDao`. 
| `StubPersonAttributeDao`| Backed by a single Map which this implementation will always return, useful for returning static values.
| `MessageFormatPersonAttributeDao`| Provides the ability to create attributes based on other other attribute values as arguments.
| `RegexGatewayPersonAttributeDao`| Conditionally execute an inner DAO if the data in the seed matches criteria set out by the configured patterns.
| `SingleRowJdbcPersonAttributeDao`| The implementation that maps from column names in the result of a SQL query to attribute names.
| `MultiRowJdbcPersonAttributeDao`| Designed to work against a table where there is a mapping of one row to many users. Should be used if the database is structured such that there is a column for attribute names and column(s) for the corresponding values.
| `XmlPersonAttributeDao`| XML backed person attribute DAO that supports wildcard searching.
| `LdapPersonAttributeDao`| Queries an LDAP directory to populate person attributes using Spring Framework's LDAP.
| `GroovyPersonAttributeDao`| Resolve attributes based on an external groovy script.
| `TomlLdapPersonAttributeDao`| Resolve person attributes and insert the ldap/context settings from an external Toml file. 
| `JsonBackedComplexStubPersonAttributeDao`| Resolve person attributes that are specified in an external JSON file.
| `LdaptivePersonAttributeDao`| Queries an LDAP directory to populate person attributes using the Ldaptive library.

More about the Person Directory and its configurable sources [can be found here](https://github.com/apereo/person-directory).

<div class="alert alert-info"><strong>Principal Resolution</strong><p>Note that in most if not all cases, 
CAS authentication is able to retrieve and resolve attributes from the authentication source, which would 
eliminate the need for configuring a separate DAO specially if both the authentication and the attribute source are the same. 
DAOs listed here should only be used when sources are different, or when there is a need to tackle more advanced attribute 
resolution use cases such as those that involve merging, cascading and elaborate 
caching techniques. <a href="../installation/Configuring-Principal-Resolution.html">See this guide</a> for more info.</p></div>


### CAS

The CAS project provides the following additional implementations:

| Component         					| Description 
|-----------------------------------+--------------------------------------------------------------------------------+
| `ShibbolethPersonAttributeDao` | Uses a Shibboleth `attribute-resolver.xml` style file to define and populate person attributes

#### Shibboleth

Note that this module is *EXPERIMENTAL*.

Support is enabled by including the following dependency in the WAR overlay:

```xml
<dependency>
    <groupId>org.apereo.cas</groupId>
    <artifactId>cas-server-support-shibboleth-attributes</artifactId>
    <version>${cas.version}</version>
</dependency>
```

You may also need to declare the following Maven repository in your 
CAS Overlay to be able to resolve dependencies:

```xml
<repositories>
    ...
    <repository>
        <id>shibboleth-releases</id>
        <url>https://build.shibboleth.net/nexus/content/repositories/releases</url>
    </repository>
    ...
</repositories>
```

The module provides a `shibbolethPersonAttributeDao` that the Shibboleth's `attribute-resolver.xml` configuration file for attribute resolution.

- Alias bean:

```xml
<alias name="shibbolethPersonAttributeDao" alias="attributeRepository" />
```

- Modify either `application.properties` or the runtime environment 
to reference the `attribute-resolver.xml` resource via a property. This is a
comma seperated list of resources to use for the configuration:

```shell
-Dcas.shibAttributeResolver.resources=classpath:attribute-resolver.xml
```
