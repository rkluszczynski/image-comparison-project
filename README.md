image-comparison-project
========================

Project with Simple Image Web Application.



== short on jetty installation ==

1. Download
2. Extract
3. Make link to jetty.sh from /etc/init.d/
4. Create /etc/default/jetty config file (default.config)

5. Add login service to etc/jetty.xml:

    <Call name="addBean">
        <Arg>
            <New class="org.eclipse.jetty.security.HashLoginService">
                <Set name="name">Test Realm</Set>
                <Set name="config"><SystemProperty name="jetty.home"
default="."/>/etc/realm.properties</Set>
                <Set name="refreshInterval">0</Set>
            </New>
        </Arg>
    </Call>

</Configure>

6. Add file etc/realm.properties
7. uncomment security in cargo-jetty-deployer plugin
