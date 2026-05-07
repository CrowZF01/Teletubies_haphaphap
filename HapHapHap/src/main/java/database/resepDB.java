package database;

import model.Resep;
import util.databaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resepDB {

    // Query sakti untuk menggabungkan tabel Resep, Kategori, dan Bahan jadi satu
    private final String BASE_QUERY =
            "SELECT r.id_resep, r.nama_resep, k.nama_kategori, r.tingkat_kepedasan, r.foto, " +
                    "GROUP_CONCAT(b.nama_bahan SEPARATOR ', ') AS daftar_bahan, " +
                    "r.langkah_pembuatan, r.waktu_estimasi " +
                    "FROM Resep r " +
                    "LEFT JOIN Kategori k ON r.id_kategori = k.id_kategori " +
                    "LEFT JOIN Resep_Bahan rb ON r.id_resep = rb.id_resep " +
                    "LEFT JOIN Bahan b ON rb.id_bahan = b.id_bahan ";

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

    // Ubah parameter agar menerima List<String>
    public List<Resep> filterBerdasarkanBahan(List<String> bahanList) {
        List<Resep> list = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
        sqlBuilder.append("GROUP BY r.id_resep HAVING "); // 1=1 sekadar trik agar bisa ditambah AND berulang kali

        // Tambahkan kondisi AND sebanyak jumlah bahan di list
        for (int i = 0; i < bahanList.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" AND "); // Tambahkan AND hanya jika ini bukan bahan pertama
            }
            sqlBuilder.append("daftar_bahan LIKE ?");
        }

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            // Masukkan kata kuncinya ke tanda tanya (?) di SQL
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

    public boolean tambahResep(Resep resep) {
        // KODE SEMENTARA: Cuma nge-insert ke tabel Resep (bahan belum ikut ter-insert).
        // Default id_user = 2 (akun felix), id_kategori = 1
        String sql = "INSERT INTO Resep (id_user, id_kategori, nama_resep, langkah_pembuatan, waktu_estimasi, tingkat_kepedasan) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 2);
            stmt.setInt(2, 1);
            stmt.setString(3, resep.getJudul());
            stmt.setString(4, resep.getLangkahPembuatan());
            stmt.setInt(5, resep.getEstimasiWaktu());
            stmt.setInt(6, resep.getTingkatKepedasan());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method bantuan biar nggak ngetik panjang-panjang terus
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
                rs.getString("foto") // <--- MASUKKAN KE SINI
        );
    }
}