package ui_handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import service.TransactionService;

public class TransactionUIHandler {

    private static final TransactionService transactionService = new TransactionService();

    public void handleTransactionMenu() {
        String answer;
        do {
            System.out.println("\n=== TRANSAKCIJE ===");
            System.out.println("1 - Servisiranje mašina (update Masina + insert Servis)");
            System.out.println("2 - Transfer mašina između rudnika");
            System.out.println("X - Povratak na glavni meni");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
                case "1":
                    performMachineMaintenance();
                    break;
                case "2":
                    performMachineTransfer();
                    break;
                case "X":
                case "x":
                    break;
                default:
                    System.out.println("Neispravna opcija! Molim pokušajte ponovo.");
            }

        } while (!answer.equalsIgnoreCase("X"));
    }

    /**
     * Transaction 1: Machine maintenance - updates Masina table and inserts into Servis table
     */
    private void performMachineMaintenance() {
        try {
            System.out.println("\n=== SERVISIRANJE MAŠINA ===");
            System.out.println("Ova transakcija će:");
            System.out.println("1. Promeniti status mašina na 'Servis'");
            System.out.println("2. Dodati zapis o održavanju u tabelu Servis");
            System.out.println();

            System.out.print("Unesite ID-jeve mašina za servisiranje (odvojeno zarezom): ");
            String input = MainUIHandler.sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Nije unesen nijedan ID mašine!");
                return;
            }

            List<Integer> masinaIds = new ArrayList<>();
            String[] idStrings = input.split(",");

            for (String idStr : idStrings) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    masinaIds.add(id);
                } catch (NumberFormatException e) {
                    System.out.println("Neispravna vrednost za ID: " + idStr);
                    return;
                }
            }

            System.out.println("Mašine za servisiranje: " + masinaIds);
            System.out.print("Da li želite da nastavite? (da/ne): ");
            String potvrda = MainUIHandler.sc.nextLine();

            if (!potvrda.equalsIgnoreCase("da")) {
                System.out.println("Transakcija je otkazana.");
                return;
            }

            // Execute transaction
            boolean success = transactionService.performMachineMaintenanceTransaction(masinaIds);

            if (success) {
                System.out.println("✓ Transakcija je uspešno izvršena!");
                System.out.println("- Status mašina je promenjen na 'Servis'");
                System.out.println("- Dodani su zapisi o održavanju");
                System.out.println("- Ukupno mašina: " + masinaIds.size());
            } else {
                System.out.println("✗ Transakcija nije uspešno izvršena!");
            }

        } catch (SQLException e) {
            System.out.println("Greška pri izvršavanju transakcije: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Neočekivana greška: " + e.getMessage());
        }
    }

    /**
     * Transaction 2: Machine transfer between mines
     */
    private void performMachineTransfer() {
        try {
            System.out.println("\n=== TRANSFER MAŠINA IZMEĐU RUDNIKA ===");
            System.out.println("Ova transakcija će:");
            System.out.println("1. Verifikovati da mašine pripadaju izvornom rudniku");
            System.out.println("2. Promeniti rudnik vlasništvo mašina");
            System.out.println("3. Pokušati da doda log transfere (ako tabela postoji)");
            System.out.println();

            System.out.print("Unesite ID izvornog rudnika: ");
            int fromRudnikId;
            try {
                fromRudnikId = Integer.parseInt(MainUIHandler.sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Neispravna vrednost za ID rudnika!");
                return;
            }

            System.out.print("Unesite ID odredišnog rudnika: ");
            int toRudnikId;
            try {
                toRudnikId = Integer.parseInt(MainUIHandler.sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Neispravna vrednost za ID rudnika!");
                return;
            }

            if (fromRudnikId == toRudnikId) {
                System.out.println("Izvorni i odredišni rudnik ne mogu biti isti!");
                return;
            }

            System.out.print("Unesite ID-jeve mašina za transfer (odvojeno zarezom): ");
            String input = MainUIHandler.sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Nije unesen nijedan ID mašine!");
                return;
            }

            List<Integer> masinaIds = new ArrayList<>();
            String[] idStrings = input.split(",");

            for (String idStr : idStrings) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    masinaIds.add(id);
                } catch (NumberFormatException e) {
                    System.out.println("Neispravna vrednost za ID: " + idStr);
                    return;
                }
            }

            System.out.println("\n=== PREGLED TRANSFERA ===");
            System.out.println("Izvorni rudnik ID: " + fromRudnikId);
            System.out.println("Odredišni rudnik ID: " + toRudnikId);
            System.out.println("Mašine za transfer: " + masinaIds);
            System.out.print("Da li želite da nastavite? (da/ne): ");
            String potvrda = MainUIHandler.sc.nextLine();

            if (!potvrda.equalsIgnoreCase("da")) {
                System.out.println("Transakcija je otkazana.");
                return;
            }

            // Execute transaction
            boolean success = transactionService.transferMachinesBetweenMines(masinaIds, fromRudnikId, toRudnikId);

            if (success) {
                System.out.println("✓ Transfer je uspešno izvršen!");
                System.out.println("- Mašine su prebačene u novi rudnik");
                System.out.println("- Status je postavljen na 'U radu'");
                System.out.println("- Ukupno mašina: " + masinaIds.size());
            } else {
                System.out.println("✗ Transfer nije uspešno izvršen!");
            }

        } catch (SQLException e) {
            System.out.println("Greška pri izvršavanju transfera: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Neočekivana greška: " + e.getMessage());
        }
    }
}