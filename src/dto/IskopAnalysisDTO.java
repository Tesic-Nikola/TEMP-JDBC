package dto;

public class IskopAnalysisDTO {
    private int iskopID;
    private String nazivIskopa;
    private String nazivRudnika;
    private String lokacijaRudnika;
    private int povrsinaHa;
    private int ukupnoJalovista;
    private int aktivnaJalovista;
    private int ukupnoRadnihLokacija;
    private int aktivnihLokacija;
    private double jalovistaDensity; // jalovista per hectare
    private String dominantniTipMaterijala;

    public IskopAnalysisDTO() {
        super();
    }

    public IskopAnalysisDTO(int iskopID, String nazivIskopa, String nazivRudnika, String lokacijaRudnika,
                            int povrsinaHa, int ukupnoJalovista, int aktivnaJalovista,
                            int ukupnoRadnihLokacija, int aktivnihLokacija,
                            double jalovistaDensity, String dominantniTipMaterijala) {
        this.iskopID = iskopID;
        this.nazivIskopa = nazivIskopa;
        this.nazivRudnika = nazivRudnika;
        this.lokacijaRudnika = lokacijaRudnika;
        this.povrsinaHa = povrsinaHa;
        this.ukupnoJalovista = ukupnoJalovista;
        this.aktivnaJalovista = aktivnaJalovista;
        this.ukupnoRadnihLokacija = ukupnoRadnihLokacija;
        this.aktivnihLokacija = aktivnihLokacija;
        this.jalovistaDensity = jalovistaDensity;
        this.dominantniTipMaterijala = dominantniTipMaterijala;
    }

    // Getters and Setters
    public int getIskopID() {
        return iskopID;
    }

    public void setIskopID(int iskopID) {
        this.iskopID = iskopID;
    }

    public String getNazivIskopa() {
        return nazivIskopa;
    }

    public void setNazivIskopa(String nazivIskopa) {
        this.nazivIskopa = nazivIskopa;
    }

    public String getNazivRudnika() {
        return nazivRudnika;
    }

    public void setNazivRudnika(String nazivRudnika) {
        this.nazivRudnika = nazivRudnika;
    }

    public String getLokacijaRudnika() {
        return lokacijaRudnika;
    }

    public void setLokacijaRudnika(String lokacijaRudnika) {
        this.lokacijaRudnika = lokacijaRudnika;
    }

    public int getPovrsinaHa() {
        return povrsinaHa;
    }

    public void setPovrsinaHa(int povrsinaHa) {
        this.povrsinaHa = povrsinaHa;
    }

    public int getUkupnoJalovista() {
        return ukupnoJalovista;
    }

    public void setUkupnoJalovista(int ukupnoJalovista) {
        this.ukupnoJalovista = ukupnoJalovista;
    }

    public int getAktivnaJalovista() {
        return aktivnaJalovista;
    }

    public void setAktivnaJalovista(int aktivnaJalovista) {
        this.aktivnaJalovista = aktivnaJalovista;
    }

    public int getUkupnoRadnihLokacija() {
        return ukupnoRadnihLokacija;
    }

    public void setUkupnoRadnihLokacija(int ukupnoRadnihLokacija) {
        this.ukupnoRadnihLokacija = ukupnoRadnihLokacija;
    }

    public int getAktivnihLokacija() {
        return aktivnihLokacija;
    }

    public void setAktivnihLokacija(int aktivnihLokacija) {
        this.aktivnihLokacija = aktivnihLokacija;
    }

    public double getJalovistaDensity() {
        return jalovistaDensity;
    }

    public void setJalovistaDensity(double jalovistaDensity) {
        this.jalovistaDensity = jalovistaDensity;
    }

    public String getDominantniTipMaterijala() {
        return dominantniTipMaterijala;
    }

    public void setDominantniTipMaterijala(String dominantniTipMaterijala) {
        this.dominantniTipMaterijala = dominantniTipMaterijala;
    }

    @Override
    public String toString() {
        return String.format("%-4d %-25.25s %-18.18s %-12.12s %-8d %-8d %-8d %-8d %-8d %-8.3f %-20.20s",
                iskopID, nazivIskopa, nazivRudnika, lokacijaRudnika, povrsinaHa,
                ukupnoJalovista, aktivnaJalovista, ukupnoRadnihLokacija, aktivnihLokacija,
                jalovistaDensity, dominantniTipMaterijala);
    }

    public static String getFormattedHeader() {
        return String.format("%-4s %-25s %-18s %-12s %-8s %-8s %-8s %-8s %-8s %-8s %-20s",
                "ID", "NAZIV_ISKOPA", "RUDNIK", "LOKACIJA", "POV_HA", "UK_JAL", "AKT_JAL",
                "UK_LOK", "AKT_LOK", "DENSITY", "DOMINANT_MATERIJAL");
    }
}