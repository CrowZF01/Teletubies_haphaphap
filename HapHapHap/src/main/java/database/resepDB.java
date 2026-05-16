package database;

import model.Bahan;
import model.Resep;
import util.databaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resepDB {

    // Query sakti huruf kecil sesuai DB
    private final String BASE_QUERY =
            "SELECT r.id_resep, r.nama_resep, k.nama_kategori, r.tingkat_kepedasan, r.foto, " +
                    "GROUP_CONCAT(b.nama_bahan SEPARATOR ', ') AS daftar_bahan, " +
                    "r.langkah_pembuatan, r.waktu_estimasi, r.porsi_sajian " +
                    "FROM resep r " +
                    "LEFT JOIN kategori k ON r.id_kategori = k.id_kategori " +
                    "LEFT JOIN resep_bahan rb ON r.id_resep = rb.id_resep " +
                    "LEFT JOIN bahan b ON rb.id_bahan = b.id_bahan ";

    public List<Resep> getAllResep() {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY + "GROUP BY r.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Resep getResepById(int idResep) {
        String sql = BASE_QUERY + "WHERE r.id_resep = ? GROUP BY r.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idResep);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToResep(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Resep> cariBerdasarkanNama(String nama) {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE r.nama_resep LIKE ? GROUP BY r.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Resep> filterBerdasarkanBahan(List<String> bahanList) {
        List<Resep> list = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
        sqlBuilder.append("GROUP BY r.id_resep HAVING ");

        for (int i = 0; i < bahanList.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append("daftar_bahan LIKE ?");
        }

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < bahanList.size(); i++) {
                stmt.setString(i + 1, "%" + bahanList.get(i) + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Resep mapToResep(ResultSet rs) throws SQLException {
        String bahan = rs.getString("daftar_bahan");
        if (bahan == null) bahan = "";

        return new Resep(
                rs.getInt("id_resep"),
                rs.getString("nama_resep"),
                rs.getString("nama_kategori"),
                rs.getInt("tingkat_kepedasan"),
                bahan,
                rs.getString("langkah_pembuatan"),
                rs.getInt("waktu_estimasi"),
                rs.getInt("porsi_sajian"),
                rs.getString("foto")
        );
    }

    public List<Resep> filterBerdasarkanKategori(String kategori) {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE k.nama_kategori = ? GROUP BY r.id_resep";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kategori);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Bahan> getBahanByResep(int idResep) {
        List<Bahan> list = new ArrayList<>();
        String sql = """
                SELECT bahan.id_bahan, resep_bahan.id_resep, bahan.nama_bahan FROM bahan 
                JOIN resep_bahan ON bahan.id_bahan = resep_bahan.id_bahan 
                WHERE resep_bahan.id_resep = ?""";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idResep);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bahan bahan = new Bahan();
                bahan.setIdBahan(rs.getInt("id_bahan"));
                bahan.setIdResep(rs.getInt("id_resep"));
                bahan.setNamaBahan(rs.getString("nama_bahan"));
                list.add(bahan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method Transaction Super Aman untuk Insert 3 Tabel Sekaligus + Foto!
    public boolean tambahResepLengkap(int idUser, String judul, int idKategori, int kepedasan, int waktu, int porsi, String langkah, List<String> bahanList, String foto) {
        Connection conn = null;
        try {
            conn = databaseUtil.getConnection();
            conn.setAutoCommit(false); // MULAI TRANSACTION

            // 1. Simpan ke tabel resep (DENGAN FOTO)
            String sqlResep = "INSERT INTO resep (id_user, id_kategori, nama_resep, langkah_pembuatan, waktu_estimasi, porsi_sajian, tingkat_kepedasan, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtResep = conn.prepareStatement(sqlResep, Statement.RETURN_GENERATED_KEYS);
            stmtResep.setInt(1, idUser);
            stmtResep.setInt(2, idKategori);
            stmtResep.setString(3, judul);
            stmtResep.setString(4, langkah);
            stmtResep.setInt(5, waktu);
            stmtResep.setInt(6, porsi);
            stmtResep.setInt(7, kepedasan);
            stmtResep.setString(8, foto); // <--- NAMA FOTO MASUK SINI
            stmtResep.executeUpdate();

            // Ambil ID Resep yang baru saja dibuat
            ResultSet rsKeys = stmtResep.getGeneratedKeys();
            int idResepBaru = -1;
            if (rsKeys.next()) {
                idResepBaru = rsKeys.getInt(1);
            }

            // 2. Simpan setiap bahan ke tabel bahan & resep_bahan
            String sqlBahan = "INSERT INTO bahan (nama_bahan) VALUES (?)";
            String sqlRelasi = "INSERT INTO resep_bahan (id_resep, id_bahan) VALUES (?, ?)";
            PreparedStatement stmtBahan = conn.prepareStatement(sqlBahan, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmtRelasi = conn.prepareStatement(sqlRelasi);

            for (String namaBahan : bahanList) {
                if(namaBahan.trim().isEmpty()) continue; // Skip kalau kosong

                stmtBahan.setString(1, namaBahan);
                stmtBahan.executeUpdate();

                ResultSet rsBahan = stmtBahan.getGeneratedKeys();
                if (rsBahan.next()) {
                    int idBahanBaru = rsBahan.getInt(1);
                    // Hubungkan Resep dan Bahan
                    stmtRelasi.setInt(1, idResepBaru);
                    stmtRelasi.setInt(2, idBahanBaru);
                    stmtRelasi.executeUpdate();
                }
            }

            conn.commit(); // SIMPAN PERMANEN JIKA SEMUA SUKSES
            return true;

        } catch (SQLException e) {
            // JIKA ADA ERROR, BATALKAN SEMUA (ROLLBACK)
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Mengambil daftar resep favorit milik user tertentu
    public List<Resep> getFavoritByUser(int idUser) {
        List<Resep> list = new ArrayList<>();
        // INNER JOIN dengan tabel favorit_user
        String sql = BASE_QUERY + " INNER JOIN favorit_user fu ON r.id_resep = fu.id_resep WHERE fu.id_user = ? GROUP BY r.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}