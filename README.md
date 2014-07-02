image-comparison-project
========================

Project with Simple Image Web Application.

Build automatically tested on Travis CI 
[![Build Status](https://travis-ci.org/rkluszczynski/image-comparison-project.svg?branch=master)](https://travis-ci.org/rkluszczynski/image-comparison-project)

[![Coverage Status](https://coveralls.io/repos/rkluszczynski/image-comparison-project/badge.png)](https://coveralls.io/r/rkluszczynski/image-comparison-project)

Features worth being remembered
-------------------------------

* We are ignoring pixels with low alpha channel value in template images, 
so it is possible to use any shape of template image (be aware of possible
boundary problems).  


Shortly about Jetty installation
--------------------------------

1. Download Jetty archive.
2. Extract it to folder */opt/jetty/*.
3. Make a symbolic link to *jetty.sh* from */etc/init.d/*.
4. Create */etc/default/jetty* config file base on *default.config*.

In order to access only particular users to use cargo-jetty-deployer plugin,
login service has to be configured. 

1. Add login service in /opt/etc/jetty.xml file before the end of *Configure* tag:

        <Call name="addBean">
            <Arg>
                <New class="org.eclipse.jetty.security.HashLoginService">
                    <Set name="name">Test Realm</Set>
                    <Set name="config"><SystemProperty name="jetty.home" default="."/>/etc/realm.properties</Set>
                    <Set name="refreshInterval">0</Set>
                </New>
            </Arg>
        </Call>

2. Create file */opt/etc/realm.properties* with ACL.
3. Uncomment security configuration in *cargo-jetty-deployer* plugin archive (JAR file).

* * *
