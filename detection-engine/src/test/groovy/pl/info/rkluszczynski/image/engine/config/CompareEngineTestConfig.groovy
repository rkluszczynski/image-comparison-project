package pl.info.rkluszczynski.image.engine.config

/**
 * Created by Rafal on 2014-06-19.
 */
class CompareEngineTestConfig {
    static String SUB_PROJECT_NAME = 'detection-engine'

    public static String getTestResourcesDirectory() {
        String cwd = System.getProperty('user.dir')
        if (!cwd.endsWith(SUB_PROJECT_NAME)) {
            return cwd + System.getProperty('file.separator') + SUB_PROJECT_NAME
        }
        return cwd
    }
}
