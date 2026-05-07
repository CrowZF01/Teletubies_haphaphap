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
    @FXML
    private TextField inputBahanField;
    @FXML
    private FlowPane tagContainer;
    @FXML
    private FlowPane resepContainer;
    private List<String> listBahanTerpilih = new java.util.ArrayList<>();
    public void initialize() {
        tampilkanResep();
    }

    public void tampilkanResep() {
        resepDB db = new resepDB();
        List<Resep> listResep = db.getAllResep();

        resepContainer.getChildren().clear();

        for (Resep resep : listResep) {
            try {
                // Memuat template FXML untuk tiap item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemResep.fxml"));
                VBox card = loader.load();

                // Mengirim data resep ke controller item tersebut
                itemResepController controller = loader.getController();
                controller.setData(resep);

                // Masukkan ke layar utama
                resepContainer.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleTambahBahan() {
        String bahan = inputBahanField.getText().trim();
        // Cek agar tidak kosong dan tidak memasukkan bahan yang sama 2x
        if (!bahan.isEmpty() && !listBahanTerpilih.contains(bahan)) {
            listBahanTerpilih.add(bahan);
            inputBahanField.clear();
            renderTags(); // Perbarui tampilan
        }
    }

    private void renderTags() {
        tagContainer.getChildren().clear(); // Bersihkan layar dulu

        for (String bahan : listBahanTerpilih) {
            Label tag = new Label(bahan + "  ✕");
            tag.setStyle("-fx-background-color: #FBE2D1; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-padding: 5 8; -fx-background-radius: 4; -fx-cursor: hand;");

            // Logika ketika tag di-klik (Hapus dari list)
            tag.setOnMouseClicked(e -> {
                listBahanTerpilih.remove(bahan);
                renderTags(); // Perbarui tampilan setelah dihapus
            });

            tagContainer.getChildren().add(tag);
        }
    }

    @FXML
    public void handleTerapkanFilter() {
        resepDB db = new resepDB();
        List<Resep> hasilFilter;

        if (listBahanTerpilih.isEmpty()) {
            hasilFilter = db.getAllResep(); // Jika kosong, tampilkan semua
        } else {
            hasilFilter = db.filterBerdasarkanBahan(listBahanTerpilih); // Jika ada, filter!
        }

        // Bersihkan dan masukkan ulang resep ke layar sesuai hasil filter
        resepContainer.getChildren().clear();
        for (Resep resep : hasilFilter) {
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/felix_71241153/app/haphaphap/itemResep.fxml"));
                javafx.scene.layout.VBox card = loader.load();
                itemResepController controller = loader.getController();
                controller.setData(resep);
                resepContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

