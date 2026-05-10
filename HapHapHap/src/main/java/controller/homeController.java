package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Resep;

import javafx.event.ActionEvent;
import java.util.List;

public class homeController {
    @FXML private TextField inputBahanField;
    @FXML private FlowPane tagContainer;
    @FXML private FlowPane resepContainer;
    @FXML private TextField searchField;

    private List<String> listBahanTerpilih = new java.util.ArrayList<>();

    // Saat aplikasi dibuka, langsung tampilkan semua resep
    public void initialize() {
        resepDB db = new resepDB();
        List<Resep> listSemuaResep = db.getAllResep();
        tampilkanKeLayar(listSemuaResep);
    }

    // Dipanggil otomatis setiap kali ada huruf yang diketik di Search Bar
    @FXML
    public void handleCariResep() {
        String keyword = searchField.getText().trim();
        resepDB db = new resepDB();
        List<Resep> hasilPencarian;
        // Jika kotak pencarian kosong, kembalikan tampilan ke semua resep
        if (keyword.isEmpty()) {
            hasilPencarian = db.getAllResep();
        } else {
            // Jika ada hurufnya, cari ke database berdasarkan nama
            hasilPencarian = db.cariBerdasarkanNama(keyword);
        }
        // Panggil method helper andalan kita untuk memunculkannya ke layar!
        tampilkanKeLayar(hasilPencarian);
    }

    @FXML
    public void kategoriSemua() {
        resepDB db = new resepDB();
        tampilkanKeLayar(db.getAllResep());
    }

    @FXML
    public void kategoriMakanan() {
        resepDB db = new resepDB();
        // Meminta database mencari kategori "Makanan"
        tampilkanKeLayar(db.filterBerdasarkanKategori("Makanan"));
    }

    @FXML
    public void kategoriDessert() {
        resepDB db = new resepDB();
        tampilkanKeLayar(db.filterBerdasarkanKategori("Dessert"));
    }

    @FXML
    public void kategoriMinuman() {
        resepDB db = new resepDB();
        tampilkanKeLayar(db.filterBerdasarkanKategori("Minuman"));
    }

    // Dipanggil saat tombol ⊕ diklik atau enter ditekan
    @FXML
    public void handleTambahBahan() {
        String bahan = inputBahanField.getText().trim();
        if (!bahan.isEmpty() && !listBahanTerpilih.contains(bahan)) {
            listBahanTerpilih.add(bahan);
            inputBahanField.clear();
            renderTags();
        }
    }

    // Menggambar ulang tag bahan di kanan layar
    private void renderTags() {
        tagContainer.getChildren().clear();

        for (String bahan : listBahanTerpilih) {
            Label tag = new Label(bahan + "  ✕");
            tag.setStyle("-fx-background-color: #FBE2D1; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-padding: 5 8; -fx-background-radius: 4; -fx-cursor: hand;");

            tag.setOnMouseClicked(e -> {
                listBahanTerpilih.remove(bahan);
                renderTags();
            });

            tagContainer.getChildren().add(tag);
        }
    }

    // Dipanggil saat tombol "Terapkan Filter" diklik
    @FXML
    public void handleTerapkanFilter() {
        resepDB db = new resepDB();
        List<Resep> hasilFilter;

        if (listBahanTerpilih.isEmpty()) {
            hasilFilter = db.getAllResep();
        } else {
            hasilFilter = db.filterBerdasarkanBahan(listBahanTerpilih);
        }

        tampilkanKeLayar(hasilFilter);
    }

    // =========================================================
    // METHOD HELPER (Menghindari penulisan kode berulang / DRY)
    // =========================================================
    private void tampilkanKeLayar(List<Resep> daftarResep) {
        resepContainer.getChildren().clear();
        for (Resep resep : daftarResep) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemResep.fxml"));
                VBox card = loader.load();
                itemResepController controller = loader.getController();
                controller.setData(resep);
                resepContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            // 1. Muat ulang file login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/login.fxml"));
            Parent root = loader.load();
            // 2. Ambil "Jendela" (Stage) aplikasi yang sedang aktif saat ini
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // 3. Ganti isinya dengan halaman Login
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Gagal memuat halaman login.");
        }
    }
}