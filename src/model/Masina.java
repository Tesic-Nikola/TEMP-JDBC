package model;

public class Masina {
    private int masinaID;
    private int rudnikID;
    private String naziv;
    private String tip;
    private String status;
    private int godinaProizvodnje;
    private Integer kapacitet; // nullable

    public Masina() {
        super();
    }

    public Masina(int masinaID, int rudnikID, String naziv, String tip, String status,
                  int godinaProizvodnje, Integer kapacitet) {
        this.masinaID = masinaID;
        this.rudnikID = rudnikID;
        this.naziv = naziv;
        this.tip = tip;
        this.status = status;
        this.godinaProizvodnje = godinaProizvodnje;
        this.kapacitet = kapacitet;
    }

    // Getters and Setters
    public int getMasinaID() {
        return masinaID;
    }

    public void setMasinaID(int masinaID) {
        this.masinaID = masinaID;
    }

    public int getRudnikID() {
        return rudnikID;
    }

    public void setRudnikID(int rudnikID) {
        this.rudnikID = rudnikID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGodinaProizvodnje() {
        return godinaProizvodnje;
    }

    public void setGodinaProizvodnje(int godinaProizvodnje) {
        this.godinaProizvodnje = godinaProizvodnje;
    }

    public Integer getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(Integer kapacitet) {
        this.kapacitet = kapacitet;
    }

    @Override
    public String toString() {
        return String.format("%-8d %-8d %-30.30s %-25.25s %-18.18s %-8d %-12s",
                masinaID, rudnikID, naziv, tip, status, godinaProizvodnje,
                kapacitet != null ? kapacitet.toString() : "N/A");
    }

    public static String getFormattedHeader() {
        return String.format("   %-8s %-8s %-30s %-25s %-18s %-8s %-12s",
                "ID", "RUD_ID", "NAZIV", "TIP", "STATUS", "GODINA", "KAPACITET");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Masina masina = (Masina) obj;
        return masinaID == masina.masinaID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(masinaID);
    }
}