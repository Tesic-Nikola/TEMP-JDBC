package dao;

import java.sql.SQLException;
import java.util.List;

import dto.RudnikStatsDTO;
import dto.IskopAnalysisDTO;
import model.Rudnik;

public interface RudnikDAO extends CRUDDao<Rudnik, Integer> {

    // Simple query - join two tables with aggregation functions
    List<RudnikStatsDTO> findRudnikWithMasinaStats() throws SQLException;

    // Complex query 1 - outer join 4 tables, aggregation, filtering, grouping, sorting
    List<RudnikStatsDTO> findComplexRudnikReport() throws SQLException;

    // Complex query 2 - join 3+ tables, advanced analysis with aggregation and filtering
    List<IskopAnalysisDTO> findIskopAnalysisWithWasteSites() throws SQLException;
}