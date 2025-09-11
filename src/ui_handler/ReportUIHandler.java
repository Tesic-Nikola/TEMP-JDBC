package ui_handler;

import java.sql.SQLException;
import java.util.List;

import dto.RudnikStatsDTO;
import dto.IskopAnalysisDTO;
import service.RudnikService;

public class ReportUIHandler {

    private static final RudnikService rudnikService = new RudnikService();

    public void handleReportMenu() {
        String answer;
        do {
            System.out.println("\n=== IZVEŠTAJI I STATISTIKE ===");
            System.out.println("1 - Jednostavan izveštaj - Rudnici sa statistikama mašina");
            System.out.println("2 - Kompleksan izveštaj 1 - Detaljni pregled rudnika");
            System.out.println("3 - Kompleksan izveštaj 2 - Analiza iskopa sa jalovištima");
            System.out.println("X - Povratak na glavni meni");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
                case "1":
                    showSimpleReport();
                    break;
                case "2":
                    showComplexReport();
                    break;
                case "3":
                    showComplexIskopAnalysisReport();
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
     * Simple query report - join two tables with aggregation functions
     * Shows mines with machine statistics
     */
    private void showSimpleReport() {
        try {
            System.out.println("\n=== JEDNOSTAVAN IZVEŠTAJ: RUDNICI SA STATISTIKAMA MAŠINA ===");
            System.out.println("Prikazuje rudnike sa brojem mašina i prosečnim kapacitetom");
            System.out.println();

            List<RudnikStatsDTO> stats = rudnikService.getRudnikWithMasinaStats();

            if (stats.isEmpty()) {
                System.out.println("Nema podataka za prikaz.");
                return;
            }

            // Print header
            System.out.printf("%-6s %-20s %-15s %-8s %-8s %-12s%n",
                    "ID", "NAZIV", "LOKACIJA", "UK_MAŠ", "AKT_MAŠ", "PROS_KAP");
            System.out.println("=".repeat(75));

            // Print data
            for (RudnikStatsDTO dto : stats) {
                System.out.printf("%-6d %-20.20s %-15.15s %-8d %-8d %-12.2f%n",
                        dto.getRudnikID(),
                        dto.getNazivRudnika(),
                        dto.getLokacija(),
                        dto.getUkupnoMasina(),
                        dto.getAktivneMasine(),
                        dto.getProsecniKapacitet()
                );
            }

            System.out.println("=".repeat(75));
            System.out.println("Ukupno rudnika: " + stats.size());

            // Calculate summary statistics
            int totalMachines = stats.stream().mapToInt(RudnikStatsDTO::getUkupnoMasina).sum();
            int totalActiveMachines = stats.stream().mapToInt(RudnikStatsDTO::getAktivneMasine).sum();
            double avgCapacityAllMines = stats.stream().mapToDouble(RudnikStatsDTO::getProsecniKapacitet).average().orElse(0.0);

            System.out.println("Ukupno mašina: " + totalMachines);
            System.out.println("Ukupno aktivnih mašina: " + totalActiveMachines);
            System.out.printf("Prosečan kapacitet svih rudnika: %.2f%n", avgCapacityAllMines);

        } catch (SQLException e) {
            System.out.println("Greška pri generisanju izveštaja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Complex query 1 - outer join 4 tables, aggregation, filtering, grouping, sorting
     * Shows detailed mine report with excavations and work locations
     */
    private void showComplexReport() {
        try {
            System.out.println("\n=== KOMPLEKSAN IZVEŠTAJ 1: DETALJNI PREGLED RUDNIKA ===");
            System.out.println("Prikazuje rudnike starije od godinu dana sa detaljnim statistikama");
            System.out.println("Uključuje: mašine, iskope i radne lokacije");
            System.out.println();

            List<RudnikStatsDTO> stats = rudnikService.getComplexRudnikReport();

            if (stats.isEmpty()) {
                System.out.println("Nema rudnika koji zadovoljavaju kriterijume:");
                System.out.println("- Stariji od godinu dana");
                System.out.println("- Imaju najmanje jednu mašinu");
                return;
            }

            // Print header
            System.out.printf("%-6s %-20s %-15s %-8s %-8s %-12s %-8s %-8s%n",
                    "ID", "NAZIV", "LOKACIJA", "UK_MAŠ", "AKT_MAŠ", "PROS_KAP", "UK_ISK", "AKT_LOK");
            System.out.println("=".repeat(95));

            // Print data
            for (RudnikStatsDTO dto : stats) {
                System.out.printf("%-6d %-20.20s %-15.15s %-8d %-8d %-12.2f %-8d %-8d%n",
                        dto.getRudnikID(),
                        dto.getNazivRudnika(),
                        dto.getLokacija(),
                        dto.getUkupnoMasina(),
                        dto.getAktivneMasine(),
                        dto.getProsecniKapacitet(),
                        dto.getUkupnoIskopa(),
                        dto.getAktivnihLokacija()
                );
            }

            System.out.println("=".repeat(95));
            System.out.println("Ukupno rudnika (stariji od god dana): " + stats.size());

            // Calculate detailed summary statistics
            int totalMachines = stats.stream().mapToInt(RudnikStatsDTO::getUkupnoMasina).sum();
            int totalActiveMachines = stats.stream().mapToInt(RudnikStatsDTO::getAktivneMasine).sum();
            int totalExcavations = stats.stream().mapToInt(RudnikStatsDTO::getUkupnoIskopa).sum();
            int totalActiveLocations = stats.stream().mapToInt(RudnikStatsDTO::getAktivnihLokacija).sum();
            double avgCapacity = stats.stream().mapToDouble(RudnikStatsDTO::getProsecniKapacitet).average().orElse(0.0);

            System.out.println("\n=== SUMARNI PREGLED ===");
            System.out.printf("%-25s: %d%n", "Ukupno mašina", totalMachines);
            System.out.printf("%-25s: %d%n", "Aktivne mašine", totalActiveMachines);
            System.out.printf("%-25s: %.1f%%%n", "Procenat aktivnih mašina",
                    totalMachines > 0 ? (totalActiveMachines * 100.0 / totalMachines) : 0);
            System.out.printf("%-25s: %d%n", "Ukupno iskopa", totalExcavations);
            System.out.printf("%-25s: %d%n", "Aktivne lokacije", totalActiveLocations);
            System.out.printf("%-25s: %.2f%n", "Prosečan kapacitet", avgCapacity);

            // Find top performers
            if (!stats.isEmpty()) {
                RudnikStatsDTO topByMachines = stats.stream()
                        .max((a, b) -> Integer.compare(a.getAktivneMasine(), b.getAktivneMasine()))
                        .get();
                RudnikStatsDTO topByCapacity = stats.stream()
                        .max((a, b) -> Double.compare(a.getProsecniKapacitet(), b.getProsecniKapacitet()))
                        .get();

                System.out.println("\n=== NAJBOLJI PERFORMERI ===");
                System.out.printf("Najviše aktivnih mašina: %s (%d mašina)%n",
                        topByMachines.getNazivRudnika(), topByMachines.getAktivneMasine());
                System.out.printf("Najveći prosečan kapacitet: %s (%.2f)%n",
                        topByCapacity.getNazivRudnika(), topByCapacity.getProsecniKapacitet());
            }

        } catch (SQLException e) {
            System.out.println("Greška pri generisanju kompleksnog izveštaja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Complex query 2 - join 3+ tables, advanced analysis with aggregation and filtering
     * Shows excavation analysis with waste sites
     */
    private void showComplexIskopAnalysisReport() {
        try {
            System.out.println("\n=== KOMPLEKSAN IZVEŠTAJ 2: ANALIZA ISKOPA SA JALOVIŠTIMA ===");
            System.out.println("Prikazuje iskope veće od 200 hektara sa analizom jalovišta");
            System.out.println("Uključuje: gustinu jalovišta, radne lokacije i dominantni tip materijala");
            System.out.println();

            List<IskopAnalysisDTO> analysis = rudnikService.getIskopAnalysisWithWasteSites();

            if (analysis.isEmpty()) {
                System.out.println("Nema iskopa koji zadovoljavaju kriterijume:");
                System.out.println("- Površina >= 200 hektara");
                System.out.println("- Imaju najmanje jedno jalovište");
                return;
            }

            // Print header
            System.out.println(IskopAnalysisDTO.getFormattedHeader());
            System.out.println("=".repeat(130));

            // Print data
            for (IskopAnalysisDTO dto : analysis) {
                System.out.println(dto.toString());
            }

            System.out.println("=".repeat(130));
            System.out.println("Ukupno iskopa: " + analysis.size());

            // Calculate summary statistics
            int totalArea = analysis.stream().mapToInt(IskopAnalysisDTO::getPovrsinaHa).sum();
            int totalWasteSites = analysis.stream().mapToInt(IskopAnalysisDTO::getUkupnoJalovista).sum();
            int totalActiveWasteSites = analysis.stream().mapToInt(IskopAnalysisDTO::getAktivnaJalovista).sum();
            int totalWorkLocations = analysis.stream().mapToInt(IskopAnalysisDTO::getUkupnoRadnihLokacija).sum();
            int totalActiveLocations = analysis.stream().mapToInt(IskopAnalysisDTO::getAktivnihLokacija).sum();

            System.out.println("\n=== SUMARNI PREGLED ANALIZA ===");
            System.out.printf("%-30s: %d ha%n", "Ukupna površina iskopa", totalArea);
            System.out.printf("%-30s: %d%n", "Ukupno jalovišta", totalWasteSites);
            System.out.printf("%-30s: %d%n", "Aktivna jalovišta", totalActiveWasteSites);
            System.out.printf("%-30s: %.1f%%%n", "Procenat aktivnih jalovišta",
                    totalWasteSites > 0 ? (totalActiveWasteSites * 100.0 / totalWasteSites) : 0);
            System.out.printf("%-30s: %d%n", "Ukupno radnih lokacija", totalWorkLocations);
            System.out.printf("%-30s: %d%n", "Aktivne radne lokacije", totalActiveLocations);

            // Find extremes
            if (!analysis.isEmpty()) {
                IskopAnalysisDTO maxArea = analysis.stream()
                        .max((a, b) -> Integer.compare(a.getPovrsinaHa(), b.getPovrsinaHa()))
                        .get();
                IskopAnalysisDTO maxWasteSites = analysis.stream()
                        .max((a, b) -> Integer.compare(a.getUkupnoJalovista(), b.getUkupnoJalovista()))
                        .get();
                
                System.out.printf("Najveća površina: %s (%d ha)%n",
                        maxArea.getNazivIskopa(), maxArea.getPovrsinaHa());
                System.out.printf("Najviše jalovišta: %s (%d jalovišta)%n",
                        maxWasteSites.getNazivIskopa(), maxWasteSites.getUkupnoJalovista());

                // Count material types
                System.out.println("\n=== DOMINANTNI TIPOVI MATERIJALA ===");
                analysis.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                IskopAnalysisDTO::getDominantniTipMaterijala,
                                java.util.stream.Collectors.counting()))
                        .entrySet().stream()
                        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                        .forEach(entry -> System.out.printf("%-25s: %d iskopa%n", entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            System.out.println("Greška pri generisanju analize iskopa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}