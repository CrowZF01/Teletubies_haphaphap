-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2026 at 03:34 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `haphaphap`
--

-- --------------------------------------------------------

--
-- Table structure for table `bahan`
--

CREATE TABLE `bahan` (
  `id_bahan` int(15) NOT NULL,
  `nama_bahan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bahan`
--

INSERT INTO `bahan` (`id_bahan`, `nama_bahan`) VALUES
(1, 'Nasi'),
(2, 'Telur'),
(3, 'Bawang Merah'),
(4, 'Bawang Putih'),
(5, 'Kecap Manis'),
(6, 'Cabai Rawit'),
(7, 'Ayam'),
(8, 'Teh'),
(9, 'Gula'),
(10, 'Es Batu');

-- --------------------------------------------------------

--
-- Table structure for table `favorit`
--

CREATE TABLE `favorit` (
  `id` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `id_resep` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `favorit_user`
--

CREATE TABLE `favorit_user` (
  `id_user` int(15) NOT NULL,
  `id_resep` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `favorit_user`
--

INSERT INTO `favorit_user` (`id_user`, `id_resep`) VALUES
(2, 1),
(2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(15) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
(1, 'Sarapan'),
(2, 'Makan Siang'),
(3, 'Makan Malam'),
(4, 'Dessert'),
(5, 'Minuman');

-- --------------------------------------------------------

--
-- Table structure for table `resep`
--

CREATE TABLE `resep` (
  `id_resep` int(15) NOT NULL,
  `id_user` int(15) NOT NULL,
  `id_kategori` int(15) NOT NULL,
  `nama_resep` varchar(100) NOT NULL,
  `langkah_pembuatan` text NOT NULL,
  `waktu_estimasi` int(11) NOT NULL,
  `porsi_sajian` int(11) DEFAULT NULL,
  `tingkat_kepedasan` int(11) DEFAULT NULL,
  `foto` varchar(255) DEFAULT '/images/default-food.png'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep`
--

INSERT INTO `resep` (`id_resep`, `id_user`, `id_kategori`, `nama_resep`, `langkah_pembuatan`, `waktu_estimasi`, `porsi_sajian`, `tingkat_kepedasan`, `foto`) VALUES
(1, 1, 1, 'Nasi Goreng Kampung', '1. Haluskan bawang dan cabai.\n2. Tumis bumbu hingga harum.\n3. Masukkan telur, orak-arik.\n4. Masukkan nasi dan kecap, aduk rata.\n5. Sajikan selagi hangat.', 15, 1, 2, 'nasi_goreng.jpg'),
(2, 1, 5, 'Es Teh Manis Segar', '1. Seduh teh dengan sedikit air panas.\n2. Tambahkan gula dan aduk hingga larut.\n3. Tambahkan air secukupnya dan masukkan es batu.', 5, 1, 0, 'es_teh.jpg'),
(3, 2, 2, 'Ayam Bakar Madu Spesial Felix', '1. Ungkep ayam dengan bumbu bawang putih dan garam.\n2. Olesi ayam dengan kecap manis.\n3. Bakar ayam hingga matang kecoklatan.', 45, 4, 1, 'ayam_bakar.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `resep_bahan`
--

CREATE TABLE `resep_bahan` (
  `id_bahan` int(15) NOT NULL,
  `id_resep` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep_bahan`
--

INSERT INTO `resep_bahan` (`id_bahan`, `id_resep`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(4, 3),
(5, 1),
(5, 3),
(6, 1),
(7, 3),
(8, 2),
(9, 2),
(10, 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(15) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`) VALUES
(1, 'System', 'admin_rahasia'),
(2, 'felix', '12345');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bahan`
--
ALTER TABLE `bahan`
  ADD PRIMARY KEY (`id_bahan`);

--
-- Indexes for table `favorit`
--
ALTER TABLE `favorit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `favorit_user`
--
ALTER TABLE `favorit_user`
  ADD PRIMARY KEY (`id_user`,`id_resep`),
  ADD KEY `fk_fav_resep` (`id_resep`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id_resep`),
  ADD KEY `fk_resep_user` (`id_user`),
  ADD KEY `fk_resep_kategori` (`id_kategori`);

--
-- Indexes for table `resep_bahan`
--
ALTER TABLE `resep_bahan`
  ADD PRIMARY KEY (`id_bahan`,`id_resep`),
  ADD KEY `fk_rb_resep` (`id_resep`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bahan`
--
ALTER TABLE `bahan`
  MODIFY `id_bahan` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `favorit`
--
ALTER TABLE `favorit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `resep`
--
ALTER TABLE `resep`
  MODIFY `id_resep` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `favorit_user`
--
ALTER TABLE `favorit_user`
  ADD CONSTRAINT `fk_fav_resep` FOREIGN KEY (`id_resep`) REFERENCES `resep` (`id_resep`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_fav_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `fk_resep_kategori` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_resep_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `resep_bahan`
--
ALTER TABLE `resep_bahan`
  ADD CONSTRAINT `fk_rb_bahan` FOREIGN KEY (`id_bahan`) REFERENCES `bahan` (`id_bahan`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rb_resep` FOREIGN KEY (`id_resep`) REFERENCES `resep` (`id_resep`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
