-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 20, 2026 lúc 09:29 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `tqsport`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banners`
--

CREATE TABLE `banners` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `link_url` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `sort_order` int(11) NOT NULL,
  `subtitle` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `banners`
--

INSERT INTO `banners` (`id`, `created_at`, `updated_at`, `active`, `image_url`, `link_url`, `position`, `sort_order`, `subtitle`, `title`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', b'1', 'https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=1800&q=82', '/products', 'HOME_HERO', 1, 'Áo đấu, bộ tập và thời trang bóng đá cho câu lạc bộ và đội tuyển quốc gia.', 'TQSport');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carts`
--

CREATE TABLE `carts` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_items`
--

CREATE TABLE `cart_items` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `cart_id` bigint(20) DEFAULT NULL,
  `variant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id`, `created_at`, `updated_at`, `name`, `slug`, `parent_id`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Áo sân nhà', 'ao-san-nha', NULL),
(2, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Bộ tập', 'bo-tap', NULL),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Áo sân khách', 'ao-san-khach', NULL),
(4, '2026-05-16 16:10:19.000000', '2026-05-16 16:10:19.000000', 'Áo khoác', 'ao-khoac', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `order_code` varchar(255) DEFAULT NULL,
  `recipient_name` varchar(255) DEFAULT NULL,
  `recipient_phone` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`id`, `created_at`, `updated_at`, `note`, `order_code`, `recipient_name`, `recipient_phone`, `shipping_address`, `status`, `total_amount`, `user_id`) VALUES
(1, '2026-05-16 16:21:51.000000', '2026-05-16 16:21:51.000000', '', 'TS-1778948511246', '', '', '', 'CANCELLED', 0.00, 1),
(2, '2026-05-16 16:22:19.000000', '2026-05-16 16:22:19.000000', 'in số áo 14 tên Tiến Quang', 'TS-1778948539338', 'Ngô Văn Tiến Quang', '0345006396', 'Thượng Cát Hà Nội', 'CANCELLED', 0.00, 1),
(3, '2026-05-16 16:35:41.000000', '2026-05-16 16:35:41.000000', '', 'TS-1778949341816', '', '', '', 'COMPLETED', 1780000.00, 4),
(4, '2026-05-16 16:44:15.000000', '2026-05-16 16:44:15.000000', '', 'TS-1778949855769', 'Ngo Quang', '0123456789', 'Thượng Cát Hà Nội', 'SHIPPING', 1740000.00, 4),
(5, '2026-05-16 17:17:30.000000', '2026-05-16 17:17:30.000000', '', 'TS-1778951850979', 'Quang', 'ádknjasd', 'ádjakdj', 'PAID', 550000.00, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_items`
--

CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(38,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `variant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_items`
--

INSERT INTO `order_items` (`id`, `created_at`, `updated_at`, `product_name`, `quantity`, `unit_price`, `order_id`, `variant_id`) VALUES
(1, '2026-05-16 16:35:41.000000', '2026-05-16 16:35:41.000000', 'Áo tuyển Việt Nam sân nhà 1995 Retro', 1, 890000.00, 3, 1),
(2, '2026-05-16 16:35:41.000000', '2026-05-16 16:35:41.000000', 'Áo tuyển Việt Nam sân khách 1995 Retro', 1, 890000.00, 3, 5),
(3, '2026-05-16 16:44:15.000000', '2026-05-16 16:44:15.000000', 'Áo sân nhà AC Milan 2000 Retro', 1, 1190000.00, 4, 6),
(4, '2026-05-16 16:44:15.000000', '2026-05-16 16:44:15.000000', 'Áo sân nhà Chelsea 1960 Retro', 1, 550000.00, 4, 7),
(5, '2026-05-16 17:17:31.000000', '2026-05-16 17:17:31.000000', 'Áo sân nhà Chelsea 1960 Retro', 1, 550000.00, 5, 7);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `featured` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`id`, `created_at`, `updated_at`, `description`, `featured`, `name`, `price`, `slug`, `status`, `category_id`, `team_id`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', '', b'1', 'Áo tuyển Việt Nam sân nhà 1995 Retro', 890000.00, 'ao-tuyen-viet-nam-san-nha-1995-retro', 'ACTIVE', 1, 1),
(2, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Sản phẩm mặc định của TQSport.', b'1', 'Bộ tập Real Madrid Pro Black', 1190000.00, 'bo-tap-real-madrid-pro-black', 'ARCHIVED', 2, 2),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Sản phẩm mặc định của TQSport.', b'1', 'Áo Argentina sân khách Heritage', 990000.00, 'ao-argentina-san-khach-heritage', 'ARCHIVED', 3, 3),
(4, '2026-05-16 16:13:02.000000', '2026-05-16 16:13:02.000000', '', b'0', 'Áo sân nhà Manchester United 26-27', 360000.00, 'ao-san-nha-manchester-united-26-27', 'ARCHIVED', 1, 4),
(5, '2026-05-16 16:16:09.000000', '2026-05-16 16:16:09.000000', '', b'0', 'Áo tuyển Việt Nam sân khách 1995 Retro', 890000.00, 'ao-tuyen-viet-nam-san-khach-1995-retro', 'ACTIVE', 3, 1),
(6, '2026-05-16 16:18:02.000000', '2026-05-16 16:18:02.000000', '', b'0', 'Áo AC Milan sân nhà 25-26', 2300000.00, 'ao-ac-milan-san-nha-25-26', 'ACTIVE', 1, 5),
(7, '2026-05-16 16:19:55.000000', '2026-05-16 16:19:55.000000', '', b'0', 'Áo AC Milan sân khách 25-26', 240000.00, 'ao-ac-milan-san-khach-25-26', 'ACTIVE', 1, 5),
(8, '2026-05-16 16:33:12.000000', '2026-05-16 16:33:12.000000', 'Sản phẩm mặc định của TQSport.', b'1', 'Áo tuyển Việt Nam sân nhà 2026', 890000.00, 'ao-tuyen-viet-nam-san-nha-2026', 'ARCHIVED', 1, 1),
(9, '2026-05-17 16:35:19.000000', '2026-05-17 16:35:19.000000', '', b'0', 'Áo Barcelona sân nhà 25-26', 230000.00, 'ao-barcelona-san-nha-25-26', 'ACTIVE', 1, 7),
(10, '2026-05-17 16:36:00.000000', '2026-05-17 16:36:00.000000', '', b'0', 'Áo Barcelona sân khách 25-26', 230000.00, 'ao-barcelona-san-khach-25-26', 'ACTIVE', 3, 7),
(11, '2026-05-17 16:44:02.000000', '2026-05-17 16:44:02.000000', '', b'0', 'Áo Real Madrid sân nhà 25-26', 250000.00, 'ao-real-madrid-san-nha-25-26', 'ACTIVE', 1, 2),
(12, '2026-05-17 16:44:42.000000', '2026-05-17 16:44:42.000000', '', b'0', 'Áo Arsenal sân nhà 25-26', 250000.00, 'ao-arsenal-san-nha-25-26', 'ACTIVE', 1, 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product_images`
--

CREATE TABLE `product_images` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `sort_order` int(11) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `product_images`
--

INSERT INTO `product_images` (`id`, `created_at`, `updated_at`, `alt_text`, `image_url`, `sort_order`, `product_id`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Áo tuyển Việt Nam sân nhà 1995 Retro', 'https://ananas.vn/wp-content/uploads/Pro_FRJ0001_1.jpg', 1, 1),
(2, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Bộ tập Real Madrid Pro Black', 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=900&q=80', 1, 2),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Áo Argentina sân khách Heritage', 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=900&q=80', 1, 3),
(4, '2026-05-16 16:13:02.000000', '2026-05-16 16:13:02.000000', 'Áo sân nhà Manchester United 26-27', 'https://www.classicfootballshirts.co.uk/cdn-cgi/image/fit=contain,q=70,w=1024,h=1024,f=webp/pub/media/catalog/product//4/0/4069996939547-1_tpl4gqcgeccah5va.jpg', 1, 4),
(5, '2026-05-16 16:16:09.000000', '2026-05-16 16:16:09.000000', 'Áo tuyển Việt Nam sân khách 1995 Retro', 'https://ananas.vn/wp-content/uploads/Pro_FRJ0002_1.jpg', 1, 5),
(6, '2026-05-16 16:18:02.000000', '2026-05-16 16:18:02.000000', 'Áo AC Milan sân nhà 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-ac-milan-soc-do-den-2526-2.png', 1, 6),
(7, '2026-05-16 16:19:55.000000', '2026-05-16 16:19:55.000000', 'Áo AC Milan sân khách 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-inter-milan-soc-xanh-den-2526-2.png', 1, 7),
(8, '2026-05-16 16:33:12.000000', '2026-05-16 16:33:12.000000', 'Áo tuyển Việt Nam sân nhà 2026', 'https://images.unsplash.com/photo-1517466787929-bc90951d0974?auto=format&fit=crop&w=900&q=80', 1, 8),
(9, '2026-05-17 16:35:19.000000', '2026-05-17 16:35:19.000000', 'Áo Barcelona sân nhà 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-barca-soc-san-nha-2526-2.png', 1, 9),
(10, '2026-05-17 16:36:00.000000', '2026-05-17 16:36:00.000000', 'Áo Barcelona sân khách 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-barca-kem-2526-hang-viet-nam-2.png', 1, 10),
(11, '2026-05-17 16:44:02.000000', '2026-05-17 16:44:02.000000', 'Áo Real Madrid sân nhà 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-real-madrid-trang-2526-1.jpg', 1, 11),
(12, '2026-05-17 16:44:42.000000', '2026-05-17 16:44:42.000000', 'Áo Arsenal sân nhà 25-26', 'https://www.sporter.vn/wp-content/uploads/2017/06/Ao-bong-da-arsenal-do-2526-1.png', 1, 12);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product_variants`
--

CREATE TABLE `product_variants` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `stock_quantity` int(11) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `product_variants`
--

INSERT INTO `product_variants` (`id`, `created_at`, `updated_at`, `color`, `size`, `sku`, `stock_quantity`, `product_id`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Default', 'M', 'ao-tuyen-viet-nam-san-nha-2026-M', 42, 1),
(2, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Default', 'M', 'bo-tap-real-madrid-pro-black-M', 18, 2),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Default', 'M', 'ao-argentina-san-khach-heritage-M', 26, 3),
(4, '2026-05-16 16:13:02.000000', '2026-05-16 16:13:02.000000', 'Default', 'M', 'ao-san-nha-manchester-united-26-27-M', 10, 4),
(5, '2026-05-16 16:16:09.000000', '2026-05-16 16:16:09.000000', 'Default', 'M', 'ao-tuyen-viet-nam-san-khach-95-retro-M', 42, 5),
(6, '2026-05-16 16:18:02.000000', '2026-05-16 16:18:02.000000', 'Default', 'M', 'ao-san-nha-ac-milan-2000-retro-M', 5, 6),
(7, '2026-05-16 16:19:55.000000', '2026-05-16 16:19:55.000000', 'Default', 'M', 'ao-san-nha-chelsea-1960-retro-M', 12, 7),
(8, '2026-05-16 16:33:12.000000', '2026-05-16 16:33:12.000000', 'Default', 'M', 'ao-tuyen-viet-nam-san-nha-2026-M', 42, 8),
(9, '2026-05-17 16:35:19.000000', '2026-05-17 16:35:19.000000', 'Default', 'M', 'ao-barcelona-san-nha-25-26-M', 15, 9),
(10, '2026-05-17 16:36:00.000000', '2026-05-17 16:36:00.000000', 'Default', 'M', 'ao-barcelona-san-khach-25-26-M', 20, 10),
(11, '2026-05-17 16:44:02.000000', '2026-05-17 16:44:02.000000', 'Default', 'M', 'ao-real-madrid-san-nha-25-26-M', 23, 11),
(12, '2026-05-17 16:44:42.000000', '2026-05-17 16:44:42.000000', 'Default', 'M', 'ao-arsenal-san-nha-25-26-M', 30, 12);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `teams`
--

CREATE TABLE `teams` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `teams`
--

INSERT INTO `teams` (`id`, `created_at`, `updated_at`, `country`, `logo_url`, `name`, `slug`, `type`) VALUES
(1, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Việt Nam', '', 'Vietnam', 'vietnam', 'NATIONAL'),
(2, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Spain', '', 'Real Madrid', 'real-madrid', 'CLUB'),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', 'Global', '', 'Arsenal', 'arsenal', 'CLUB'),
(4, '2026-05-16 16:10:13.000000', '2026-05-16 16:10:13.000000', 'Global', '', 'Manchester United', 'manchester-united', 'CLUB'),
(5, '2026-05-16 16:17:32.000000', '2026-05-16 16:17:32.000000', 'Global', '', 'AC Milan', 'ac-milan', 'CLUB'),
(7, '2026-05-17 16:34:12.000000', '2026-05-17 16:34:12.000000', 'Global', '', 'Barcelona', 'barcelona', 'CLUB');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('USER','ADMIN') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `created_at`, `updated_at`, `active`, `email`, `full_name`, `password_hash`, `phone`, `role`) VALUES
(1, '2026-05-15 04:46:26.000000', '2026-05-15 04:46:26.000000', b'1', 'nvtquang1425@gmail.com', 'Ngo Tien Qunag', '{bcrypt-placeholder}TienQuang14', NULL, 'USER'),
(2, '2026-05-15 08:28:21.000000', '2026-05-15 08:28:21.000000', b'1', 'minhp@gmail.com', 'Minh Phương', '{bcrypt-placeholder}12345678', NULL, 'USER'),
(3, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', b'1', 'admin@tqsport.vn', 'TQSport Admin', '{bcrypt-placeholder}12345678', NULL, 'ADMIN'),
(4, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', b'1', 'user@tqsport.vn', 'TQSport User', '{bcrypt-placeholder}12345678', NULL, 'USER'),
(5, '2026-05-16 09:06:34.000000', '2026-05-16 09:06:34.000000', b'1', 'superadmin@tqsport.vn', 'TQSport Super Admin', '{bcrypt-placeholder}12345678', NULL, 'ADMIN');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `banners`
--
ALTER TABLE `banners`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_64t7ox312pqal3p7fg9o503c2` (`user_id`);

--
-- Chỉ mục cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  ADD KEY `FK5yyw1o0dor9gmxfra1dqvn4qa` (`variant_id`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`);

--
-- Chỉ mục cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  ADD KEY `FKemq71edpbn9wsxnxncfn1algp` (`variant_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  ADD KEY `FKis6798wv3am27jlceg6fu6nrm` (`team_id`);

--
-- Chỉ mục cho bảng `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqnq71xsohugpqwf3c9gxmsuy` (`product_id`);

--
-- Chỉ mục cho bảng `product_variants`
--
ALTER TABLE `product_variants`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKosqitn4s405cynmhb87lkvuau` (`product_id`);

--
-- Chỉ mục cho bảng `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `banners`
--
ALTER TABLE `banners`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `carts`
--
ALTER TABLE `carts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `product_variants`
--
ALTER TABLE `product_variants`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `teams`
--
ALTER TABLE `teams`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FK5yyw1o0dor9gmxfra1dqvn4qa` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`),
  ADD CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`);

--
-- Các ràng buộc cho bảng `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKemq71edpbn9wsxnxncfn1algp` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`id`);

--
-- Các ràng buộc cho bảng `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FKis6798wv3am27jlceg6fu6nrm` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  ADD CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Các ràng buộc cho bảng `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Các ràng buộc cho bảng `product_variants`
--
ALTER TABLE `product_variants`
  ADD CONSTRAINT `FKosqitn4s405cynmhb87lkvuau` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
