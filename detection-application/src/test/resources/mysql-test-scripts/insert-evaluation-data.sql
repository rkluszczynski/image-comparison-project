--
-- Wartości dla pola `status` w tabeli `evaluation`:
--    0 - nowy
--    1 - rozpoczęto przetwarzanie
--    2 - przetwarzanie OK
--    3 - błąd przetwarzania
--

INSERT INTO `evaluation` (`id`, `adddate`, `startdate`, `enddate`, `status`, `result`)
VALUES (1, NOW(), NULL, NULL, 0, NULL);


--
-- Markers data
--
INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (100, 1, 1, NULL, "000000", "image-data/patterns/bon-pattern1-shelfstoper.png")

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (100, 1, 2, NULL, "000000", "image-data/patterns/bon-pattern2-woobler.png")

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (100, 1, 3, NULL, "000000", "image-data/patterns/zubr-pattern1-poster.png")

INSERT INTO `evaluationmarker` (`id`, `evaluation_id`, `tobefound`, `found`, `color`, `path`)
VALUES (100, 1, 3, NULL, "000000", "image-data/patterns/zubr-pattern2-shelfstoper.png")


--
-- Images data
--
INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (201, 1, "image-data/images/bon-image.png", NULL);

INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (202, 2, "image-data/images/bon-image.png", NULL);

INSERT INTO `evaluationimage` (`id`, `evaluation_id`, `path`, `processedpath`)
VALUES (203, 3, "image-data/images/zubr-image.png", NULL);

