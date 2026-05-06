package database;

import model.Resep;
import util.databaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resepDB {

    public List<Resep> getAllResep() {
        List<Resep> list = new ArrayList<>();

        String sql = "SELECT * FROM resep";

        try (Connection conn = databaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Resep resep = new Resep(
                        rs.getInt("id_resep"),
                        rs.getString("judul"),
                        rs.getString("jenis_makanan"),
                        rs.getInt("tingkat_kepedasan"),
                        rs.getString("bahan"),
                        rs.getString("langkah_pembuatan"),
                        rs.getInt("estimasi_waktu")
                );

                list.add(resep);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean tambahResep(Resep resep) {
        String sql = "INSERT INTO resep (judul, jenis_makanan, tingkat_kepedasan, bahan, langkah_pembuatan, estimasi_waktu) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resep.getJudul());
            stmt.setString(2, resep.getJenisMakanan());
            stmt.setInt(3, resep.getTingkatKepedasan());
            stmt.setString(4, resep.getBahan());
            stmt.setString(5, resep.getLangkahPembuatan());
            stmt.setInt(6, resep.getEstimasiWaktu());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Resep getResepById(int idResep) {
        String sql = "SELECT * FROM resep WHERE id_resep = ?";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idResep);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Resep(
                        rs.getInt("id_resep"),
                        rs.getString("judul"),
                        rs.getString("jenis_makanan"),
                        rs.getInt("tingkat_kepedasan"),
                        rs.getString("bahan"),
                        rs.getString("langkah_pembuatan"),
                        rs.getInt("estimasi_waktu")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Resep> cariBerdasarkanNama(String nama) {
        List<Resep> list = new ArrayList<>();

        String sql = "SELECT * FROM resep WHERE judul LIKE ?";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Resep resep = new Resep(
                        rs.getInt("id_resep"),
                        rs.getString("judul"),
                        rs.getString("jenis_makanan"),
                        rs.getInt("tingkat_kepedasan"),
                        rs.getString("bahan"),
                        rs.getString("langkah_pembuatan"),
                        rs.getInt("estimasi_waktu")
                );

                list.add(resep);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Resep> filterBerdasarkanBahan(String bahan) {
        List<Resep> list = new ArrayList<>();

        String sql = "SELECT * FROM resep WHERE bahan LIKE ?";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + bahan + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Resep resep = new Resep(
                        rs.getInt("id_resep"),
                        rs.getString("judul"),
                        rs.getString("jenis_makanan"),
                        rs.getInt("tingkat_kepedasan"),
                        rs.getString("bahan"),
                        rs.getString("langkah_pembuatan"),
                        rs.getInt("estimasi_waktu")
                );

                list.add(resep);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}