package ui_handler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Rudnik;
import service.RudnikService;

public class RudnikUIHandler {

    private static final RudnikService rudnikService = new RudnikService();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public void handleRudnikMenu() {
        String answer;
        do {
            System.out.println("\n=== UPRAVLJANJE RUDNICIMA ===");
            System.out.println("1 - Prikaz svih rudnika");
            System.out.println("2 - Prikaz rudnika po ID-ju");
            System.out.println("3 - Dodavanje novog rudnika");
            System.out.println("4 - Ažuriranje rudnika");
            System.out.println("5 - Brisanje rudnika");
            System.out.println("6 - Broj rudnika u sistemu");
            System.out.println("X - Povratak na glavni meni");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
                case "1":
                    showAllRudnici();
                    break;
                case "2":
                    showRudnikById();
                    break;
                case "3":
                    addNewRudnik();
                    break;
                case "4":
                    updateRudnik();
                    break;
                case "5":
                    deleteRudnik();
                    break;
                case "6":
                    showRudnikCount();
                    break;
                case "X":
                case "x":
                    break;
                default:
                    System.out.println("Neispravna opcija! Molim pokušajte ponovo.");
            }

        } while (!answer.equalsIgnoreCase("X"));
    }

    private void showAllRudnici() {
        try {
            System.out.println("\n=== SVI RUDNICI ===");
            System.out.println(Rudnik.getFormattedHeader());
            System.out.println("=========================================");

            for (Rudnik rudnik : rudnikService.getAll()) {
                System.out.println(rudnik);
            }
        } catch (SQLException e) {
            System.out.println("Greška pri učitavanju rudnika: " + e.getMessage());
        }
    }

    private void showRudnikById() {
        try {
            System.out.print("Unesite ID rudnika: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());

            Rudnik rudnik = rudnikService.getById(id);
            if (rudnik != null) {
                System.out.println("\n=== RUDNIK ===");
                System.out.println(Rudnik.getFormattedHeader());
                System.out.println("==================================");
                System.out.println(rudnik);
            } else {
                System.out.println("Rudnik sa ID " + id + " nije pronađen!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost za ID!");
        } catch (SQLException e) {
            System.out.println("Greška pri pretraživanju rudnika: " + e.getMessage());
        }
    }

    private void addNewRudnik() {
        try {
            System.out.println("\n=== DODAVANJE NOVOG RUDNIKA ===");

            System.out.print("ID rudnika: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());

            if (rudnikService.existsById(id)) {
                System.out.println("Rudnik sa ID " + id + " već postoji!");
                return;
            }

            System.out.print("Naziv rudnika: ");
            String naziv = MainUIHandler.sc.nextLine();

            System.out.print("Lokacija: ");
            String lokacija = MainUIHandler.sc.nextLine();

            System.out.print("Datum osnivanja (dd.MM.yyyy): ");
            Date datumOsnivanja = dateFormat.parse(MainUIHandler.sc.nextLine());

            System.out.print("Početak radnog vremena (dd.MM.yyyy HH:mm) - opciono: ");
            String pocetakStr = MainUIHandler.sc.nextLine().trim();
            Date pocetakRadnogVremena = null;
            if (!pocetakStr.isEmpty()) {
                pocetakRadnogVremena = dateTimeFormat.parse(pocetakStr);
            }

            System.out.print("Kraj radnog vremena (dd.MM.yyyy HH:mm) - opciono: ");
            String krajStr = MainUIHandler.sc.nextLine().trim();
            Date krajRadnogVremena = null;
            if (!krajStr.isEmpty()) {
                krajRadnogVremena = dateTimeFormat.parse(krajStr);
            }

            Rudnik newRudnik = new Rudnik(id, naziv, lokacija, datumOsnivanja,
                    pocetakRadnogVremena, krajRadnogVremena);

            if (rudnikService.save(newRudnik)) {
                System.out.println("Rudnik je uspešno dodat!");
            } else {
                System.out.println("Greška pri dodavanju rudnika!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost za ID!");
        } catch (ParseException e) {
            System.out.println("Neispravna format datuma! Koristite format dd.MM.yyyy ili dd.MM.yyyy HH:mm");
        } catch (SQLException e) {
            System.out.println("Greška pri dodavanju rudnika: " + e.getMessage());
        }
    }

    private void updateRudnik() {
        try {
            System.out.print("Unesite ID rudnika za ažuriranje: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());

            Rudnik existingRudnik = rudnikService.getById(id);
            if (existingRudnik == null) {
                System.out.println("Rudnik sa ID " + id + " nije pronađen!");
                return;
            }

            System.out.println("Trenutni podaci:");
            System.out.println(Rudnik.getFormattedHeader());
            System.out.println(existingRudnik);

            System.out.println("\n=== AŽURIRANJE RUDNIKA ===");
            System.out.println("Pritisnite Enter da zadržite trenutnu vrednost");

            System.out.print("Novi naziv [" + existingRudnik.getNaziv() + "]: ");
            String naziv = MainUIHandler.sc.nextLine().trim();
            if (naziv.isEmpty()) naziv = existingRudnik.getNaziv();

            System.out.print("Nova lokacija [" + existingRudnik.getLokacija() + "]: ");
            String lokacija = MainUIHandler.sc.nextLine().trim();
            if (lokacija.isEmpty()) lokacija = existingRudnik.getLokacija();

            System.out.print("Novi datum osnivanja (dd.MM.yyyy) [" +
                    dateFormat.format(existingRudnik.getDatumOsnivanja()) + "]: ");
            String datumStr = MainUIHandler.sc.nextLine().trim();
            Date datumOsnivanja = existingRudnik.getDatumOsnivanja();
            if (!datumStr.isEmpty()) {
                datumOsnivanja = dateFormat.parse(datumStr);
            }

            Rudnik updatedRudnik = new Rudnik(id, naziv, lokacija, datumOsnivanja,
                    existingRudnik.getPocetakRadnogVremena(),
                    existingRudnik.getKrajRadnogVremena());

            if (rudnikService.save(updatedRudnik)) {
                System.out.println("Rudnik je uspešno ažuriran!");
            } else {
                System.out.println("Greška pri ažuriranju rudnika!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost za ID!");
        } catch (ParseException e) {
            System.out.println("Neispravna format datuma! Koristite format dd.MM.yyyy");
        } catch (SQLException e) {
            System.out.println("Greška pri ažuriranju rudnika: " + e.getMessage());
        }
    }

    private void deleteRudnik() {
        try {
            System.out.print("Unesite ID rudnika za brisanje: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());

            Rudnik rudnik = rudnikService.getById(id);
            if (rudnik == null) {
                System.out.println("Rudnik sa ID " + id + " nije pronađen!");
                return;
            }

            System.out.println("Rudnik za brisanje:");
            System.out.println(Rudnik.getFormattedHeader());
            System.out.println(rudnik);

            System.out.print("Da li ste sigurni da želite da obrišete ovaj rudnik? (da/ne): ");
            String potvrda = MainUIHandler.sc.nextLine();

            if (potvrda.equalsIgnoreCase("da")) {
                if (rudnikService.deleteById(id)) {
                    System.out.println("Rudnik je uspešno obrisan!");
                } else {
                    System.out.println("Greška pri brisanju rudnika!");
                }
            } else {
                System.out.println("Brisanje je otkazano.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost za ID!");
        } catch (SQLException e) {
            System.out.println("Greška pri brisanju rudnika: " + e.getMessage());
        }
    }

    private void showRudnikCount() {
        try {
            int count = rudnikService.count();
            System.out.println("Ukupan broj rudnika u sistemu: " + count);
        } catch (SQLException e) {
            System.out.println("Greška pri brojanju rudnika: " + e.getMessage());
        }
    }
}