package subway.section;

import org.springframework.util.ReflectionUtils;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionDao {
    private static SectionDao sectionDao = new SectionDao();
    public static final int INITIAL_DISTANCE = 0;
    private Long seq = 0L;
    private Sections sections = new Sections();

    public static void clear() {
        getInstance().sections = new Sections();
        getInstance().seq = 0L;
    }

    public Section save(Section section) {
        Section persistSection = createNewObject(section);
        sections.add(persistSection);
        return persistSection;
    }

    private Section createNewObject(Section section) {
        Field field = ReflectionUtils.findField(Section.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, section, ++seq);
        return section;
    }

    public static SectionDao getInstance() {
        return sectionDao;
    }

    public Section makeSection(Long upStationId, Long downStationId, int distance, Long lineId) {
        Sections sectionsByLineId = sections.findByLineId(lineId);
        sectionsByLineId.validateSection(upStationId,downStationId,distance);
        return new Section(lineId,
                sectionsByLineId.getExtendedStationId(upStationId,downStationId),
                sectionsByLineId.calculateRelativeDistance(upStationId,downStationId,distance));
    }


    public List<StationResponse> getStationResponsesByLineId(Long lineId) {
        List<Long> stationIdsByDistance = sections.findByLineId(lineId).getSortedStationIdsByDistance();
        return stationIdsByDistance.stream().map(stationId ->
                new StationResponse(stationId,StationDao.getInstance().getStationById(stationId).getName()))
                .collect(Collectors.toList());
    }

    public void deleteByLineIdAndStationId(Long lineId, Long stationId) {
        Sections sectionsByLineId = sections.findByLineId(lineId);
        sectionsByLineId.validateDeleteStation(stationId);
        sections.removeStation(lineId,stationId);

    }

    public void LineInitialize(Long lineId, Long upStationId, Long downStationId, int distance) {
        SectionDao.getInstance().save(new Section(lineId, upStationId, INITIAL_DISTANCE));
        SectionDao.getInstance().save(new Section(lineId, downStationId, distance));
    }
}
