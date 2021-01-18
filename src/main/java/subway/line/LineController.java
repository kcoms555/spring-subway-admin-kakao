package subway.line;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.SectionDao;
import subway.section.Sections;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        Line newLine = LineDao.getInstance().save(line);
        SectionDao.getInstance().LineInitialize(newLine.getId(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance());
        LineResponse lineResponse = LineDao.getInstance().getLineResponseById(newLine.getId());
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(lineResponse);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(LineDao.getInstance().getLineResponses());
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable Long lineId) {
        LineDao.getInstance().deleteById(lineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(LineDao.getInstance().getLineResponseById(lineId));
    }

    @PutMapping(value = "/lines/{lineId}")
    public ResponseEntity putLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        Line line = LineDao.getInstance().getLineById(lineId);
        line.update(lineRequest.getName(),lineRequest.getColor());
        return ResponseEntity.ok().build();
    }

}
