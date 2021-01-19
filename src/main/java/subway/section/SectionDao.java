package subway.section;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    public static final int INITIAL_DISTANCE = 0;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Section save(Section section) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement psmt = con.prepareStatement(
                    "insert into section (line_id,station_id,distance) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psmt.setLong(1, section.getLineId());
            psmt.setLong(2, section.getStationId());
            psmt.setInt(3, section.getDistance());
            return psmt;
        }, keyHolder);

        Long id = (Long) keyHolder.getKey();
        return new Section(section.getLineId(), section.getStationId(), section.getDistance());
    }

    public List<Section> findAll() {
        return jdbcTemplate.query("select * from section ", new SectionDao.SectionMapper());
    }

    public List<Section> findByLineId(Long lineId) {
        try {
            return optionalToList(Optional.ofNullable(jdbcTemplate.queryForObject("select * from section where line_id = ?",
                    new SectionDao.SectionMapper(), lineId)));
        } catch (EmptyResultDataAccessException e) {
            return optionalToList(Optional.empty());
        }
    }

    public List<Section> optionalToList (Optional<Section> opt) {
        return opt.isPresent()
                ? Collections.singletonList(opt.get())
                : Collections.emptyList();
    }

    public void makeSection(Long upStationId, Long downStationId, int distance, Long lineId) {
        Sections sectionsByLineId = new Sections(findByLineId(lineId));
        sectionsByLineId.validateSection(upStationId,downStationId,distance);
        save(new Section(lineId,
                sectionsByLineId.getExtendedStationId(upStationId,downStationId),
                sectionsByLineId.calculateRelativeDistance(upStationId,downStationId,distance)));
    }



    public void deleteByLineIdAndStationId(Long lineId, Long stationId) {
        Sections sectionsByLineId = new Sections(findByLineId(lineId));
        sectionsByLineId.validateDeleteStation(stationId);
        jdbcTemplate.update("delete from section where line_id = ? and station_id = ?", lineId,stationId);
    }

    public void LineInitialize(Long lineId, Long upStationId, Long downStationId, int distance) {
        save(new Section(lineId, upStationId, INITIAL_DISTANCE));
        save(new Section(lineId, downStationId, distance));
    }

    private final static class SectionMapper implements RowMapper<Section> {
        @Override
        public Section mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("station_id"),
                    rs.getInt("distance")
            );
        }
    }


}
