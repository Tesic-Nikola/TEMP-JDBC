package ui_handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import service.TransactionService;
import service.RudnikService;          // Add this
import dao.MasinaDAO;                  // Add this
import dao.impl.MasinaDAOImpl;         // Add this
import model.Rudnik;                   // Add this
import model.Masina;                   // Add this

public class TransactionUIHandler {

    private static final TransactionService transactionService = new TransactionService();

    public void handleTransactionMenu() {
        String answer;
        do {
            System.out.println("\n=== TRANSAKCIJE ===");
            System.out.println("1 - Servisiranje mašina");
            System.out.println("2 - Obrada rezultata inspekcije");
            System.out.println("X - Povratak na glavni meni");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
                case "1":
                    performMachineMaintenance();
                    break;
                case "2":
                    performInspectionResponse();
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

            // Step 1: Show all mines and let user pick
            RudnikService rudnikService = new RudnikService();
            MasinaDAO masinaDAO = new MasinaDAOImpl();

            List<Rudnik> allMines = rudnikService.getAll();

            System.out.println("=== IZABERITE RUDNIK ===");
            for (int i = 0; i < allMines.size(); i++) {
                Rudnik mine = allMines.get(i);
                System.out.printf("%d - %s (%s)%n", i + 1, mine.getNaziv(), mine.getLokacija());
            }

            System.out.print("Izaberite broj rudnika: ");
            int mineChoice = Integer.parseInt(MainUIHandler.sc.nextLine()) - 1;

            if (mineChoice < 0 || mineChoice >= allMines.size()) {
                System.out.println("Neispravna opcija!");
                return;
            }

            Rudnik selectedMine = allMines.get(mineChoice);

            // Step 2: Show machines from selected mine
            List<Masina> machinesInMine = masinaDAO.findByRudnikId(selectedMine.getRudnikID());

            if (machinesInMine.isEmpty()) {
                System.out.println("Nema mašina u odabranom rudniku!");
                return;
            }

            // Filter only machines that are NOT already in service
            List<Masina> availableMachines = new ArrayList<>();
            for (Masina machine : machinesInMine) {
                if (!"Servis".equals(machine.getStatus())) {
                    availableMachines.add(machine);
                }
            }

            if (availableMachines.isEmpty()) {
                System.out.println("Sve mašine u rudniku '" + selectedMine.getNaziv() + "' su već na servisu!");
                return;
            }

            System.out.println("\n=== MAŠINE U RUDNIKU: " + selectedMine.getNaziv() + " ===");
            System.out.println("(Prikazane su samo mašine koje NISU na servisu)");
            System.out.println(Masina.getFormattedHeader());
            System.out.println("=".repeat(120));

            for (int i = 0; i < availableMachines.size(); i++) {
                System.out.printf("%d - %s%n", i + 1, availableMachines.get(i).toString());
            }

            System.out.print("\nIzaberite mašine za servisiranje (brojevi odvojeni zarezom): ");
            String input = MainUIHandler.sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Nije odabrana nijedna mašina!");
                return;
            }

            List<Integer> selectedMachineIds = new ArrayList<>();
            String[] choices = input.split(",");

            for (String choice : choices) {
                try {
                    int index = Integer.parseInt(choice.trim()) - 1;
                    if (index >= 0 && index < availableMachines.size()) {
                        selectedMachineIds.add(availableMachines.get(index).getMasinaID());
                    } else {
                        System.out.println("Neispravna opcija: " + choice);
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Neispravna vrednost: " + choice);
                    return;
                }
            }

            // Step 3: Show maintenance summary
            System.out.println("\n=== PREGLED SERVISIRANJA ===");
            System.out.println("Rudnik: " + selectedMine.getNaziv());
            System.out.println("Broj mašina za servis: " + selectedMachineIds.size());

            System.out.println("\nMašine koje će ići na servis:");
            for (Integer id : selectedMachineIds) {
                Masina machine = availableMachines.stream()
                        .filter(m -> m.getMasinaID() == id)
                        .findFirst().orElse(null);
                if (machine != null) {
                    System.out.println("- " + machine.getNaziv() + " (" + machine.getTip() +
                            ") - trenutno: " + machine.getStatus());
                }
            }

            System.out.print("\nPotvrdjujete servisiranje? (da/ne): ");
            String confirmation = MainUIHandler.sc.nextLine();

            if (!confirmation.equalsIgnoreCase("da")) {
                System.out.println("Servisiranje je otkazano.");
                return;
            }

            // Step 4: Execute transaction
            boolean success = transactionService.performMachineMaintenanceTransaction(selectedMachineIds);

            if (success) {
                System.out.println("✓ Servisiranje je uspešno izvršeno!");
                System.out.println("- Status mašina je promenjen na 'Servis'");
                System.out.println("- Dodani su zapisi o održavanju u tabelu Servis");
                System.out.println("- Ukupno mašina poslato na servis: " + selectedMachineIds.size());
            } else {
                System.out.println("✗ Servisiranje nije uspešno izvršeno!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost!");
        } catch (SQLException e) {
            System.out.println("Greška pri izvršavanju transakcije: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Neočekivana greška: " + e.getMessage());
        }
    }

    private void performInspectionResponse() {
        try {
            System.out.println("\n=== OBRADA REZULTATA INSPEKCIJE ===");
            System.out.println("Ova transakcija će:");
            System.out.println("1. Ažurirati rezultat inspekcije");
            System.out.println("2. Promeniti status jalovišta");
            System.out.println("3. Deaktivirati radne lokacije ako je potrebno");

            System.out.println("\n=== DOSTUPNE INSPEKCIJE ===");
            System.out.println("1 - Inspekcija #1 (Jalovište #1)");
            System.out.println("2 - Inspekcija #2 (Jalovište #3)");
            System.out.println("3 - Inspekcija #3 (Jalovište #5)");
            System.out.println("4 - Inspekcija #4 (Jalovište #2)");
            System.out.println("5 - Inspekcija #5 (Jalovište #7)");

            System.out.print("Izaberite inspekciju (1-5): ");
            int choice = Integer.parseInt(MainUIHandler.sc.nextLine());

            if (choice < 1 || choice > 5) {
                System.out.println("Neispravna opcija!");
                return;
            }

            System.out.println("\n=== REZULTATI INSPEKCIJE ===");
            System.out.println("1 - Zadovoljava");
            System.out.println("2 - Potrebne mere");
            System.out.println("3 - Ne zadovoljava");

            System.out.print("Izaberite rezultat: ");
            int resultChoice = Integer.parseInt(MainUIHandler.sc.nextLine());

            String result;
            switch (resultChoice) {
                case 1: result = "Zadovoljava"; break;
                case 2: result = "Potrebne mere"; break;
                case 3: result = "Ne zadovoljava"; break;
                default:
                    System.out.println("Neispravna opcija!");
                    return;
            }

            System.out.printf("Inspekcija #%d - Rezultat: %s%n", choice, result);
            System.out.print("Potvrđujete obradu? (da/ne): ");

            if (!MainUIHandler.sc.nextLine().equalsIgnoreCase("da")) {
                System.out.println("Obrada otkazana.");
                return;
            }

            boolean success = transactionService.processInspectionResults(choice, result);

            if (success) {
                System.out.println("Inspekcija uspešno obrađena!");
                if ("Ne zadovoljava".equals(result)) {
                    System.out.println("- Jalovište je označeno za sanaciju");
                    System.out.println("- Radne lokacije su deaktivirane");
                }
            } else {
                System.out.println("Greška pri obradi inspekcije!");
            }

        } catch (Exception e) {
            System.out.println("Greška: " + e.getMessage());
        }
    }
}