package model;

public class Resep {
    private int idResep;
    private String judul;
    private String jenisMakanan;
    private int tingkatKepedasan;
    private String bahan;
    private String langkahPembuatan;
    private int estimasiWaktu;

    public Resep() {
    }

    public Resep(int idResep, String judul, String jenisMakanan, int tingkatKepedasan,
                 String bahan, String langkahPembuatan, int estimasiWaktu) {
        this.idResep = idResep;
        this.judul = judul;
        this.jenisMakanan = jenisMakanan;
        this.tingkatKepedasan = tingkatKepedasan;
        this.bahan = bahan;
        this.langkahPembuatan = langkahPembuatan;
        this.estimasiWaktu = estimasiWaktu;
    }

    public int getIdResep() {
        return idResep;
    }

    public String getJudul() {
        return judul;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    public int getTingkatKepedasan() {
        return tingkatKepedasan;
    }

    public String getBahan() {
        return bahan;
    }

    public String getLangkahPembuatan() {
        return langkahPembuatan;
    }

    public int getEstimasiWaktu() {
        return estimasiWaktu;
    }

    public String getDetailResep() {
        return judul + "\n\nBahan:\n" + bahan + "\n\nLangkah:\n" + langkahPembuatan;
    }
}