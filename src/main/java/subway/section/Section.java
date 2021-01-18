package subway.section;

import subway.exceptions.exception.SectionSameStationException;

public class Section {
    private Long id;
    private Long stationId;
    private Long lineId;
    private int distance;

    public Section(Long lineId, Long stationId, int distance) {
        this.stationId = stationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    //어느게 오름차순인지는 모르겠
    public int compareDistance(Section otherSection) {
        return this.getDistance()-otherSection.getDistance();
    }

}
