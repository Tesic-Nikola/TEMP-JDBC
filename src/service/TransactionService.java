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

    /**
     * Transaction: Transfer machines from one mine to another and update their work locations
     * This involves updating Masina table and potentially Transfer_Log table
     */
    public boolean transferMachinesBetweenMines(List<Integer> masinaIds, int fromRudnikId, int toRudnikId) throws SQLException {
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try {
                // Step 1: Verify all machines belong to the source mine
                String verifyQuery = "SELECT COUNT(*) FROM Masina WHERE MasinaID = ? AND Rudnik_RudnikID = ?";

                for (Integer masinaId : masinaIds) {
                    try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
                        verifyStmt.setInt(1, masinaId);
                        verifyStmt.setInt(2, fromRudnikId);

                        try (var resultSet = verifyStmt.executeQuery()) {
                            if (resultSet.next() && resultSet.getInt(1) == 0) {
                                throw new SQLException("Machine " + masinaId + " does not belong to mine " + fromRudnikId);
                            }
                        }
                    }
                }

                // Step 2: Update machine mine assignment
                String updateMasinaQuery = "UPDATE Masina SET Rudnik_RudnikID = ?, Status = 'U radu' WHERE MasinaID = ?";

                for (Integer masinaId : masinaIds) {
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateMasinaQuery)) {
                        updateStmt.setInt(1, toRudnikId);
                        updateStmt.setInt(2, masinaId);

                        int rowsUpdated = updateStmt.executeUpdate();

                        if (rowsUpdated != 1) {
                            throw new SQLException("Failed to transfer machine with ID: " + masinaId);
                        }
                    }
                }

                // Step 3: Insert transfer log
                String insertLogQuery = "INSERT INTO Transfer_Log (MasinaID, From_RudnikID, To_RudnikID, Transfer_Date) " +
                        "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

                for (Integer masinaId : masinaIds) {
                    try (PreparedStatement logStmt = connection.prepareStatement(insertLogQuery)) {
                        logStmt.setInt(1, masinaId);
                        logStmt.setInt(2, fromRudnikId);
                        logStmt.setInt(3, toRudnikId);

                        // This might fail if Transfer_Log table doesn't exist, which is fine for demo
                        try {
                            logStmt.executeUpdate();
                        } catch (SQLException e) {
                            // Log table doesn't exist, continue without logging
                            System.out.println("Transfer log table not available, continuing without logging...");
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
}