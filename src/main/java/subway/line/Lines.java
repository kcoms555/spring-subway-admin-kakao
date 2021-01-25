package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        if(lines == null){
            this.lines = new ArrayList<>();
        }
        this.lines = lines;
    }

    public List<LineResponse> toResponse(){
        return lines.stream()
                .map(Line::toResponse)
                .collect(Collectors.toList());
    }
}
