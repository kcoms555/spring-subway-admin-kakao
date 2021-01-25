package subway.service;

import org.springframework.stereotype.Service;
import subway.exceptions.exception.LineCreateException;
import subway.exceptions.exception.SectionConnectException;
import subway.exceptions.exception.StationRemoveException;
import subway.line.*;
import subway.section.Section;
import subway.section.SectionDao;
import subway.section.SectionRequest;
import subway.station.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubwayService {
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    SubwayService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao){
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }


    // 역 사이를 체크 안하고 직접 연결
    public void directConnect(Section downSection, Section upSection, int distance) {
        if(distance <= 0){
            throw new SectionConnectException("distance는 1 이상이여야 합니다");
        }
        if(downSection == null && upSection == null){
            return;
        }
        if(downSection == null){
            upSection.setDownId(-1L);
            return;
        }
        if(upSection == null){
            downSection.setUpId(-1L);
            return;
        }
        downSection.setUpId(upSection.getId());
        downSection.setUpDistance(distance);
        upSection.setDownId(downSection.getId());
        upSection.setDownDistance(distance);
        sectionDao.update(downSection);
        sectionDao.update(upSection);
    }


    public Station createStation(StationRequest stationRequest){
        return stationDao.create(stationRequest.getName());
    }

    public Line createLine(LineRequest lineRequest) {
        if(lineDao.existByName(lineRequest.getName())){
            throw new LineCreateException("이미 존재하는 노선입니다");
        }
        return lineDao.create(lineRequest.getName(), lineRequest.getColor(), null);
    }

    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Section downSection = null, middleSection = null, upSection = null;
        int down_middle_distance = 0, middle_up_distance = 0;
        if(sectionDao.countByLineId(lineId) == 0) {
            downSection = sectionDao.create(lineId, sectionRequest.getDownStationId());
            upSection = sectionDao.create(lineId, sectionRequest.getUpStationId());
            directConnect(downSection, upSection, sectionRequest.getDistance());
            return;
        }
        if(sectionDao.exists(lineId, sectionRequest.getDownStationId()) && sectionDao.exists(lineId, sectionRequest.getUpStationId())) {
            return;
        }
        if(!sectionDao.exists(lineId, sectionRequest.getDownStationId()) && !sectionDao.exists(lineId, sectionRequest.getUpStationId())) {
            return;
        }
        if(sectionDao.exists(lineId, sectionRequest.getDownStationId())) {
            downSection = sectionDao.getSectionBy(lineId, sectionRequest.getDownStationId());
            upSection = sectionDao.getSectionBy(downSection.getUpId());
            middleSection = sectionDao.create(lineId, sectionRequest.getUpStationId());
            down_middle_distance = sectionRequest.getDistance();
            middle_up_distance = downSection.getUpDistance() - down_middle_distance;
        }
        if(sectionDao.exists(lineId, sectionRequest.getUpStationId())) {
            upSection = sectionDao.getSectionBy(lineId, sectionRequest.getUpStationId());
            middleSection = sectionDao.create(lineId, sectionRequest.getDownStationId());
            downSection = sectionDao.getSectionBy(upSection.getDownId());
            middle_up_distance = sectionRequest.getDistance();
            down_middle_distance = upSection.getDownDistance() - middle_up_distance;
        }
        directConnect(downSection, middleSection, down_middle_distance);
        directConnect(middleSection, upSection, middle_up_distance);
    }

    public Stations getAllStations() {
        return new Stations(stationDao.getAllStations());
    }

    public Stations getStations(Long lineId) {
        List<Station> stations = new ArrayList<>();
        Section head = sectionDao.getSectionHeadBy(lineId);
        while(head != null){
            stations.add(stationDao.getStationBy(head.getStationId()));
            head = sectionDao.getSectionBy(head.getDownId());
        }
        return new Stations(stations);
    }

    public Line getLine(Long lineId) {
        return lineDao.getLineBy(lineId);
    }

    public Lines getLines() {
        return new Lines(lineDao.getAllLines());
    }


    public void deleteStation(Long id) {
        if(sectionDao.countByStationId(id) > 0){
            throw new StationRemoveException("해당 역을 이용하는 구간을 먼저 제거해주시기 바랍니다");
        }
        stationDao.deleteById(id);
    }

    public void deleteLine(Long lineId) {
        sectionDao.deleteByLineId(lineId);
        lineDao.deleteBy(lineId);
    }

    public void deleteSection(Long lineId, Long stationId) {

    }


    public void putLine(Long lineId, LineRequest lineRequest) {
        Line line = new Line(lineId, lineRequest.getName(), lineRequest.getColor(), null);
        lineDao.update(line);
    }
}
