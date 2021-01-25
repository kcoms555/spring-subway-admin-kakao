package subway.line;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.SectionRequest;
import subway.service.SubwayService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private SubwayService subwayService;

    LineController(SubwayService subwayService){
        this.subwayService = subwayService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line line = subwayService.createLine(lineRequest);
        subwayService.createSection(line.getId(), new SectionRequest(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line.toResponse());
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(subwayService.getLines().toResponse());
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable Long lineId) {
        subwayService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        LineResponse lineResponse = subwayService.getLine(lineId).toResponse();
        lineResponse.setStations(subwayService.getStations(lineId).toResponse());
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping(value = "/lines/{lineId}")
    public ResponseEntity putLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        subwayService.putLine(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }

}
