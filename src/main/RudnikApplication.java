package main;

import connection.ConnectionUtil_HikariCP;
import ui_handler.MainUIHandler;

public class RudnikApplication {

	public static void main(String[] args) {

		System.out.println("=================================================");
		System.out.println("    RUDARSTVO INFORMACIONI SISTEM");
		System.out.println("    Fakultet tehničkih nauka - Novi Sad");
		System.out.println("    Baze podataka 2 - Projekat");
		System.out.println("    PostgreSQL implementacija");
		System.out.println("=================================================");

		MainUIHandler mainUIHandler = new MainUIHandler();
		try {
			mainUIHandler.handleMainMenu();
		} catch (Exception e) {
			System.err.println("Kritična greška u aplikaciji: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Clean up connection pool
			try {
				ConnectionUtil_HikariCP.closeDataSource();
				System.out.println("\nKonekcija sa bazom podataka je zatvorena.");
			} catch (Exception e) {
				System.err.println("Greška pri zatvaranju konekcije: " + e.getMessage());
			}
		}
	}
}