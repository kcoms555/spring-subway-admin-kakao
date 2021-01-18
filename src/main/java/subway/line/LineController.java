package subway.line;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineDao lineDao;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line newLine = lineDao.create(lineRequest.getName(), lineRequest.getColor(),
                lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(newLine.toResponse());
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineDao.getAllLineResponses());
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable Long lineId) {
        lineDao.deleteBy(lineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(lineDao.getLineBy(lineId).toResponse());
    }

    @PutMapping(value = "/lines/{lineId}")
    public ResponseEntity putLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        lineDao.update(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }

}
