package subway.line.section;

import subway.exceptions.exception.SectionConnectException;
import subway.station.Station;
import subway.station.StationDao;
import subway.station.StationResponse;

public class Section {
    private Long stationId;

    private Section up;
    private Section down;

    private int upDistance;
    private int downDistance;

    public Section(Long stationId) {
        this.stationId = stationId;
        this.up = null;
        this.down = null;
        this.upDistance = 0;
        this.downDistance = 0;
    }

    public static void connect(Section downStation, Section upStation, int distance) {
        if(isThereNoConnectionBetween(downStation, upStation)) {
            directConnect(downStation, upStation, distance);
            return;
        }
        if(isNewlyAdded(downStation)) {
            Section downdownStation = upStation.down;
            int downdown_down_distance = upStation.downDistance - distance;
            int down_up_distance = distance;
            directConnect(downStation, upStation, down_up_distance);
            directConnect(downdownStation, downStation, downdown_down_distance);
            return;
        }
        if(isNewlyAdded(upStation)){
            Section upupStation = downStation.up;
            int up_upup_distance = downStation.upDistance - distance;
            int down_up_distance = distance;
            directConnect(downStation, upStation, down_up_distance);
            directConnect(upStation, upupStation, up_upup_distance);
            return;
        }
        throw new SectionConnectException("불가능한 연결 요청입니다");
    }

    private static boolean isNewlyAdded(Section station) {
        return !station.hasUp() && !station.hasDown();
    }

    private static boolean isThereNoConnectionBetween(Section downStation, Section upStation) {
        return !downStation.hasUp() && !upStation.hasDown();
    }

    public static void remove(Section section) {
        if(section.hasUp()){
            section.up.down = section.down;
            section.up.downDistance = section.upDistance + section.downDistance;
        }
        if(section.hasDown()){
            section.down.up = section.up;
            section.down.upDistance = section.upDistance + section.downDistance;
        }
        section.up = null;
        section.down = null;
        section.upDistance = 0;
        section.downDistance = 0;
    }

    public boolean hasUp(){
        return this.up != null;
    }

    public boolean hasDown(){
        return this.down != null;
    }

    // 역 사이를 체크 안하고 직접 연결
    public static void directConnect(Section downSection, Section upSection, int distance) {
        if(distance <= 0){
            throw new SectionConnectException("distance는 1 이상이여야 합니다");
        }
        downSection.setUp(upSection);
        downSection.setUpDistance(distance);
        upSection.setDown(downSection);
        upSection.setDownDistance(distance);
    }


    public Station toStation(){
        return StationDao.getInstance().getStationById(stationId);
    }

    public Long getStationId() {
        return stationId;
    }

    public Section getUp() {
        return up;
    }

    public Section getDown() {
        return down;
    }

    public int getUpDistance() {
        return upDistance;
    }

    public int getDownDistance() {
        return downDistance;
    }

    public void setUp(Section up) {
        this.up = up;
    }

    public void setDown(Section down) {
        this.down = down;
    }

    public void setUpDistance(int upDistance) {
        this.upDistance = upDistance;
    }

    public void setDownDistance(int downDistance) {
        this.downDistance = downDistance;
    }

    public StationResponse toStationResponse() {
        Station station = StationDao.getInstance().getStationById(stationId);
        return new StationResponse(station.getId(), station.getName());
    }
}
