package subway.section;

import subway.exceptions.exception.SectionDeleteException;
import subway.exceptions.exception.SectionNoStationException;
import subway.exceptions.exception.SectionSameStationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    public static final int NUM_OF_INITIAL_STATIONS = 2;
    public static final int BASIC_NUM_OF_NEW_SECTION = 1;
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public Sections findByLineId(Long lineId) {
        return new Sections(sections.stream().filter(section -> section.getLineId().equals(lineId))
                .collect(Collectors.toList()));
    }

    public Long getExtendedStationId(Long upStationId, Long downStationId) {
        if (isStationIdExist(upStationId)) {
            return downStationId;
        }
        return upStationId;
    }

    public boolean isStationIdExist(Long stationId) {
        return sections.stream()
                .map(Section::getStationId)
                .anyMatch(lineId -> lineId.equals(stationId));
    }

    public void validateSection(Long upStationId, Long downStationId, int distance) {
        if (isStationIdExist(upStationId) && isStationIdExist(downStationId)) {
            throw new SectionSameStationException();
        }
        if (!isStationIdExist(upStationId) && !isStationIdExist(downStationId)) {
            throw new SectionNoStationException();
        }
        if (isStationIdExist(upStationId) && !isStationIdExist(downStationId)) {
            int upDistance = getSectionDistanceByStationId(upStationId);
            distanceValidate(upDistance, upDistance + distance);
            return;
        }
        if (!isStationIdExist(upStationId) && isStationIdExist(downStationId)) {
            int downDistance = getSectionDistanceByStationId(downStationId);
            distanceValidate(downDistance - distance, downDistance);
            return;
        }
    }

    private int getSectionDistanceByStationId(Long stationId) {
        return sections.stream().filter(section -> section.getStationId().equals(stationId))
                .findFirst()
                .map(Section::getDistance)
                .hashCode();

    }

    private void distanceValidate(int upDistance, int downDistance) {
        if (areThereAnyStationsBetweenNewSections(upDistance, downDistance)) {
            throw new IllegalArgumentException("입력된 거리가 올바르지 않습니다.");
        }
    }

    private boolean areThereAnyStationsBetweenNewSections(int upDistance, int downDistance) {
        return sections.stream().map(Section::getDistance)
                .filter(distance -> (distance >= upDistance && distance <= downDistance))
                .count() != BASIC_NUM_OF_NEW_SECTION;
    }

    public int calculateRelativeDistance(Long upStationId, Long downStationId, int distance) {
        if (isStationIdExist(upStationId)) {
            return getSectionDistanceByStationId(upStationId) + distance;
        }
        return getSectionDistanceByStationId(downStationId) - distance;
    }

    public List<Long> getSortedStationIdsByDistance() {
        return sections.stream().sorted(Section::compareDistance)
                .map(Section::getStationId)
                .collect(Collectors.toList());
    }

    public void validateDeleteStation(Long stationId) {
        if (!isStationIdExist(stationId)) {
            throw new SectionDeleteException();
        }
        if (sections.size() == NUM_OF_INITIAL_STATIONS) {
            throw new SectionDeleteException();
        }
    }

}
