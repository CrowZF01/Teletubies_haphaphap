package controller;

import database.resepDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Resep;
import util.sessionManager;

import java.util.List;

public class favoritController {

    @FXML private VBox favoritContainer;

    @FXML
    public void initialize() {
        loadDataFavorit();
    }

    public void loadDataFavorit() {
        favoritContainer.getChildren().clear();

        if (!sessionManager.isLogin()) return;

        resepDB db = new resepDB();
        int idUser = sessionManager.getUser().getId();
        List<Resep> listFavorit = db.getFavoritByUser(idUser);

        if (listFavorit.isEmpty()) {
            Label kosong = new Label("Belum ada resep yang difavoritkan.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            favoritContainer.getChildren().add(kosong);
            return;
        }

        for (Resep resep : listFavorit) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemFavorit.fxml"));
                HBox card = loader.load();
                itemFavoritController controller = loader.getController();

                // Kirim data resep dan "this" (controller ini) agar bisa refresh saat dihapus
                controller.setData(resep, this);

                favoritContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =========================================================
    // MENU NAVIGASI & FILTER KATEGORI
    // =========================================================

    @FXML
    public void pindahHome(javafx.scene.input.MouseEvent event) {
        bukaHomeDenganKategori("Semua", event);
    }

    @FXML
    public void kategoriSemua(javafx.scene.input.MouseEvent event) {
        bukaHomeDenganKategori("Semua", event);
    }

    @FXML
    public void kategoriMakanan(javafx.scene.input.MouseEvent event) {
        bukaHomeDenganKategori("Makanan", event);
    }

    @FXML
    public void kategoriDessert(javafx.scene.input.MouseEvent event) {
        bukaHomeDenganKategori("Dessert", event);
    }

    @FXML
    public void kategoriMinuman(javafx.scene.input.MouseEvent event) {
        bukaHomeDenganKategori("Minuman", event);
    }

    // Fungsi sakti untuk pindah halaman dan langsung tembak filter di Home
    private void bukaHomeDenganKategori(String kategori, javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/home.fxml"));
            Parent root = loader.load();

            // Ambil controller dari home.fxml
            homeController controller = loader.getController();

            // 1. TAMPILKAN SCENE-NYA DULU BIAR ENGINE GRAFISNYA SIAP
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // 2. DELAY EKSEKUSI FILTER SAMPAI UI BENAR-BENAR SIAP
            javafx.application.Platform.runLater(() -> {
                if (kategori.equals("Makanan")) {
                    controller.kategoriMakanan();
                } else if (kategori.equals("Dessert")) {
                    controller.kategoriDessert();
                } else if (kategori.equals("Minuman")) {
                    controller.kategoriMinuman();
                } else {
                    controller.kategoriSemua();
                }
            });

        } catch (Exception e) {
            System.out.println("Gagal memuat halaman Home");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddRecipe(ActionEvent event) {
        try {
            // 1. Load file addResep.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/add.fxml"));
            Parent root = loader.load();

            // 2. Ambil Stage (jendela) yang sedang aktif
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Ganti isinya dengan halaman Add Recipe
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("❌ Gagal membuka halaman Tambah Resep!");
            e.printStackTrace();
        }
    }
}