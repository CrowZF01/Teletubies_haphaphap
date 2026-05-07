package controller;

import database.resepDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Resep;
import java.util.List;

public class homeController {
    @FXML private TextField inputBahanField;
    @FXML private FlowPane tagContainer;
    @FXML private FlowPane resepContainer;

    private List<String> listBahanTerpilih = new java.util.ArrayList<>();

    // Saat aplikasi dibuka, langsung tampilkan semua resep
    public void initialize() {
        resepDB db = new resepDB();
        List<Resep> listSemuaResep = db.getAllResep();
        tampilkanKeLayar(listSemuaResep);
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
}