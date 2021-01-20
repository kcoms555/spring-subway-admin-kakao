package subway.line.section;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SectionsDao {
    private JdbcTemplate jdbcTemplate;

    public SectionsDao(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

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

    public void update(Section section){
        jdbcTemplate.update("UPDATE SECTION SET up_section_id = ?, down_section_id = ?, up_distance = ?, down_distance = ?",
                section.getUp().getId(),
                section.getDown().getId(),
                section.getUpDistance(),
                section.getDownDistance()
        );
    }

}
