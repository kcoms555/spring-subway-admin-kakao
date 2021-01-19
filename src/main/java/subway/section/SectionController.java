package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.exceptions.exception.LineNotFoundException;
import subway.line.Line;
import subway.line.LineDao;
import subway.line.LineResponse;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SectionController {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionController(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        sectionDao.makeSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance(),lineId);
        return ResponseEntity.ok().body(getLineResponseById(lineId));
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionDao.deleteByLineIdAndStationId(lineId,stationId);
        return ResponseEntity.ok().build();
    }

    private List<StationResponse> getStationResponsesByLine(Line line) {
        return getStationResponsesByLineId(line.getId());
    }

    public LineResponse getLineResponseById(Long lineId) {
        return lineDao.findAll().stream().filter(line -> line.getId().equals(lineId))
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(),
                        getStationResponsesByLine(line)))
                .findAny().orElseThrow(LineNotFoundException::new);
    }

    public List<StationResponse> getStationResponsesByLineId(Long lineId) {
        Sections sectionsByLineId = new Sections(sectionDao.findByLineId(lineId));
        List<Long> stationIdsByDistance = sectionsByLineId.getSortedStationIdsByDistance();
        return stationIdsByDistance.stream().map(stationId ->
                new StationResponse(stationId,stationDao.findById(stationId).getName()))
                .collect(Collectors.toList());
    }

}
