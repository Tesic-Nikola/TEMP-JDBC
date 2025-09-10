package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.RudnikDAO;
import dao.impl.RudnikDAOImpl;
import dto.RudnikStatsDTO;
import dto.IskopAnalysisDTO;
import model.Rudnik;

public class RudnikService {
    private static final RudnikDAO rudnikDAO = new RudnikDAOImpl();

    public List<Rudnik> getAll() throws SQLException {
        return (List<Rudnik>) rudnikDAO.findAll();
    }

    public Rudnik getById(int id) throws SQLException {
        return rudnikDAO.findById(id);
    }

    public boolean existsById(int id) throws SQLException {
        return rudnikDAO.existsById(id);
    }

    public boolean save(Rudnik rudnik) throws SQLException {
        return rudnikDAO.save(rudnik);
    }

    public boolean deleteById(int id) throws SQLException {
        return rudnikDAO.deleteById(id);
    }

    public int saveAll(List<Rudnik> rudnici) throws SQLException {
        return rudnikDAO.saveAll(rudnici);
    }

    public int count() throws SQLException {
        return rudnikDAO.count();
    }

    // Simple query service method
    public List<RudnikStatsDTO> getRudnikWithMasinaStats() throws SQLException {
        return rudnikDAO.findRudnikWithMasinaStats();
    }

    // Complex query 1 service method
    public List<RudnikStatsDTO> getComplexRudnikReport() throws SQLException {
        return rudnikDAO.findComplexRudnikReport();
    }

    // Complex query 2 service method
    public List<IskopAnalysisDTO> getIskopAnalysisWithWasteSites() throws SQLException {
        return rudnikDAO.findIskopAnalysisWithWasteSites();
    }
}