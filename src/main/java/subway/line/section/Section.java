package subway.line.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import subway.exceptions.exception.SectionConnectException;

@Component
public class Section {
    private static SectionDao sectionDao;

    @Autowired
    public void setSectionDao(SectionDao sectionDao) {
        Section.sectionDao = sectionDao;
    }


    private Long id;
    private Long lineId;
    private Long stationId;

    private Long upId;
    private Long downId;

    private int upDistance;
    private int downDistance;

    public Section(){}

    public Section(Long id, Long lineId, Long stationId) {
        this(lineId, stationId);
        this.id = id;
    }

    public Section(Long lineId, Long stationId) {
        this.id = null;
        this.lineId = lineId;
        this.stationId = stationId;
        this.upId = null;
        this.downId = null;
        this.upDistance = 0;
        this.downDistance = 0;
    }

    public static void connect(Section downStation, Section upStation, int distance) {
        if(isThereNoConnectionBetween(downStation, upStation)) {
            directConnect(downStation, upStation, distance);
            return;
        }
        if(isNewlyAdded(downStation)) {
            Section downdownStation = upStation.getDown();
            int downdown_down_distance = upStation.downDistance - distance;
            int down_up_distance = distance;
            directConnect(downStation, upStation, down_up_distance);
            directConnect(downdownStation, downStation, downdown_down_distance);
            return;
        }
        if(isNewlyAdded(upStation)){
            Section upupStation = downStation.getUp();
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
            section.getUp().setDownIdBy(section.getDown());
            section.getUp().downDistance = section.upDistance + section.downDistance;
        }
        if(section.hasDown()){
            section.getDown().setUpIdBy(section.getUp());
            section.getDown().upDistance = section.upDistance + section.downDistance;
        }
        section.setUpIdBy(null);
        section.setUpIdBy(null);
        section.upDistance = 0;
        section.downDistance = 0;
    }

    public boolean hasUp(){
        return this.upId != null;
    }

    public boolean hasDown(){
        return this.downId != null;
    }

    // 역 사이를 체크 안하고 직접 연결
    public static void directConnect(Section downSection, Section upSection, int distance) {
        if(distance <= 0){
            throw new SectionConnectException("distance는 1 이상이여야 합니다");
        }
        downSection.setUpIdBy(upSection);
        downSection.setUpDistance(distance);
        upSection.setDownIdBy(downSection);
        upSection.setDownDistance(distance);
        sectionDao.update(upSection);
        sectionDao.update(downSection);
    }

    public Long getId() {
        return id;
    }
    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Section getUp() {
        return sectionDao.getSectionBy(upId);
    }

    public Section getDown() {
        return sectionDao.getSectionBy(downId);
    }

    public void setUpIdBy(Section section) {
        if(section == null){
            this.upId = null;
            return;
        }
        this.upId = section.getId();
    }

    public void setDownIdBy(Section section) {
        if(section == null){
            this.downId = null;
            return;
        }
        this.downId = section.getId();
    }

    public int getUpDistance() {
        return upDistance;
    }

    public int getDownDistance() {
        return downDistance;
    }

    public void setUpDistance(int upDistance) {
        this.upDistance = upDistance;
    }

    public void setDownDistance(int downDistance) {
        this.downDistance = downDistance;
    }
}
