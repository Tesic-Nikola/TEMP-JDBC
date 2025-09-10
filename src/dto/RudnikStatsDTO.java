package dto;

public class RudnikStatsDTO {
    private int rudnikID;
    private String nazivRudnika;
    private String lokacija;
    private int ukupnoMasina;
    private int aktivneMasine;
    private double prosecniKapacitet;
    private int ukupnoIskopa;
    private int aktivnihLokacija;

    public RudnikStatsDTO() {
        super();
    }

    public RudnikStatsDTO(int rudnikID, String nazivRudnika, String lokacija,
                          int ukupnoMasina, int aktivneMasine, double prosecniKapacitet) {
        this.rudnikID = rudnikID;
        this.nazivRudnika = nazivRudnika;
        this.lokacija = lokacija;
        this.ukupnoMasina = ukupnoMasina;
        this.aktivneMasine = aktivneMasine;
        this.prosecniKapacitet = prosecniKapacitet;
    }

    public RudnikStatsDTO(int rudnikID, String nazivRudnika, String lokacija,
                          int ukupnoMasina, int aktivneMasine, double prosecniKapacitet,
                          int ukupnoIskopa, int aktivnihLokacija) {
        this.rudnikID = rudnikID;
        this.nazivRudnika = nazivRudnika;
        this.lokacija = lokacija;
        this.ukupnoMasina = ukupnoMasina;
        this.aktivneMasine = aktivneMasine;
        this.prosecniKapacitet = prosecniKapacitet;
        this.ukupnoIskopa = ukupnoIskopa;
        this.aktivnihLokacija = aktivnihLokacija;
    }

    // Getters and Setters
    public int getRudnikID() {
        return rudnikID;
    }

    public void setRudnikID(int rudnikID) {
        this.rudnikID = rudnikID;
    }

    public String getNazivRudnika() {
        return nazivRudnika;
    }

    public void setNazivRudnika(String nazivRudnika) {
        this.nazivRudnika = nazivRudnika;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public int getUkupnoMasina() {
        return ukupnoMasina;
    }

    public void setUkupnoMasina(int ukupnoMasina) {
        this.ukupnoMasina = ukupnoMasina;
    }

    public int getAktivneMasine() {
        return aktivneMasine;
    }

    public void setAktivneMasine(int aktivneMasine) {
        this.aktivneMasine = aktivneMasine;
    }

    public double getProsecniKapacitet() {
        return prosecniKapacitet;
    }

    public void setProsecniKapacitet(double prosecniKapacitet) {
        this.prosecniKapacitet = prosecniKapacitet;
    }

    public int getUkupnoIskopa() {
        return ukupnoIskopa;
    }

    public void setUkupnoIskopa(int ukupnoIskopa) {
        this.ukupnoIskopa = ukupnoIskopa;
    }

    public int getAktivnihLokacija() {
        return aktivnihLokacija;
    }

    public void setAktivnihLokacija(int aktivnihLokacija) {
        this.aktivnihLokacija = aktivnihLokacija;
    }

    @Override
    public String toString() {
        return String.format("%-6d %-20.20s %-15.15s %-8d %-8d %-12.2f %-8d %-8d",
                rudnikID, nazivRudnika, lokacija, ukupnoMasina, aktivneMasine,
                prosecniKapacitet, ukupnoIskopa, aktivnihLokacija);
    }

    public static String getFormattedHeader() {
        return String.format("%-6s %-20s %-15s %-8s %-8s %-12s %-8s %-8s",
                "ID", "NAZIV", "LOKACIJA", "UK_MAŠ", "AKT_MAŠ", "PROS_KAP", "UK_ISK", "AKT_LOK");
    }
}