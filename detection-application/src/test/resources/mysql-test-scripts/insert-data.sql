--
-- INSERT INTO table_name (column1,column2,column3,...)
-- VALUES (value1,value2,value3,...);
--

INSERT INTO `planogram` (`id`, `path`) VALUES (1, "image-data/patterns/bon-pattern1-shelfstoper.png");
INSERT INTO `planogram` (`id`, `path`) VALUES (2, "image-data/patterns/bon-pattern2-woobler.png");
INSERT INTO `planogram` (`id`, `path`) VALUES (3, "image-data/patterns/zubr-pattern1-poster.png");
INSERT INTO `planogram` (`id`, `path`) VALUES (4, "image-data/patterns/zubr-pattern2-shelfstoper.png");


--
-- Wartości dla pola `status`:
--    0 - nowy
--    1 - rozpoczęto przetwarzanie
--    2 - przetwarzanie OK
--    3 - błąd przetwarzania
--

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (11, 1, "image-data/images/bon-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (12, 2, "image-data/images/bon-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (13, 3, "image-data/images/bon-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (14, 4, "image-data/images/bon-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");


INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (21, 1, "image-data/images/zubr-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (22, 2, "image-data/images/zubr-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (23, 3, "image-data/images/zubr-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

INSERT INTO `processedimage` (`id`, `planogram_id`, `sourcepath`, `processedpath`, `adddate`, `startdate`, `enddate`, `status`, `result`, `fullstatus`)
VALUES (24, 4, "image-data/images/zubr-image.png", NULL, NOW(), NULL, NULL, 0, NULL, "");

