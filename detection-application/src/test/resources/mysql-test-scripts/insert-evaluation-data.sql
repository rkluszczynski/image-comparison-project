--
-- Wartości dla pola `status` w tabeli `evaluation`:
--    0 - nowy
--    1 - rozpoczęto przetwarzanie
--    2 - przetwarzanie OK
--    3 - błąd przetwarzania
--

INSERT INTO `evaluation` (`id`, `adddate`, `startdate`, `enddate`, `status`, `result`)
VALUES (1, NOW(), NULL, NULL, 0, NULL);

INSERT INTO `evaluation` (`id`, `adddate`, `startdate`, `enddate`, `status`, `result`)
VALUES (2, NOW(), NULL, NULL, 0, NULL);

INSERT INTO `evaluation` (`id`, `adddate`, `startdate`, `enddate`, `status`, `result`)
VALUES (3, NOW(), NULL, NULL, 0, NULL);


--
-- Markers data
--
INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (101, 1, 1, NULL, "FFFFFF", "image-data/patterns/bon-pattern1-shelfstoper.png");

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (102, 2, 1, NULL, "FF0000", "image-data/patterns/bon-pattern2-woobler.png");

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (103, 3, 1, NULL, "00FF00", "image-data/patterns/zubr-pattern1-poster.png");

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (104, 3, 2, NULL, "0000FF", "image-data/patterns/zubr-pattern2-shelfstoper.png");


--
-- Images data
--
INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (201, 1, "image-data/images/bon-image.png", NULL);

INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (202, 2, "image-data/images/bon-image.png", NULL);

INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (203, 3, "image-data/images/zubr-image.png", NULL);

