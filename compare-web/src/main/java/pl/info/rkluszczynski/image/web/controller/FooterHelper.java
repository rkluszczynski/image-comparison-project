package pl.info.rkluszczynski.image.web.controller;

import org.springframework.ui.Model;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rafal on 2014-06-01.
 */
final
public class FooterHelper {

    public static void setWebApplicationBuildDate(Model model) {
        model.addAttribute("webAppBuildDate", resolveWebAppBuildDate());
    }

    private static String resolveWebAppBuildDate() {
        SimpleDateFormat dateFormat;
        Date date;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            File archiveFile = new File(
                    new FooterHelper()
                            .getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );
            date = new Date(archiveFile.lastModified());
        } catch (URISyntaxException e) {
            dateFormat = new SimpleDateFormat("yyyy");
            date = new Date();
        }
        return String.format("build: %s", dateFormat.format(date));
    }

    private FooterHelper() {
    }
}
