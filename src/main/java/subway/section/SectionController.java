package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.Line;
import subway.line.LineDao;
import subway.line.LineResponse;
import subway.station.StationDao;

@RestController
public class SectionController {


    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Section section = SectionDao.getInstance().makeSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance(),lineId);
        Section newSection = SectionDao.getInstance().save(section);
        Line nowLine = LineDao.getInstance().getLineById(lineId);
        LineResponse lineResponse = new LineResponse(lineId,nowLine.getName(),nowLine.getColor(),
                SectionDao.getInstance().getStationResponsesByLineId(lineId));
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        SectionDao.getInstance().deleteByLineIdAndStationId(lineId,stationId);
        return ResponseEntity.ok().build();
    }

}
