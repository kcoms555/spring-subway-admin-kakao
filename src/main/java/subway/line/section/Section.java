package subway.line.section;

import subway.exceptions.exception.SectionSameStationException;

public class Section {
    private Long id;
    private Long stationId;
    private Long lineId;

    private Section upSection;
    private Section downSection;

    private Distance upDistance = new Distance();
    private Distance downDistance = new Distance();

    public Section(Long id, Long stationId, Long lineId){
        this(stationId, lineId);
        this.id = id;
    }

    public Section(Long stationId, Long lineId){
        this.stationId = stationId;
        this.lineId = lineId;
    }

    static public void connectStations(Section upSection, Section downSection, int distance) {

        if (upSection.hasDownStation() && downSection.hasUpStation()) {
            throw new SectionSameStationException();
        }

        if (!upSection.hasDownStation() && !downSection.hasUpStation()) {
            directConnect(upSection, downSection, distance);
            return;
        }

        if (upSection.hasDownStation()) {
            directConnect(downSection, upSection.downSection, upSection.downDistance.calculateDistance(distance));
            directConnect(upSection, downSection, distance);
            return;
        }

        if (downSection.hasUpStation()) {
            directConnect(downSection.upSection, upSection, downSection.upDistance.calculateDistance(distance));
            directConnect(upSection, downSection, distance);
        }
    }

    private boolean hasUpStation() {
        return this.upSection != null;
    }

    private boolean hasDownStation() {
        return this.downSection != null;
    }

    static private void directConnect(Section upSection, Section downSection, int distance) {
        upSection.downSection = downSection;
        upSection.downDistance.setDistance(distance);
        downSection.upSection = upSection;
        downSection.upDistance.setDistance(distance);
    }

    public void deleteSection() {
        int distance = Distance.addDistance(upDistance, downDistance);
        directConnect(upSection, downSection, distance);
    }

    public boolean validDownDistance(int distance) {
        return this.downDistance.validateDistance(distance);
    }

    public boolean validUpDistance(int distance) {
        return this.upDistance.validateDistance(distance);
    }

    public Long getDownStationId() {
        return this.downSection.getStationId();
    }

    public Long getUpStationId() {
        return this.upSection.getStationId();
    }

    public Long getStationId() {
        return stationId;
    }
    public Long getLineId() {
        return lineId;
    }

}
