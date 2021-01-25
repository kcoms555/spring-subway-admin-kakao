package subway.station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        if(stations == null){
            this.stations = new ArrayList<>();
        }
        this.stations = stations;
    }

    public List<StationResponse> toResponse(){
        return stations.stream()
                .map(Station::toResponse)
                .collect(Collectors.toList());
    }
}
