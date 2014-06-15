package pl.info.rkluszczynski.image.config

/**
 * Created by Rafal on 2014-06-15.
 */
class ImageCoreTestConfig {
    static String SUB_PROJECT_NAME = 'image-core'

    public static String getTestResourcesDirectory() {
        String cwd = System.getProperty('user.dir')
        if (!cwd.endsWith(SUB_PROJECT_NAME)) {
            return cwd + System.getProperty('file.separator') + SUB_PROJECT_NAME
        }
        return cwd
    }
}
