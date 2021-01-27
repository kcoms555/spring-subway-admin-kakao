package subway.section;

import org.springframework.stereotype.Service;
import subway.line.LineDao;
import subway.section.Section;
import subway.section.SectionDao;
import subway.section.SectionRequest;
import subway.station.StationDao;

@Service
public class SectionService {
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    SectionService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    // 역 사이를 체크 안하고 직접 연결
    public void directConnect(Section downSection, Section upSection, int distance) {
    }


    public void createSection(Long lineId, SectionRequest sectionRequest) {

    }

    public void deleteSection(Long lineId, Long stationId) {

    }

}
