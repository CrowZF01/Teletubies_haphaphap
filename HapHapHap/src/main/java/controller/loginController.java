package controller;

import database.userDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.sessionManager;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        userDB db = new userDB();
        User user = db.validasiLogin(username, password);

        if (user != null) {
            sessionManager.setUser(user);
            System.out.println("Login berhasil: " + user.getUsername());

            // --- KODE UNTUK PINDAH KE HOME.FXML ---
            try {
                // 1. Ambil jendela (Stage) yang sedang aktif saat ini
                Stage stage = (Stage) usernameField.getScene().getWindow();

                // 2. Load file home.fxml dari resources
                // Pastikan path-nya sesuai dengan struktur folder resources kamu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/home.fxml"));                Parent root = loader.load();

                // 3. Set scene baru dan tampilkan
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                System.out.println("Gagal memuat halaman Home!");
                e.printStackTrace();
            }
            // --------------------------------------

        } else {
            System.out.println("Login gagal: Username atau Password salah");
        }
    }

    @FXML
    public void pindahDaftar(){
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/daftar.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            System.out.println("Gagal memuat halaman Daftar");
            e.printStackTrace();
        }


    }
}