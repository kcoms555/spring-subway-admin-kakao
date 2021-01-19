package subway.station;


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
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Station save(Station station) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement psmt = con.prepareStatement(
                    "insert into station (name) values(?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psmt.setString(1, station.getName());
            return psmt;
        }, keyHolder);

        Long id = (Long) keyHolder.getKey();
        return new Station(id, station.getName());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from station where id = ?", id);
    }

    public List<Station> findAll() {
        return jdbcTemplate.query("select * from station", new StationMapper());
    }

    public Station findById(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from station where id = ?", new StationMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<StationResponse> getStationResponses() {
        return findAll().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private final static class StationMapper implements RowMapper<Station> {
        @Override
        public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );
        }
    }

}
