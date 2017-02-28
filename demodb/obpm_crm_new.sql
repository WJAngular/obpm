-- MySQL dump 10.13  Distrib 5.1.72, for Win32 (ia32)
--
-- Host: localhost    Database: obpm_crm_new
-- ------------------------------------------------------
-- Server version	5.1.72-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auth_产品信息`
--

DROP TABLE IF EXISTS `auth_产品信息`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_产品信息` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_产品信息`
--

LOCK TABLES `auth_产品信息` WRITE;
/*!40000 ALTER TABLE `auth_产品信息` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_产品信息` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_产品类型`
--

DROP TABLE IF EXISTS `auth_产品类型`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_产品类型` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_产品类型`
--

LOCK TABLES `auth_产品类型` WRITE;
/*!40000 ALTER TABLE `auth_产品类型` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_产品类型` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_公海设置`
--

DROP TABLE IF EXISTS `auth_公海设置`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_公海设置` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_公海设置`
--

LOCK TABLES `auth_公海设置` WRITE;
/*!40000 ALTER TABLE `auth_公海设置` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_公海设置` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_回款信息表`
--

DROP TABLE IF EXISTS `auth_回款信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_回款信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_回款信息表`
--

LOCK TABLES `auth_回款信息表` WRITE;
/*!40000 ALTER TABLE `auth_回款信息表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_回款信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_客户`
--

DROP TABLE IF EXISTS `auth_客户`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_客户` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_客户`
--

LOCK TABLES `auth_客户` WRITE;
/*!40000 ALTER TABLE `auth_客户` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_客户` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_客户来源`
--

DROP TABLE IF EXISTS `auth_客户来源`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_客户来源` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_客户来源`
--

LOCK TABLES `auth_客户来源` WRITE;
/*!40000 ALTER TABLE `auth_客户来源` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_客户来源` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_客户状态`
--

DROP TABLE IF EXISTS `auth_客户状态`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_客户状态` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_客户状态`
--

LOCK TABLES `auth_客户状态` WRITE;
/*!40000 ALTER TABLE `auth_客户状态` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_客户状态` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_客户级别`
--

DROP TABLE IF EXISTS `auth_客户级别`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_客户级别` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_客户级别`
--

LOCK TABLES `auth_客户级别` WRITE;
/*!40000 ALTER TABLE `auth_客户级别` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_客户级别` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_开票信息表`
--

DROP TABLE IF EXISTS `auth_开票信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_开票信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_开票信息表`
--

LOCK TABLES `auth_开票信息表` WRITE;
/*!40000 ALTER TABLE `auth_开票信息表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_开票信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_成交产品明细`
--

DROP TABLE IF EXISTS `auth_成交产品明细`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_成交产品明细` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_成交产品明细`
--

LOCK TABLES `auth_成交产品明细` WRITE;
/*!40000 ALTER TABLE `auth_成交产品明细` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_成交产品明细` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_成交信息表`
--

DROP TABLE IF EXISTS `auth_成交信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_成交信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_成交信息表`
--

LOCK TABLES `auth_成交信息表` WRITE;
/*!40000 ALTER TABLE `auth_成交信息表` DISABLE KEYS */;
INSERT INTO `auth_成交信息表` VALUES ('11e6-6532-e3bb1efb-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff','11e4-7b56-045d6210-a888-6d6b162bf5de'),('11e6-6534-12a72f64-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529'),('11e6-68fb-20b5e3fa-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be'),('11e6-69a5-a76c1f51-82ae-bbe47783a3bc','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be'),('11e6-6b5d-03e468cb-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388'),('11e6-6b5d-08f0acfd-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388'),('11e6-6b5f-9f38e56c-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529'),('11e6-6b69-de033914-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529'),('11e6-6b6a-0c81148f-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529'),('11e6-6b6c-bef313b1-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de'),('11e6-6b6c-c4578a8f-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de');
/*!40000 ALTER TABLE `auth_成交信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_机会信息表`
--

DROP TABLE IF EXISTS `auth_机会信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_机会信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_机会信息表`
--

LOCK TABLES `auth_机会信息表` WRITE;
/*!40000 ALTER TABLE `auth_机会信息表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_机会信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_线索信息表`
--

DROP TABLE IF EXISTS `auth_线索信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_线索信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_线索信息表`
--

LOCK TABLES `auth_线索信息表` WRITE;
/*!40000 ALTER TABLE `auth_线索信息表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_线索信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_线索来源`
--

DROP TABLE IF EXISTS `auth_线索来源`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_线索来源` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_线索来源`
--

LOCK TABLES `auth_线索来源` WRITE;
/*!40000 ALTER TABLE `auth_线索来源` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_线索来源` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_联系人信息表`
--

DROP TABLE IF EXISTS `auth_联系人信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_联系人信息表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_联系人信息表`
--

LOCK TABLES `auth_联系人信息表` WRITE;
/*!40000 ALTER TABLE `auth_联系人信息表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_联系人信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_计量单位`
--

DROP TABLE IF EXISTS `auth_计量单位`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_计量单位` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_计量单位`
--

LOCK TABLES `auth_计量单位` WRITE;
/*!40000 ALTER TABLE `auth_计量单位` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_计量单位` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_跟进记录表`
--

DROP TABLE IF EXISTS `auth_跟进记录表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_跟进记录表` (
  `ID` varchar(200) NOT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `VALUE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_跟进记录表`
--

LOCK TABLES `auth_跟进记录表` WRITE;
/*!40000 ALTER TABLE `auth_跟进记录表` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_跟进记录表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_actorhis`
--

