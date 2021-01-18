package subway.line;

import org.junit.jupiter.api.Test;
import subway.station.Station;
import subway.station.StationDao;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {
    @Test
    void lineTest(){
        Station 회기역 = new Station(1L, "회기역");
        Station 서울역 = new Station(2L, "서울역");
        Station 용산역 = new Station(3L, "용산역");
        StationDao.getInstance().save(회기역);
        StationDao.getInstance().save(서울역);
        StationDao.getInstance().save(용산역);
        Line line = new Line("1호선", "파랑색", 회기역.getId(), 서울역.getId(), 100);
        line.connectSection(용산역.getId(), 서울역.getId(), 100);
        line.getStationResponses().forEach(System.out::println);
    }

}