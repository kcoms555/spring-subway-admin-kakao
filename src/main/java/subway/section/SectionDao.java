package subway.section;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.station.Station;

import java.util.List;

@Repository
public class SectionDao {
    public static final RowMapper<Section> sectionMapper = (result, rowNumber) -> new Section(
            result.getLong("id"),
            result.getLong("line_id"),
            result.getLong("station_id"),
            result.getLong("up_section_id"),
            result.getLong("down_section_id"),
            result.getInt("up_distance"),
            result.getInt("down_distance")
    );

    private JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    public void clear() {
        jdbcTemplate.update("DELETE * from SECTION");
    }

    public Section create(Long lineId, Long stationId) {
        jdbcTemplate.update("INSERT INTO SECTION(line_id, station_id, up_section_id, down_section_id) VALUES(?, ?, -1, -1)", lineId, stationId);
        return getSectionBy(lineId, stationId);
    }

    public Section getSectionBy(Long lineId, Long stationId) {
        try {
            if(stationId == -1){
                return null;
            }
            return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE station_id = ? and line_id = ?", sectionMapper, stationId, lineId);
        } catch(IncorrectResultSizeDataAccessException e){
            return null;
        }
    }

    public Section getSectionBy(Long sectionId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE id = ?", sectionMapper, sectionId);
        } catch(IncorrectResultSizeDataAccessException e){
            return null;
        }
    }

    public void deleteBy(Long id) {
        jdbcTemplate.update("DELETE FROM SECTION WHERE id = ?", id);
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("DELETE FROM SECTION WHERE line_id = ?", lineId);
    }

    public void update(Section section){
        jdbcTemplate.update("UPDATE SECTION SET up_section_id = ?, down_section_id = ?, up_distance = ?, down_distance = ? WHERE id = ?",
                section.getUpId(),
                section.getDownId(),
                section.getUpDistance(),
                section.getDownDistance(),
                section.getId()
        );
    }

    public int countByLineId(Long lineId) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE line_id = ?", Integer.class, lineId);
    }

    public int countByStationId(Long stationId) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE station_id = ?", Integer.class, stationId);
    }

    public Section createOrGet(Long lineId, Long stationId) {
        try{
            return getSectionBy(lineId, stationId);
        } catch(IncorrectResultSizeDataAccessException e){
            return create(lineId, stationId);
        }
    }
    public List<Section> getAllSections() {
        return jdbcTemplate.query("SELECT * FROM SECTION", sectionMapper);
    }

    public Section getSectionHeadBy(Long lineId) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE up_section_id = -1 and line_id = ?", sectionMapper, lineId);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public boolean exists(Long lineId, Long stationId) {
        return getSectionBy(lineId, stationId) != null;
    }

    /*
    public boolean has(Section section) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE id = ?", Integer.class, section.getId()) == 1;
    }
     */
}
