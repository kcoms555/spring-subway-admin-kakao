package subway.line.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.Line;
import subway.line.LineDao;
import subway.line.LineResponse;

@RestController
public class SectionController {
    private SectionDao sectionDao;
    private LineDao lineDao;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Line line = lineDao.getLineBy(lineId);
        line.connectSection(sectionRequest.getDownStationId(), sectionRequest.getUpStationId(), sectionRequest.getDistance());
        lineDao.save(line);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        Line line = lineDao.getLineBy(lineId);
        line.deleteSection(stationId);
        lineDao.save(line);
        return ResponseEntity.ok().build();
    }

}
