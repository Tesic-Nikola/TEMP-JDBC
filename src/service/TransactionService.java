package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import connection.ConnectionUtil_HikariCP;
import dao.MasinaDAO;
import dao.impl.MasinaDAOImpl;
import model.Masina;

public class TransactionService {
    private static final MasinaDAO masinaDAO = new MasinaDAOImpl();

    /**
     * Transaction: Update machine status to "Servis" and insert maintenance records for multiple machines
     * This transaction involves at least 2 tables: Masina and Servis
     */
    public boolean performMachineMaintenanceTransaction(List<Integer> masinaIds) throws SQLException {
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try {
                // Step 1: Update machine status to "Servis"
                String updateMasinaQuery = "UPDATE Masina SET Status = 'Servis' WHERE MasinaID = ?";

                for (Integer masinaId : masinaIds) {
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateMasinaQuery)) {
                        updateStmt.setInt(1, masinaId);
                        int rowsUpdated = updateStmt.executeUpdate();

                        if (rowsUpdated != 1) {
                            throw new SQLException("Failed to update machine with ID: " + masinaId);
                        }
                    }
                }

                // Step 2: Insert maintenance records into Servis table
                String insertServisQuery = "INSERT INTO Servis (Masina_MasinaID, Opis, Datum_Servisa, Tip_servisa, Troskovi) " +
                        "VALUES (?, 'Redovno održavanje', CURRENT_DATE, 'Preventivno', ?)";

                for (Integer masinaId : masinaIds) {
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertServisQuery)) {
                        insertStmt.setInt(1, masinaId);
                        insertStmt.setInt(2, 5000 + (int)(Math.random() * 10000)); // Random cost between 5000-15000

                        int rowsInserted = insertStmt.executeUpdate();

                        if (rowsInserted != 1) {
                            throw new SQLException("Failed to insert service record for machine ID: " + masinaId);
                        }
                    }
                }

                connection.commit(); // Commit transaction
                return true;

            } catch (SQLException e) {
                connection.rollback(); // Rollback on error
                throw e;
            }
        }
    }
    public boolean processInspectionResults(int inspekcijaId, String newResult) throws SQLException {
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // 1. Update inspection result
                String updateInspekcijaQuery = "UPDATE Inspekcija SET Rezultat = ?, Datum = CURRENT_DATE WHERE InspekcijaID = ?";
                int jalovisteId;

                try (PreparedStatement stmt1 = connection.prepareStatement(updateInspekcijaQuery)) {
                    stmt1.setString(1, newResult);
                    stmt1.setInt(2, inspekcijaId);
                    stmt1.executeUpdate();
                }

                // Get jaloviste ID for this inspection
                String getJalovisteQuery = "SELECT Jaloviste_JalovisteID FROM Inspekcija WHERE InspekcijaID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(getJalovisteQuery)) {
                    stmt.setInt(1, inspekcijaId);
                    try (var rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Inspection not found");
                        }
                        jalovisteId = rs.getInt(1);
                    }
                }

                // 2. Update jaloviste status based on inspection result
                String newJalovisteStatus;
                switch (newResult) {
                    case "Ne zadovoljava":
                        newJalovisteStatus = "Sanacija";
                        break;
                    case "Potrebne mere":
                        newJalovisteStatus = "Neaktivno";
                        break;
                    default: // "Zadovoljava"
                        newJalovisteStatus = "Aktivno";
                }

                String updateJalovisteQuery = "UPDATE Jaloviste SET Status = ? WHERE JalovisteID = ?";
                try (PreparedStatement stmt2 = connection.prepareStatement(updateJalovisteQuery)) {
                    stmt2.setString(1, newJalovisteStatus);
                    stmt2.setInt(2, jalovisteId);
                    stmt2.executeUpdate();
                }

                // 3. If waste site needs cleanup, deactivate related work locations
                if ("Ne zadovoljava".equals(newResult)) {
                    String updateRadnaLokQuery = "UPDATE Radna_Lok SET Status = 'Neaktivna' " +
                            "WHERE Iskop_IskopID = (SELECT Iskop_IskopID FROM Jaloviste WHERE JalovisteID = ?)";
                    try (PreparedStatement stmt3 = connection.prepareStatement(updateRadnaLokQuery)) {
                        stmt3.setInt(1, jalovisteId);
                        stmt3.executeUpdate();
                    }
                }

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}