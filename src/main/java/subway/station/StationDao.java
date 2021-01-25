package subway.station;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    public static final RowMapper<Station> stationMapper = (result, rowNumber) -> new Station(
            result.getLong("id"), result.getString("name")
    );

    private JdbcTemplate jdbcTemplate;

    StationDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    public Station create(String name) {
        jdbcTemplate.update("INSERT INTO STATION(name) VALUES(?)", name);
        return getStationBy(name);

    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM STATION WHERE id = ?", id);
    }

    private Station getStationBy(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM STATION WHERE name = ?", stationMapper, name);
    }

    public Station getStationBy(Long stationId) {
        return jdbcTemplate.queryForObject("SELECT * FROM STATION WHERE id = ?", stationMapper, stationId);
    }

    public List<Station> getAllStations(){
        return jdbcTemplate.query("SELECT * FROM STATION", stationMapper);
    }

    public void clear() {
        jdbcTemplate.update("DELETE * FROM STATION");
    }

    /*

    public List<StationResponse> getStationResponses() {
        return getAllStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

     */

}
