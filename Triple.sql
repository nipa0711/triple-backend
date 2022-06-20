-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- 생성 시간: 22-06-20 19:52
-- 서버 버전: 5.5.68-MariaDB
-- PHP 버전: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 데이터베이스: `Triple`
--

-- --------------------------------------------------------

CREATE SCHEMA `triple` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE triple;

--
-- 테이블 구조 `histories`
--

CREATE TABLE `histories` (
  `idx` int(11) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `point` int(11) NOT NULL,
  `content` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 테이블 구조 `photos`
--

CREATE TABLE `photos` (
  `photoId` varchar(50) NOT NULL,
  `reviewId` varchar(50) NOT NULL,
  `registerTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 테이블 구조 `reviews`
--

CREATE TABLE `reviews` (
  `reviewId` varchar(50) NOT NULL COMMENT 'UUID format review id',
  `content` text NOT NULL COMMENT 'content',
  `userId` varchar(50) NOT NULL COMMENT 'review owner',
  `placeId` varchar(50) NOT NULL COMMENT 'UUID format place id',
  `isPhotoExist` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'photo is existed or not',
  `createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'review created time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='One review';

-- --------------------------------------------------------

--
-- 테이블 구조 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL COMMENT 'user index',
  `userId` varchar(50) NOT NULL COMMENT 'user id',
  `totalPoint` int(11) NOT NULL DEFAULT '0' COMMENT 'user total point',
  `createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created user account'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='saved user information';

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `histories`
--
ALTER TABLE `histories`
  ADD PRIMARY KEY (`idx`),
  ADD KEY `userId` (`userId`);

--
-- 테이블의 인덱스 `photos`
--
ALTER TABLE `photos`
  ADD KEY `reviewId` (`reviewId`);

--
-- 테이블의 인덱스 `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`reviewId`) USING BTREE,
  ADD KEY `userId` (`userId`);

--
-- 테이블의 인덱스 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `userId` (`userId`);

--
-- 덤프된 테이블의 AUTO_INCREMENT
--

--
-- 테이블의 AUTO_INCREMENT `histories`
--
ALTER TABLE `histories`
  MODIFY `idx` int(11) NOT NULL AUTO_INCREMENT;

--
-- 테이블의 AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'user index';

--
-- 덤프된 테이블의 제약사항
--

--
-- 테이블의 제약사항 `histories`
--
ALTER TABLE `histories`
  ADD CONSTRAINT `histories_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- 테이블의 제약사항 `photos`
--
ALTER TABLE `photos`
  ADD CONSTRAINT `photos_ibfk_1` FOREIGN KEY (`reviewId`) REFERENCES `reviews` (`reviewId`);

--
-- 테이블의 제약사항 `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
