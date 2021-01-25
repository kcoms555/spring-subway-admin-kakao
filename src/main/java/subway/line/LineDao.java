package subway.line;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.section.Section;
import subway.section.SectionDao;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    public static final RowMapper<Line> lineMapper = ((result, rowNumber) -> new Line(
            result.getLong("id"),
            result.getString("name"),
            result.getString("color"),
            result.getInt("extra_fare")
    ));

    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    LineDao(JdbcTemplate jdbcTemplate, SectionDao sectionDao){
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = sectionDao;
    }

    public Line create(String name, String color, Integer extraFare) {
        jdbcTemplate.update("INSERT INTO LINE(name, color, extra_fare) VALUES(?, ?, ?)", name, color, extraFare);
        return getLineBy(name);
    }

    public void update(Line line){
        if (line.getName() != null) {
            jdbcTemplate.update("UPDATE LINE SET name = ? WHERE id = ?", line.getName(), line.getId());
        }
        if (line.getColor() != null) {
            jdbcTemplate.update("UPDATE LINE SET color = ? WHERE id = ?", line.getColor(), line.getId());
        }
    }

    public Line getLineBy(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM LINE WHERE name = ?", lineMapper, name);
    }

    public Line getLineBy(Long lineId) {
        return jdbcTemplate.queryForObject("SELECT * FROM LINE WHERE id = ?", lineMapper, lineId);
    }

    public List<Line> getAllLines() {
        return jdbcTemplate.query("SELECT * FROM LINE", lineMapper);
    }

    public void deleteBy(Long id) {
        jdbcTemplate.update("DELETE FROM LINE WHERE id = ?", id);
    }

    public void clear() {
        jdbcTemplate.update("DELETE * from LINE");
    }

    public boolean existByName(String name) {
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) FROM LINE WHERE name = ?", Integer.class, name);
        return !count.equals(0);
    }

    /*


    public List<LineResponse> getAllLineResponses() {
        return getAllLines().stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStationResponses()))
                .collect(Collectors.toList());
    }


    public void updateSections(Long lineId, Long upSectionId, Long downSectionId) {
        jdbcTemplate.update("UPDATE LINE SET up_section_end_point_id = ?, down_section_end_porint_id WHERE id = ?", upSectionId, downSectionId, lineId);
    }

    public void updateLineNameOrColor(Long lineId, LineRequest lineRequest) {
        if (lineRequest.getName() != null) {
            jdbcTemplate.update("UPDATE LINE SET name = ? WHERE id = ?", lineRequest.getName(), lineId);
        }
        if (lineRequest.getColor() != null) {
            jdbcTemplate.update("UPDATE LINE SET color = ? WHERE id = ?", lineRequest.getColor(), lineId);
        }
    }
     */

}
