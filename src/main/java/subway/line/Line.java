package subway.line;

import subway.exceptions.exception.SectionDeleteException;
import subway.exceptions.exception.SectionNoStationException;
import subway.exceptions.exception.SectionSameStationException;
import subway.line.section.Section;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public static final Long NULL_SECTION_POINT = 0L; // 일급 컬렉션으로 만드는 것이 좋아보임.
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Long upSectionEndPointId;
    private Long downSectionEndPointId;

    public Line(Long id, String name, String color, int extraFare, Long upStationId, Long downStationId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upSectionEndPointId = upStationId;
        this.downSectionEndPointId = downStationId;
    }

    private void initializeLine(Long upStationId, Long downStationId, int distance) {
        sections.put(NULL_SECTION_POINT, new Section(NULL_SECTION_POINT));
        sections.put(upStationId, new Section(upStationId));
        sections.put(downStationId, new Section(downStationId));
        Section.connectStations(sections.get(NULL_SECTION_POINT), sections.get(upStationId), Integer.MAX_VALUE);
        Section.connectStations(sections.get(downStationId), sections.get(NULL_SECTION_POINT), Integer.MAX_VALUE);
        Section.connectStations(sections.get(upStationId), sections.get(downStationId), distance);
    }

    public void makeSection(Long upStationId, Long downStationId, int distance) {
        if (isThereTwoStations(upStationId, downStationId)) {
            throw new SectionSameStationException();
        }

        if (canMakeDownSection(upStationId, distance)) {
            sections.put(downStationId, new Section(downStationId));
            connect(upStationId, downStationId, distance);
            return;
        }

        if (canMakeUpSection(downStationId, distance)) {
            sections.put(upStationId, new Section(upStationId));
            connect(upStationId, downStationId, distance);
            return;
        }

        throw new SectionNoStationException();
    }

    private boolean canMakeUpSection(Long downStationId, int distance) {
        return sections.containsKey(downStationId) && sections.get(downStationId).validUpDistance(distance);
    }

    private boolean canMakeDownSection(Long upStationId, int distance) {
        return sections.containsKey(upStationId) && sections.get(upStationId).validDownDistance(distance);
    }

    private boolean isThereTwoStations(Long stationId1, Long stationId2) {
        return sections.containsKey(stationId1) && sections.containsKey(stationId2);
    }

    private void connect(Long upStationId, Long downStationId, int distance) {
        Section.connectStations(sections.get(upStationId), sections.get(downStationId), distance);
        updateEndPoints();
    }


    private void updateEndPoints() {
        upSectionEndPointId = sections.get(NULL_SECTION_POINT).getDownStationId();
        downSectionEndPointId = sections.get(NULL_SECTION_POINT).getUpStationId();
    }


    public void deleteSection(Long stationId) {
        if (!sections.containsKey(stationId) || areThereOnlyTwoStations()) {
            throw new SectionDeleteException();
        }
        sections.get(stationId).deleteSection();
        sections.remove(stationId);
        updateEndPoints();
    }

    private boolean areThereOnlyTwoStations() {
        return sections.get(upSectionEndPointId).getDownStationId().equals(downSectionEndPointId);
    }

    public List<StationResponse> getStationResponses() {
        Long nowId = upSectionEndPointId;
        List<StationResponse> stationResponses = new ArrayList<>();
        while (!stationIsEnd(nowId)) {
            stationResponses.add(new StationResponse(nowId, StationDao.getInstance().getStationBy(nowId).getName()));
            nowId = sections.get(nowId).getDownStationId();
        }
        return stationResponses;
    }

    private boolean stationIsEnd(Long nowId) {
        return nowId.equals(NULL_SECTION_POINT);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineResponse toResponse() {
        return new LineResponse(id, name, color, getStationResponses());
    }
}
