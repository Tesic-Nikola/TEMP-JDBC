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

    /**
     * Transaction 2: Machine transfer between mines
     */
    private void performMachineTransfer() {
        try {
            System.out.println("\n=== TRANSFER MAŠINA IZMEĐU RUDNIKA ===");

            // Step 1: Show all mines and let user pick source mine
            RudnikService rudnikService = new RudnikService();
            MasinaDAO masinaDAO = new MasinaDAOImpl();  // Use the real DAO

            List<Rudnik> allMines = rudnikService.getAll();

            System.out.println("\n=== IZABERITE IZVORNI RUDNIK ===");
            for (int i = 0; i < allMines.size(); i++) {
                Rudnik mine = allMines.get(i);
                System.out.printf("%d - %s (%s)%n", i + 1, mine.getNaziv(), mine.getLokacija());
            }

            System.out.print("Izaberite broj izvornog rudnika: ");
            int sourceChoice = Integer.parseInt(MainUIHandler.sc.nextLine()) - 1;

            if (sourceChoice < 0 || sourceChoice >= allMines.size()) {
                System.out.println("Neispravna opcija!");
                return;
            }

            Rudnik sourceMine = allMines.get(sourceChoice);
            int fromRudnikId = sourceMine.getRudnikID();

            // Step 2: Show machines from selected mine using DAO
            List<Masina> machinesInMine = masinaDAO.findByRudnikId(fromRudnikId);

            if (machinesInMine.isEmpty()) {
                System.out.println("Nema mašina u odabranom rudniku!");
                return;
            }

            System.out.println("\n=== MAŠINE U RUDNIKU: " + sourceMine.getNaziv() + " ===");
            System.out.println(Masina.getFormattedHeader());
            System.out.println("=".repeat(120));

            for (int i = 0; i < machinesInMine.size(); i++) {
                System.out.printf("%d - %s%n", i + 1, machinesInMine.get(i).toString());
            }

            System.out.print("Izaberite mašine za transfer (brojevi odvojeni zarezom): ");
            String input = MainUIHandler.sc.nextLine().trim();

            List<Integer> selectedMachineIds = new ArrayList<>();
            String[] choices = input.split(",");

            for (String choice : choices) {
                try {
                    int index = Integer.parseInt(choice.trim()) - 1;
                    if (index >= 0 && index < machinesInMine.size()) {
                        selectedMachineIds.add(machinesInMine.get(index).getMasinaID());
                    } else {
                        System.out.println("Neispravna opcija: " + choice);
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Neispravna vrednost: " + choice);
                    return;
                }
            }

            // Step 3: Show destination mines (excluding source)
            System.out.println("\n=== IZABERITE ODREDIŠNI RUDNIK ===");
            List<Rudnik> destinationMines = new ArrayList<>();

            for (Rudnik mine : allMines) {
                if (mine.getRudnikID() != fromRudnikId) {
                    destinationMines.add(mine);
                    System.out.printf("%d - %s (%s)%n",
                            destinationMines.size(), mine.getNaziv(), mine.getLokacija());
                }
            }

            System.out.print("Izaberite broj odredišnog rudnika: ");
            int destChoice = Integer.parseInt(MainUIHandler.sc.nextLine()) - 1;

            if (destChoice < 0 || destChoice >= destinationMines.size()) {
                System.out.println("Neispravna opcija!");
                return;
            }

            Rudnik destinationMine = destinationMines.get(destChoice);
            int toRudnikId = destinationMine.getRudnikID();

            // Step 4: Show transfer summary
            System.out.println("\n=== PREGLED TRANSFERA ===");
            System.out.println("Iz rudnika: " + sourceMine.getNaziv());
            System.out.println("U rudnik: " + destinationMine.getNaziv());
            System.out.println("Broj mašina: " + selectedMachineIds.size());

            System.out.println("\nMašine za transfer:");
            for (Integer id : selectedMachineIds) {
                Masina machine = machinesInMine.stream()
                        .filter(m -> m.getMasinaID() == id)
                        .findFirst().orElse(null);
                if (machine != null) {
                    System.out.println("- " + machine.getNaziv() + " (" + machine.getTip() + ")");
                }
            }

            System.out.print("\nPotvrdjujete transfer? (da/ne): ");
            String confirmation = MainUIHandler.sc.nextLine();

            if (!confirmation.equalsIgnoreCase("da")) {
                System.out.println("Transfer je otkazan.");
                return;
            }

            // Step 5: Execute transfer
            boolean success = transactionService.transferMachinesBetweenMines(
                    selectedMachineIds, fromRudnikId, toRudnikId);

            if (success) {
                System.out.println("Transfer je uspešno izvršen!");
                System.out.println("- Mašine su prebačene iz '" + sourceMine.getNaziv() +
                        "' u '" + destinationMine.getNaziv() + "'");
                System.out.println("- Ukupno transferovano: " + selectedMachineIds.size() + " mašina");
            } else {
                System.out.println("Transfer nije uspešan!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Neispravna vrednost!");
        } catch (Exception e) {
            System.out.println("Greška pri transferu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}