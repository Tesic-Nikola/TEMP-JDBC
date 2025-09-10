package ui_handler;

import java.util.Scanner;

public class MainUIHandler {

    public static Scanner sc = new Scanner(System.in);

    private final RudnikUIHandler rudnikUIHandler = new RudnikUIHandler();
    private final ReportUIHandler reportUIHandler = new ReportUIHandler();
    private final TransactionUIHandler transactionUIHandler = new TransactionUIHandler();

    public void handleMainMenu() {

        String answer;
        do {
            System.out.println("\n=== RUDARSTVO INFORMACIONI SISTEM ===");
            System.out.println("Odaberite opciju:");
            System.out.println("1 - Upravljanje rudnicima");
            System.out.println("2 - Izveštaji i statistike");
            System.out.println("3 - Transakcije");
            System.out.println("X - Izlaz iz programa");

            answer = sc.nextLine();

            switch (answer) {
                case "1":
                    rudnikUIHandler.handleRudnikMenu();
                    break;
                case "2":
                    reportUIHandler.handleReportMenu();
                    break;
                case "3":
                    transactionUIHandler.handleTransactionMenu();
                    break;
                case "X":
                case "x":
                    System.out.println("Hvala što ste koristili sistem!");
                    break;
                default:
                    System.out.println("Neispravna opcija! Molim pokušajte ponovo.");
            }

        } while (!answer.equalsIgnoreCase("X"));

        sc.close();
    }
}