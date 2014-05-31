package pl.info.rkluszczynski.image.app;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rafal on 2014-05-15.
 */
public class LastModifiedTestApp {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        try {
            File jarFile = new File(
                    new LastModifiedTestApp()
                            .getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );
            Date date = new Date(jarFile.lastModified());
            System.out.println("jar date: " + date);

            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.println(dt.format(date));

            SimpleDateFormat dt1 = new SimpleDateFormat("E yyyy-MM-dd HH:mm");
            System.out.println(dt1.format(date));
        } catch (URISyntaxException e1) {
        }
    }
}
