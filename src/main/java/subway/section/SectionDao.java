package subway.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.Line;
import subway.station.Station;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SectionDao {
    public static final RowMapper<Section> sectionMapper = (result, rowNumber) -> new Section(
            result.getLong("id"),
            result.getLong("line_id"),
            new Station(result.getLong("up_station_id"), result.getString("up_station_name")),
            new Station(result.getLong("down_station_id"), result.getString("down_station_name")),
            result.getInt("distance")
    );

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTION").usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> parameters = new HashMap<>(4, 1.0f);
        parameters.put("line_id", line.getId());
        parameters.put("up_station_id", section.getUpStation().getId());
        parameters.put("down_station_id", section.getDownStation().getId());
        parameters.put("distance", section.getDistance());

        Long id = (Long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Section(id, section.getLineId(), section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    /*
    public Section getSectionBy(Long id) {
        String sql = "SELECT S.id AS id, " +
                "S.line_id AS line_id, " +
                "US.id AS up_station_id, " +
                "US.name AS up_station_name, " +
                "DS.id AS down_station_id, " +
                "DS.name AS down_station_name, " +
                "S.distance AS distance " +
                " FROM SECTION S " +
                "LEFT JOIN STATION US ON S.up_station_id = US.id " +
                "LEFT JOIN STATION DS ON S.down_station_id = DS.id " +
                "WHERE id = ?";
        return jdbcTemplate.queryForObject("SELECT * FROM SECTION WHERE id = ?", sectionMapper, id);
    }
     */

    public void deleteBy(Long id) {
        jdbcTemplate.update("DELETE FROM SECTION WHERE id = ?", id);
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("DELETE FROM SECTION WHERE line_id = ?", lineId);
    }

    public void update(Section section) {
        String sql = "UPDATE SECTION SET up_station_id = ?, down_station_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance(),
                section.getId()
        );
    }

    public int countByStationId(Long stationId) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE up_station_id = ? OR down_station_id = ?", Integer.class, stationId, stationId);
    }

    /*
    public int countByLineId(Long lineId) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE line_id = ?", Integer.class, lineId);
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

    public boolean has(Section section) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM SECTION WHERE id = ?", Integer.class, section.getId()) == 1;
    }
     */
}
