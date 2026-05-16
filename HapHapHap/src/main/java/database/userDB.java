package database;

import model.User;
import util.databaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDB {

    public User validasiLogin(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 1. Fungsi Cek apakah sudah difavoritkan
    public boolean cekFavorit(int idUser, int idResep) {
        String sql = "SELECT * FROM favorit_user WHERE id_user = ? AND id_resep = ?";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // True jika ada, False jika tidak ada
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Fungsi Tambah Favorit (Nama tabel disesuaikan)
    public boolean tambahKeFavorit(int idUser, int idResep) {
        String sql = "INSERT INTO favorit_user (id_user, id_resep) VALUES (?, ?)";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Fungsi Hapus Favorit (Unsave)
    public boolean hapusFavorit(int idUser, int idResep) {
        String sql = "DELETE FROM favorit_user WHERE id_user = ? AND id_resep = ?";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}