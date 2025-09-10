package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionUtil_HikariCP;
import dao.RudnikDAO;
import dto.RudnikStatsDTO;
import dto.IskopAnalysisDTO;
import model.Rudnik;

public class RudnikDAOImpl implements RudnikDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Rudnik";

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
    public boolean delete(Rudnik entity) throws SQLException {
        return deleteById(entity.getRudnikID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Rudnik";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Rudnik WHERE RudnikID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT 1 FROM Rudnik WHERE RudnikID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Rudnik> findAll() throws SQLException {
        String query = "SELECT RudnikID, Naziv, Lokacija, Datum_osnivanja, " +
                "Pocetak_radnog_vremena, Kraj_radnog_vremena FROM Rudnik ORDER BY RudnikID";
        List<Rudnik> rudnici = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Rudnik rudnik = new Rudnik(
                        resultSet.getInt("RudnikID"),
                        resultSet.getString("Naziv"),
                        resultSet.getString("Lokacija"),
                        resultSet.getDate("Datum_osnivanja"),
                        resultSet.getTimestamp("Pocetak_radnog_vremena"),
                        resultSet.getTimestamp("Kraj_radnog_vremena")
                );
                rudnici.add(rudnik);
            }
        }

        return rudnici;
    }

    @Override
    public Iterable<Rudnik> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Rudnik> rudnici = new ArrayList<>();

        // Count how many IDs we have
        int count = 0;
        for (Integer id : ids) {
            count++;
        }

        if (count == 0) {
            return rudnici; // Return empty list
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT RudnikID, Naziv, Lokacija, Datum_osnivanja, " +
                "Pocetak_radnog_vremena, Kraj_radnog_vremena FROM Rudnik WHERE RudnikID IN (");

        for (int i = 0; i < count; i++) {
            if (i > 0) queryBuilder.append(",");
            queryBuilder.append("?");
        }
        queryBuilder.append(") ORDER BY RudnikID");

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Rudnik rudnik = new Rudnik(
                            resultSet.getInt("RudnikID"),
                            resultSet.getString("Naziv"),
                            resultSet.getString("Lokacija"),
                            resultSet.getDate("Datum_osnivanja"),
                            resultSet.getTimestamp("Pocetak_radnog_vremena"),
                            resultSet.getTimestamp("Kraj_radnog_vremena")
                    );
                    rudnici.add(rudnik);
                }
            }
        }

        return rudnici;
    }

    @Override
    public Rudnik findById(Integer id) throws SQLException {
        String query = "SELECT RudnikID, Naziv, Lokacija, Datum_osnivanja, " +
                "Pocetak_radnog_vremena, Kraj_radnog_vremena FROM Rudnik WHERE RudnikID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Rudnik(
                            resultSet.getInt("RudnikID"),
                            resultSet.getString("Naziv"),
                            resultSet.getString("Lokacija"),
                            resultSet.getDate("Datum_osnivanja"),
                            resultSet.getTimestamp("Pocetak_radnog_vremena"),
                            resultSet.getTimestamp("Kraj_radnog_vremena")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public boolean save(Rudnik entity) throws SQLException {
        if (existsById(entity.getRudnikID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Rudnik entity) throws SQLException {
        String query = "INSERT INTO Rudnik (RudnikID, Naziv, Lokacija, Datum_osnivanja, " +
                "Pocetak_radnog_vremena, Kraj_radnog_vremena) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, entity.getRudnikID());
            preparedStatement.setString(2, entity.getNaziv());
            preparedStatement.setString(3, entity.getLokacija());
            preparedStatement.setDate(4, new java.sql.Date(entity.getDatumOsnivanja().getTime()));

            if (entity.getPocetakRadnogVremena() != null) {
                preparedStatement.setTimestamp(5, new java.sql.Timestamp(entity.getPocetakRadnogVremena().getTime()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
            }

            if (entity.getKrajRadnogVremena() != null) {
                preparedStatement.setTimestamp(6, new java.sql.Timestamp(entity.getKrajRadnogVremena().getTime()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.TIMESTAMP);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Rudnik entity) throws SQLException {
        String query = "UPDATE Rudnik SET Naziv = ?, Lokacija = ?, Datum_osnivanja = ?, " +
                "Pocetak_radnog_vremena = ?, Kraj_radnog_vremena = ? WHERE RudnikID = ?";

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, entity.getNaziv());
            preparedStatement.setString(2, entity.getLokacija());
            preparedStatement.setDate(3, new java.sql.Date(entity.getDatumOsnivanja().getTime()));

            if (entity.getPocetakRadnogVremena() != null) {
                preparedStatement.setTimestamp(4, new java.sql.Timestamp(entity.getPocetakRadnogVremena().getTime()));
            } else {
                preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
            }

            if (entity.getKrajRadnogVremena() != null) {
                preparedStatement.setTimestamp(5, new java.sql.Timestamp(entity.getKrajRadnogVremena().getTime()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
            }

            preparedStatement.setInt(6, entity.getRudnikID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Rudnik> entities) throws SQLException {
        int savedCount = 0;

        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);

            try {
                for (Rudnik entity : entities) {
                    if (saveTransactional(connection, entity)) {
                        savedCount++;
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }

        return savedCount;
    }

    private boolean saveTransactional(Connection connection, Rudnik entity) throws SQLException {
        String insertQuery = "INSERT INTO Rudnik (RudnikID, Naziv, Lokacija, Datum_osnivanja, " +
                "Pocetak_radnog_vremena, Kraj_radnog_vremena) VALUES (?, ?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE Rudnik SET Naziv = ?, Lokacija = ?, Datum_osnivanja = ?, " +
                "Pocetak_radnog_vremena = ?, Kraj_radnog_vremena = ? WHERE RudnikID = ?";

        boolean exists = existsByIdTransactional(connection, entity.getRudnikID());
        String query = exists ? updateQuery : insertQuery;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (exists) {
                // Update query parameters
                preparedStatement.setString(1, entity.getNaziv());
                preparedStatement.setString(2, entity.getLokacija());
                preparedStatement.setDate(3, new java.sql.Date(entity.getDatumOsnivanja().getTime()));

                if (entity.getPocetakRadnogVremena() != null) {
                    preparedStatement.setTimestamp(4, new java.sql.Timestamp(entity.getPocetakRadnogVremena().getTime()));
                } else {
                    preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
                }

                if (entity.getKrajRadnogVremena() != null) {
                    preparedStatement.setTimestamp(5, new java.sql.Timestamp(entity.getKrajRadnogVremena().getTime()));
                } else {
                    preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
                }

                preparedStatement.setInt(6, entity.getRudnikID());
            } else {
                // Insert query parameters
                preparedStatement.setInt(1, entity.getRudnikID());
                preparedStatement.setString(2, entity.getNaziv());
                preparedStatement.setString(3, entity.getLokacija());
                preparedStatement.setDate(4, new java.sql.Date(entity.getDatumOsnivanja().getTime()));

                if (entity.getPocetakRadnogVremena() != null) {
                    preparedStatement.setTimestamp(5, new java.sql.Timestamp(entity.getPocetakRadnogVremena().getTime()));
                } else {
                    preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
                }

                if (entity.getKrajRadnogVremena() != null) {
                    preparedStatement.setTimestamp(6, new java.sql.Timestamp(entity.getKrajRadnogVremena().getTime()));
                } else {
                    preparedStatement.setNull(6, java.sql.Types.TIMESTAMP);
                }
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean existsByIdTransactional(Connection connection, Integer id) throws SQLException {
        String query = "SELECT 1 FROM Rudnik WHERE RudnikID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Simple query - join two tables with aggregation functions
    @Override
    public List<RudnikStatsDTO> findRudnikWithMasinaStats() throws SQLException {
        String query = "SELECT r.RudnikID, r.Naziv, r.Lokacija, " +
                "COUNT(m.MasinaID) as ukupno_masina, " +
                "COUNT(CASE WHEN m.Status = 'U radu' THEN 1 END) as aktivne_masine, " +
                "COALESCE(AVG(m.Kapacitet), 0) as prosecni_kapacitet " +
                "FROM Rudnik r " +
                "LEFT JOIN Masina m ON r.RudnikID = m.Rudnik_RudnikID " +
                "GROUP BY r.RudnikID, r.Naziv, r.Lokacija " +
                "ORDER BY ukupno_masina DESC";

        List<RudnikStatsDTO> results = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                RudnikStatsDTO dto = new RudnikStatsDTO(
                        resultSet.getInt("RudnikID"),
                        resultSet.getString("Naziv"),
                        resultSet.getString("Lokacija"),
                        resultSet.getInt("ukupno_masina"),
                        resultSet.getInt("aktivne_masine"),
                        resultSet.getDouble("prosecni_kapacitet")
                );
                results.add(dto);
            }
        }

        return results;
    }

    // Complex query - outer join 4 tables, aggregation, filtering, grouping, sorting
    @Override
    public List<RudnikStatsDTO> findComplexRudnikReport() throws SQLException {
        String query = "SELECT r.RudnikID, r.Naziv, r.Lokacija, " +
                "COUNT(DISTINCT m.MasinaID) as ukupno_masina, " +
                "COUNT(DISTINCT CASE WHEN m.Status = 'U radu' THEN m.MasinaID END) as aktivne_masine, " +
                "COALESCE(AVG(m.Kapacitet), 0) as prosecni_kapacitet, " +
                "COUNT(DISTINCT i.IskopID) as ukupno_iskopa, " +
                "COUNT(DISTINCT CASE WHEN rl.Status = 'Aktivna' THEN rl.RadLokID END) as aktivnih_lokacija " +
                "FROM Rudnik r " +
                "LEFT OUTER JOIN Masina m ON r.RudnikID = m.Rudnik_RudnikID " +
                "LEFT OUTER JOIN Iskop i ON r.RudnikID = i.Rudnik_RudnikID " +
                "LEFT OUTER JOIN Radna_Lok rl ON i.IskopID = rl.Iskop_IskopID " +
                "WHERE r.Datum_osnivanja < CURRENT_DATE - INTERVAL '365 days' " +
                "GROUP BY r.RudnikID, r.Naziv, r.Lokacija " +
                "HAVING COUNT(DISTINCT m.MasinaID) > 0 " +
                "ORDER BY aktivne_masine DESC, ukupno_iskopa DESC";

        List<RudnikStatsDTO> results = new ArrayList<>();

        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                RudnikStatsDTO dto = new RudnikStatsDTO(
                        resultSet.getInt("RudnikID"),
                        resultSet.getString("Naziv"),
                        resultSet.getString("Lokacija"),
                        resultSet.getInt("ukupno_masina"),
                        resultSet.getInt("aktivne_masine"),
                        resultSet.getDouble("prosecni_kapacitet"),
                        resultSet.getInt("ukupno_iskopa"),
                        resultSet.getInt("aktivnih_lokacija")
                );
                results.add(dto);
            }
        }

        return results;
    }
}