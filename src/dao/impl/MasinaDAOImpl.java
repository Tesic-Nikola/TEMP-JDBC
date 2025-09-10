package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionUtil_HikariCP;
import dao.MasinaDAO;
import model.Masina;

public class MasinaDAOImpl implements MasinaDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Masina";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean delete(Masina entity) throws SQLException {
        return deleteById(entity.getMasinaID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Masina";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Masina WHERE MasinaID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT * FROM Masina WHERE MasinaID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Masina> findAll() throws SQLException {
        String query = "SELECT MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet FROM Masina";
        List<Masina> masine = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Masina masina = new Masina(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getObject(7) != null ? resultSet.getInt(7) : null
                );
                masine.add(masina);
            }
        }

        return masine;
    }

    @Override
    public Iterable<Masina> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Masina> masine = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet FROM Masina WHERE MasinaID IN (");

        int count = 0;
        for (Integer id : ids) {
            if (count > 0) queryBuilder.append(",");
            queryBuilder.append("?");
            count++;
        }
        queryBuilder.append(")");

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Masina masina = new Masina(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getObject(7) != null ? resultSet.getInt(7) : null
                    );
                    masine.add(masina);
                }
            }
        }

        return masine;
    }

    @Override
    public Masina findById(Integer id) throws SQLException {
        String query = "SELECT MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet FROM Masina WHERE MasinaID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Masina(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getObject(7) != null ? resultSet.getInt(7) : null
                    );
                }
            }
        }

        return null;
    }

    @Override
    public boolean save(Masina entity) throws SQLException {
        if (existsById(entity.getMasinaID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Masina entity) throws SQLException {
        String query = "INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, entity.getMasinaID());
            preparedStatement.setInt(2, entity.getRudnikID());
            preparedStatement.setString(3, entity.getNaziv());
            preparedStatement.setString(4, entity.getTip());
            preparedStatement.setString(5, entity.getStatus());
            preparedStatement.setInt(6, entity.getGodinaProizvodnje());
            if (entity.getKapacitet() != null) {
                preparedStatement.setInt(7, entity.getKapacitet());
            } else {
                preparedStatement.setNull(7, java.sql.Types.INTEGER);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Masina entity) throws SQLException {
        String query = "UPDATE Masina SET Rudnik_RudnikID = ?, Naziv = ?, Tip = ?, Status = ?, Godina_proizvodnje = ?, Kapacitet = ? WHERE MasinaID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, entity.getRudnikID());
            preparedStatement.setString(2, entity.getNaziv());
            preparedStatement.setString(3, entity.getTip());
            preparedStatement.setString(4, entity.getStatus());
            preparedStatement.setInt(5, entity.getGodinaProizvodnje());
            if (entity.getKapacitet() != null) {
                preparedStatement.setInt(6, entity.getKapacitet());
            } else {
                preparedStatement.setNull(6, java.sql.Types.INTEGER);
            }
            preparedStatement.setInt(7, entity.getMasinaID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Masina> entities) throws SQLException {
        int savedCount = 0;

        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);

            for (Masina entity : entities) {
                if (saveTransactional(connection, entity)) {
                    savedCount++;
                }
            }

            connection.commit();
        }

        return savedCount;
    }

    private boolean saveTransactional(Connection connection, Masina entity) throws SQLException {
        String insertQuery = "INSERT INTO Masina (MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE Masina SET Rudnik_RudnikID = ?, Naziv = ?, Tip = ?, Status = ?, Godina_proizvodnje = ?, Kapacitet = ? WHERE MasinaID = ?";

        boolean exists = existsByIdTransactional(connection, entity.getMasinaID());
        String query = exists ? updateQuery : insertQuery;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (exists) {
                preparedStatement.setInt(1, entity.getRudnikID());
                preparedStatement.setString(2, entity.getNaziv());
                preparedStatement.setString(3, entity.getTip());
                preparedStatement.setString(4, entity.getStatus());
                preparedStatement.setInt(5, entity.getGodinaProizvodnje());
                if (entity.getKapacitet() != null) {
                    preparedStatement.setInt(6, entity.getKapacitet());
                } else {
                    preparedStatement.setNull(6, java.sql.Types.INTEGER);
                }
                preparedStatement.setInt(7, entity.getMasinaID());
            } else {
                preparedStatement.setInt(1, entity.getMasinaID());
                preparedStatement.setInt(2, entity.getRudnikID());
                preparedStatement.setString(3, entity.getNaziv());
                preparedStatement.setString(4, entity.getTip());
                preparedStatement.setString(5, entity.getStatus());
                preparedStatement.setInt(6, entity.getGodinaProizvodnje());
                if (entity.getKapacitet() != null) {
                    preparedStatement.setInt(7, entity.getKapacitet());
                } else {
                    preparedStatement.setNull(7, java.sql.Types.INTEGER);
                }
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean existsByIdTransactional(Connection connection, Integer id) throws SQLException {
        String query = "SELECT * FROM Masina WHERE MasinaID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public List<Masina> findByRudnikId(Integer rudnikId) throws SQLException {
        String query = "SELECT MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet FROM Masina WHERE Rudnik_RudnikID = ?";
        List<Masina> masine = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, rudnikId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Masina masina = new Masina(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getObject(7) != null ? resultSet.getInt(7) : null
                    );
                    masine.add(masina);
                }
            }
        }

        return masine;
    }

    @Override
    public List<Masina> findByStatus(String status) throws SQLException {
        String query = "SELECT MasinaID, Rudnik_RudnikID, Naziv, Tip, Status, Godina_proizvodnje, Kapacitet FROM Masina WHERE Status = ?";
        List<Masina> masine = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Masina masina = new Masina(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getObject(7) != null ? resultSet.getInt(7) : null
                    );
                    masine.add(masina);
                }
            }
        }

        return masine;
    }

    @Override
    public boolean updateMasinaStatusAndInsertMaintenance(Connection connection, List<Masina> masine) throws SQLException {
        // This method is used by TransactionService for complex transactions
        String updateStatusQuery = "UPDATE Masina SET Status = ? WHERE MasinaID = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updateStatusQuery)) {
            for (Masina masina : masine) {
                updateStmt.setString(1, masina.getStatus());
                updateStmt.setInt(2, masina.getMasinaID());
                updateStmt.addBatch();
            }

            int[] results = updateStmt.executeBatch();

            // Check if all updates were successful
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }

            return true;
        }
    }
}