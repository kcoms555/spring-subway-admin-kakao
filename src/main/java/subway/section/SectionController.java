package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.Line;
import subway.line.LineDao;
import subway.line.LineResponse;
import subway.service.SubwayService;

@RestController
public class SectionController {
    private SubwayService subwayService;

    SectionController(SubwayService subwayService){
        this.subwayService = subwayService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        subwayService.createSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        subwayService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }

}
