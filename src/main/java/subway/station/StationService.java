package subway.station;

import org.springframework.stereotype.Service;
import subway.exceptions.exception.StationRemoveException;
import subway.section.SectionDao;

@Service
public class StationService {
    private StationDao stationDao;
    private SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Station createStation(StationRequest stationRequest) {
        return stationDao.create(stationRequest.getName());
    }

    public Stations getAllStations() {
        return new Stations(stationDao.getAllStations());
    }


    public void deleteStation(Long id) {
        if (sectionDao.countByStationId(id) > 0) {
            throw new StationRemoveException("해당 역을 이용하는 구간을 먼저 제거해주시기 바랍니다");
        }
        stationDao.deleteById(id);
    }

}
