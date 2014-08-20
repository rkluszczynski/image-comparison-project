package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*

CREATE TABLE `processedimage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `planogram_id` int(11) DEFAULT NULL,
  `sourcepath` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `processedpath` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `adddate` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  `result` int(11) DEFAULT NULL,
  `fullstatus` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `IDX_E7477B165AFB77AB` (`planogram_id`),
  CONSTRAINT `FK_E7477B165AFB77AB` FOREIGN KEY (`planogram_id`) REFERENCES `planogram` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

 */

@Entity(name = "processedimage")
public class ProcessedImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String sourcepath;
    private String processedpath;

    private int status;
    private int result;
    private String fullstatus;

    public ProcessedImageEntity() {
    }
}
