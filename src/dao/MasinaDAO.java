package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Masina;

public interface MasinaDAO extends CRUDDao<Masina, Integer> {

    List<Masina> findByRudnikId(Integer rudnikId) throws SQLException;

    List<Masina> findByStatus(String status) throws SQLException;

    // Transaction method - update multiple machines and insert maintenance records
    boolean updateMasinaStatusAndInsertMaintenance(Connection connection, List<Masina> masine) throws SQLException;
}