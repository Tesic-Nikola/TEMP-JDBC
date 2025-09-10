package model;

import java.sql.Time;
import java.util.Date;

public class Rudnik {
    private int rudnikID;
    private String naziv;
    private String lokacija;
    private Date datumOsnivanja;
    private Time pocetakRadnogVremena;  // Changed to Time
    private Time krajRadnogVremena;     // Changed to Time

    public Rudnik() {
        super();
    }

    public Rudnik(int rudnikID, String naziv, String lokacija, Date datumOsnivanja,
                  Time pocetakRadnogVremena, Time krajRadnogVremena) {
        this.rudnikID = rudnikID;
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.datumOsnivanja = datumOsnivanja;
        this.pocetakRadnogVremena = pocetakRadnogVremena;
        this.krajRadnogVremena = krajRadnogVremena;
    }

    // Constructor without ID for new records (auto-increment)
    public Rudnik(String naziv, String lokacija, Date datumOsnivanja,
                  Time pocetakRadnogVremena, Time krajRadnogVremena) {
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.datumOsnivanja = datumOsnivanja;
        this.pocetakRadnogVremena = pocetakRadnogVremena;
        this.krajRadnogVremena = krajRadnogVremena;
    }

    // Getters and Setters
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

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Date getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(Date datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }

    public Time getPocetakRadnogVremena() {
        return pocetakRadnogVremena;
    }

    public void setPocetakRadnogVremena(Time pocetakRadnogVremena) {
        this.pocetakRadnogVremena = pocetakRadnogVremena;
    }

    public Time getKrajRadnogVremena() {
        return krajRadnogVremena;
    }

    public void setKrajRadnogVremena(Time krajRadnogVremena) {
        this.krajRadnogVremena = krajRadnogVremena;
    }

    @Override
    public String toString() {
        return String.format("%-6d %-20.20s %-15.15s %-20.20s %-10.10s %-10.10s",
                rudnikID, naziv, lokacija,
                datumOsnivanja != null ? datumOsnivanja.toString() : "N/A",
                pocetakRadnogVremena != null ? pocetakRadnogVremena.toString() : "N/A",
                krajRadnogVremena != null ? krajRadnogVremena.toString() : "N/A");
    }

    public static String getFormattedHeader() {
        return String.format("%-6s %-20s %-15s %-20s %-10s %-10s",
                "ID", "NAZIV", "LOKACIJA", "DATUM OSNIVANJA", "POČETAK", "KRAJ");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rudnik rudnik = (Rudnik) obj;
        return rudnikID == rudnik.rudnikID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rudnikID);
    }
}