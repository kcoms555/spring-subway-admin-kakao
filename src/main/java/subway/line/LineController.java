package subway.line;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.exceptions.exception.LineNotFoundException;
import subway.section.SectionDao;
import subway.section.Sections;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineController {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineController(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        Line newLine = lineDao.save(line);
        sectionDao.LineInitialize(newLine.getId(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance());
        LineResponse lineResponse = getLineResponseById(newLine.getId());
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(lineResponse);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(getLineResponses());
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable Long lineId) {
        lineDao.deleteById(lineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(getLineResponseById(lineId));
    }

    @PutMapping(value = "/lines/{lineId}")
    public ResponseEntity putLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        lineDao.update(new Line(lineId, lineRequest.getName(),lineRequest.getColor()));
        return ResponseEntity.ok().build();
    }

    public List<LineResponse> getLineResponses() {
        return lineDao.findAll().stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), getStationResponsesByLine(line)))
                .collect(Collectors.toList());
    }

    private List<StationResponse> getStationResponsesByLine(Line line) {
        return getStationResponsesByLineId(line.getId());
    }

    public LineResponse getLineResponseById(Long lineId) {
        return lineDao.findById(lineId).stream()
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
