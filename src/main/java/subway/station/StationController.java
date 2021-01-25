package subway.station;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.SubwayService;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private SubwayService subwayService;

    StationController(SubwayService subwayService){
        this.subwayService = subwayService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        Station station = subwayService.createStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station.toResponse());
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(subwayService.getAllStations().toResponse());
    }

    @DeleteMapping("/stations/{lineId}")
    public ResponseEntity deleteStation(@PathVariable Long lineId) {
        subwayService.deleteStation(lineId);
        return ResponseEntity.noContent().build();
    }

}
