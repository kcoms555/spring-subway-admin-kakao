package subway.line.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.Line;
import subway.line.LineDao;
import subway.line.LineResponse;

@RestController
public class SectionController {
    private SectionDao sectionDao;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Section upSection = sectionDao.getSectionBy(lineId, sectionRequest.getUpStationId());
        Section downSection = sectionDao.getSectionBy(lineId, sectionRequest.getDownStationId());
        Section.connectStations(upSection, downSection, sectionRequest.getDistance());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        Line line = LineDao.getInstance().getLineById(lineId);
        line.deleteSection(stationId);
        return ResponseEntity.ok().build();
    }

}
