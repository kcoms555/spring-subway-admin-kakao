package subway.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import subway.exceptions.exception.SectionConnectException;
import subway.exceptions.exception.SectionRemoveException;
import subway.line.section.Section;
import subway.line.section.SectionDao;
import subway.line.section.Sections;
import subway.station.Station;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Line {
    private static LineDao lineDao;
    private static SectionDao sectionDao;
    private static StationDao stationDao;

    @Autowired
    public void setSectionDao(SectionDao sectionDao) {
        Line.sectionDao = sectionDao;
    }

    @Autowired
    public void setLineDao(LineDao lineDao) {
        Line.lineDao = lineDao;
    }

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Long upSectionEndPointId;
    private Long downSectionEndPointId;

    public Line(){}

    public Line(Long id, String name, String color, int extraFare, Long upSectionEndPointId ,Long downSectionEndPointId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upSectionEndPointId = upSectionEndPointId;
        this.downSectionEndPointId = downSectionEndPointId;
    }

    public void connectSection(Section upSection, Section downSection, int distance) {
        if(!sectionDao.has(upSection) && !sectionDao.has(downSection)){
            throw new SectionConnectException("두 역 모두 기존 노선에 등록되지 않은 역입니다");
        }
        if(sectionDao.has(upSection) && sectionDao.has(downSection)){
            throw new SectionConnectException("두 역 모두 기존 노선에 등록된 역입니다");
        }
        Section.connect(upSection, downSection, distance);
        extend();
    }

    private void extend() {
        Section upSection = sectionDao.getSectionBy(upSectionEndPointId);
        Section downSection = sectionDao.getSectionBy(downSectionEndPointId);
        //TODO
        while(upSection.hasUp()){
            upSection = upSection.getUp();
            upSectionEndPointId = upSection.getStationId();
        }
        while(downSection.hasDown()){
            downSection = downSection.getDown();
            downSectionEndPointId = downSection.getStationId();
        }
        lineDao.updateSections(id, upSectionEndPointId, downSectionEndPointId);
    }

    public void removeByStationId(Long stationId) {
        if(sectionDao.size() <= 2){
            throw new SectionRemoveException("등록된 역의 크기가 2개 이하라 더이상 제거할 수 없습니다");
        }
        if(stationId.equals(upSectionEndPointId)){
            this.upSectionEndPointId = sectionDao.getSectionBy(upSectionEndPointId).getDown().getStationId();
        }
        if(stationId.equals(downSectionEndPointId)){
            this.downSectionEndPointId = sectionDao.getSectionBy(downSectionEndPointId).getUp().getStationId();
        }
        sectionDao.deleteBy(sectionDao.getSectionBy(id, stationId).getId());
    }

    //상행 종점에서 하행 종점까지 순서대로 반환
    public List<StationResponse> getStationResponses() {
        List<StationResponse> tmpStationIds = new ArrayList<>();
        Section head = sectionDao.getSectionBy(upSectionEndPointId);
        while (head != null) {
            tmpStationIds.add(stationDao.getStationBy(head.getStationId()).toResponse());
            head = head.getDown();
        }
        return tmpStationIds;
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

    public int getExtraFare() {
        return extraFare;
    }

    public LineResponse toResponse(){
        return new LineResponse(id, name, color, getStationResponses());
    }
}
