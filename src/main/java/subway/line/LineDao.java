package subway.line;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.line.section.Section;
import subway.line.section.SectionDao;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    LineDao(JdbcTemplate jdbcTemplate, SectionDao sectionDao){
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = sectionDao;
    }

    public void clear() {
        jdbcTemplate.update("DELETE * from LINE");
    }

    public Line create(String name, String color, Long upStationId, Long downStationId, int distance) {
        jdbcTemplate.update("INSERT INTO LINE(name, color) VALUES(?, ?)", name, color);
        Line line = getLineBy(name);
        Section upSection = sectionDao.create(line.getId(), upStationId);
        Section downSection = sectionDao.create(line.getId(), downStationId);
        Section.directConnect(downSection, upSection, distance);
        updateSections(line.getId(), upSection.getId(), downSection.getId());
        return line;
    }

    public void deleteBy(Long id) {
        jdbcTemplate.update("DELETE FROM LINE WHERE id = ?", id);
    }

    public List<LineResponse> getAllLineResponses() {
        return getAllLines().stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStationResponses()))
                .collect(Collectors.toList());
    }

    private static RowMapper<Line> lineMapper = ((rs, rowNum) -> new Line(
            rs.getLong("id"), rs.getString("name"), rs.getString("color"),
            rs.getInt("extra_fare"), rs.getLong("up_section_end_point_id"), rs.getLong("down_section_end_point_id")
    ));

    public List<Line> getAllLines() {
        return jdbcTemplate.query("SELECT * FROM LINE", lineMapper);
    }

    public Line getLineBy(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM LINE WHERE name = ?", Line.class, name);
    }

    public Line getLineBy(Long lineId) {
        return jdbcTemplate.queryForObject("SELECT * FROM LINE WHERE id = ?", Line.class, lineId);
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

}
