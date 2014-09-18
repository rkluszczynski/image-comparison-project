--
-- Table structure for table `evaluation`
--
DROP TABLE IF EXISTS `evaluation`;

CREATE TABLE `evaluation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `adddate` datetime DEFAULT NULL,      -- data dodania
  `startdate` datetime DEFAULT NULL,    -- data rozpoczecia przetwarzania
  `enddate` datetime DEFAULT NULL,      -- data zakonczenia przetwarzania
  `status` int(11) NOT NULL,            -- status przetwarzania
  `result` int(11) DEFAULT NULL,        -- wynik przetwarzania
  `manualresult` int(11) DEFAULT NULL,  -- reczny wynik (nie interesuje Cie)
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `evaluationimage`
--
DROP TABLE IF EXISTS `evaluationimage`;

CREATE TABLE `evaluationimage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `evaluation_id` int(11) DEFAULT NULL,
  `processedpath` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,    -- sciezka do przetworzonego obrazu
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,             -- sciezka obrazu do przetworzenia
  PRIMARY KEY (`id`),
  KEY `IDX_D55E3BFC456C5646` (`evaluation_id`),
  CONSTRAINT `FK_D55E3BFC456C5646` FOREIGN KEY (`evaluation_id`) REFERENCES `evaluation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `evaluationmarker`
--
DROP TABLE IF EXISTS `evaluationmarker`;

CREATE TABLE `evaluationmarker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `evaluation_id` int(11) DEFAULT NULL,
  `tobefound` int(11) DEFAULT NULL,		-- ile markerow powinno zostac znalezionych
  `found` int(11) DEFAULT NULL,			-- ile markerow znaleziono
  `color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL, -- kolor (hex) na jaki zaznaczac wystapienia markera
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,	 -- sciezka do pliku z markerem
  PRIMARY KEY (`id`),
  KEY `IDX_CD00B193456C5646` (`evaluation_id`),
  CONSTRAINT `FK_CD00B193456C5646` FOREIGN KEY (`evaluation_id`) REFERENCES `evaluation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