DROP TABLE IF EXISTS `t_actorhis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_actorhis` (
  `ID` varchar(200) NOT NULL,
  `ACTORID` varchar(200) DEFAULT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `AGENTID` varchar(200) DEFAULT NULL,
  `AGENTNAME` varchar(200) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `PROCESSTIME` datetime DEFAULT NULL,
  `ATTITUDE` varchar(2000) DEFAULT NULL,
  `NODEHIS_ID` varchar(200) DEFAULT NULL,
  `FLOWSTATERT_ID` varchar(200) DEFAULT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `SIGNATURE` longtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_actorhis`
--

LOCK TABLES `t_actorhis` WRITE;
/*!40000 ALTER TABLE `t_actorhis` DISABLE KEYS */;
INSERT INTO `t_actorhis` VALUES ('11e6-6532-9f38f925-abfb-7f3dc8737aff','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）',NULL,NULL,3,'2016-08-18 18:57:57','','11e6-6532-9f38f924-abfb-7f3dc8737aff','11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff',''),('11e6-6532-cede8b7e-abfb-7f3dc8737aff','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）',NULL,NULL,3,'2016-08-18 18:59:17','','11e6-6532-cede8b7d-abfb-7f3dc8737aff','11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e6-6532-a68b0f0a-abfb-7f3dc8737aff',''),('11e6-6532-e39c254a-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-18 18:59:52','','11e6-6532-e39c2549-abfb-7f3dc8737aff','11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff',''),('11e6-6533-0d84e800-abfb-7f3dc8737aff','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-18 19:01:02','','11e6-6533-0d84e7ff-abfb-7f3dc8737aff','11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e6-6532-a68b0f0a-abfb-7f3dc8737aff',''),('11e6-6533-ec306239-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-18 19:07:16','','11e6-6533-ec306238-abfb-7f3dc8737aff','11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff',''),('11e6-6534-03067c28-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-18 19:07:54','','11e6-6534-03067c27-abfb-7f3dc8737aff','11e6-6534-0210f88f-abfb-7f3dc8737aff','11e6-6533-f095b82c-abfb-7f3dc8737aff',''),('11e6-6534-128cf11b-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-18 19:08:20','','11e6-6534-128cf11a-abfb-7f3dc8737aff','11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff',''),('11e6-6534-4732b900-abfb-7f3dc8737aff','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-18 19:09:48','','11e6-6534-4732b8ff-abfb-7f3dc8737aff','11e6-6534-0210f88f-abfb-7f3dc8737aff','11e6-6533-f095b82c-abfb-7f3dc8737aff',''),('11e6-68e4-7f57ee1e-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','系统管理员',NULL,NULL,3,'2016-08-23 11:48:47','','11e6-68e4-7f57ee1d-abfb-7f3dc8737aff','11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff',''),('11e6-68f8-fed579b8-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-23 14:15:31','','11e6-68f8-fed579b7-abfb-7f3dc8737aff','11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff',''),('11e6-68fa-ee1a1c67-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','系统管理员',NULL,NULL,3,'2016-08-23 14:29:22','','11e6-68fa-ee1a1c66-abfb-7f3dc8737aff','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',''),('11e6-68fa-fba24ff4-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-23 14:29:45','','11e6-68fa-fba24ff3-abfb-7f3dc8737aff','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',''),('11e6-68fb-20a06029-abfb-7f3dc8737aff','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','admin',NULL,NULL,3,'2016-08-23 14:30:47','','11e6-68fb-20a06028-abfb-7f3dc8737aff','11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff',NULL),('11e6-68fb-249a70fb-abfb-7f3dc8737aff','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','admin',NULL,NULL,3,'2016-08-23 14:30:54','','11e6-68fb-249a70fa-abfb-7f3dc8737aff','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',NULL),('11e6-69a5-9192b4d3-82ae-bbe47783a3bc','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','系统管理员',NULL,NULL,3,'2016-08-24 10:50:51','','11e6-69a5-9192b4d2-82ae-bbe47783a3bc','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',''),('11e6-69a5-a751e090-82ae-bbe47783a3bc','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-24 10:51:27','','11e6-69a5-a751e08f-82ae-bbe47783a3bc','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',''),('11e6-6a8c-5f632eb0-82ae-bbe47783a3bc','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-25 14:23:00','','11e6-6a8c-5f632eaf-82ae-bbe47783a3bc','11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-6a8c-4f906f24-82ae-bbe47783a3bc',''),('11e6-6b35-2f2f8211-82ae-bbe47783a3bc','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 10:31:25','','11e6-6b35-2f2f8210-82ae-bbe47783a3bc','11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-6a8c-4f906f24-82ae-bbe47783a3bc',''),('11e6-6b35-725328ba-82ae-bbe47783a3bc','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 10:33:17','','11e6-6b35-725328b9-82ae-bbe47783a3bc','11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-6a8c-4f906f24-82ae-bbe47783a3bc',''),('11e6-6b3c-379c044f-bbbd-ef6eef62debe','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 11:21:45','','11e6-6b3c-379c044e-bbbd-ef6eef62debe','11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-6a8c-4f906f24-82ae-bbe47783a3bc',''),('11e6-6b5c-c0c0c27b-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:14:39','','11e6-6b5c-c0be788a-ab37-bf0a0f2138df','11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df',''),('11e6-6b5c-f26b99d2-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:16:02','','11e6-6b5c-f26b99d1-ab37-bf0a0f2138df','11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df',''),('11e6-6b5d-03d15696-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 15:16:32','','11e6-6b5d-03d15695-ab37-bf0a0f2138df','11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df',''),('11e6-6b5d-08dd9a2c-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 15:16:40','','11e6-6b5d-08dd9a2b-ab37-bf0a0f2138df','11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df',''),('11e6-6b5d-3bc7cff1-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:18:06','','11e6-6b5d-3bc7cff0-ab37-bf0a0f2138df','11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e6-6b5d-235c97e6-ab37-bf0a0f2138df',''),('11e6-6b5d-63237f1f-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:19:12','','11e6-6b5d-63237f1e-ab37-bf0a0f2138df','11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e6-6b5d-235c97e6-ab37-bf0a0f2138df',''),('11e6-6b5d-cb7e4747-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:22:07','','11e6-6b5d-cb7e4746-ab37-bf0a0f2138df','11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e6-6b5d-be746b96-ab37-bf0a0f2138df',''),('11e6-6b5d-d577af50-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 15:22:23','','11e6-6b5d-d577af4f-ab37-bf0a0f2138df','11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e6-6b5d-be746b96-ab37-bf0a0f2138df',''),('11e6-6b5f-997e86a0-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 15:35:02','','11e6-6b5f-997e869f-ab37-bf0a0f2138df','11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df',''),('11e6-6b5f-9f25d29b-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 15:35:11','','11e6-6b5f-9f25d29a-ab37-bf0a0f2138df','11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df',''),('11e6-6b69-d958c406-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 16:48:24','','11e6-6b69-d958c405-ab37-bf0a0f2138df','11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df',''),('11e6-6b69-ddf02659-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 16:48:32','','11e6-6b69-ddf02658-ab37-bf0a0f2138df','11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df',''),('11e6-6b6a-01a78576-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 16:49:31','','11e6-6b6a-01a78575-ab37-bf0a0f2138df','11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df',''),('11e6-6b6a-0c707236-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 16:49:50','','11e6-6b6a-0c707235-ab37-bf0a0f2138df','11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df',''),('11e6-6b6c-0d696688-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）',NULL,NULL,3,'2016-08-26 17:04:10','','11e6-6b6c-0d696687-ab37-bf0a0f2138df','11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df',''),('11e6-6b6c-8072dd46-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）',NULL,NULL,3,'2016-08-26 17:07:23','','11e6-6b6c-8072dd45-ab37-bf0a0f2138df','11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df',''),('11e6-6b6c-bee271e0-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 17:09:08','','11e6-6b6c-bee271df-ab37-bf0a0f2138df','11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df',''),('11e6-6b6c-c444775a-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）',NULL,NULL,3,'2016-08-26 17:09:17','','11e6-6b6c-c4447759-ab37-bf0a0f2138df','11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df',''),('11e6-6b6e-a8d7fad5-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）',NULL,NULL,3,'2016-08-26 17:22:50','','11e6-6b6e-a8d7fad4-ab37-bf0a0f2138df','11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e6-6b6e-8854c495-ab37-bf0a0f2138df',''),('11e6-6b6e-c6080de8-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管',NULL,NULL,3,'2016-08-26 17:23:39','','11e6-6b6e-c6080de7-ab37-bf0a0f2138df','11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e6-6b6e-8854c495-ab37-bf0a0f2138df','');
/*!40000 ALTER TABLE `t_actorhis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_actorrt`
--

DROP TABLE IF EXISTS `t_actorrt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_actorrt` (
  `ID` varchar(200) NOT NULL,
  `ACTORID` varchar(200) DEFAULT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `ISPROCESSED` bit(1) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `NODERT_ID` varchar(200) DEFAULT NULL,
  `FLOWSTATERT_ID` varchar(200) DEFAULT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `DEADLINE` datetime DEFAULT NULL,
  `PENDING` bit(1) DEFAULT NULL,
  `ISREAD` bit(1) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `APPROVAL_POSITION` int(11) DEFAULT NULL,
  `LASTOVERDUEREMINDER` datetime DEFAULT NULL,
  `REMINDER_TIMES` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_actorrt`
--

LOCK TABLES `t_actorrt` WRITE;
/*!40000 ALTER TABLE `t_actorrt` DISABLE KEYS */;
INSERT INTO `t_actorrt` VALUES ('11e6-6532-e39769ec-abfb-7f3dc8737aff','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）','\0',3,'11e6-6532-e39769eb-abfb-7f3dc8737aff','11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6534-12883629-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','\0',3,'11e6-6534-12883628-abfb-7f3dc8737aff','11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff',NULL,'','','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-68fb-209def27-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','系统管理员','\0',3,'11e6-68fb-209def26-abfb-7f3dc8737aff','11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-69a5-a751e08e-82ae-bbe47783a3bc','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','系统管理员','\0',3,'11e6-69a5-a74f6f8d-82ae-bbe47783a3bc','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b5d-03cee4ee-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管','\0',3,'11e6-6b5d-03cee4ed-ab37-bf0a0f2138df','11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b5d-08dd9a2a-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管','\0',3,'11e6-6b5d-08db2929-ab37-bf0a0f2138df','11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df',NULL,'','','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b5f-9f236282-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','\0',3,'11e6-6b5f-9f20f12c-ab37-bf0a0f2138df','11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df',NULL,'','','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b69-ddedb557-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','\0',3,'11e6-6b69-ddedb556-ab37-bf0a0f2138df','11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df',NULL,'','','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b6a-0c6e0134-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','\0',3,'11e6-6b6a-0c6e0133-ab37-bf0a0f2138df','11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df',NULL,'','','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b6c-bee00056-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）','\0',3,'11e6-6b6c-bedd8f55-ab37-bf0a0f2138df','11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0),('11e6-6b6c-c4420712-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','邢主管（行政主管）','\0',3,'11e6-6b6c-c43fbcc1-ab37-bf0a0f2138df','11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df',NULL,'','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,0);
/*!40000 ALTER TABLE `t_actorrt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_circulator`
--

DROP TABLE IF EXISTS `t_circulator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_circulator` (
  `ID` varchar(200) NOT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `USERID` varchar(200) DEFAULT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `NODERT_ID` varchar(200) DEFAULT NULL,
  `FLOWSTATERT_ID` varchar(200) DEFAULT NULL,
  `CCTIME` datetime DEFAULT NULL,
  `READTIME` datetime DEFAULT NULL,
  `DEADLINE` datetime DEFAULT NULL,
  `ISREAD` int(11) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_circulator`
--

LOCK TABLES `t_circulator` WRITE;
/*!40000 ALTER TABLE `t_circulator` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_circulator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_counter`
--

DROP TABLE IF EXISTS `t_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_counter` (
  `ID` varchar(200) NOT NULL,
  `COUNTER` int(11) DEFAULT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_counter`
--

LOCK TABLES `t_counter` WRITE;
/*!40000 ALTER TABLE `t_counter` DISABLE KEYS */;
INSERT INTO `t_counter` VALUES ('11e6-5ff3-1bb0ec86-b967-4ba4af478a40',2,'KH160812','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6526-595e65da-9d40-a7ec8e34cb38',4,'KH160818','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6532-7c98af44-abfb-7f3dc8737aff',2,'YGLZ160818','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6834-86141581-abfb-7f3dc8737aff',2,'KH160822','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-68e4-6bf68929-abfb-7f3dc8737aff',16,'YGLZ160823','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-68f6-27a83fc7-abfb-7f3dc8737aff',7,'KH160823','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-69d4-ca21930b-82ae-bbe47783a3bc',3,'KH160824','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-69e5-615f2028-82ae-bbe47783a3bc',1,'YGLZ160824','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6a6c-d0d34e3d-82ae-bbe47783a3bc',8,'YGLZ160825','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6ab0-bf58ba22-82ae-bbe47783a3bc',9,'KH160825','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6b52-68105563-bbbd-ef6eef62debe',111,'KH160826','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6b5c-a9d09060-ab37-bf0a0f2138df',7,'YGLZ160826','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467'),('11e6-6db9-a0c23c2b-ab37-bf0a0f2138df',1,'KH160829','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467');
/*!40000 ALTER TABLE `t_counter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_datamaptemplate`
--

DROP TABLE IF EXISTS `t_datamaptemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_datamaptemplate` (
  `ID` varchar(200) NOT NULL,
  `DATAMAPCFG_ID` varchar(200) DEFAULT NULL,
  `CULEFIELD` varchar(200) DEFAULT NULL,
  `CULEFIELD2` varchar(200) DEFAULT NULL,
  `TEMPLATE` longtext,
  `CONTENT` longtext,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_datamaptemplate`
--

LOCK TABLES `t_datamaptemplate` WRITE;
/*!40000 ALTER TABLE `t_datamaptemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_datamaptemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_document`
--

DROP TABLE IF EXISTS `t_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_document` (
  `ID` varchar(200) NOT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `SORTID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `INITIATOR` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `PARENT` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `MAPPINGID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_document`
--

LOCK TABLES `t_document` WRITE;
/*!40000 ALTER TABLE `t_document` DISABLE KEYS */;
INSERT INTO `t_document` VALUES ('11e6-5fef-13fda127-b967-4ba4af478a40','2016-08-12 02:11:54','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:11:51','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-13fda127-b967-4ba4af478a40'),('11e6-5fef-19ba48d4-b967-4ba4af478a40','2016-08-12 02:12:03','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:01','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-19ba48d4-b967-4ba4af478a40'),('11e6-5fef-1de8d78b-b967-4ba4af478a40','2016-08-12 02:12:11','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:08','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-1de8d78b-b967-4ba4af478a40'),('11e6-5fef-22317e0c-b967-4ba4af478a40','2016-08-12 02:12:18','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:15','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-22317e0c-b967-4ba4af478a40'),('11e6-5fef-26fd5d67-b967-4ba4af478a40','2016-08-12 02:12:25','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:23','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-26fd5d67-b967-4ba4af478a40'),('11e6-5fef-2aca1d5c-b967-4ba4af478a40','2016-08-12 02:12:33','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:30','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-2aca1d5c-b967-4ba4af478a40'),('11e6-5fef-2fb4f69b-b967-4ba4af478a40','2016-08-12 02:12:40','CRM/基础设置/线索来源/线索来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:38','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-2fb4f69b-b967-4ba4af478a40'),('11e6-5fef-92be5bcc-b967-4ba4af478a40','2016-08-12 02:15:34','CRM/基础设置/客户来源/客户来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:24','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-92be5bcc-b967-4ba4af478a40'),('11e6-5fef-9b975fa9-b967-4ba4af478a40','2016-08-12 02:15:46','CRM/基础设置/客户来源/客户来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:39','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-9b975fa9-b967-4ba4af478a40'),('11e6-5fef-a25c9ed0-b967-4ba4af478a40','2016-08-12 02:16:00','CRM/基础设置/客户来源/客户来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:50','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-a25c9ed0-b967-4ba4af478a40'),('11e6-5fef-b0cc24d1-b967-4ba4af478a40','2016-08-12 02:16:23','CRM/基础设置/客户来源/客户来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:16:14','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-b0cc24d1-b967-4ba4af478a40'),('11e6-5fef-b902f58c-b967-4ba4af478a40','2016-08-12 02:16:41','CRM/基础设置/客户来源/客户来源',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:16:28','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-b902f58c-b967-4ba4af478a40'),('11e6-5fef-d9d73195-b967-4ba4af478a40','2016-08-12 02:18:15','CRM/基础设置/客户级别/客户级别',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:23','11e6-39df-6cbaf295-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-d9d73195-b967-4ba4af478a40'),('11e6-5fef-dfb7b610-b967-4ba4af478a40','2016-08-12 02:17:38','CRM/基础设置/客户级别/客户级别',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:33','11e6-39df-6cbaf295-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-dfb7b610-b967-4ba4af478a40'),('11e6-5fef-e8b1fdc6-b967-4ba4af478a40','2016-08-12 02:17:52','CRM/基础设置/客户级别/客户级别',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:48','11e6-39df-6cbaf295-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5fef-e8b1fdc6-b967-4ba4af478a40'),('11e6-5ff0-0cb0ec62-b967-4ba4af478a40','2016-08-12 02:18:51','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:18:49','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-0cb0ec62-b967-4ba4af478a40'),('11e6-5ff0-11601caf-b967-4ba4af478a40','2016-08-12 02:19:00','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:18:56','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-11601caf-b967-4ba4af478a40'),('11e6-5ff0-176df2b6-b967-4ba4af478a40','2016-08-12 02:19:09','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:07','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-176df2b6-b967-4ba4af478a40'),('11e6-5ff0-1bf242a7-b967-4ba4af478a40','2016-08-12 02:19:17','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:14','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-1bf242a7-b967-4ba4af478a40'),('11e6-5ff0-20f27872-b967-4ba4af478a40','2016-08-12 02:19:25','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:23','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-20f27872-b967-4ba4af478a40'),('11e6-5ff0-24f3b5e7-b967-4ba4af478a40','2016-08-12 02:19:32','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:29','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-24f3b5e7-b967-4ba4af478a40'),('11e6-5ff0-28dab3b6-b967-4ba4af478a40','2016-08-12 02:19:38','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:36','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-28dab3b6-b967-4ba4af478a40'),('11e6-5ff0-2cf87a0f-b967-4ba4af478a40','2016-08-12 02:19:45','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:43','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-2cf87a0f-b967-4ba4af478a40'),('11e6-5ff0-316e98b2-b967-4ba4af478a40','2016-08-12 02:19:52','CRM/基础设置/客户状态/客户状态',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:50','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-316e98b2-b967-4ba4af478a40'),('11e6-5ff0-498659f1-b967-4ba4af478a40','2016-08-12 02:20:33','CRM/基础设置/产品类型/产品类型',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:20:31','11e6-3c45-a2d92ad0-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-498659f1-b967-4ba4af478a40'),('11e6-5ff0-4db4e88e-b967-4ba4af478a40','2016-08-12 02:20:40','CRM/基础设置/产品类型/产品类型',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:20:38','11e6-3c45-a2d92ad0-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-4db4e88e-b967-4ba4af478a40'),('11e6-5ff0-5c6bfec7-b967-4ba4af478a40','2016-08-12 02:21:16','CRM/基础设置/计量单位/计量单位',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:21:02','11e6-3c4f-b8b129f9-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-5c6bfec7-b967-4ba4af478a40'),('11e6-5ff0-70280749-b967-4ba4af478a40','2016-08-22 17:19:46','CRM/基础设置/公海设置/公海设置',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:21:35','11e6-41c7-fe93dfcd-8bb8-6142300f1a0f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-5ff0-70280749-b967-4ba4af478a40'),('11e6-6526-3253e3f8-9d40-a7ec8e34cb38','2016-08-18 18:01:51','客户管理/线索管理/线索管理/线索信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:29:00','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6526-3253e3f8-9d40-a7ec8e34cb38'),('11e6-6526-5959aaa3-9d40-a7ec8e34cb38','2016-08-18 17:31:34','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:30:06','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6526-5959aaa3-9d40-a7ec8e34cb38'),('11e6-6526-a3f0aaec-9d40-a7ec8e34cb38','2016-08-25 19:01:01','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:32:11','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',7,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6526-a3f0aaec-9d40-a7ec8e34cb38'),('11e6-6526-b7972ff9-9d40-a7ec8e34cb38','2016-08-18 17:36:39','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:32:44','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6526-b7972ff9-9d40-a7ec8e34cb38'),('11e6-652a-8bd85158-9d40-a7ec8e34cb38','2016-08-18 18:02:02','客户管理/线索管理/线索管理/线索信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 18:00:09','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-652a-8bd85158-9d40-a7ec8e34cb38'),('11e6-652e-61de7a55-9d40-a7ec8e34cb38','2016-08-18 18:30:36','客户管理/客户管理/客户管理/客户',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:27:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-652e-61de7a55-9d40-a7ec8e34cb38'),('11e6-652e-fee155c6-9d40-a7ec8e34cb38','2016-08-18 18:33:11','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:32:00','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-652e-fee155c6-9d40-a7ec8e34cb38'),('11e6-652f-7c94bf13-9d40-a7ec8e34cb38','2016-08-18 18:36:33','客户管理/机会管理/机会/机会信息表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:35:30','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-652f-7c94bf13-9d40-a7ec8e34cb38'),('11e6-652f-d8f7df0e-9d40-a7ec8e34cb38','2016-08-18 18:38:26','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:38:05','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-652f-d8f7df0e-9d40-a7ec8e34cb38'),('11e6-6532-2c9aef28-abfb-7f3dc8737aff','2016-08-25 19:02:35','客户管理/联系人/联系人/联系人信息表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:54:45','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6532-2c9aef28-abfb-7f3dc8737aff'),('11e6-6532-7c859c55-abfb-7f3dc8737aff','2016-08-18 18:59:52','客户管理/成交/成交/成交信息表','2016-08-18 18:59:52','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:56:59','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e4-7b56-045d6210-a888-6d6b162bf5de','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6532-9d9c872d-abfb-7f3dc8737aff',256,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6532-7c859c55-abfb-7f3dc8737aff'),('11e6-6532-a68b0f0a-abfb-7f3dc8737aff','2016-08-18 19:01:02','客户管理/回款管理/回款管理/回款信息表','2016-08-18 19:01:02','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:58:09','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e4-7b56-045d6210-a888-6d6b162bf5de','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6532-cdfe64a5-abfb-7f3dc8737aff',1048576,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6532-a68b0f0a-abfb-7f3dc8737aff'),('11e6-6533-56eabb43-abfb-7f3dc8737aff','2016-08-24 17:59:28','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:03:05','11e6-39f5-414b6696-a60d-2b305113d028','\0',10,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6533-56eabb43-abfb-7f3dc8737aff'),('11e6-6533-c78aab27-abfb-7f3dc8737aff','2016-08-18 19:08:20','客户管理/成交/成交/成交信息表','2016-08-18 19:08:20','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:06:14','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6533-eae03b81-abfb-7f3dc8737aff',256,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6533-c78aab27-abfb-7f3dc8737aff'),('11e6-6533-f095b82c-abfb-7f3dc8737aff','2016-08-18 19:09:48','客户管理/回款管理/回款管理/回款信息表','2016-08-18 19:09:48','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:07:23','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6534-0210f88f-abfb-7f3dc8737aff',1048576,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6533-f095b82c-abfb-7f3dc8737aff'),('11e6-6833-6a388aa5-abfb-7f3dc8737aff','2016-08-22 14:41:44','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 14:41:11','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6833-6a388aa5-abfb-7f3dc8737aff'),('11e6-6834-8611cb4a-abfb-7f3dc8737aff','2016-08-26 18:09:57','客户管理/客户管理/客户管理/客户',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 14:49:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',8,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6834-8611cb4a-abfb-7f3dc8737aff'),('11e6-6850-c3417caa-abfb-7f3dc8737aff','2016-08-22 18:31:42','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:11:16','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6850-c3417caa-abfb-7f3dc8737aff'),('11e6-6853-b8c19f52-abfb-7f3dc8737aff','2016-08-22 18:33:22','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:32:27','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6853-b8c19f52-abfb-7f3dc8737aff'),('11e6-6853-d9d8b160-abfb-7f3dc8737aff','2016-08-22 18:33:22','客户关系/客户管理/客户管理/客户',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be',NULL,'2016-08-22 18:33:22','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,NULL,0,NULL,'11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,NULL),('11e6-6854-1cb9830e-abfb-7f3dc8737aff','2016-08-22 18:36:10','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:35:14','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6854-1cb9830e-abfb-7f3dc8737aff'),('11e6-6856-3ee156b1-abfb-7f3dc8737aff','2016-08-22 18:50:53','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:50:31','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6856-3ee156b1-abfb-7f3dc8737aff'),('11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','2016-08-23 14:30:47','客户管理/成交/成交/成交信息表','2016-08-23 14:30:47','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 11:48:15','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','创建','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','系统管理员','86',NULL,'11e6-68e4-7e757d56-abfb-7f3dc8737aff',256,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467014814628\",\"stateLabel\":\"创建\",\"auditors\":[{\"id\":\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\",\"name\":\"系统管理员\"}]}]}]','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"prevAuditNode\":\"已确认\"}]','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"prevAuditUser\":\"admin\"}]',NULL,'11e6-68e4-6bef5d1a-abfb-7f3dc8737aff'),('11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','2016-08-24 10:51:28','客户管理/成交/成交/成交信息表','2016-08-24 10:51:27','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 14:28:55','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',5,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','创建','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e3-8a58-82144b41-9194-1d682b48d529','系统管理员','81',NULL,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff',256,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467014814628\",\"stateLabel\":\"创建\",\"auditors\":[{\"id\":\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\",\"name\":\"系统管理员\"}]}]}]','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-68fa-ddcde6bc-abfb-7f3dc8737aff'),('11e6-691d-23eef66a-abfb-7f3dc8737aff','2016-08-26 14:59:11','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 18:34:15','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-691d-23eef66a-abfb-7f3dc8737aff'),('11e6-6a76-1d3d4b19-82ae-bbe47783a3bc','2016-08-25 11:45:59','客户管理/产品/产品/产品信息',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-25 11:43:41','11e6-3c42-1fa0f894-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6a76-1d3d4b19-82ae-bbe47783a3bc'),('11e6-6a8c-4f906f24-82ae-bbe47783a3bc','2016-08-26 11:21:45','客户管理/开票管理/开票管理/开票信息表','2016-08-26 11:21:45','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-25 14:22:34','11e6-3ce0-3cd2af43-9676-8f334e66899f','\0',4,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已开票','11e3-8a58-82144b41-9194-1d682b48d529','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6a8c-5e832ee7-82ae-bbe47783a3bc',1048576,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467085956786\":[]}','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"flowName\":\"开票申请\",\"flowId\":\"11e6-3ce4-14f48d78-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已开票\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"prevAuditNode\":\"未开票\"}]','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6a8c-4f906f24-82ae-bbe47783a3bc'),('11e6-6b52-6f113e54-bbbd-ef6eef62debe','2016-08-26 14:58:52','客户管理/线索管理/线索管理/线索信息表',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 14:00:47','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b52-6f113e54-bbbd-ef6eef62debe'),('11e6-6b55-449f4731-ab37-bf0a0f2138df','2016-08-26 15:27:41','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:21:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b55-449f4731-ab37-bf0a0f2138df'),('11e6-6b55-88cb8438-ab37-bf0a0f2138df','2016-08-26 14:24:40','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:22:59','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b55-88cb8438-ab37-bf0a0f2138df'),('11e6-6b56-355c0056-ab37-bf0a0f2138df','2016-08-26 14:33:27','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:27:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b56-355c0056-ab37-bf0a0f2138df'),('11e6-6b57-01f18119-ab37-bf0a0f2138df','2016-08-26 14:35:04','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:33:32','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b57-01f18119-ab37-bf0a0f2138df'),('11e6-6b57-3c3742ea-ab37-bf0a0f2138df','2016-08-26 14:36:12','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:35:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b57-3c3742ea-ab37-bf0a0f2138df'),('11e6-6b57-ef94500b-ab37-bf0a0f2138df','2016-08-26 15:27:57','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:40:10','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b57-ef94500b-ab37-bf0a0f2138df'),('11e6-6b58-1f731b1c-ab37-bf0a0f2138df','2016-08-26 15:27:18','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:41:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b58-1f731b1c-ab37-bf0a0f2138df'),('11e6-6b58-6ba68d86-ab37-bf0a0f2138df','2016-08-26 14:45:32','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:43:38','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b58-6ba68d86-ab37-bf0a0f2138df'),('11e6-6b58-b20a1cd7-ab37-bf0a0f2138df','2016-08-26 14:46:45','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:45:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b58-b20a1cd7-ab37-bf0a0f2138df'),('11e6-6b58-ddba5968-ab37-bf0a0f2138df','2016-08-26 15:40:31','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:46:50','11e6-39f5-414b6696-a60d-2b305113d028','\0',4,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b58-ddba5968-ab37-bf0a0f2138df'),('11e6-6b59-3545b199-ab37-bf0a0f2138df','2016-08-26 17:00:06','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:49:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',3,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b59-3545b199-ab37-bf0a0f2138df'),('11e6-6b59-eff2645a-ab37-bf0a0f2138df','2016-08-26 14:57:22','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:54:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b59-eff2645a-ab37-bf0a0f2138df'),('11e6-6b5a-b104f78a-ab37-bf0a0f2138df','2016-08-26 15:00:36','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:59:54','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5a-b104f78a-ab37-bf0a0f2138df'),('11e6-6b5a-d9f835ef-ab37-bf0a0f2138df','2016-08-26 15:01:35','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:01:02','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5a-d9f835ef-ab37-bf0a0f2138df'),('11e6-6b5b-172ab722-ab37-bf0a0f2138df','2016-08-26 15:03:28','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:02:45','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-172ab722-ab37-bf0a0f2138df'),('11e6-6b5b-3dc2c83f-ab37-bf0a0f2138df','2016-08-26 15:04:20','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:03:50','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-3dc2c83f-ab37-bf0a0f2138df'),('11e6-6b5b-5e26dda0-ab37-bf0a0f2138df','2016-08-26 15:06:02','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:04:44','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-5e26dda0-ab37-bf0a0f2138df'),('11e6-6b5b-9d9592cb-ab37-bf0a0f2138df','2016-08-26 15:07:03','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:06:31','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-9d9592cb-ab37-bf0a0f2138df'),('11e6-6b5b-baee3cb2-ab37-bf0a0f2138df','2016-08-26 15:08:41','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:07:20','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-baee3cb2-ab37-bf0a0f2138df'),('11e6-6b5b-ef5f8782-ab37-bf0a0f2138df','2016-08-26 15:09:09','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:08:48','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5b-ef5f8782-ab37-bf0a0f2138df'),('11e6-6b5c-0cbced28-ab37-bf0a0f2138df','2016-08-26 15:10:20','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:09:37','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5c-0cbced28-ab37-bf0a0f2138df'),('11e6-6b5c-2dd89288-ab37-bf0a0f2138df','2016-08-26 15:11:47','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:10:33','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5c-2dd89288-ab37-bf0a0f2138df'),('11e6-6b5c-75e71354-ab37-bf0a0f2138df','2016-08-26 15:13:49','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:12:34','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5c-75e71354-ab37-bf0a0f2138df'),('11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','2016-08-26 15:16:40','客户管理/成交/成交/成交信息表','2016-08-26 15:16:40','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:01','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-9544-1111a2cb-a2c5-53255716f388','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','80',NULL,'11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df',256,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-9544-1111a2cb-a2c5-53255716f388\",\"name\":\"蔡主管_财务主管\"}]}]}]','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b5c-a9ce2041-ab37-bf0a0f2138df'),('11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','2016-08-26 15:16:32','客户管理/成交/成交/成交信息表','2016-08-26 15:16:32','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:45','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-9544-1111a2cb-a2c5-53255716f388','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','80',NULL,'11e6-6b5c-f16eea8c-ab37-bf0a0f2138df',256,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-9544-1111a2cb-a2c5-53255716f388\",\"name\":\"蔡主管_财务主管\"}]}]}]','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b5c-c4354ef8-ab37-bf0a0f2138df'),('11e6-6b5c-cbb94a29-ab37-bf0a0f2138df','2016-08-26 15:16:02','客户管理/成交/成交/成交产品明细',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:58','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,'11e6-6b5c-c4354ef8-ab37-bf0a0f2138df',NULL,0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5c-cbb94a29-ab37-bf0a0f2138df'),('11e6-6b5d-235c97e6-ab37-bf0a0f2138df','2016-08-26 15:19:12','客户管理/回款管理/回款管理/回款信息表','2016-08-26 15:19:12','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:17:25','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-9544-1111a2cb-a2c5-53255716f388','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6b5d-3b0913c8-ab37-bf0a0f2138df',1048576,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6b5d-235c97e6-ab37-bf0a0f2138df'),('11e6-6b5d-be746b96-ab37-bf0a0f2138df','2016-08-26 15:22:24','客户管理/开票管理/开票管理/开票信息表','2016-08-26 15:22:23','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:21:45','11e6-3ce0-3cd2af43-9676-8f334e66899f','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已开票','11e3-9544-1111a2cb-a2c5-53255716f388','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6b5d-caaa084e-ab37-bf0a0f2138df',1048576,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467085956786\":[]}','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"flowName\":\"开票申请\",\"flowId\":\"11e6-3ce4-14f48d78-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已开票\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"未开票\"}]','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6b5d-be746b96-ab37-bf0a0f2138df'),('11e6-6b5d-e66d44e0-ab37-bf0a0f2138df','2016-08-26 15:24:20','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:22:52','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5d-e66d44e0-ab37-bf0a0f2138df'),('11e6-6b5e-28dba5c7-ab37-bf0a0f2138df','2016-08-26 15:26:02','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:24:43','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5e-28dba5c7-ab37-bf0a0f2138df'),('11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','2016-08-26 15:35:11','客户管理/成交/成交/成交信息表','2016-08-26 15:35:11','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:34:17','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b5f-987f6618-ab37-bf0a0f2138df',256,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df'),('11e6-6b5f-c162039b-ab37-bf0a0f2138df','2016-08-26 15:37:17','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:36:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5f-c162039b-ab37-bf0a0f2138df'),('11e6-6b5f-ed9097ec-ab37-bf0a0f2138df','2016-08-26 15:40:10','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:37:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b5f-ed9097ec-ab37-bf0a0f2138df'),('11e6-6b60-7aa45046-ab37-bf0a0f2138df','2016-08-26 15:41:35','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:41:20','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b60-7aa45046-ab37-bf0a0f2138df'),('11e6-6b60-8fd1ea81-ab37-bf0a0f2138df','2016-08-26 15:42:05','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:41:55','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b60-8fd1ea81-ab37-bf0a0f2138df'),('11e6-6b62-06125347-ab37-bf0a0f2138df','2016-08-26 15:53:28','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:52:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b62-06125347-ab37-bf0a0f2138df'),('11e6-6b62-580c0b88-ab37-bf0a0f2138df','2016-08-26 15:56:04','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:54:40','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b62-580c0b88-ab37-bf0a0f2138df'),('11e6-6b64-959c78f5-ab37-bf0a0f2138df','2016-08-26 18:42:09','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:10:43','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b64-959c78f5-ab37-bf0a0f2138df'),('11e6-6b64-cd84c1c6-ab37-bf0a0f2138df','2016-08-26 16:13:53','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:12:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b64-cd84c1c6-ab37-bf0a0f2138df'),('11e6-6b65-0d3625f7-ab37-bf0a0f2138df','2016-08-26 16:15:05','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:14:03','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b65-0d3625f7-ab37-bf0a0f2138df'),('11e6-6b65-423b4c8f-ab37-bf0a0f2138df','2016-08-26 16:16:48','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:15:32','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b65-423b4c8f-ab37-bf0a0f2138df'),('11e6-6b68-18540915-ab37-bf0a0f2138df','2016-08-26 17:19:13','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:35:51','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-18540915-ab37-bf0a0f2138df'),('11e6-6b68-a746e2ad-ab37-bf0a0f2138df','2016-08-26 16:40:31','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:39:50','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-a746e2ad-ab37-bf0a0f2138df'),('11e6-6b68-c1f5a1a0-ab37-bf0a0f2138df','2016-08-26 17:14:19','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:40:35','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-c1f5a1a0-ab37-bf0a0f2138df'),('11e6-6b68-cc87a0d5-ab37-bf0a0f2138df','2016-08-26 16:41:53','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:40:53','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-cc87a0d5-ab37-bf0a0f2138df'),('11e6-6b68-d980dae3-ab37-bf0a0f2138df','2016-08-26 16:41:51','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:41:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-d980dae3-ab37-bf0a0f2138df'),('11e6-6b68-f1b2db1a-ab37-bf0a0f2138df','2016-08-26 16:42:27','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:41:55','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-f1b2db1a-ab37-bf0a0f2138df'),('11e6-6b68-f3411c26-ab37-bf0a0f2138df','2016-08-30 15:27:57','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:41:58','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b68-f3411c26-ab37-bf0a0f2138df'),('11e6-6b69-13248f96-ab37-bf0a0f2138df','2016-08-26 16:43:40','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:42:51','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-13248f96-ab37-bf0a0f2138df'),('11e6-6b69-21f82fb6-ab37-bf0a0f2138df','2016-08-26 16:44:25','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:43:16','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-21f82fb6-ab37-bf0a0f2138df'),('11e6-6b69-35147546-ab37-bf0a0f2138df','2016-08-26 17:16:14','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:43:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-35147546-ab37-bf0a0f2138df'),('11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','2016-08-26 16:48:32','客户管理/成交/成交/成交信息表','2016-08-26 16:48:32','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:44:38','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b69-d8954d30-ab37-bf0a0f2138df',256,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b69-52ad5cbe-ab37-bf0a0f2138df'),('11e6-6b69-6a829698-ab37-bf0a0f2138df','2016-08-26 16:45:54','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:45:18','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-6a829698-ab37-bf0a0f2138df'),('11e6-6b69-8c321815-ab37-bf0a0f2138df','2016-08-26 16:47:10','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:46:14','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-8c321815-ab37-bf0a0f2138df'),('11e6-6b69-b0120df8-ab37-bf0a0f2138df','2016-08-26 16:47:57','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:47:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-b0120df8-ab37-bf0a0f2138df'),('11e6-6b69-cd71bc89-ab37-bf0a0f2138df','2016-08-26 16:48:46','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:48:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-cd71bc89-ab37-bf0a0f2138df'),('11e6-6b69-d2ad9c45-ab37-bf0a0f2138df','2016-08-26 16:48:24','客户管理/成交/成交/成交产品明细',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:48:13','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,'11e6-6b69-52ad5cbe-ab37-bf0a0f2138df',NULL,0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-d2ad9c45-ab37-bf0a0f2138df'),('11e6-6b69-eb012e5f-ab37-bf0a0f2138df','2016-08-26 17:16:35','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:48:53','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b69-eb012e5f-ab37-bf0a0f2138df'),('11e6-6b69-edcecd3b-ab37-bf0a0f2138df','2016-08-26 16:49:50','客户管理/成交/成交/成交信息表','2016-08-26 16:49:50','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:48:58','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b6a-00da716e-ab37-bf0a0f2138df',256,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b69-edcecd3b-ab37-bf0a0f2138df'),('11e6-6b6a-06225f8f-ab37-bf0a0f2138df','2016-08-26 16:50:17','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:49:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-06225f8f-ab37-bf0a0f2138df'),('11e6-6b6a-20af8d83-ab37-bf0a0f2138df','2016-08-26 16:50:55','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:50:24','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-20af8d83-ab37-bf0a0f2138df'),('11e6-6b6a-3710c576-ab37-bf0a0f2138df','2016-08-26 16:52:02','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:51:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-3710c576-ab37-bf0a0f2138df'),('11e6-6b6a-60bbbb30-ab37-bf0a0f2138df','2016-08-26 16:52:52','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:52:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-60bbbb30-ab37-bf0a0f2138df'),('11e6-6b6a-7c4ceb69-ab37-bf0a0f2138df','2016-08-26 16:53:35','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:52:57','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-7c4ceb69-ab37-bf0a0f2138df'),('11e6-6b6a-98d12f1a-ab37-bf0a0f2138df','2016-08-26 16:54:20','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:53:45','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-98d12f1a-ab37-bf0a0f2138df'),('11e6-6b6a-fa729eeb-ab37-bf0a0f2138df','2016-08-26 16:57:23','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:56:29','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6a-fa729eeb-ab37-bf0a0f2138df'),('11e6-6b6b-3a8cfe1c-ab37-bf0a0f2138df','2016-08-26 17:17:25','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:58:16','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6b-3a8cfe1c-ab37-bf0a0f2138df'),('11e6-6b6b-8ab0e439-ab37-bf0a0f2138df','2016-08-26 17:01:34','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:00:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6b-8ab0e439-ab37-bf0a0f2138df'),('11e6-6b6b-bf2e3da5-ab37-bf0a0f2138df','2016-08-26 17:03:14','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:01:59','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6b-bf2e3da5-ab37-bf0a0f2138df'),('11e6-6b6b-d5301717-ab37-bf0a0f2138df','2016-08-26 17:03:22','客户管理/机会管理/机会/机会信息表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:02:36','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6b-d5301717-ab37-bf0a0f2138df'),('11e6-6b6b-f152b294-ab37-bf0a0f2138df','2016-08-26 17:04:18','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:03:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6b-f152b294-ab37-bf0a0f2138df'),('11e6-6b6b-f5b59756-ab37-bf0a0f2138df','2016-08-26 17:09:17','客户管理/成交/成交/成交信息表','2016-08-26 17:09:17','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:03:30','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e4-7b56-045d6210-a888-6d6b162bf5de','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6b6c-0c906be2-ab37-bf0a0f2138df',256,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b6b-f5b59756-ab37-bf0a0f2138df'),('11e6-6b6c-09f01dd7-ab37-bf0a0f2138df','2016-08-26 17:04:10','客户管理/成交/成交/成交产品明细',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:04:04','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,'11e6-6b6b-f5b59756-ab37-bf0a0f2138df',NULL,0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6c-09f01dd7-ab37-bf0a0f2138df'),('11e6-6b6c-1729958e-ab37-bf0a0f2138df','2016-08-26 17:06:30','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:04:27','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6c-1729958e-ab37-bf0a0f2138df'),('11e6-6b6c-3ad053a0-ab37-bf0a0f2138df','2016-08-26 17:06:06','客户管理/机会管理/机会/机会信息表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:05:26','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6c-3ad053a0-ab37-bf0a0f2138df'),('11e6-6b6c-673a6981-ab37-bf0a0f2138df','2016-08-26 17:08:50','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:06:41','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6c-673a6981-ab37-bf0a0f2138df'),('11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','2016-08-26 17:09:08','客户管理/成交/成交/成交信息表','2016-08-26 17:09:08','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:06:50','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e4-7b56-045d6210-a888-6d6b162bf5de','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6b6c-7f92b66e-ab37-bf0a0f2138df',256,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'11e6-6b6c-6cb463ad-ab37-bf0a0f2138df'),('11e6-6b6c-ba94e9a1-ab37-bf0a0f2138df','2016-08-26 17:13:06','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:09:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6c-ba94e9a1-ab37-bf0a0f2138df'),('11e6-6b6d-521df1bb-ab37-bf0a0f2138df','2016-08-26 17:16:05','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:13:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6d-521df1bb-ab37-bf0a0f2138df'),('11e6-6b6d-c615e634-ab37-bf0a0f2138df','2016-08-26 17:28:04','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:16:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6d-c615e634-ab37-bf0a0f2138df'),('11e6-6b6e-544ebe9a-ab37-bf0a0f2138df','2016-08-26 17:20:44','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:20:28','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6e-544ebe9a-ab37-bf0a0f2138df'),('11e6-6b6e-8854c495-ab37-bf0a0f2138df','2016-08-26 17:23:39','客户管理/回款管理/回款管理/回款信息表','2016-08-26 17:23:39','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:21:55','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e4-7b56-045d6210-a888-6d6b162bf5de','11e3-9544-1111a2cb-a2c5-53255716f388','','80',NULL,'11e6-6b6e-a8193eac-ab37-bf0a0f2138df',1048576,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'11e6-6b6e-8854c495-ab37-bf0a0f2138df'),('11e6-6b6e-fdc7c10e-ab37-bf0a0f2138df','2016-08-26 17:27:00','客户管理/客户管理/客户管理/客户',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:25:12','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6e-fdc7c10e-ab37-bf0a0f2138df'),('11e6-6b6f-670066b9-ab37-bf0a0f2138df','2016-08-26 17:29:00','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:28:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6f-670066b9-ab37-bf0a0f2138df'),('11e6-6b6f-a6ce52f2-ab37-bf0a0f2138df','2016-08-26 17:30:40','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:29:56','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6f-a6ce52f2-ab37-bf0a0f2138df'),('11e6-6b6f-cd1efa32-ab37-bf0a0f2138df','2016-08-26 17:32:19','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:31:00','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6f-cd1efa32-ab37-bf0a0f2138df'),('11e6-6b6f-fe5761b7-ab37-bf0a0f2138df','2016-08-26 17:33:14','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:32:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b6f-fe5761b7-ab37-bf0a0f2138df'),('11e6-6b70-3efe6fa4-ab37-bf0a0f2138df','2016-08-26 17:50:38','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:34:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b70-3efe6fa4-ab37-bf0a0f2138df'),('11e6-6b72-9035892b-ab37-bf0a0f2138df','2016-08-26 17:51:40','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:50:47','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b72-9035892b-ab37-bf0a0f2138df'),('11e6-6b72-b4830e2b-ab37-bf0a0f2138df','2016-08-26 17:52:35','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:51:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b72-b4830e2b-ab37-bf0a0f2138df'),('11e6-6b72-d546a86b-ab37-bf0a0f2138df','2016-08-26 18:17:09','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:52:43','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b72-d546a86b-ab37-bf0a0f2138df'),('11e6-6b76-63b57d9a-ab37-bf0a0f2138df','2016-08-26 18:21:32','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:18:10','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b76-63b57d9a-ab37-bf0a0f2138df'),('11e6-6b76-e099619a-ab37-bf0a0f2138df','2016-08-26 18:22:23','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:21:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b76-e099619a-ab37-bf0a0f2138df'),('11e6-6b76-ff20c802-ab37-bf0a0f2138df','2016-08-26 18:23:10','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:22:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b76-ff20c802-ab37-bf0a0f2138df'),('11e6-6b77-1b5405de-ab37-bf0a0f2138df','2016-08-26 18:24:10','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:23:18','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-1b5405de-ab37-bf0a0f2138df'),('11e6-6b77-3e3e5036-ab37-bf0a0f2138df','2016-08-26 18:24:53','客户管理/客户管理/客户管理/客户',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:24:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-3e3e5036-ab37-bf0a0f2138df'),('11e6-6b77-66d23051-ab37-bf0a0f2138df','2016-08-26 18:27:43','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:25:25','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-66d23051-ab37-bf0a0f2138df'),('11e6-6b77-bebf58b7-ab37-bf0a0f2138df','2016-08-26 18:28:30','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:27:52','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-bebf58b7-ab37-bf0a0f2138df'),('11e6-6b77-dabbcded-ab37-bf0a0f2138df','2016-08-26 18:29:17','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:28:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-dabbcded-ab37-bf0a0f2138df'),('11e6-6b77-e63ebc4a-ab37-bf0a0f2138df','2016-08-26 18:29:55','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:28:58','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-e63ebc4a-ab37-bf0a0f2138df'),('11e6-6b77-f41f221e-ab37-bf0a0f2138df','2016-08-26 18:30:00','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:29:22','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b77-f41f221e-ab37-bf0a0f2138df'),('11e6-6b78-0af9f71f-ab37-bf0a0f2138df','2016-08-26 18:30:51','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-0af9f71f-ab37-bf0a0f2138df'),('11e6-6b78-0e5dbabe-ab37-bf0a0f2138df','2016-08-26 18:30:47','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:06','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-0e5dbabe-ab37-bf0a0f2138df'),('11e6-6b78-2aeb9b37-ab37-bf0a0f2138df','2016-08-26 18:31:59','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-2aeb9b37-ab37-bf0a0f2138df'),('11e6-6b78-56dc3b83-ab37-bf0a0f2138df','2016-08-26 18:32:50','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:32:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-56dc3b83-ab37-bf0a0f2138df'),('11e6-6b78-76fda242-ab37-bf0a0f2138df','2016-08-26 18:33:47','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:33:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-76fda242-ab37-bf0a0f2138df'),('11e6-6b78-9a49bc91-ab37-bf0a0f2138df','2016-08-26 18:34:46','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:34:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-9a49bc91-ab37-bf0a0f2138df'),('11e6-6b78-bccfef40-ab37-bf0a0f2138df','2016-08-26 18:35:45','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:34:58','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-bccfef40-ab37-bf0a0f2138df'),('11e6-6b78-dd1ea771-ab37-bf0a0f2138df','2016-08-26 18:36:54','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:35:53','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-dd1ea771-ab37-bf0a0f2138df'),('11e6-6b78-fa9d779e-ab37-bf0a0f2138df','2016-08-26 18:55:48','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:36:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b78-fa9d779e-ab37-bf0a0f2138df'),('11e6-6b79-044f4eff-ab37-bf0a0f2138df','2016-08-26 18:37:51','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:36:58','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-044f4eff-ab37-bf0a0f2138df'),('11e6-6b79-268ba6cb-ab37-bf0a0f2138df','2016-08-26 18:38:37','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:37:56','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-268ba6cb-ab37-bf0a0f2138df'),('11e6-6b79-434b940c-ab37-bf0a0f2138df','2016-08-26 18:39:29','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:38:44','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-434b940c-ab37-bf0a0f2138df'),('11e6-6b79-73d39623-ab37-bf0a0f2138df','2016-08-26 18:40:38','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:40:05','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-73d39623-ab37-bf0a0f2138df'),('11e6-6b79-89d0b4c5-ab37-bf0a0f2138df','2016-08-26 18:41:32','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:40:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-89d0b4c5-ab37-bf0a0f2138df'),('11e6-6b79-a9f94856-ab37-bf0a0f2138df','2016-08-26 18:42:49','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:41:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-a9f94856-ab37-bf0a0f2138df'),('11e6-6b79-d887c518-ab37-bf0a0f2138df','2016-08-26 18:43:38','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:42:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-d887c518-ab37-bf0a0f2138df'),('11e6-6b79-f4fdd6f9-ab37-bf0a0f2138df','2016-08-26 18:44:21','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:43:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b79-f4fdd6f9-ab37-bf0a0f2138df'),('11e6-6b7a-1146984a-ab37-bf0a0f2138df','2016-08-26 18:45:41','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:44:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7a-1146984a-ab37-bf0a0f2138df'),('11e6-6b7a-3f4fb894-ab37-bf0a0f2138df','2016-08-26 18:49:39','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:45:47','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7a-3f4fb894-ab37-bf0a0f2138df'),('11e6-6b7a-cf5253a5-ab37-bf0a0f2138df','2016-08-26 18:50:48','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:49:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7a-cf5253a5-ab37-bf0a0f2138df'),('11e6-6b7a-fe55b2e9-ab37-bf0a0f2138df','2016-08-26 18:51:59','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:51:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7a-fe55b2e9-ab37-bf0a0f2138df'),('11e6-6b7b-24100d60-ab37-bf0a0f2138df','2016-08-26 18:55:05','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:52:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-24100d60-ab37-bf0a0f2138df'),('11e6-6b7b-5ddc5993-ab37-bf0a0f2138df','2016-08-26 18:55:23','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:53:48','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-5ddc5993-ab37-bf0a0f2138df'),('11e6-6b7b-909482f8-ab37-bf0a0f2138df','2016-08-26 18:56:57','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:55:13','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-909482f8-ab37-bf0a0f2138df'),('11e6-6b7b-c3b81854-ab37-bf0a0f2138df','2016-08-26 18:58:09','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:56:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-c3b81854-ab37-bf0a0f2138df'),('11e6-6b7b-d26802d0-ab37-bf0a0f2138df','2016-08-26 18:57:22','客户管理/跟进记录/跟进记录/跟进记录表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:03','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-d26802d0-ab37-bf0a0f2138df'),('11e6-6b7b-d476e598-ab37-bf0a0f2138df','2016-08-26 18:57:46','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-d476e598-ab37-bf0a0f2138df'),('11e6-6b7b-e26a5e4a-ab37-bf0a0f2138df','2016-08-26 18:58:30','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:30','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-e26a5e4a-ab37-bf0a0f2138df'),('11e6-6b7b-fb351c28-ab37-bf0a0f2138df','2016-08-26 18:59:20','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:12','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-fb351c28-ab37-bf0a0f2138df'),('11e6-6b7b-fbd26d6a-ab37-bf0a0f2138df','2016-08-26 18:59:59','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:13','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7b-fbd26d6a-ab37-bf0a0f2138df'),('11e6-6b7c-0f745e9c-ab37-bf0a0f2138df','2016-08-26 18:59:34','客户管理/机会管理/机会/机会信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:46','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-0f745e9c-ab37-bf0a0f2138df'),('11e6-6b7c-28b166b5-ab37-bf0a0f2138df','2016-08-26 19:00:13','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:59:28','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-28b166b5-ab37-bf0a0f2138df'),('11e6-6b7c-3e15ede6-ab37-bf0a0f2138df','2016-08-26 19:00:46','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-3e15ede6-ab37-bf0a0f2138df'),('11e6-6b7c-47dfbac0-ab37-bf0a0f2138df','2016-08-26 19:01:21','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:20','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-47dfbac0-ab37-bf0a0f2138df'),('11e6-6b7c-5bbd2d94-ab37-bf0a0f2138df','2016-08-26 19:01:55','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-5bbd2d94-ab37-bf0a0f2138df'),('11e6-6b7c-73f6801e-ab37-bf0a0f2138df','2016-08-26 19:02:57','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:01:34','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-73f6801e-ab37-bf0a0f2138df'),('11e6-6b7c-be6c16d5-ab37-bf0a0f2138df','2016-08-26 19:04:17','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:03:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-be6c16d5-ab37-bf0a0f2138df'),('11e6-6b7c-dc181185-ab37-bf0a0f2138df','2016-08-26 19:05:05','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:04:29','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-dc181185-ab37-bf0a0f2138df'),('11e6-6b7c-f71c91e5-ab37-bf0a0f2138df','2016-08-26 19:05:54','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:05:14','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7c-f71c91e5-ab37-bf0a0f2138df'),('11e6-6b7d-12e48955-ab37-bf0a0f2138df','2016-08-26 19:06:40','客户管理/客户管理/客户管理/客户',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:06:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-12e48955-ab37-bf0a0f2138df'),('11e6-6b7d-36010892-ab37-bf0a0f2138df','2016-08-26 19:09:12','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:07:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-36010892-ab37-bf0a0f2138df'),('11e6-6b7d-878a7214-ab37-bf0a0f2138df','2016-08-26 19:09:50','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:09:17','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-878a7214-ab37-bf0a0f2138df'),('11e6-6b7d-9e0aa2fb-ab37-bf0a0f2138df','2016-08-26 19:10:17','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:09:54','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-9e0aa2fb-ab37-bf0a0f2138df'),('11e6-6b7d-ae48a8a2-ab37-bf0a0f2138df','2016-08-26 19:10:39','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:10:22','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-ae48a8a2-ab37-bf0a0f2138df'),('11e6-6b7d-bb680871-ab37-bf0a0f2138df','2016-08-26 19:11:01','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:10:44','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-bb680871-ab37-bf0a0f2138df'),('11e6-6b7d-c8791040-ab37-bf0a0f2138df','2016-08-26 19:11:23','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:11:05','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-c8791040-ab37-bf0a0f2138df'),('11e6-6b7d-d582ec9f-ab37-bf0a0f2138df','2016-08-26 19:11:55','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:11:27','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-d582ec9f-ab37-bf0a0f2138df'),('11e6-6b7d-e8e6c2fe-ab37-bf0a0f2138df','2016-08-26 19:12:25','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-e8e6c2fe-ab37-bf0a0f2138df'),('11e6-6b7d-fa32401e-ab37-bf0a0f2138df','2016-08-26 19:12:50','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:29','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7d-fa32401e-ab37-bf0a0f2138df'),('11e6-6b7e-09974a8d-ab37-bf0a0f2138df','2016-08-26 19:13:29','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:55','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-09974a8d-ab37-bf0a0f2138df'),('11e6-6b7e-2603c067-ab37-bf0a0f2138df','2016-08-26 19:14:03','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:13:42','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-2603c067-ab37-bf0a0f2138df'),('11e6-6b7e-3c94bab1-ab37-bf0a0f2138df','2016-08-26 19:15:15','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:14:20','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-3c94bab1-ab37-bf0a0f2138df'),('11e6-6b7e-604c19b9-ab37-bf0a0f2138df','2016-08-26 19:15:43','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:15:20','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-604c19b9-ab37-bf0a0f2138df'),('11e6-6b7e-715e3717-ab37-bf0a0f2138df','2016-08-26 19:16:10','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:15:49','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-715e3717-ab37-bf0a0f2138df'),('11e6-6b7e-80ebd852-ab37-bf0a0f2138df','2016-08-26 19:16:40','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:16:15','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-80ebd852-ab37-bf0a0f2138df'),('11e6-6b7e-95324633-ab37-bf0a0f2138df','2016-08-26 19:17:13','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:16:49','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-95324633-ab37-bf0a0f2138df'),('11e6-6b7e-a689838e-ab37-bf0a0f2138df','2016-08-26 19:17:36','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:17:18','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-a689838e-ab37-bf0a0f2138df'),('11e6-6b7e-b5a96eb9-ab37-bf0a0f2138df','2016-08-26 19:18:03','客户管理/联系人/联系人/联系人信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:17:43','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6b7e-b5a96eb9-ab37-bf0a0f2138df'),('11e6-6e82-537810da-ab37-bf0a0f2138df','2016-08-30 15:22:20','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:21:10','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6e82-537810da-ab37-bf0a0f2138df'),('11e6-6e82-7fe226c6-ab37-bf0a0f2138df','2016-08-30 15:25:47','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:22:25','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6e82-7fe226c6-ab37-bf0a0f2138df'),('11e6-6e83-0645fd9e-ab37-bf0a0f2138df','2016-08-30 15:26:56','客户管理/机会管理/机会/机会信息表',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:26:10','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,NULL,NULL,'',NULL,NULL,'',0,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'11e6-6e83-0645fd9e-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `t_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_flow_intervention`
--

DROP TABLE IF EXISTS `t_flow_intervention`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_flow_intervention` (
  `ID` varchar(200) NOT NULL,
  `SUMMARY` longtext,
  `FLOWNAME` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `INITIATOR` varchar(200) DEFAULT NULL,
  `INITIATORID` varchar(200) DEFAULT NULL,
  `LASTAUDITOR` varchar(200) DEFAULT NULL,
  `FIRSTPROCESSTIME` datetime DEFAULT NULL,
  `LASTPROCESSTIME` datetime DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `DOCID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  `STATUS` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `AUDITORLIST` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `INITIATOR_DEPT` varchar(200) DEFAULT NULL,
  `INITIATOR_DEPT_ID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_flow_intervention`
--

LOCK TABLES `t_flow_intervention` WRITE;
/*!40000 ALTER TABLE `t_flow_intervention` DISABLE KEYS */;
INSERT INTO `t_flow_intervention` VALUES ('11e6-6532-7c859c55-abfb-7f3dc8737aff','','成交确认','已确认','邢主管（行政主管）','11e4-7b56-045d6210-a888-6d6b162bf5de','钟总经理（总经理）','2016-08-18 18:57:57','2016-08-18 18:59:52','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6532-7c859c55-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','行政部','11e3-866b-820a6aa1-81ef-b131c495402b'),('11e6-6532-a68b0f0a-abfb-7f3dc8737aff','','回款确认','已确认','邢主管（行政主管）','11e4-7b56-045d6210-a888-6d6b162bf5de','蔡主管_财务主管','2016-08-18 18:59:17','2016-08-18 19:01:02','11e6-3cdf-e9547b0d-9676-8f334e66899f','11e6-3cdb-2581d9ba-9676-8f334e66899f','11e6-6532-a68b0f0a-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467084174418\":[]}','80','行政部','11e3-866b-820a6aa1-81ef-b131c495402b'),('11e6-6533-c78aab27-abfb-7f3dc8737aff','','成交确认','已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','2016-08-18 19:07:16','2016-08-18 19:08:20','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6533-c78aab27-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6533-f095b82c-abfb-7f3dc8737aff','','回款确认','已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','2016-08-18 19:07:54','2016-08-18 19:09:49','11e6-3cdf-e9547b0d-9676-8f334e66899f','11e6-3cdb-2581d9ba-9676-8f334e66899f','11e6-6533-f095b82c-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467084174418\":[]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','','成交确认','创建','系统管理员','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','admin','2016-08-23 11:48:47','2016-08-23 14:30:47','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','系统管理员','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','86','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','系统管理员  客户名称:2343  成交金额:2元','成交确认','创建','系统管理员','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','钟总经理（总经理）','2016-08-23 14:29:22','2016-08-24 10:51:28','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','系统管理员','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','81','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6a8c-4f906f24-82ae-bbe47783a3bc','钟总经理（总经理）  客户名称:网通广告  开票金额:5万元','开票申请','已开票','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','2016-08-25 14:23:00','2016-08-26 11:21:45','11e6-3ce4-14f48d78-9676-8f334e66899f','11e6-3ce0-3cd2af43-9676-8f334e66899f','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467085956786\":[]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  成交金额:12万元','成交确认','已确认','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','钟总经理（总经理）','2016-08-26 15:14:39','2016-08-26 15:16:40','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','蔡主管_财务主管','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','80','财务部','11e1-81e2-afbbfc08-9124-47aada6b7467'),('11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  成交金额:34万元','成交确认','已确认','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','钟总经理（总经理）','2016-08-26 15:16:02','2016-08-26 15:16:32','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','蔡主管_财务主管','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','80','财务部','11e1-81e2-afbbfc08-9124-47aada6b7467'),('11e6-6b5d-235c97e6-ab37-bf0a0f2138df','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  回款金额:3万元','回款确认','已确认','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管','2016-08-26 15:18:06','2016-08-26 15:19:12','11e6-3cdf-e9547b0d-9676-8f334e66899f','11e6-3cdb-2581d9ba-9676-8f334e66899f','11e6-6b5d-235c97e6-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467084174418\":[]}','80','财务部','11e1-81e2-afbbfc08-9124-47aada6b7467'),('11e6-6b5d-be746b96-ab37-bf0a0f2138df','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  开票金额:12万元','开票申请','已开票','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','蔡主管_财务主管','2016-08-26 15:22:07','2016-08-26 15:22:24','11e6-3ce4-14f48d78-9676-8f334e66899f','11e6-3ce0-3cd2af43-9676-8f334e66899f','11e6-6b5d-be746b96-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467085956786\":[]}','80','财务部','11e1-81e2-afbbfc08-9124-47aada6b7467'),('11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','钟总经理（总经理）  客户名称:网通广告  成交金额:12万元','成交确认','已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','2016-08-26 15:35:02','2016-08-26 15:35:11','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','钟总经理（总经理）  客户名称:上海万豪贸易有限公司  成交金额:23万元','成交确认','已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','2016-08-26 16:48:24','2016-08-26 16:48:32','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6b69-edcecd3b-ab37-bf0a0f2138df','钟总经理（总经理）  客户名称:上海四季酒店有限公司  成交金额:15万元','成交确认','已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','2016-08-26 16:49:31','2016-08-26 16:49:50','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','微OA365','11e1-81e2-37fe734a-9124-47aada6b7467'),('11e6-6b6b-f5b59756-ab37-bf0a0f2138df','邢主管（行政主管）  客户名称:江苏里布科技有限公司  成交金额:2万元','成交确认','已确认','邢主管（行政主管）','11e4-7b56-045d6210-a888-6d6b162bf5de','钟总经理（总经理）','2016-08-26 17:04:10','2016-08-26 17:09:17','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','行政部','11e3-866b-820a6aa1-81ef-b131c495402b'),('11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','邢主管（行政主管）  客户名称:江苏里布科技有限公司  成交金额:12万元','成交确认','已确认','邢主管（行政主管）','11e4-7b56-045d6210-a888-6d6b162bf5de','钟总经理（总经理）','2016-08-26 17:07:23','2016-08-26 17:09:08','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'pending','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','行政部','11e3-866b-820a6aa1-81ef-b131c495402b'),('11e6-6b6e-8854c495-ab37-bf0a0f2138df','邢主管（行政主管）  客户名称:江苏里布科技有限公司  回款金额:11万元','回款确认','已确认','邢主管（行政主管）','11e4-7b56-045d6210-a888-6d6b162bf5de','蔡主管_财务主管','2016-08-26 17:22:50','2016-08-26 17:23:39','11e6-3cdf-e9547b0d-9676-8f334e66899f','11e6-3cdb-2581d9ba-9676-8f334e66899f','11e6-6b6e-8854c495-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96','11e1-81e2-37f74759-9124-47aada6b7467',0,'completed','','{\"1467084174418\":[]}','80','行政部','11e3-866b-820a6aa1-81ef-b131c495402b');
/*!40000 ALTER TABLE `t_flow_intervention` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_flow_proxy`
--

DROP TABLE IF EXISTS `t_flow_proxy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_flow_proxy` (
  `ID` varchar(200) NOT NULL,
  `FLOWNAME` varchar(200) DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AGENTS` longtext,
  `AGENTSNAME` longtext,
  `OWNER` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_flow_proxy`
--

LOCK TABLES `t_flow_proxy` WRITE;
/*!40000 ALTER TABLE `t_flow_proxy` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_flow_proxy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_flowstatert`
--

DROP TABLE IF EXISTS `t_flowstatert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_flowstatert` (
  `ID` varchar(200) NOT NULL,
  `DOCID` varchar(200) DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `STATE` int(11) DEFAULT NULL,
  `PARENT` varchar(200) DEFAULT NULL,
  `FLOWNAME` varchar(200) DEFAULT NULL,
  `FLOWXML` longtext,
  `LASTMODIFIERID` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `SUBFLOWNODEID` varchar(200) DEFAULT NULL,
  `COMPLETE` int(11) DEFAULT NULL,
  `CALLBACK` int(11) DEFAULT NULL,
  `TOKEN` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `INITIATOR` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `AUDITORLIST` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `SUB_POSITION` int(11) DEFAULT NULL,
  `ISARCHIVED` int(11) DEFAULT NULL,
  `ISTERMINATED` bit(1) DEFAULT NULL,
  `PREV_AUDIT_NODE` varchar(200) DEFAULT NULL,
  `PREV_AUDIT_USER` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_flowstatert`
--

LOCK TABLES `t_flowstatert` WRITE;
/*!40000 ALTER TABLE `t_flowstatert` DISABLE KEYS */;
INSERT INTO `t_flowstatert` VALUES ('11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-18 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','邢主管（行政主管）','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','2016-08-18 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e6-6532-a68b0f0a-abfb-7f3dc8737aff','11e6-3cdf-e9547b0d-9676-8f334e66899f',1048576,NULL,'回款确认',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-18 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已确认','邢主管（行政主管）','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467084174418\":[]}','80','2016-08-18 00:00:00',0,0,'\0','待确认','蔡主管_财务主管'),('11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-18 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','2016-08-18 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6534-0210f88f-abfb-7f3dc8737aff','11e6-6533-f095b82c-abfb-7f3dc8737aff','11e6-3cdf-e9547b0d-9676-8f334e66899f',1048576,NULL,'回款确认',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-18 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已确认','钟总经理（总经理）','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467084174418\":[]}','80','2016-08-18 00:00:00',0,0,'\0','待确认','蔡主管_财务主管'),('11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-23 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'创建','系统管理员','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','系统管理员','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','86','2016-08-23 00:00:00',0,0,'\0','已确认','admin'),('11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-23 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'创建','系统管理员','11e3-8a58-82144b41-9194-1d682b48d529','系统管理员','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','81','2016-08-24 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','11e6-3ce4-14f48d78-9676-8f334e66899f',1048576,NULL,'开票申请',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-25 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已开票','钟总经理（总经理）','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467085956786\":[]}','80','2016-08-26 00:00:00',0,0,'\0','未开票','蔡主管_财务主管'),('11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','蔡主管_财务主管','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','蔡主管_财务主管','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e6-6b5d-235c97e6-ab37-bf0a0f2138df','11e6-3cdf-e9547b0d-9676-8f334e66899f',1048576,NULL,'回款确认',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已确认','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467084174418\":[]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','蔡主管_财务主管'),('11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e6-6b5d-be746b96-ab37-bf0a0f2138df','11e6-3ce4-14f48d78-9676-8f334e66899f',1048576,NULL,'开票申请',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已开票','蔡主管_财务主管','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467085956786\":[]}','80','2016-08-26 00:00:00',0,0,'\0','未开票','蔡主管_财务主管'),('11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','钟总经理（总经理）','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','邢主管（行政主管）','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',256,NULL,'成交确认',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,0,0,NULL,'已确认','邢主管（行政主管）','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','钟总经理（总经理）'),('11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e6-6b6e-8854c495-ab37-bf0a0f2138df','11e6-3cdf-e9547b0d-9676-8f334e66899f',1048576,NULL,'回款确认',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 00:00:00','11e6-429d-dd7a3284-86a2-074015f7cc96',NULL,1,0,NULL,'已确认','邢主管（行政主管）','11e3-9544-1111a2cb-a2c5-53255716f388','','{\"1467084174418\":[]}','80','2016-08-26 00:00:00',0,0,'\0','待确认','蔡主管_财务主管');
/*!40000 ALTER TABLE `t_flowstatert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_nodert`
--

DROP TABLE IF EXISTS `t_nodert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nodert` (
  `ID` varchar(200) NOT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `NODEID` varchar(200) DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `DOCID` varchar(200) DEFAULT NULL,
  `FLOWSTATERT_ID` varchar(200) DEFAULT NULL,
  `NOTIFIABLE` bit(1) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `FLOWOPTION` varchar(200) DEFAULT NULL,
  `SPLITTOKEN` varchar(200) DEFAULT NULL,
  `PASSCONDITION` int(11) DEFAULT NULL,
  `PARENTNODERTID` varchar(200) DEFAULT NULL,
  `DEADLINE` datetime DEFAULT NULL,
  `ORDERLY` bit(1) DEFAULT NULL,
  `APPROVAL_POSITION` int(11) DEFAULT NULL,
  `STATE` int(11) DEFAULT NULL,
  `LASTPROCESSTIME` datetime DEFAULT NULL,
  `REMINDER_TIMES` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_nodert`
--

LOCK TABLES `t_nodert` WRITE;
/*!40000 ALTER TABLE `t_nodert` DISABLE KEYS */;
INSERT INTO `t_nodert` VALUES ('11e6-6532-e39769eb-abfb-7f3dc8737aff','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6532-7c859c55-abfb-7f3dc8737aff','11e6-6532-9d9c872d-abfb-7f3dc8737aff','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6534-12883628-abfb-7f3dc8737aff','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6533-c78aab27-abfb-7f3dc8737aff','11e6-6533-eae03b81-abfb-7f3dc8737aff','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-68fb-209def26-abfb-7f3dc8737aff','创建','1467014814628','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','11e6-68e4-7e757d56-abfb-7f3dc8737aff','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','创建','86','',0,'1467196460566',NULL,'\0',0,0,NULL,0),('11e6-69a5-a74f6f8d-82ae-bbe47783a3bc','创建','1467014814628','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','创建','81','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b5d-03cee4ed-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b5d-08db2929-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b5f-9f20f12c-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','11e6-6b5f-987f6618-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b69-ddedb556-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','11e6-6b69-d8954d30-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b6a-0c6e0133-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','11e6-6b6a-00da716e-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b6c-bedd8f55-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0),('11e6-6b6c-c43fbcc1-ab37-bf0a0f2138df','已确认','1467196460566','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','11e6-6b6c-0c906be2-ab37-bf0a0f2138df','\0','11e1-81e2-37f74759-9124-47aada6b7467','11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','80','',0,'1467014834079',NULL,'\0',0,0,NULL,0);
/*!40000 ALTER TABLE `t_nodert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pending`
--

DROP TABLE IF EXISTS `t_pending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_pending` (
  `ID` varchar(200) NOT NULL,
  `DOCID` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `ISSUBDOC` bit(1) DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `CHILDS` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `PARENT` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `SUMMARY` longtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pending`
--

LOCK TABLES `t_pending` WRITE;
/*!40000 ALTER TABLE `t_pending` DISABLE KEYS */;
INSERT INTO `t_pending` VALUES ('11e6-6532-e3bfd930-abfb-7f3dc8737aff','11e6-6532-7c859c55-abfb-7f3dc8737aff','2016-08-18 18:59:52','客户管理/成交/成交/成交信息表','2016-08-18 18:59:52','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-18 18:56:59',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467',''),('11e6-6534-12abea99-abfb-7f3dc8737aff','11e6-6533-c78aab27-abfb-7f3dc8737aff','2016-08-18 19:08:20','客户管理/成交/成交/成交信息表','2016-08-18 19:08:20','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-18 19:06:14',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6533-eae03b81-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467',''),('11e6-68fb-20b82e2f-abfb-7f3dc8737aff','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','2016-08-23 14:30:47','客户管理/成交/成交/成交信息表','2016-08-23 14:30:47','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-23 11:48:15',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','创建','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','系统管理员','86',NULL,'11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467',''),('11e6-69a5-a770da86-82ae-bbe47783a3bc','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','2016-08-24 10:51:28','客户管理/成交/成交/成交信息表','2016-08-24 10:51:27','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-23 14:28:55',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','创建','11e3-8a58-82144b41-9194-1d682b48d529','系统管理员','81',NULL,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','系统管理员  客户名称:2343  成交金额:2元'),('11e6-6b5d-03e92400-ab37-bf0a0f2138df','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','2016-08-26 15:16:32','客户管理/成交/成交/成交信息表','2016-08-26 15:16:32','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 15:14:45',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','80',NULL,'11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  成交金额:34万元'),('11e6-6b5d-08f56832-ab37-bf0a0f2138df','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','2016-08-26 15:16:40','客户管理/成交/成交/成交信息表','2016-08-26 15:16:40','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 15:14:01',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','蔡主管_财务主管','80',NULL,'11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','蔡主管_财务主管  客户名称:北京华软信息科技有限公司  成交金额:12万元'),('11e6-6b5f-9f3b2fa1-ab37-bf0a0f2138df','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','2016-08-26 15:35:11','客户管理/成交/成交/成交信息表','2016-08-26 15:35:11','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 15:34:17',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','钟总经理（总经理）  客户名称:网通广告  成交金额:12万元'),('11e6-6b69-de07f469-ab37-bf0a0f2138df','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','2016-08-26 16:48:32','客户管理/成交/成交/成交信息表','2016-08-26 16:48:32','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 16:44:38',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','钟总经理（总经理）  客户名称:上海万豪贸易有限公司  成交金额:23万元'),('11e6-6b6a-0c85cec4-ab37-bf0a0f2138df','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','2016-08-26 16:49:50','客户管理/成交/成交/成交信息表','2016-08-26 16:49:50','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 16:48:58',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','钟总经理（总经理）','80',NULL,'11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','钟总经理（总经理）  客户名称:上海四季酒店有限公司  成交金额:15万元'),('11e6-6b6c-bef7cee6-ab37-bf0a0f2138df','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','2016-08-26 17:09:08','客户管理/成交/成交/成交信息表','2016-08-26 17:09:08','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 17:06:50',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','邢主管（行政主管）  客户名称:江苏里布科技有限公司  成交金额:12万元'),('11e6-6b6c-c45c44c4-ab37-bf0a0f2138df','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','2016-08-26 17:09:17','客户管理/成交/成交/成交信息表','2016-08-26 17:09:17','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 17:03:30',NULL,'11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde',NULL,NULL,'11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde',NULL,'11e6-429d-dd7a3284-86a2-074015f7cc96','已确认','11e3-8a58-82144b41-9194-1d682b48d529','邢主管（行政主管）','80',NULL,'11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','邢主管（行政主管）  客户名称:江苏里布科技有限公司  成交金额:2万元');
/*!40000 ALTER TABLE `t_pending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pending_actor_set`
--

DROP TABLE IF EXISTS `t_pending_actor_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_pending_actor_set` (
  `DOCID` varchar(200) DEFAULT NULL,
  `ACTORID` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pending_actor_set`
--

LOCK TABLES `t_pending_actor_set` WRITE;
/*!40000 ALTER TABLE `t_pending_actor_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_pending_actor_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_relationhis`
--

DROP TABLE IF EXISTS `t_relationhis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_relationhis` (
  `ID` varchar(200) NOT NULL,
  `ACTIONTIME` datetime DEFAULT NULL,
  `PROCESSTIME` datetime DEFAULT NULL,
  `STARTNODENAME` varchar(200) DEFAULT NULL,
  `FLOWID` varchar(200) DEFAULT NULL,
  `FLOWNAME` varchar(200) DEFAULT NULL,
  `DOCID` varchar(200) DEFAULT NULL,
  `ENDNODEID` varchar(200) DEFAULT NULL,
  `ENDNODENAME` varchar(200) DEFAULT NULL,
  `STARTNODEID` varchar(200) DEFAULT NULL,
  `ISPASSED` bit(1) DEFAULT NULL,
  `ATTITUDE` varchar(2000) DEFAULT NULL,
  `AUDITOR` varchar(200) DEFAULT NULL,
  `FLOWOPERATION` varchar(200) DEFAULT NULL,
  `REMINDERCOUNT` int(11) DEFAULT NULL,
  `FLOWSTATERT_ID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_relationhis`
--

LOCK TABLES `t_relationhis` WRITE;
/*!40000 ALTER TABLE `t_relationhis` DISABLE KEYS */;
INSERT INTO `t_relationhis` VALUES ('11e6-6532-9f38f924-abfb-7f3dc8737aff','2016-08-18 18:57:57','2016-08-18 18:59:52','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6532-7c859c55-abfb-7f3dc8737aff','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6532-cede8b7d-abfb-7f3dc8737aff','2016-08-18 18:59:17','2016-08-18 19:01:02','新建','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6532-a68b0f0a-abfb-7f3dc8737aff','1467084168730','待确认','1467084164315','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6532-e39c2549-abfb-7f3dc8737aff','2016-08-18 18:59:52','2016-08-18 18:59:52','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6532-7c859c55-abfb-7f3dc8737aff','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6533-0d84e7ff-abfb-7f3dc8737aff','2016-08-18 19:01:02','2016-08-18 19:01:02','待确认','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6532-a68b0f0a-abfb-7f3dc8737aff','1467084174418','已确认','1467084168730','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6533-ec306238-abfb-7f3dc8737aff','2016-08-18 19:07:16','2016-08-18 19:08:20','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6533-c78aab27-abfb-7f3dc8737aff','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6534-03067c27-abfb-7f3dc8737aff','2016-08-18 19:07:54','2016-08-18 19:09:48','新建','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6533-f095b82c-abfb-7f3dc8737aff','1467084168730','待确认','1467084164315','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6534-0210f88f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6534-128cf11a-abfb-7f3dc8737aff','2016-08-18 19:08:20','2016-08-18 19:08:20','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6533-c78aab27-abfb-7f3dc8737aff','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6533-eae03b81-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6534-4732b8ff-abfb-7f3dc8737aff','2016-08-18 19:09:48','2016-08-18 19:09:48','待确认','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6533-f095b82c-abfb-7f3dc8737aff','1467084174418','已确认','1467084168730','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6534-0210f88f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68e4-7f57ee1d-abfb-7f3dc8737aff','2016-08-23 11:48:47','2016-08-23 14:15:31','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68f8-fed579b7-abfb-7f3dc8737aff','2016-08-23 14:15:31','2016-08-23 14:30:47','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','1467196460566','已确认','1467014834079','\0','','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','80',0,'11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68fa-ee1a1c66-abfb-7f3dc8737aff','2016-08-23 14:29:22','2016-08-23 14:29:45','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68fa-fba24ff3-abfb-7f3dc8737aff','2016-08-23 14:29:45','2016-08-23 14:30:54','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','1467196460566','已确认','1467014834079','\0','','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','80',0,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68fb-20a06028-abfb-7f3dc8737aff','2016-08-23 14:30:47','2016-08-23 14:30:47','已确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff','1467014814628','创建','1467196460566','\0','','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','86',0,'11e6-68e4-7e757d56-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-68fb-249a70fa-abfb-7f3dc8737aff','2016-08-23 14:30:54','2016-08-24 10:50:51','已确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','1467014814628','创建','1467196460566','\0','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','86',0,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-69a5-9192b4d2-82ae-bbe47783a3bc','2016-08-24 10:50:51','2016-08-24 10:51:27','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-69a5-a751e08f-82ae-bbe47783a3bc','2016-08-24 10:51:27','2016-08-24 10:51:28','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff','1467014814628','创建','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','81',0,'11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6a8c-5f632eaf-82ae-bbe47783a3bc','2016-08-25 14:23:00','2016-08-26 10:31:25','新建','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','1467085954184','财务审核','1467085952322','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b35-2f2f8210-82ae-bbe47783a3bc','2016-08-26 10:31:25','2016-08-26 10:33:17','财务审核','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','1467085952322','新建','1467085954184','\0','','11e3-8a58-82144b41-9194-1d682b48d529','81',0,'11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b35-725328b9-82ae-bbe47783a3bc','2016-08-26 10:33:17','2016-08-26 11:21:45','新建','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','1467085954184','财务审核','1467085952322','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b3c-379c044e-bbbd-ef6eef62debe','2016-08-26 11:21:45','2016-08-26 11:21:45','财务审核','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','1467085956786','已开票','1467085954184','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5c-c0be788a-ab37-bf0a0f2138df','2016-08-26 15:14:39','2016-08-26 15:16:40','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5c-f26b99d1-ab37-bf0a0f2138df','2016-08-26 15:16:02','2016-08-26 15:16:32','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-03d15695-ab37-bf0a0f2138df','2016-08-26 15:16:32','2016-08-26 15:16:32','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-08dd9a2b-ab37-bf0a0f2138df','2016-08-26 15:16:40','2016-08-26 15:16:40','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-3bc7cff0-ab37-bf0a0f2138df','2016-08-26 15:18:06','2016-08-26 15:19:12','新建','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6b5d-235c97e6-ab37-bf0a0f2138df','1467084168730','待确认','1467084164315','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-63237f1e-ab37-bf0a0f2138df','2016-08-26 15:19:12','2016-08-26 15:19:12','待确认','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6b5d-235c97e6-ab37-bf0a0f2138df','1467084174418','已确认','1467084168730','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-cb7e4746-ab37-bf0a0f2138df','2016-08-26 15:22:07','2016-08-26 15:22:23','新建','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6b5d-be746b96-ab37-bf0a0f2138df','1467085954184','财务审核','1467085952322','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5d-d577af4f-ab37-bf0a0f2138df','2016-08-26 15:22:23','2016-08-26 15:22:23','财务审核','11e6-3ce4-14f48d78-9676-8f334e66899f','开票申请','11e6-6b5d-be746b96-ab37-bf0a0f2138df','1467085956786','已开票','1467085954184','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5f-997e869f-ab37-bf0a0f2138df','2016-08-26 15:35:02','2016-08-26 15:35:11','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b5f-9f25d29a-ab37-bf0a0f2138df','2016-08-26 15:35:11','2016-08-26 15:35:11','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b69-d958c405-ab37-bf0a0f2138df','2016-08-26 16:48:24','2016-08-26 16:48:32','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b69-ddf02658-ab37-bf0a0f2138df','2016-08-26 16:48:32','2016-08-26 16:48:32','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6a-01a78575-ab37-bf0a0f2138df','2016-08-26 16:49:31','2016-08-26 16:49:50','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6a-0c707235-ab37-bf0a0f2138df','2016-08-26 16:49:50','2016-08-26 16:49:50','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b69-edcecd3b-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6c-0d696687-ab37-bf0a0f2138df','2016-08-26 17:04:10','2016-08-26 17:09:17','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6c-8072dd45-ab37-bf0a0f2138df','2016-08-26 17:07:23','2016-08-26 17:09:08','创建','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','1467014834079','待确认','1467014814628','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6c-bee271df-ab37-bf0a0f2138df','2016-08-26 17:09:08','2016-08-26 17:09:08','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6c-c4447759-ab37-bf0a0f2138df','2016-08-26 17:09:17','2016-08-26 17:09:17','待确认','11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde','成交确认','11e6-6b6b-f5b59756-ab37-bf0a0f2138df','1467196460566','已确认','1467014834079','\0','','11e3-8a58-82144b41-9194-1d682b48d529','80',0,'11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6e-a8d7fad4-ab37-bf0a0f2138df','2016-08-26 17:22:50','2016-08-26 17:23:39','新建','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6b6e-8854c495-ab37-bf0a0f2138df','1467084168730','待确认','1467084164315','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','80',0,'11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96'),('11e6-6b6e-c6080de7-ab37-bf0a0f2138df','2016-08-26 17:23:39','2016-08-26 17:23:39','待确认','11e6-3cdf-e9547b0d-9676-8f334e66899f','回款确认','11e6-6b6e-8854c495-ab37-bf0a0f2138df','1467084174418','已确认','1467084168730','\0','','11e3-9544-1111a2cb-a2c5-53255716f388','7',0,'11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e6-429d-dd7a3284-86a2-074015f7cc96');
/*!40000 ALTER TABLE `t_relationhis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_upload`
--

DROP TABLE IF EXISTS `t_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_upload` (
  `ID` varchar(200) DEFAULT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `IMGBINARY` mediumblob,
  `FIELDID` varchar(200) DEFAULT NULL,
  `TYPE` varchar(200) DEFAULT NULL,
  `FILESIZE` int(11) DEFAULT NULL,
  `USERID` varchar(200) DEFAULT NULL,
  `MODIFYDATE` varchar(200) DEFAULT NULL,
  `PATH` varchar(4000) DEFAULT NULL,
  `FOLDERPATH` varchar(4000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_upload`
--

LOCK TABLES `t_upload` WRITE;
/*!40000 ALTER TABLE `t_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_workflow_reminder_history`
--

DROP TABLE IF EXISTS `t_workflow_reminder_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_workflow_reminder_history` (
  `ID` varchar(200) NOT NULL,
  `REMINDER_CONTENT` varchar(200) DEFAULT NULL,
  `USER_ID` varchar(200) DEFAULT NULL,
  `USER_NAME` varchar(200) DEFAULT NULL,
  `NODE_NAME` varchar(200) DEFAULT NULL,
  `DOC_ID` varchar(200) DEFAULT NULL,
  `FLOW_INSTANCE_ID` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `PROCESS_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_workflow_reminder_history`
--

LOCK TABLES `t_workflow_reminder_history` WRITE;
/*!40000 ALTER TABLE `t_workflow_reminder_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_workflow_reminder_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_产品信息`
--

DROP TABLE IF EXISTS `tlk_产品信息`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_产品信息` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_产品名称` varchar(200) DEFAULT NULL,
  `ITEM_产品编号` varchar(200) DEFAULT NULL,
  `ITEM_产品类型` varchar(200) DEFAULT NULL,
  `ITEM_价格` decimal(22,10) DEFAULT NULL,
  `ITEM_单位` varchar(200) DEFAULT NULL,
  `ITEM_附件` longtext,
  `ITEM_状态` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_产品信息`
--

LOCK TABLES `tlk_产品信息` WRITE;
/*!40000 ALTER TABLE `tlk_产品信息` DISABLE KEYS */;
INSERT INTO `tlk_产品信息` VALUES (NULL,'2016-08-25 11:45:59','客户管理/产品/产品/产品信息','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-25 11:43:41','11e6-3c42-1fa0f894-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','3433','软件','2500.0000000000','个','','正常','11e6-6a76-1d3d4b19-82ae-bbe47783a3bc');
/*!40000 ALTER TABLE `tlk_产品信息` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_产品类型`
--

DROP TABLE IF EXISTS `tlk_产品类型`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_产品类型` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_产品类型`
--

LOCK TABLES `tlk_产品类型` WRITE;
/*!40000 ALTER TABLE `tlk_产品类型` DISABLE KEYS */;
INSERT INTO `tlk_产品类型` VALUES (NULL,'2016-08-12 02:20:33','CRM/基础设置/产品类型/产品类型','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:20:31','11e6-3c45-a2d92ad0-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'软件','11e6-5ff0-498659f1-b967-4ba4af478a40'),(NULL,'2016-08-12 02:20:40','CRM/基础设置/产品类型/产品类型','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:20:38','11e6-3c45-a2d92ad0-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'服务','11e6-5ff0-4db4e88e-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_产品类型` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_公海设置`
--

DROP TABLE IF EXISTS `tlk_公海设置`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_公海设置` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_跟进客户数量` decimal(22,10) DEFAULT NULL,
  `ITEM_跟进天数` decimal(22,10) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_公海设置`
--

LOCK TABLES `tlk_公海设置` WRITE;
/*!40000 ALTER TABLE `tlk_公海设置` DISABLE KEYS */;
INSERT INTO `tlk_公海设置` VALUES (NULL,'2016-08-22 17:19:46','CRM/基础设置/公海设置/公海设置','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:21:35','11e6-41c7-fe93dfcd-8bb8-6142300f1a0f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'100.0000000000','30.0000000000','11e6-5ff0-70280749-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_公海设置` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_回款信息表`
--

DROP TABLE IF EXISTS `tlk_回款信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_回款信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_成交日期` datetime DEFAULT NULL,
  `ITEM_成交金额` decimal(22,10) DEFAULT NULL,
  `ITEM_归属人` longtext,
  `ITEM_回款日期` datetime DEFAULT NULL,
  `ITEM_回款金额` decimal(22,10) DEFAULT NULL,
  `ITEM_回款方式` varchar(200) DEFAULT NULL,
  `ITEM_附件` longtext,
  `ITEM_备注` longtext,
  `ITEM_成交编号` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_回款信息表`
--

LOCK TABLES `tlk_回款信息表` WRITE;
/*!40000 ALTER TABLE `tlk_回款信息表` DISABLE KEYS */;
INSERT INTO `tlk_回款信息表` VALUES (NULL,'2016-08-18 19:01:02','客户管理/回款管理/回款管理/回款信息表','11e6-6532-cdfe64a5-abfb-7f3dc8737aff','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-18 19:01:02','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:58:09','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已确认','','80','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6532-cdfe64a5-abfb-7f3dc8737aff\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'杭州信息','2016-08-18 00:00:00','2.0000000000','11e3-81b0-6e890f2c-95c1-e91b6587a8c7','2016-08-19 00:00:00','2.0000000000','支票','','','YGLZ160818001','11e6-6532-a68b0f0a-abfb-7f3dc8737aff'),(NULL,'2016-08-18 19:09:48','客户管理/回款管理/回款管理/回款信息表','11e6-6534-0210f88f-abfb-7f3dc8737aff','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-18 19:09:48','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:07:23','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已确认','','80','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6534-0210f88f-abfb-7f3dc8737aff\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'网通广告','2016-08-30 00:00:00','5.0000000000','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-30 00:00:00','5.0000000000','现金','','','YGLZ160818002','11e6-6533-f095b82c-abfb-7f3dc8737aff'),(NULL,'2016-08-26 15:19:12','客户管理/回款管理/回款管理/回款信息表','11e6-6b5d-3b0913c8-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 15:19:12','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:17:25','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已确认','','80','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5d-3b0913c8-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'北京华软信息科技有限公司','2016-08-30 00:00:00','12.0000000000','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 00:00:00','3.0000000000','支票','','','YGLZ160826001','11e6-6b5d-235c97e6-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:23:39','客户管理/回款管理/回款管理/回款信息表','11e6-6b6e-a8193eac-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:23:39','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:21:55','11e6-3cdb-2581d9ba-9676-8f334e66899f','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已确认','','80','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467084174418\":[]}','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"flowName\":\"回款确认\",\"flowId\":\"11e6-3cdf-e9547b0d-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已确认\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6e-a8193eac-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'江苏里布科技有限公司','2016-08-30 00:00:00','12.0000000000','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 00:00:00','11.0000000000','支票','','','YGLZ160826007','11e6-6b6e-8854c495-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_回款信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_客户`
--

DROP TABLE IF EXISTS `tlk_客户`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_客户` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_客户编号` varchar(200) DEFAULT NULL,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_客户来源` varchar(200) DEFAULT NULL,
  `ITEM_客户级别` varchar(200) DEFAULT NULL,
  `ITEM_行业` varchar(200) DEFAULT NULL,
  `ITEM_客户状态` varchar(200) DEFAULT NULL,
  `ITEM_省份` varchar(200) DEFAULT NULL,
  `ITEM_城市` varchar(200) DEFAULT NULL,
  `ITEM_地址` varchar(200) DEFAULT NULL,
  `ITEM_网址` varchar(200) DEFAULT NULL,
  `ITEM_备注` longtext,
  `ITEM_附件` longtext,
  `ITEM_具体项目` varchar(200) DEFAULT NULL,
  `ITEM_项目内容` varchar(200) DEFAULT NULL,
  `ITEM_内部阶段` longtext,
  `ITEM_计划确定时间` datetime DEFAULT NULL,
  `ITEM_技术` varchar(200) DEFAULT NULL,
  `ITEM_公司人员规模` decimal(22,10) DEFAULT NULL,
  `ITEM_预计合作方式` varchar(200) DEFAULT NULL,
  `ITEM_还评估哪些厂家` varchar(200) DEFAULT NULL,
  `ITEM_项目预算` varchar(200) DEFAULT NULL,
  `ITEM_金额` varchar(200) DEFAULT NULL,
  `ITEM_联系人` varchar(200) DEFAULT NULL,
  `ITEM_电话` varchar(200) DEFAULT NULL,
  `ITEM_联系人职位` varchar(200) DEFAULT NULL,
  `ITEM_联系人角色` longtext,
  `ITEM_联合跟进人` longtext,
  `ITEM_下次跟进时间` datetime DEFAULT NULL,
  `ITEM_负责人` longtext,
  `ITEM_领取时间` datetime DEFAULT NULL,
  `ITEM_转手次数` decimal(22,10) DEFAULT NULL,
  `ITEM_成交状态` varchar(200) DEFAULT NULL,
  `ITEM_是否公海` decimal(22,10) DEFAULT NULL,
  `ITEM_是否作废` decimal(22,10) DEFAULT NULL,
  `ITEM_退回公海时间` datetime DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  `ITEM_状态` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_客户`
--

LOCK TABLES `tlk_客户` WRITE;
/*!40000 ALTER TABLE `tlk_客户` DISABLE KEYS */;
INSERT INTO `tlk_客户` VALUES (NULL,'2016-08-18 17:31:34','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:30:06','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160818001','中信娱乐','转介绍','重点客户',' ','','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','张伟','13535223343','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-18 17:31:34','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6526-5959aaa3-9d40-a7ec8e34cb38',NULL),(NULL,'2016-08-18 18:30:36','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:27:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160818003','杭州信息','转介绍','重点客户','房地产/物业管理','初步沟通','浙江','杭州','','','','','','','',NULL,'','0.0000000000','','','','','李丽','32897723','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-18 18:30:36','0.0000000000','已成交','0.0000000000','0.0000000000',NULL,'11e6-652e-61de7a55-9d40-a7ec8e34cb38',NULL),(NULL,'2016-08-24 17:59:28','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:03:05','11e6-39f5-414b6696-a60d-2b305113d028','\0',10,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160818004','网通广告','转介绍','重点客户','娱乐休闲/餐饮/服务','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','李丽','32379632','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-24 17:59:28','1.0000000000','已成交','0.0000000000','0.0000000000',NULL,'11e6-6533-56eabb43-abfb-7f3dc8737aff',NULL),(NULL,'2016-08-26 18:09:57','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 14:49:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',8,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160822001','crfdsafd','','',' ','','dsfd','sfdsagd','','','','','','','',NULL,'','0.0000000000','','','','','4324','4354','','','',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-22 15:30:54','2.0000000000','已成交','0.0000000000','1.0000000000','2016-08-22 15:30:38','11e6-6834-8611cb4a-abfb-7f3dc8737aff',NULL),(NULL,'2016-08-22 18:33:22','客户关系/客户管理/客户管理/客户',NULL,NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be',NULL,'2016-08-22 18:33:22','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,NULL,'11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160822002','2343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'543543',NULL,NULL,NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','2016-08-22 18:33:22',NULL,'已成交','0.0000000000','0.0000000000',NULL,'11e6-6853-d9d8b160-abfb-7f3dc8737aff',NULL),(NULL,'2016-08-26 15:27:41','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:21:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826005','上海户外广告有限公司','转介绍','重点客户','推广/会展','确定意向','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','王先生','32456755','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:22:50','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b55-449f4731-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:24:40','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:22:59','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826006','北京华软信息科技有限公司','线上注册','一般客户',' ','见面拜访','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','李小姐','43222989','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:24:39','0.0000000000','已成交','0.0000000000','0.0000000000',NULL,'11e6-6b55-88cb8438-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:33:27','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:27:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826007','杭州1837游戏公司','公司资源','一般客户',' ','初步沟通','浙江','杭州','','','','','','','',NULL,'','0.0000000000','','','','','顾晓','23789780','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:33:27','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b56-355c0056-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:35:04','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:33:32','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826008','北京金同贸易有限公司','转介绍','一般客户','快速消费品（食品/饮料等）','初步沟通','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','张丽','43398890','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:35:04','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b57-01f18119-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:36:12','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:35:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826009','北京半球机械有限责任公司','转介绍','一般客户',' ','初步沟通','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','马芸','35655433','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:36:12','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b57-3c3742ea-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:27:57','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:40:10','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826010','上海影视文化有限公司','转介绍','关注客户','酒店/旅游','确定意向','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','倪妮','54354223','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:41:26','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b57-ef94500b-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:27:18','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:41:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826011','大连迪士尼贸易有限公司','公司资源','关注客户',' ','初步沟通','辽宁','大连','','','','','','','',NULL,'','0.0000000000','','','','','王小明','43245433','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:43:20','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b58-1f731b1c-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:45:32','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:43:38','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826012','青岛娱乐有限公司','线上注册','关注客户','娱乐休闲/餐饮/服务','初步沟通','山东','青岛','','','','','','','',NULL,'','0.0000000000','','','','','张妮妮','49897788','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:45:32','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b58-6ba68d86-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:46:45','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:45:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826013','杭州极限贸易有限公司','公司资源','一般客户','快速消费品（食品/饮料等）','初步沟通','浙江','杭州','','','','','','','',NULL,'','0.0000000000','','','','','张夏玲','65333235','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 14:46:45','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b58-b20a1cd7-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:40:31','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:46:50','11e6-39f5-414b6696-a60d-2b305113d028','\0',4,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826014','上海工商集团有限公司','转介绍','一般客户','推广/会展','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','张涵','43543338','','','11e3-9544-1111a2cb-a2c5-53255716f388',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 15:35:51','1.0000000000','未成交','0.0000000000','0.0000000000','2016-08-26 14:57:48','11e6-6b58-ddba5968-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:00:06','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:49:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826015','江苏里布科技有限公司','转介绍','一般客户','推广/会展','初步沟通','江苏','苏州','','','','','','','',NULL,'','0.0000000000','','','','','张妮','64346443','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 17:00:06','1.0000000000','已成交','0.0000000000','0.0000000000','2016-08-26 14:57:35','11e6-6b59-3545b199-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 14:57:22','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:54:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826016','北京百度贸易有限公司','线上注册','一般客户','快速消费品（食品/饮料等）','见面拜访','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','范碧波','43253322','','','',NULL,'',NULL,'0.0000000000','未成交','1.0000000000','0.0000000000','2016-08-26 14:57:22','11e6-6b59-eff2645a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:37:17','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:36:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826018','上海中心有限公司','转介绍','关注客户','计算机/网络','见面拜访','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','张伟','23545566','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 15:37:17','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b5f-c162039b-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:40:10','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:37:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826019','上海周达电影有限公司','转介绍','关注客户','房地产/物业管理','','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','吴晓','32387854','','','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-30 00:00:00','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 15:39:24','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b5f-ed9097ec-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:53:28','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:52:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826020','上海大世界娱乐有限公司','转介绍','一般客户','娱乐休闲/餐饮/服务','见面拜访','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','于小明','34377644','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 15:53:28','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b62-06125347-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 15:56:04','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:54:40','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826021','上海万豪贸易有限公司','转介绍','关注客户','推广/会展','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','张妮碧','32354545','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 15:56:04','0.0000000000','已成交','0.0000000000','0.0000000000',NULL,'11e6-6b62-580c0b88-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:42:09','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:10:43','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826022','上海四季酒店有限公司','线上注册','一般客户','酒店/旅游','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','李丽','23545449','','','','2016-08-30 00:00:00','11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 16:12:01','0.0000000000','已成交','0.0000000000','0.0000000000',NULL,'11e6-6b64-959c78f5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:13:53','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:12:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826023','上海娱乐广告有限公司','线上注册','关注客户','娱乐休闲/餐饮/服务','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','张丽','35465554','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 16:13:53','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b64-cd84c1c6-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:15:05','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:14:03','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826024','北京酒店管理有限公司','线上注册','一般客户','酒店/旅游','初步沟通','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','王欣','24347565','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 16:15:05','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b65-0d3625f7-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:16:48','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:15:32','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826025','上海新义州贸易有限公司','转介绍','关注客户','推广/会展','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','胡明星','65453432','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 16:16:48','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b65-423b4c8f-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:19:13','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:35:51','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826026','生益科技有限公司 ','转介绍','关注客户','计算机/网络','见面拜访','安徽','合肥','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','123456','','','','2016-08-30 00:00:00','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:39:45','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b68-18540915-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:40:31','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:39:50','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826027','春桥科技有限公司','转介绍','关注客户','计算机/网络','正式报价','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','李先生','145622','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:40:31','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b68-a746e2ad-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:14:19','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:40:35','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826028',' 新达共创科技有限公司','公司资源','一般客户','金融/银行','正式报价','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','刘先生','78551563','','','11e4-7b56-045d6210-a888-6d6b162bf5de',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:41:09','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b68-c1f5a1a0-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:41:51','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:41:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826030','锐步科技责任公司','线上注册','重点客户','金融/银行','见面拜访','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','69546552','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:41:51','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b68-d980dae3-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:42:27','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:41:55','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826031','近利科技有限公司','线上注册','关注客户','超市/百货','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','李先生','5552658','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:42:26','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b68-f1b2db1a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:43:40','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:42:51','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826032','华盖科技有限公司','线上注册','关注客户','计算机/网络','确定意向','广东','惠州','','','','','','','',NULL,'','0.0000000000','','','','','李伟建','5748244','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:43:40','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-13248f96-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:16:14','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:43:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826033','卓普科技有限公司','线上注册','重点客户','金融/银行','见面拜访','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','林先生','889954','','','11e3-8a58-82144b41-9194-1d682b48d529;11e3-9544-1111a2cb-a2c5-53255716f388',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:44:42','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-35147546-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:45:54','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:45:18','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826034','崇胜科技有限公司','公司资源','重点客户','超市/百货','商务洽谈','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','李先生','9996442','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:45:54','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-6a829698-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:47:10','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:46:14','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826035','起名科技有限公司','线上注册','一般客户','金融/银行','确定意向','四川','重庆','','','','','','','','2016-08-26 00:00:00','','0.0000000000','','','','','李先生','546655','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:47:10','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-8c321815-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:47:57','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:47:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826036','朗鑫智能科技有限公司','公司资源','关注客户','计算机/网络','确定意向','四川','城都','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','7745566','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:47:57','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-b0120df8-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:48:46','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:48:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826037',' 中程科技有限公司','公司资源','关注客户','保险/公关/市场','正式报价','四川','重庆','','','','','','','',NULL,'','0.0000000000','','','','','林小姐','74411255','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:48:46','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-cd71bc89-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:16:35','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:48:53','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826038','佳彩科技有限公司','线上注册','关注客户','建筑','售后服务','江西','南昌','','','','','','','',NULL,'','0.0000000000','','','','','陈先生','4561858','','','11e4-7b56-045d6210-a888-6d6b162bf5de',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:49:32','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b69-eb012e5f-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:50:17','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:49:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826039','威尔达科技有限公司','线上注册','一般客户','计算机/网络','商务洽谈','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','吴小姐','744558855','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:50:16','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-06225f8f-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:50:55','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:50:24','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826040','创惠科技有限公司 ','公司资源','关注客户','仓储/物流','正式报价','广东','开平','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','4556241','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:50:55','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-20af8d83-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:52:02','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:51:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826041','恒鑫科技发展有限公司','公司资源','关注客户','汽车/摩托车','签约成交','广东','开平','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','7894561','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:52:02','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-3710c576-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:52:52','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:52:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826042','广义科技有限公司 ','电话咨询','重点客户','计算机/网络','正式报价','广东','佛山','','','','','','','',NULL,'','0.0000000000','','','','','林笑笑','4561237','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:52:52','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-60bbbb30-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:53:35','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:52:57','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826043',' 丽泰科技有限公司','公司资源','关注客户','仓储/物流','正式报价','江西','南昌','','','','','','','',NULL,'','0.0000000000','','','','','龙鹏','1485236','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:53:35','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-7c4ceb69-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:54:20','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:53:45','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826044','特康科技有限公司','公司资源','重点客户','仓储/物流','商务洽谈','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','林伟强','8945123','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:54:20','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-98d12f1a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 16:57:23','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:56:29','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826045','宽洋科技有限公司','线上注册','关注客户','仓储/物流','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','李美眉','5842369','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 16:57:23','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6a-fa729eeb-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:17:25','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 16:58:16','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826046','唯莱电子科技有限公司','公司资源','关注客户','推广/会展','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','李小姐','7895201','','','11e4-7b56-045d6210-a888-6d6b162bf5de',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:00:24','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6b-3a8cfe1c-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:01:34','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:00:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826047','伊养生物科技有限公司','线上注册','重点客户','仓储/物流','正式报价','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','吴小姐','4563965','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:01:34','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6b-8ab0e439-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:03:14','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:01:59','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826048','零零壹科技有限公司','公司资源','一般客户','汽车/摩托车','签约成交','江西','赣州','','','','','','','',NULL,'','0.0000000000','','','','','龙哥','8456921','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:03:14','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b6b-bf2e3da5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:04:18','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:03:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826049','梦龙软件按有限公司','公司资源','关注客户','仓储/物流','正式报价','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','唐先生','8745962','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:04:18','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b6b-f152b294-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:06:30','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:04:27','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826050','文达通科技有限公司','公司资源','一般客户','教育/科研/培训','正式报价','广东','东莞','','','','','','','',NULL,'','0.0000000000','','','','','李晓来','8542690','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:06:30','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b6c-1729958e-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:08:50','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:06:41','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826051','冠准科技有限公司   ','公司资源','重点客户','生物/制药/医疗器械','正式报价','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','关先生','7412563','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:08:50','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6c-673a6981-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:13:06','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:09:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826052','阿尔泰科技有限公司','电话咨询','重点客户','仓储/物流','商务洽谈','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','吴强东','7403658','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:13:06','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6c-ba94e9a1-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:16:05','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:13:15','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826053','万政数码科技有限公司','公司资源','关注客户','酒店/旅游','正式报价','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','马东','7458222','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:16:05','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6d-521df1bb-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:28:04','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:16:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826054','亚安科技电子有限公司','公司资源','重点客户','计算机/网络','正式报价','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','7458333','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:28:04','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6d-c615e634-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:27:00','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:25:12','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826056','上海一致信息有限公司','转介绍','一般客户','推广/会展','初步沟通','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','吴晓','32354655','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 17:27:00','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6e-fdc7c10e-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:29:00','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:28:09','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826057','创赢智能科技有限公司','电话咨询','关注客户','建筑','正式报价','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','林达','7856932','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:28:59','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6f-670066b9-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:30:40','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:29:56','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826058',' 三丰电子科技有限公司','电话咨询','重点客户','仓储/物流','正式报价','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','琳达','4102358','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:30:39','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6f-a6ce52f2-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:32:19','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:31:00','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826059','数码人科技有限公司','电话咨询','关注客户','建筑','签约成交','广东','惠州','','','','','','','',NULL,'','0.0000000000','','','','','吴郑斌','88889541','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:32:19','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6f-cd1efa32-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:33:14','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:32:23','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826060','中脉科技有限公司','线上注册','关注客户','其他行业','签约成交','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','李强','77888965','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:33:14','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b6f-fe5761b7-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:50:38','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:34:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826061','拓达科技有限公司','转介绍','关注客户','汽车/摩托车','见面拜访','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','李海','1596348','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:50:38','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b70-3efe6fa4-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:51:40','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:50:47','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826062','恒普有限公司','公司资源','关注客户','汽车/摩托车','商务洽谈','四川','成都','','','','','','','',NULL,'','0.0000000000','','','','','凯宁','56998741','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:51:40','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b72-9035892b-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 17:52:35','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:51:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826063','斯维尔科技有限公司','电话咨询','关注客户','酒店/旅游','签约成交','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','林强','4123695','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 17:52:32','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b72-b4830e2b-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:17:09','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 17:52:43','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826064','天宇鸿图科技有限公司','转介绍','一般客户','耐用消费品（家具/家电等）','正式报价','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','彭先生','712843','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:17:09','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b72-d546a86b-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:21:32','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:18:10','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826065','长益信息科技有限公司','线上注册','关注客户','金融/银行','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','吴先生','1235964','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:21:31','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b76-63b57d9a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:22:23','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:21:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826066','万政数码有限公司','线上注册','关注客户','金融/银行','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','琳琳','8955656','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:22:22','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b76-e099619a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:23:10','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:22:31','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826067','阿尔泰有限公司','线上注册','关注客户','金融/银行','正式报价','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','来来','665554565','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:23:10','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b76-ff20c802-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:24:10','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:23:18','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826068','冠准有限公司','转介绍','关注客户','金融/银行','确定意向','北京','北京','','','','','','','',NULL,'','0.0000000000','','','','','周先生','9985564','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:24:10','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b77-1b5405de-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:24:53','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 18:24:17','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826069','文达科技有限公司','转介绍','关注客户','仓储/物流','确定意向','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','刘强','5698741','','','',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 18:24:53','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b77-3e3e5036-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:27:43','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:25:25','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826070','梦龙有限公司','线上注册','关注客户','金融/银行','正式报价','安徽','合肥','','','','','','','',NULL,'','0.0000000000','','','','','刘见','5455665','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:27:42','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b77-66d23051-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:28:30','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:27:52','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826072','壹科技有限公司','线上注册','关注客户','计算机/网络','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','刘啊','896554563','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:28:30','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b77-bebf58b7-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:29:17','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:28:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826073','伊生物科技有限公司','公司资源','关注客户','计算机/网络','确定意向','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','吴斌','256412','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:29:17','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b77-dabbcded-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:30:00','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:29:22','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826074','莱特电子科技有限公司','公司资源','重点客户','金融/银行','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','玉玉','3546565','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:30:00','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b77-f41f221e-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:30:47','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:06','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826075','洋科技有限公司','转介绍','一般客户','建筑','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','吴的','5555248','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:30:47','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-0e5dbabe-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:31:59','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826076','天互倒科技有限公司','线上注册','重点客户','计算机/网络','见面拜访','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','榆树','5698988','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:31:59','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-2aeb9b37-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:32:50','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:32:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826077','达共创科技有限公司','转介绍','关注客户','酒店/旅游','见面拜访','四川','成都','','','','','','','',NULL,'','0.0000000000','','','','','萨小姐','4985521','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:32:50','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-56dc3b83-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:33:47','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:33:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826078','春阿萨桥科技有限公司','转介绍','关注客户','建筑','确定意向','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','韩亮','8845511','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:33:47','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-76fda242-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:34:46','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:34:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826079','生益微科技有限公司','线上注册','关注客户','建筑','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','余姚市','5922636','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:34:45','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-9a49bc91-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:35:45','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:34:58','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826080','三丰电子科技有限公司','公司资源','一般客户','金融/银行','确定意向','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','卡卡','1258413','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:35:44','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-bccfef40-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:36:54','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:35:53','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826081','创赢科技有限公司','线上注册','重点客户','计算机/网络','见面拜访','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','爱萨','5698423','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:36:54','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-dd1ea771-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:55:48','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:36:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826082','新达共创科技有限公司','线上注册','关注客户','推广/会展','确定意向','福建','福州','','','','','','','',NULL,'','0.0000000000','','','','','吴小姐','41975','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:55:48','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b78-fa9d779e-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:37:51','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:36:58','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826083','亚安卡科技电子有限公司','线上注册','关注客户','建筑','见面拜访','广东','开平','','','','','','','',NULL,'','0.0000000000','','','','','李宇','9846631','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:37:50','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-044f4eff-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:38:37','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:37:56','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826084','腾乐科技有限公司','转介绍','关注客户','计算机/网络','确定意向','广东','开平','','','','','','','',NULL,'','0.0000000000','','','','','李达','5455889','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:38:37','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-268ba6cb-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:39:29','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:38:44','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826085','诺比节能科技有限公司 ','线上注册','重点客户','建筑','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','黄家驹','54123658','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:39:29','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-434b940c-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:40:38','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:40:05','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826086','富蓝商贸有限公司','线上注册','重点客户','计算机/网络','确定意向','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','榆树','58425969','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:40:38','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-73d39623-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:41:32','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:40:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826087','佰特商贸有限公司','转介绍','关注客户','计算机/网络','见面拜访','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','云豹','9874465','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:41:32','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-89d0b4c5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:42:49','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:41:36','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826088','龙源泰商贸有限公司','线上注册','关注客户','建筑','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','大阿斯','5657411','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:42:49','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-a9f94856-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:43:38','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:42:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826089','思泉商贸有限公司','线上注册','一般客户','仓储/物流','初步沟通','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','吴京','85412369','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:43:37','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-d887c518-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:44:21','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:43:42','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826090','港华商贸有限公司','线上注册','重点客户','仓储/物流','确定意向','上海','上海','','','','','','','',NULL,'','0.0000000000','','','','','煜掠','5547555','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:44:21','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b79-f4fdd6f9-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:45:41','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:44:30','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826091','鸿升行商贸有限公司','电话咨询','关注客户','保险/公关/市场','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','余亮','5841125','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:45:41','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7a-1146984a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:49:39','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:45:47','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826092','众义达商贸有限公司','电话咨询','关注客户','建筑','商务洽谈','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','静姐','45882545','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:49:39','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7a-3f4fb894-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:50:48','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:49:48','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826093','库弘商贸有限公司','线上注册','重点客户','计算机/网络','确定意向','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','艾云','242465','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:50:48','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7a-cf5253a5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:51:59','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:51:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826094','昆腾商贸有限公司','线上注册','重点客户','金融/银行','正式报价','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','林友尧','96548554','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:51:58','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7a-fe55b2e9-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:55:05','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:52:11','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826095','赐金商贸有限公司','线上注册','关注客户','建筑','商务洽谈','广东','江门','','','','','','','',NULL,'','0.0000000000','','','','','爱爱','552452512','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:55:05','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7b-24100d60-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:56:57','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:55:13','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826097','赐非商贸有限公司','线上注册','关注客户','计算机/网络','正式报价','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','温非','45555520','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:56:57','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7b-909482f8-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:58:09','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:56:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826099','天互科技有限公司','公司资源','一般客户','建筑','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','黄小姐','513427','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:58:09','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7b-c3b81854-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:57:46','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:07','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826100','四海友诚商贸有限公司','线上注册','关注客户','金融/银行','确定意向','广东','惠州','','','','','','','',NULL,'','0.0000000000','','','','','微带','9855656547','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:57:46','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7b-d476e598-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:59:20','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:12','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826101','晨升商贸有限责任公司','公司资源','关注客户','金融/银行','确定意向','福建','福州','','','','','','','',NULL,'','0.0000000000','','','','','利达','55551100','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:59:20','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7b-fb351c28-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 18:59:59','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:13','11e6-39f5-414b6696-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826102','春桥技有限公司','转介绍','关注客户','建筑','确定意向','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','彭先生','534168','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 18:59:56','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7b-fbd26d6a-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:00:13','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:59:28','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826103','通达九州商贸有限公司','公司资源','重点客户','其他行业','见面拜访','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','阿斯','6545640','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:00:13','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-28b166b5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:00:46','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:04','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826104','中脉技有限公司','','重点客户','汽车/摩托车','确定意向','江苏','南京','','','','','','','',NULL,'','0.0000000000','','','','','李先生','125896358','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:00:46','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7c-3e15ede6-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:01:21','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:20','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826105','靖浩商贸有限公司','公司资源','关注客户','酒店/旅游','签约成交','广东','广州','','','','','','','',NULL,'','0.0000000000','','','','','达呢','12554863','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:01:21','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-47dfbac0-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:01:55','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:00:54','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826106','诺比节能有限公司','线上注册','一般客户','建筑','商务洽谈','江西','赣州','','','','','','','',NULL,'','0.0000000000','','','','','黄先生','18200778489','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:01:55','0.0000000000','未成交','0.0000000000','0.0000000000',NULL,'11e6-6b7c-5bbd2d94-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:02:57','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:01:34','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826107','超前商贸有限公司','线上注册','关注客户','计算机/网络','正式报价','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','洋阿','52456','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:02:57','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-73f6801e-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:04:17','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:03:39','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826108','同心商贸有限公司','线上注册','关注客户','金融/银行','确定意向','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','环飒飒','225411253','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:04:17','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-be6c16d5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:05:05','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:04:29','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826109','中意万达商贸有限公司','线上注册','一般客户','超市/百货','见面拜访','广东','惠州','','','','','','','',NULL,'','0.0000000000','','','','','阿斯·','6664124','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:05:05','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-dc181185-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:05:54','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:05:14','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826110','爱顺商贸有限公司','线上注册','重点客户','计算机/网络','确定意向','广东','惠州','','','','','','','',NULL,'','0.0000000000','','','','','建国','0123447','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:05:54','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7c-f71c91e5-ab37-bf0a0f2138df',NULL),(NULL,'2016-08-26 19:06:40','客户管理/客户管理/客户管理/客户','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:06:01','11e6-39f5-414b6696-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'KH160826111','昀康商贸有限公司','电话咨询','重点客户','建筑','正式报价','江苏','杭州','','','','','','','',NULL,'','0.0000000000','','','','','瑜可','5222148','','','',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','2016-08-26 19:06:40','0.0000000000',' ','0.0000000000','0.0000000000',NULL,'11e6-6b7d-12e48955-ab37-bf0a0f2138df',NULL);
/*!40000 ALTER TABLE `tlk_客户` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_客户来源`
--

DROP TABLE IF EXISTS `tlk_客户来源`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_客户来源` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_客户来源`
--

LOCK TABLES `tlk_客户来源` WRITE;
/*!40000 ALTER TABLE `tlk_客户来源` DISABLE KEYS */;
INSERT INTO `tlk_客户来源` VALUES (NULL,'2016-08-12 02:15:34','CRM/基础设置/客户来源/客户来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:24','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'转介绍','11e6-5fef-92be5bcc-b967-4ba4af478a40'),(NULL,'2016-08-12 02:15:46','CRM/基础设置/客户来源/客户来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:39','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'线上注册','11e6-5fef-9b975fa9-b967-4ba4af478a40'),(NULL,'2016-08-12 02:16:00','CRM/基础设置/客户来源/客户来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:15:50','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'公司资源','11e6-5fef-a25c9ed0-b967-4ba4af478a40'),(NULL,'2016-08-12 02:16:23','CRM/基础设置/客户来源/客户来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:16:14','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'电话咨询','11e6-5fef-b0cc24d1-b967-4ba4af478a40'),(NULL,'2016-08-12 02:16:41','CRM/基础设置/客户来源/客户来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:16:28','11e6-39de-bfbe6e6f-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'其它','11e6-5fef-b902f58c-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_客户来源` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_客户状态`
--

DROP TABLE IF EXISTS `tlk_客户状态`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_客户状态` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_客户状态`
--

LOCK TABLES `tlk_客户状态` WRITE;
/*!40000 ALTER TABLE `tlk_客户状态` DISABLE KEYS */;
INSERT INTO `tlk_客户状态` VALUES (NULL,'2016-08-12 02:18:51','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:18:49','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'初步沟通','11e6-5ff0-0cb0ec62-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:00','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:18:56','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'见面拜访','11e6-5ff0-11601caf-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:09','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:07','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'确定意向','11e6-5ff0-176df2b6-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:17','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:14','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'正式报价','11e6-5ff0-1bf242a7-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:25','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:23','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'商务洽谈','11e6-5ff0-20f27872-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:32','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:29','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'签约成交','11e6-5ff0-24f3b5e7-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:38','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:36','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'售后服务','11e6-5ff0-28dab3b6-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:45','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:43','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'停滞客户','11e6-5ff0-2cf87a0f-b967-4ba4af478a40'),(NULL,'2016-08-12 02:19:52','CRM/基础设置/客户状态/客户状态','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:19:50','11e6-39e0-28932cbd-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'流失客户','11e6-5ff0-316e98b2-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_客户状态` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_客户级别`
--

DROP TABLE IF EXISTS `tlk_客户级别`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_客户级别` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ITEM_重要程度` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_客户级别`
--

LOCK TABLES `tlk_客户级别` WRITE;
/*!40000 ALTER TABLE `tlk_客户级别` DISABLE KEYS */;
INSERT INTO `tlk_客户级别` VALUES (NULL,'2016-08-12 02:18:15','CRM/基础设置/客户级别/客户级别','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:23','11e6-39df-6cbaf295-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'一般客户','一般','11e6-5fef-d9d73195-b967-4ba4af478a40'),(NULL,'2016-08-12 02:17:38','CRM/基础设置/客户级别/客户级别','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:33','11e6-39df-6cbaf295-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'关注客户','重要','11e6-5fef-dfb7b610-b967-4ba4af478a40'),(NULL,'2016-08-12 02:17:52','CRM/基础设置/客户级别/客户级别','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:17:48','11e6-39df-6cbaf295-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'重点客户','重要','11e6-5fef-e8b1fdc6-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_客户级别` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_开票信息表`
--

DROP TABLE IF EXISTS `tlk_开票信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_开票信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_成交日期` datetime DEFAULT NULL,
  `ITEM_成交金额` decimal(22,10) DEFAULT NULL,
  `ITEM_开票日期` datetime DEFAULT NULL,
  `ITEM_开票金额` decimal(22,10) DEFAULT NULL,
  `ITEM_开票类型` varchar(200) DEFAULT NULL,
  `ITEM_发票号码` varchar(200) DEFAULT NULL,
  `ITEM_附件上传` longtext,
  `ITEM_备注` longtext,
  `ID` varchar(200) NOT NULL,
  `ITEM_成交编号` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_开票信息表`
--

LOCK TABLES `tlk_开票信息表` WRITE;
/*!40000 ALTER TABLE `tlk_开票信息表` DISABLE KEYS */;
INSERT INTO `tlk_开票信息表` VALUES (NULL,'2016-08-26 11:21:45','客户管理/开票管理/开票管理/开票信息表','11e6-6a8c-5e832ee7-82ae-bbe47783a3bc','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 11:21:45','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-25 14:22:34','11e6-3ce0-3cd2af43-9676-8f334e66899f','\0',4,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已开票','','80','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467085956786\":[]}','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"flowName\":\"开票申请\",\"flowId\":\"11e6-3ce4-14f48d78-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已开票\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"prevAuditNode\":\"未开票\"}]','[{\"instanceId\":\"11e6-6a8c-5e832ee7-82ae-bbe47783a3bc\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'网通广告','2016-08-30 00:00:00','5.0000000000','2016-08-31 00:00:00','5.0000000000','增值税专用发票','253354554','','','11e6-6a8c-4f906f24-82ae-bbe47783a3bc','YGLZ160818002'),(NULL,'2016-08-26 15:22:24','客户管理/开票管理/开票管理/开票信息表','11e6-6b5d-caaa084e-ab37-bf0a0f2138df','11e3-9544-1111a2cb-a2c5-53255716f388','2016-08-26 15:22:23','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:21:45','11e6-3ce0-3cd2af43-9676-8f334e66899f','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',1048576,'已开票','','80','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467085956786\":[]}','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"flowName\":\"开票申请\",\"flowId\":\"11e6-3ce4-14f48d78-9676-8f334e66899f\",\"nodes\":[{\"nodeId\":\"\",\"stateLabel\":\"已开票\",\"auditors\":[]}]}]','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"未开票\"}]','[{\"instanceId\":\"11e6-6b5d-caaa084e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"蔡主管_财务主管\"}]',NULL,'北京华软信息科技有限公司','2016-08-30 00:00:00','12.0000000000','2016-08-26 00:00:00','12.0000000000','增值税专用发票','8766557677','','','11e6-6b5d-be746b96-ab37-bf0a0f2138df','YGLZ160826001');
/*!40000 ALTER TABLE `tlk_开票信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_成交产品明细`
--

DROP TABLE IF EXISTS `tlk_成交产品明细`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_成交产品明细` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_产品编号` varchar(200) DEFAULT NULL,
  `ITEM_产品类型` varchar(200) DEFAULT NULL,
  `ITEM_价格` decimal(22,10) DEFAULT NULL,
  `ITEM_单位` varchar(200) DEFAULT NULL,
  `ITEM_数量` decimal(22,10) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_成交产品明细`
--

LOCK TABLES `tlk_成交产品明细` WRITE;
/*!40000 ALTER TABLE `tlk_成交产品明细` DISABLE KEYS */;
INSERT INTO `tlk_成交产品明细` VALUES ('11e6-6b5c-c4354ef8-ab37-bf0a0f2138df','2016-08-26 15:16:02','客户管理/成交/成交/成交产品明细',NULL,NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:58','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'3433','软件','2500.0000000000','个','1.0000000000','11e6-6b5c-cbb94a29-ab37-bf0a0f2138df'),('11e6-6b69-52ad5cbe-ab37-bf0a0f2138df','2016-08-26 16:48:24','客户管理/成交/成交/成交产品明细',NULL,NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:48:13','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'3433','软件','2500.0000000000','个','2.0000000000','11e6-6b69-d2ad9c45-ab37-bf0a0f2138df'),('11e6-6b6b-f5b59756-ab37-bf0a0f2138df','2016-08-26 17:04:10','客户管理/成交/成交/成交产品明细',NULL,NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:04:04','11e6-3dc5-0679b5b2-a7fe-5b9b510af4bf','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'3433','软件','2500.0000000000','个','1.0000000000','11e6-6b6c-09f01dd7-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_成交产品明细` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_成交信息表`
--

DROP TABLE IF EXISTS `tlk_成交信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_成交信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_成交日期` datetime DEFAULT NULL,
  `ITEM_成交金额` decimal(22,10) DEFAULT NULL,
  `ITEM_归属人` longtext,
  `ITEM_收入类型` varchar(200) DEFAULT NULL,
  `ITEM_折扣` decimal(22,10) DEFAULT NULL,
  `ITEM_提成信息` longtext,
  `ITEM_备注` longtext,
  `ITEM_合同编号` varchar(200) DEFAULT NULL,
  `ITEM_合同名称` varchar(200) DEFAULT NULL,
  `ITEM_开始日期` datetime DEFAULT NULL,
  `ITEM_截止日期` datetime DEFAULT NULL,
  `ITEM_附件` longtext,
  `ITEM_成交编号` varchar(200) DEFAULT NULL,
  `ITEM_客户编号` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_成交信息表`
--

LOCK TABLES `tlk_成交信息表` WRITE;
/*!40000 ALTER TABLE `tlk_成交信息表` DISABLE KEYS */;
INSERT INTO `tlk_成交信息表` VALUES (NULL,'2016-08-18 18:59:52','客户管理/成交/成交/成交信息表','11e6-6532-9d9c872d-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-18 18:59:52','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:56:59','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','邢主管（行政主管）','80','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6532-9d9c872d-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'杭州信息','2016-08-18 00:00:00','2.0000000000','11e4-7b56-045d6210-a888-6d6b162bf5de','销售费用','2.0000000000','总金额2%','','','',NULL,NULL,'','YGLZ160818001','KH160818003','11e6-6532-7c859c55-abfb-7f3dc8737aff'),(NULL,'2016-08-18 19:08:20','客户管理/成交/成交/成交信息表','11e6-6533-eae03b81-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-18 19:08:20','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 19:06:14','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','钟总经理（总经理）','80','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6533-eae03b81-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'网通广告','2016-08-30 00:00:00','5.0000000000','11e3-8a58-82144b41-9194-1d682b48d529','销售费用','2.0000000000','合同金额2%','','','',NULL,NULL,'','YGLZ160818002','KH160818004','11e6-6533-c78aab27-abfb-7f3dc8737aff'),(NULL,'2016-08-23 14:30:47','客户管理/成交/成交/成交信息表','11e6-68e4-7e757d56-abfb-7f3dc8737aff','11de-8349-464c4b8b-bfa0-bbeb78b9de4d','2016-08-23 14:30:47','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 11:48:15','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'创建','系统管理员','86','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467014814628\",\"stateLabel\":\"创建\",\"auditors\":[{\"id\":\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\",\"name\":\"系统管理员\"}]}]}]','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"prevAuditNode\":\"已确认\"}]','[{\"instanceId\":\"11e6-68e4-7e757d56-abfb-7f3dc8737aff\",\"prevAuditUser\":\"admin\"}]',NULL,'crfdsafd','2016-08-23 00:00:00','3.0000000000','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','销售费用','2.0000000000','4324','','','',NULL,NULL,'','YGLZ160823001','KH160822001','11e6-68e4-6bef5d1a-abfb-7f3dc8737aff'),(NULL,'2016-08-24 10:51:28','客户管理/成交/成交/成交信息表','11e6-68fa-ecdabe8f-abfb-7f3dc8737aff','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-24 10:51:27','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 14:28:55','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',5,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'创建','系统管理员','81','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467014814628\":[\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\"]}','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467014814628\",\"stateLabel\":\"创建\",\"auditors\":[{\"id\":\"11e4-63ff-5d9df2ad-9f19-57d7b83ae7be\",\"name\":\"系统管理员\"}]}]}]','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-68fa-ecdabe8f-abfb-7f3dc8737aff\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'2343','2016-08-23 00:00:00','2.0000000000','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','销售费用','3.0000000000','3243','','','',NULL,NULL,'','YGLZ160823002','KH160822002','11e6-68fa-ddcde6bc-abfb-7f3dc8737aff'),(NULL,'2016-08-26 15:16:40','客户管理/成交/成交/成交信息表','11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 15:16:40','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:01','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','蔡主管_财务主管','80','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-9544-1111a2cb-a2c5-53255716f388\",\"name\":\"蔡主管_财务主管\"}]}]}]','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5c-bf15f8a3-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'北京华软信息科技有限公司','2016-08-30 00:00:00','12.0000000000','11e3-9544-1111a2cb-a2c5-53255716f388','销售费用','2.0000000000','2%','','','',NULL,NULL,'','YGLZ160826001','KH160826006','11e6-6b5c-a9ce2041-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:16:32','客户管理/成交/成交/成交信息表','11e6-6b5c-f16eea8c-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 15:16:32','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:14:45','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','蔡主管_财务主管','80','11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-9544-1111a2cb-a2c5-53255716f388\"]}','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-9544-1111a2cb-a2c5-53255716f388\",\"name\":\"蔡主管_财务主管\"}]}]}]','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5c-f16eea8c-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'北京华软信息科技有限公司','2016-08-29 00:00:00','34.0000000000','11e3-9544-1111a2cb-a2c5-53255716f388','销售费用','2.0000000000','2%','','','',NULL,NULL,'','YGLZ160826002','KH160826006','11e6-6b5c-c4354ef8-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:35:11','客户管理/成交/成交/成交信息表','11e6-6b5f-987f6618-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 15:35:11','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:34:17','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','钟总经理（总经理）','80','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b5f-987f6618-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'网通广告','2016-09-16 00:00:00','12.0000000000','11e3-8a58-82144b41-9194-1d682b48d529','销售费用','2.0000000000','2%','','','',NULL,NULL,'','YGLZ160826003','KH160818004','11e6-6b5f-7ee7bbe8-ab37-bf0a0f2138df'),(NULL,'2016-08-26 16:48:32','客户管理/成交/成交/成交信息表','11e6-6b69-d8954d30-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 16:48:32','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:44:38','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','钟总经理（总经理）','80','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b69-d8954d30-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'上海万豪贸易有限公司','2016-09-08 00:00:00','23.0000000000','11e3-8a58-82144b41-9194-1d682b48d529','销售费用','2.0000000000','2%','','','',NULL,NULL,'','YGLZ160826004','KH160826021','11e6-6b69-52ad5cbe-ab37-bf0a0f2138df'),(NULL,'2016-08-26 16:49:50','客户管理/成交/成交/成交信息表','11e6-6b6a-00da716e-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 16:49:50','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:48:58','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','钟总经理（总经理）','80','11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e3-8a58-82144b41-9194-1d682b48d529\"]}','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e3-8a58-82144b41-9194-1d682b48d529\",\"name\":\"钟总经理（总经理）\"}]}]}]','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6a-00da716e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'上海四季酒店有限公司','2016-08-30 00:00:00','15.0000000000','11e3-8a58-82144b41-9194-1d682b48d529','销售费用','1.0000000000','1%','','','',NULL,NULL,'','YGLZ160826005','KH160826022','11e6-6b69-edcecd3b-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:09:17','客户管理/成交/成交/成交信息表','11e6-6b6c-0c906be2-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 17:09:17','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:03:30','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','邢主管（行政主管）','80','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6c-0c906be2-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'江苏里布科技有限公司','2016-08-26 00:00:00','2.0000000000','11e4-7b56-045d6210-a888-6d6b162bf5de','销售费用','1.0000000000','1%','','','',NULL,NULL,'','YGLZ160826006','KH160826015','11e6-6b6b-f5b59756-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:09:08','客户管理/成交/成交/成交信息表','11e6-6b6c-7f92b66e-ab37-bf0a0f2138df','11e3-8a58-82144b41-9194-1d682b48d529','2016-08-26 17:09:08','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:06:50','11e6-3c3a-1610fafb-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',256,'已确认','邢主管（行政主管）','80','11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{\"1467196460566\":[\"11e4-7b56-045d6210-a888-6d6b162bf5de\"]}','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"flowName\":\"成交确认\",\"flowId\":\"11e6-3c3e-7707afa6-8ba1-7fc5de3ffcde\",\"nodes\":[{\"nodeId\":\"1467196460566\",\"stateLabel\":\"已确认\",\"auditors\":[{\"id\":\"11e4-7b56-045d6210-a888-6d6b162bf5de\",\"name\":\"邢主管（行政主管）\"}]}]}]','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"prevAuditNode\":\"待确认\"}]','[{\"instanceId\":\"11e6-6b6c-7f92b66e-ab37-bf0a0f2138df\",\"prevAuditUser\":\"钟总经理（总经理）\"}]',NULL,'江苏里布科技有限公司','2016-08-30 00:00:00','12.0000000000','11e4-7b56-045d6210-a888-6d6b162bf5de','销售费用','2.0000000000','2%','','','',NULL,NULL,'','YGLZ160826007','KH160826015','11e6-6b6c-6cb463ad-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_成交信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_机会信息表`
--

DROP TABLE IF EXISTS `tlk_机会信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_机会信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_机会名称` varchar(200) DEFAULT NULL,
  `ITEM_所属客户` varchar(200) DEFAULT NULL,
  `ITEM_状态` varchar(200) DEFAULT NULL,
  `ITEM_预计成交日期` datetime DEFAULT NULL,
  `ITEM_预计成交金额` decimal(22,10) DEFAULT NULL,
  `ITEM_发现日期` datetime DEFAULT NULL,
  `ITEM_归属人` longtext,
  `ITEM_需求说明` longtext,
  `ITEM_备注` longtext,
  `ITEM_机会来源` varchar(200) DEFAULT NULL,
  `ITEM_机会类型` varchar(200) DEFAULT NULL,
  `ITEM_销售阶段` varchar(200) DEFAULT NULL,
  `ITEM_客户编号` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_机会信息表`
--

LOCK TABLES `tlk_机会信息表` WRITE;
/*!40000 ALTER TABLE `tlk_机会信息表` DISABLE KEYS */;
INSERT INTO `tlk_机会信息表` VALUES (NULL,'2016-08-18 17:36:39','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:32:44','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微OA销售','中信娱乐','进行中','2016-08-30 00:00:00','3.0000000000','2016-08-17 00:00:00','11e3-8a58-82144b41-9194-1d682b48d529','需要流程、调查、会议等模块','','独立开发','普通机会','初期沟通（10%）','KH160818001','11e6-6526-b7972ff9-9d40-a7ec8e34cb38'),(NULL,'2016-08-18 18:36:33','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:35:30','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'企业软件购买','杭州信息','进行中','2016-08-30 00:00:00','9.0000000000','2016-08-17 00:00:00','11e4-7b56-045d6210-a888-6d6b162bf5de','','','独立开发','普通机会','立项跟踪（30%）','KH160818003','11e6-652f-7c94bf13-9d40-a7ec8e34cb38'),(NULL,'2016-08-26 15:08:41','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:07:20','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','上海户外广告有限公司','进行中','2016-09-28 00:00:00','21.0000000000','2016-08-18 00:00:00','11e3-9544-1111a2cb-a2c5-53255716f388','','','客户介绍','普通机会','初期沟通（10%）','KH160826005','11e6-6b5b-baee3cb2-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:10:20','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:09:37','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'crm服务合同','北京金同贸易有限公司','进行中','2016-08-30 00:00:00','15.0000000000',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','','','独立开发','重要机会','初期沟通（10%）','KH160826008','11e6-6b5c-0cbced28-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:11:47','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:10:33','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'天翎开发平台','北京华软信息科技有限公司','进行中','2016-09-22 00:00:00','24.0000000000',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','','','独立开发','重要机会','初期沟通（10%）','KH160826006','11e6-6b5c-2dd89288-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:13:49','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:12:34','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'天翎开发平台','青岛娱乐有限公司','进行中','2016-09-08 00:00:00','12.0000000000','2016-08-26 00:00:00','11e3-9544-1111a2cb-a2c5-53255716f388','','','独立开发','重要机会','初期沟通（10%）','KH160826012','11e6-6b5c-75e71354-ab37-bf0a0f2138df'),(NULL,'2016-08-26 16:41:53','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:40:53','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','上海大世界娱乐有限公司','进行中','2016-09-29 00:00:00','28.0000000000','2016-08-26 00:00:00','11e3-8a58-82144b41-9194-1d682b48d529','','','独立开发','普通机会','初期沟通（10%）','KH160826020','11e6-6b68-cc87a0d5-ab37-bf0a0f2138df'),(NULL,'2016-08-30 15:27:57','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:41:58','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'crm','上海工商集团有限公司','进行中','2016-09-21 00:00:00','3.0000000000','2016-08-26 00:00:00','11e3-8a58-82144b41-9194-1d682b48d529','','','独立开发','普通机会',' ','KH160826014','11e6-6b68-f3411c26-ab37-bf0a0f2138df'),(NULL,'2016-08-26 16:44:25','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 16:43:16','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'crm','上海娱乐广告有限公司','进行中','2016-09-22 00:00:00','2.0000000000',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','','','互联网广告','普通机会','初期沟通（10%）','KH160826023','11e6-6b69-21f82fb6-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:03:22','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:02:36','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','江苏里布科技有限公司','进行中','2016-08-30 00:00:00','2.0000000000','2016-08-26 00:00:00','11e4-7b56-045d6210-a888-6d6b162bf5de','','','独立开发','普通机会','初期沟通（10%）','KH160826015','11e6-6b6b-d5301717-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:06:06','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:05:26','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','江苏里布科技有限公司','进行中','2016-08-30 00:00:00','2.0000000000',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','','','独立开发','普通机会','初期沟通（10%）','KH160826015','11e6-6b6c-3ad053a0-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:58:30','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:30','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'软件销售','上海大世界娱乐有限公司','进行中','2016-09-24 00:00:00','2.0000000000','2016-08-26 00:00:00','11e3-8a58-82144b41-9194-1d682b48d529','','','独立开发','普通机会','立项跟踪（30%）','KH160826020','11e6-6b7b-e26a5e4a-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:59:34','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:58:46','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'软件销售','上海娱乐广告有限公司','进行中','2016-09-28 00:00:00','12.0000000000','2016-08-26 00:00:00','11e3-8a58-82144b41-9194-1d682b48d529','','','来电咨询','普通机会','初期沟通（10%）','KH160826023','11e6-6b7c-0f745e9c-ab37-bf0a0f2138df'),(NULL,'2016-08-30 15:22:20','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:21:10','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','上海影视文化有限公司','进行中','2016-08-30 00:00:00','3.0000000000',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','','','促销活动','普通机会','呈报方案（50%）','KH160826010','11e6-6e82-537810da-ab37-bf0a0f2138df'),(NULL,'2016-08-30 15:25:47','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:22:25','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'微oa','创惠科技有限公司 ','进行中','2016-08-30 00:00:00','0.0000000000',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','','',' ',' ','商务谈判（80%）','KH160826040','11e6-6e82-7fe226c6-ab37-bf0a0f2138df'),(NULL,'2016-08-30 15:26:56','客户管理/机会管理/机会/机会信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-30 15:26:10','11e6-3c1a-b79d3c5c-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'crm','北京半球机械有限责任公司','进行中','2016-08-30 00:00:00','2.0000000000',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','','',' ',' ','赢单（100%）','KH160826009','11e6-6e83-0645fd9e-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_机会信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_线索信息表`
--

DROP TABLE IF EXISTS `tlk_线索信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_线索信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_线索来源` varchar(200) DEFAULT NULL,
  `ITEM_联系方式` varchar(200) DEFAULT NULL,
  `ITEM_备注` longtext,
  `ITEM_处理结果` varchar(200) DEFAULT NULL,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_处理描述` longtext,
  `ITEM_负责人` longtext,
  `ITEM_操作状态` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_线索信息表`
--

LOCK TABLES `tlk_线索信息表` WRITE;
/*!40000 ALTER TABLE `tlk_线索信息表` DISABLE KEYS */;
INSERT INTO `tlk_线索信息表` VALUES (NULL,'2016-08-18 18:01:51','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:29:00','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','88843323','',' ','','','11e3-8a58-82144b41-9194-1d682b48d529','分配','11e6-6526-3253e3f8-9d40-a7ec8e34cb38'),(NULL,'2016-08-18 18:02:02','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 18:00:09','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'搜索引擎','23545533','搜索办公系统',' ','','','11e3-8a58-82144b41-9194-1d682b48d529','分配','11e6-652a-8bd85158-9d40-a7ec8e34cb38'),(NULL,'2016-08-18 18:33:11','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:32:00','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','32439980','咨询企业软件',' ','','','11e4-7b56-045d6210-a888-6d6b162bf5de','分配','11e6-652e-fee155c6-9d40-a7ec8e34cb38'),(NULL,'2016-08-22 14:41:44','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 14:41:11','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','43253','','无效线索','','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','处理','11e6-6833-6a388aa5-abfb-7f3dc8737aff'),(NULL,'2016-08-22 18:31:42','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:11:16','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','43243','','转为已有客户','crfdsafd','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','处理','11e6-6850-c3417caa-abfb-7f3dc8737aff'),(NULL,'2016-08-22 18:33:22','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:32:27','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','543543','','转为新客户','2343','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','处理','11e6-6853-b8c19f52-abfb-7f3dc8737aff'),(NULL,'2016-08-22 18:36:10','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:35:14','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','42343','','转为已有客户','crfdsafd','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','处理','11e6-6854-1cb9830e-abfb-7f3dc8737aff'),(NULL,'2016-08-22 18:50:53','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-22 18:50:31','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','3432','',' ','','','11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','分配','11e6-6856-3ee156b1-abfb-7f3dc8737aff'),(NULL,'2016-08-26 14:59:11','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-23 18:34:15','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',3,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','43243','','无效线索','','','11e3-9544-1111a2cb-a2c5-53255716f388','处理','11e6-691d-23eef66a-abfb-7f3dc8737aff'),(NULL,'2016-08-26 14:58:52','客户管理/线索管理/线索管理/线索信息表','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 14:00:47','11e6-39b1-0b0fad33-a60d-2b305113d028','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','32338786','',' ','','','11e3-9544-1111a2cb-a2c5-53255716f388','分配','11e6-6b52-6f113e54-bbbd-ef6eef62debe');
/*!40000 ALTER TABLE `tlk_线索信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_线索来源`
--

DROP TABLE IF EXISTS `tlk_线索来源`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_线索来源` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_线索来源`
--

LOCK TABLES `tlk_线索来源` WRITE;
/*!40000 ALTER TABLE `tlk_线索来源` DISABLE KEYS */;
INSERT INTO `tlk_线索来源` VALUES (NULL,'2016-08-12 02:11:54','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:11:51','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'促销活动','11e6-5fef-13fda127-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:03','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:01','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'会议资源','11e6-5fef-19ba48d4-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:11','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:08','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'搜索引擎','11e6-5fef-1de8d78b-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:18','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:15','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'互联网广告','11e6-5fef-22317e0c-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:25','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:23','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'平面广告','11e6-5fef-26fd5d67-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:33','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:30','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'电视广告','11e6-5fef-2aca1d5c-b967-4ba4af478a40'),(NULL,'2016-08-12 02:12:40','CRM/基础设置/线索来源/线索来源','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:12:38','11e6-39b2-849c0d19-a60d-2b305113d028','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'其它','11e6-5fef-2fb4f69b-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_线索来源` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_联系人信息表`
--

DROP TABLE IF EXISTS `tlk_联系人信息表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_联系人信息表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_姓名` varchar(200) DEFAULT NULL,
  `ITEM_所属客户` varchar(200) DEFAULT NULL,
  `ITEM_部门` varchar(200) DEFAULT NULL,
  `ITEM_职务` varchar(200) DEFAULT NULL,
  `ITEM_关键决策人` varchar(200) DEFAULT NULL,
  `ITEM_电话` varchar(200) DEFAULT NULL,
  `ITEM_邮件` varchar(200) DEFAULT NULL,
  `ITEM_手机` varchar(200) DEFAULT NULL,
  `ITEM_QQ` varchar(200) DEFAULT NULL,
  `ITEM_地址` varchar(200) DEFAULT NULL,
  `ITEM_生日` datetime DEFAULT NULL,
  `ITEM_性别` varchar(200) DEFAULT NULL,
  `ITEM_介绍人` varchar(200) DEFAULT NULL,
  `ITEM_爱好` varchar(200) DEFAULT NULL,
  `ITEM_传真` varchar(200) DEFAULT NULL,
  `ITEM_新浪微博` varchar(200) DEFAULT NULL,
  `ITEM_微信` varchar(200) DEFAULT NULL,
  `ITEM_角色关系` varchar(200) DEFAULT NULL,
  `ITEM_亲密程度` varchar(200) DEFAULT NULL,
  `ITEM_备注` longtext,
  `ITEM_负责人` longtext,
  `ITEM_状态` decimal(22,10) DEFAULT NULL,
  `ITEM_客户编号` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_联系人信息表`
--

LOCK TABLES `tlk_联系人信息表` WRITE;
/*!40000 ALTER TABLE `tlk_联系人信息表` DISABLE KEYS */;
INSERT INTO `tlk_联系人信息表` VALUES (NULL,'2016-08-25 19:01:01','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-18 17:32:11','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',7,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'张伟','中信娱乐','','','是','','','','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160818001','11e6-6526-a3f0aaec-9d40-a7ec8e34cb38'),(NULL,'2016-08-25 19:02:35','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:54:45','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李丽','杭州信息','','','是','34978433','','','','',NULL,'男','','','','','','商务决策人','普通朋友','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160818003','11e6-6532-2c9aef28-abfb-7f3dc8737aff'),(NULL,'2016-08-26 15:00:36','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 14:59:54','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'王先生','上海户外广告有限公司','','','是','32456755','','','','',NULL,'男','','','','','',' ','初相识','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826005','11e6-6b5a-b104f78a-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:01:35','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:01:02','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李小姐','北京华软信息科技有限公司','','','否','43222989','','','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826006','11e6-6b5a-d9f835ef-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:03:28','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:02:45','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'顾晓','杭州1837游戏公司','','','是','23789780','','15454433453','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826007','11e6-6b5b-172ab722-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:04:20','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:03:50','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'张丽','北京金同贸易有限公司','','','是','43398890','','','','',NULL,'女','','','','','','商务决策人','普通朋友','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826008','11e6-6b5b-3dc2c83f-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:06:02','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:04:44','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'王小明','大连迪士尼贸易有限公司','','','是','43245433','','','','',NULL,'男','','','','','','商务决策人','初相识','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826011','11e6-6b5b-5e26dda0-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:07:03','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:06:31','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'张夏玲','杭州极限贸易有限公司','','','是','65333235','','','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826013','11e6-6b5b-9d9592cb-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:24:20','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:22:52','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'张苏','','','','是','234308988','','13278866666','','',NULL,'','','','','','','决策人','初相识','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b5d-e66d44e0-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:26:02','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:24:43','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'陈妮妮','上海影视文化有限公司','','','是','23254544','','12448786663','','',NULL,'女','','','','','','意见影响人','一般关系','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','KH160826010','11e6-6b5e-28dba5c7-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:29:55','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:28:58','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李丽','','','',' ','57654232','','13568733332','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b77-e63ebc4a-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:30:51','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:30:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'徐丽丽','','','',' ','23885433','','13628767665','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b78-0af9f71f-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:55:23','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:53:48','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'陈呆萌','昆腾商贸有限公司','','','是','239773323','','13679643433','','',NULL,'男','','','','','','商务决策人','初相识','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160826094','11e6-6b7b-5ddc5993-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:09:12','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:07:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴建国','上海工商集团有限公司','','',' ','2333654','','13256489654','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160826014','11e6-6b7d-36010892-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:09:50','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:09:17','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李斌','上海周达电影有限公司','','',' ','2333654','','13654896596','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160826019','11e6-6b7d-878a7214-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:10:17','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:09:54','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴强','上海娱乐广告有限公司','','',' ','2366695','','13698542587','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','KH160826023','11e6-6b7d-9e0aa2fb-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:10:39','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:10:22','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李海','','','',' ','2369854','','13698520147','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-ae48a8a2-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:11:01','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:10:44','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李建','','','',' ','2369854','','13205698741','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-bb680871-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:11:23','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:11:05','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴雨','','','',' ','2356987','','13025698522','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-c8791040-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:11:55','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:11:27','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'李宇','','','',' ','2365897','','13255569855','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-d582ec9f-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:12:25','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:00','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'明青','','','',' ','2369854','','13022256987','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-e8e6c2fe-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:12:50','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:29','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'郁可唯','','','',' ','23698548','','13220698547','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7d-fa32401e-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:13:29','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:12:55','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴刚','','','',' ','2369854','','13025698544','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7e-09974a8d-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:14:03','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 19:13:42','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'刘达','','','',' ','2369854','','13205698456','','',NULL,'','','','','','',' ',' ','','11e4-7b56-045d6210-a888-6d6b162bf5de','0.0000000000','','11e6-6b7e-2603c067-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:15:15','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:14:20','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'林达','','','',' ','2356985','','13256985201','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-3c94bab1-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:15:43','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:15:20','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'金强','','','',' ','2354698','','13205698456','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-604c19b9-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:16:10','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:15:49','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'易生','','','',' ','23654789','','13025698520','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-715e3717-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:16:40','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:16:15','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'高媛媛','','','',' ','2356478','','13205487456','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-80ebd852-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:17:13','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:16:49','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴国宇','','','',' ','2356987','','13256987456','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-95324633-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:17:36','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:17:18','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'吴国强','','','',' ','2369854','','13205698745','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-a689838e-ab37-bf0a0f2138df'),(NULL,'2016-08-26 19:18:03','客户管理/联系人/联系人/联系人信息表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 19:17:43','11e6-3c11-16beeafe-8ba1-7fc5de3ffcde','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'林强','','','',' ','2354789','','13025698745','','',NULL,'','','','','','',' ',' ','','11e3-9544-1111a2cb-a2c5-53255716f388','0.0000000000','','11e6-6b7e-b5a96eb9-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_联系人信息表` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_计量单位`
--

DROP TABLE IF EXISTS `tlk_计量单位`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_计量单位` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_类别名称` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_计量单位`
--

LOCK TABLES `tlk_计量单位` WRITE;
/*!40000 ALTER TABLE `tlk_计量单位` DISABLE KEYS */;
INSERT INTO `tlk_计量单位` VALUES (NULL,'2016-08-12 02:21:16','CRM/基础设置/计量单位/计量单位','',NULL,NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-12 02:21:02','11e6-3c4f-b8b129f9-8ba1-7fc5de3ffcde','\0',2,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-63ff-5d9df2ad-9f19-57d7b83ae7be','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'个','11e6-5ff0-5c6bfec7-b967-4ba4af478a40');
/*!40000 ALTER TABLE `tlk_计量单位` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tlk_跟进记录表`
--

DROP TABLE IF EXISTS `tlk_跟进记录表`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tlk_跟进记录表` (
  `PARENT` varchar(200) DEFAULT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL,
  `FORMNAME` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `AUDITUSER` varchar(200) DEFAULT NULL,
  `AUDITDATE` datetime DEFAULT NULL,
  `AUTHOR` varchar(200) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(2000) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `FORMID` varchar(200) DEFAULT NULL,
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT NULL,
  `APPLICATIONID` varchar(200) DEFAULT NULL,
  `STATEINT` int(11) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `AUDITORNAMES` longtext,
  `LASTFLOWOPERATION` varchar(200) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL,
  `DOMAINID` varchar(200) DEFAULT NULL,
  `AUDITORLIST` longtext,
  `STATELABELINFO` longtext,
  `PREVAUDITNODE` longtext,
  `PREVAUDITUSER` longtext,
  `OPTIONITEM` longtext,
  `ITEM_客户名称` varchar(200) DEFAULT NULL,
  `ITEM_跟进内容` longtext,
  `ITEM_跟进日期` datetime DEFAULT NULL,
  `ITEM_客户编号` varchar(200) DEFAULT NULL,
  `ID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tlk_跟进记录表`
--

LOCK TABLES `tlk_跟进记录表` WRITE;
/*!40000 ALTER TABLE `tlk_跟进记录表` DISABLE KEYS */;
INSERT INTO `tlk_跟进记录表` VALUES (NULL,'2016-08-18 18:38:26','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-18 18:38:05','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'杭州信息','了解需求','2016-08-18 00:00:00','KH160818003','11e6-652f-d8f7df0e-9d40-a7ec8e34cb38'),(NULL,'2016-08-26 15:09:09','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37fe734a-9124-47aada6b7467_11e1-81e2-afbbfc08-9124-47aada6b7467','2016-08-26 15:08:48','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-9544-1111a2cb-a2c5-53255716f388','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'上海户外广告有限公司','需求沟通','2016-08-26 00:00:00','KH160826005','11e6-6b5b-ef5f8782-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:41:35','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:41:20','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'中信娱乐','需求了解','2016-08-26 00:00:00','KH160818001','11e6-6b60-7aa45046-ab37-bf0a0f2138df'),(NULL,'2016-08-26 15:42:05','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 15:41:55','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'上海工商集团有限公司','需求沟通','2016-08-26 00:00:00','KH160826014','11e6-6b60-8fd1ea81-ab37-bf0a0f2138df'),(NULL,'2016-08-26 17:20:44','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37fe734a-9124-47aada6b7467_11e3-866b-820a6aa1-81ef-b131c495402b','2016-08-26 17:20:28','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e4-7b56-045d6210-a888-6d6b162bf5de','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'江苏里布科技有限公司','需求了解','2016-08-26 00:00:00','KH160826015','11e6-6b6e-544ebe9a-ab37-bf0a0f2138df'),(NULL,'2016-08-26 18:57:22','客户管理/跟进记录/跟进记录/跟进记录表','',NULL,NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37fe734a-9124-47aada6b7467','2016-08-26 18:57:03','11e6-3cf5-4091f4f8-9676-8f334e66899f','\0',1,'11e6-429d-dd7a3284-86a2-074015f7cc96',0,NULL,'',NULL,'11e3-8a58-82144b41-9194-1d682b48d529','11e1-81e2-37f74759-9124-47aada6b7467','{}',NULL,NULL,NULL,NULL,'上海万豪贸易有限公司','需求了解','2016-08-30 00:00:00','KH160826021','11e6-6b7b-d26802d0-ab37-bf0a0f2138df');
/*!40000 ALTER TABLE `tlk_跟进记录表` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-02 10:47:54
