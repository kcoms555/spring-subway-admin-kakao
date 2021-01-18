package subway.line.section;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SectionDao {
    private JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    public void clear() {
        jdbcTemplate.update("DELETE * from SECTION");
    }

    public Section create(Long lineId, Long stationId) {
        jdbcTemplate.update("INSERT INTO SECTION(lineId, stationId) VALUES(?, ?)", lineId, stationId);
        return getSectionBy(lineId, stationId);
    }

    public void deleteBy(Long id) {
        jdbcTemplate.update("DELETE FROM SECTION WHERE id = ?", id);
    }

    public Section getSectionBy(Long lineId, Long stationId) {
        return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE stationId = ? and lineId = ?", Section.class, stationId, lineId);
    }
    public Section getSectionBy(Long sectionId) {
        return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE id = ?", Section.class, sectionId);
    }

}
