package subway.line;

import subway.exceptions.exception.SectionConnectException;
import subway.exceptions.exception.SectionRemoveException;
import subway.line.section.Section;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Line {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Long upStationId;
    private Long downStationId;
    private Map<Long, Section> sections;

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.sections = new HashMap<>();
        Section.directConnect(getOrCreateSection(downStationId), getOrCreateSection(upStationId), distance);
    }

    public void connectSection(Long downStationId, Long upStationId, int distance) {
        if(!sections.containsKey(downStationId) && !sections.containsKey(upStationId)){
            throw new SectionConnectException("두 역 모두 기존 노선에 등록되지 않은 역입니다");
        }
        if(sections.containsKey(downStationId) && sections.containsKey(upStationId)){
            throw new SectionConnectException("두 역 모두 기존 노선에 등록된 역입니다");
        }
        Section.connect(getOrCreateSection(downStationId), getOrCreateSection(upStationId), distance);
        extendSections();
    }

    private void extendSections() {
        Section upSection = sections.get(upStationId);
        Section downSection = sections.get(downStationId);
        while(upSection.hasUp()){
            upSection = upSection.getUp();
            upStationId = upSection.getStationId();
        }
        while(downSection.hasDown()){
            downSection = downSection.getDown();
            downStationId = downSection.getStationId();
        }
    }

    public void removeSection(Long stationId) {
        if(sections.size() <= 2){
            throw new SectionRemoveException("등록된 역의 크기가 2개 이하라 더이상 제거할 수 없습니다");
        }
        if(stationId.equals(upStationId)){
            this.upStationId = sections.get(upStationId).getDown().getStationId();
        }
        if(stationId.equals(downStationId)){
            this.downStationId = sections.get(downStationId).getUp().getStationId();
        }
        Section.remove(sections.get(stationId));
        sections.remove(stationId);
    }

    private Section getOrCreateSection(Long stationId) {
        if(!sections.containsKey(stationId))
        {
           sections.put(stationId, new Section(stationId));
        }
        return sections.get(stationId);
    }

    public void update(LineRequest lineRequest) {
        if (lineRequest.getName() != null) {
            this.name = lineRequest.getName();
        }
        if (lineRequest.getColor() != null) {
            this.color = lineRequest.getColor();
        }
    }

    //상행 종점에서 하행 종점까지 순서대로 반환
    public List<StationResponse> getStationResponses() {
        List<StationResponse> tmpStationResponses = new ArrayList<>();
        Section head = sections.get(upStationId);
        while (head != null) {
            tmpStationResponses.add(head.toStationResponse());
            head = head.getDown();
        }
        return tmpStationResponses;
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

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
