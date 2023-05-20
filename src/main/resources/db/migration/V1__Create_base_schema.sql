-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               PostgreSQL 10.20, compiled by Visual C++ build 1800, 64-bit
-- Server OS:
-- HeidiSQL Version:             12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table public.documentation
CREATE TABLE IF NOT EXISTS "documentation" (
	"id" INTEGER NOT NULL,
	"path" VARCHAR(255) NULL DEFAULT NULL,
	"type" INTEGER NOT NULL,
	PRIMARY KEY ("id")
);

-- Data exporting was unselected.

-- Dumping structure for table public.user_client
CREATE TABLE IF NOT EXISTS "user_client" (
	"id" BIGINT NOT NULL,
	"doc" TIMESTAMP NULL DEFAULT NULL,
	"dou" TIMESTAMP NULL DEFAULT NULL,
	"email" VARCHAR(255) NULL DEFAULT NULL,
	"name" VARCHAR(255) NULL DEFAULT NULL,
	"password" VARCHAR(255) NULL DEFAULT NULL,
	"role" VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY ("id")
);

-- Data exporting was unselected.

-- Dumping structure for table public.split
CREATE TABLE IF NOT EXISTS "split" (
	"id" INTEGER NOT NULL,
	"weight" INTEGER NOT NULL,
	"user_id" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkfl82yoopagh09bn7uibor2jyt" FOREIGN KEY ("user_id") REFERENCES "user_client" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Data exporting was unselected.

-- Dumping structure for table public.income
CREATE TABLE IF NOT EXISTS "income" (
	"id" BIGINT NOT NULL,
	"doi" DATE NULL DEFAULT NULL,
	"sub_type" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"type" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"value" REAL NULL DEFAULT NULL,
	"client_id" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkm4jo255013tgooqyptvwvt6ki" FOREIGN KEY ("client_id") REFERENCES "user_client" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Data exporting was unselected.

-- Dumping structure for table public.purchase
CREATE TABLE IF NOT EXISTS "purchase" (
	"id" BIGINT NOT NULL,
	"dop" DATE NULL DEFAULT NULL,
	"name" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"sub_type" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"type" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"value" REAL NULL DEFAULT NULL,
	"documentation_id" INTEGER NULL DEFAULT NULL,
	"split_id" INTEGER NULL DEFAULT NULL,
	"client_id" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk9q5or7chfvtt54vdl1a7g4mm4" FOREIGN KEY ("client_id") REFERENCES "user_client" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkf313rx7rs75s70996fac454sb" FOREIGN KEY ("documentation_id") REFERENCES "documentation" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkgyrbdciadu5i486mdsfit4l6" FOREIGN KEY ("split_id") REFERENCES "split" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
