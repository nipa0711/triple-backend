-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- 생성 시간: 22-06-18 22:54
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
  `photoId` varchar(200) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `placeId` varchar(50) NOT NULL,
  `registerTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 테이블 구조 `reviews`
--

CREATE TABLE `reviews` (
  `reviewIdx` int(11) NOT NULL COMMENT 'review index',
  `userId` varchar(50) NOT NULL COMMENT 'review owner',
  `reviewId` varchar(50) NOT NULL COMMENT 'UUID format review id',
  `content` text NOT NULL COMMENT 'content',
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
  ADD UNIQUE KEY `photoId` (`photoId`),
  ADD KEY `userId` (`userId`);

--
-- 테이블의 인덱스 `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`reviewIdx`),
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
-- 테이블의 AUTO_INCREMENT `reviews`
--
ALTER TABLE `reviews`
  MODIFY `reviewIdx` int(11) NOT NULL AUTO_INCREMENT COMMENT 'review index';

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
  ADD CONSTRAINT `photos_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- 테이블의 제약사항 `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `userId` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
