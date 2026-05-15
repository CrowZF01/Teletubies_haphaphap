package controller;

import database.resepDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Resep;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class homeController {

    @FXML private TextField inputBahanField;
    @FXML private FlowPane tagContainer;
    @FXML private FlowPane resepContainer;
    @FXML private TextField searchField;

    private final resepDB db = new resepDB();

    private final List<String> listBahanTerpilih = new ArrayList<>();
    private final List<Resep> masterData = new ArrayList<>();

    private String kategoriAktif = "Semua";

    @FXML
    public void initialize() {
        masterData.clear();
        masterData.addAll(db.getAllResep());
        tampilkanKeLayar(masterData);

        // Search otomatis setiap diketik, lebih smooth seperti SiBarista
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            terapkanSemuaFilter();
        });
    }

    @FXML
    public void handleCariResep() {
        terapkanSemuaFilter();
    }

    @FXML
    public void kategoriSemua() {
        kategoriAktif = "Semua";
        terapkanSemuaFilter();
    }

    @FXML
    public void kategoriMakanan() {
        kategoriAktif = "Makanan";
        terapkanSemuaFilter();
    }

    @FXML
    public void kategoriDessert() {
        kategoriAktif = "Dessert";
        terapkanSemuaFilter();
    }

    @FXML
    public void kategoriMinuman() {
        kategoriAktif = "Minuman";
        terapkanSemuaFilter();
    }

    @FXML
    public void handleTambahBahan() {
        String bahan = inputBahanField.getText().trim();

        if (!bahan.isEmpty() && !containsIgnoreCase(listBahanTerpilih, bahan)) {
            listBahanTerpilih.add(bahan);
            inputBahanField.clear();
            renderTags();
            terapkanSemuaFilter();
        }
    }

    private void renderTags() {
        tagContainer.getChildren().clear();

        List<String> copyList = new ArrayList<>(listBahanTerpilih);

        for (String bahan : copyList) {
            Label tag = new Label(bahan + "  ✕");
            tag.setStyle(
                    "-fx-background-color: #FBE2D1;" +
                            "-fx-text-fill: #555555;" +
                            "-fx-font-size: 11px;" +
                            "-fx-padding: 5 8;" +
                            "-fx-background-radius: 4;" +
                            "-fx-cursor: hand;"
            );

            tag.setOnMouseClicked(e -> {
                listBahanTerpilih.remove(bahan);
                renderTags();
                terapkanSemuaFilter();
            });

            tagContainer.getChildren().add(tag);
        }
    }

    @FXML
    public void handleTerapkanFilter() {
        terapkanSemuaFilter();
    }

    private void terapkanSemuaFilter() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);

        List<Resep> hasil = masterData.stream()
                .filter(resep -> cocokKeyword(resep, keyword))
                .filter(this::cocokKategori)
                .filter(this::cocokBahan)
                .toList();

        tampilkanKeLayar(hasil);
    }

    private boolean cocokKeyword(Resep resep, String keyword) {
        if (keyword.isEmpty()) return true;

        String judul = resep.getJudul() == null ? "" : resep.getJudul().toLowerCase(Locale.ROOT);
        return judul.contains(keyword);
    }

    private boolean cocokKategori(Resep resep) {
        if ("Semua".equalsIgnoreCase(kategoriAktif)) return true;

        String kategoriResep = resep.getJenisMakanan();
        if (kategoriResep == null) return false;

        return kategoriResep.equalsIgnoreCase(kategoriAktif);
    }

    private boolean cocokBahan(Resep resep) {
        if (listBahanTerpilih.isEmpty()) return true;

        String bahanResep = resep.getBahan() == null ? "" : resep.getBahan().toLowerCase(Locale.ROOT);

        for (String bahan : listBahanTerpilih) {
            if (!bahanResep.contains(bahan.toLowerCase(Locale.ROOT))) {
                return false;
            }
        }

        return true;
    }

    private boolean containsIgnoreCase(List<String> list, String value) {
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void tampilkanKeLayar(List<Resep> daftarResep) {
        resepContainer.getChildren().clear();

        if (daftarResep == null || daftarResep.isEmpty()) {
            Label kosong = new Label("Tidak ada resep yang cocok.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
            resepContainer.getChildren().add(kosong);
            return;
        }

        for (Resep resep : daftarResep) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/felix_71241153/app/haphaphap/itemResep.fxml")
                );

                VBox card = loader.load();

                itemResepController controller = loader.getController();
                controller.setData(resep);

                card.setOnMouseClicked(event -> bukaHalamanDetail(resep, event));

                resepContainer.getChildren().add(card);

            } catch (Exception e) {
                System.out.println("Gagal memuat itemResep.fxml");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/felix_71241153/app/haphaphap/login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Gagal memuat halaman login.");
        }
    }

    private void bukaHalamanDetail(Resep resep, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/felix_71241153/app/haphaphap/detail.fxml")
            );

            Parent root = loader.load();

            detailController controller = loader.getController();
            controller.setResepData(resep);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("Gagal membuka halaman detail!");
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
