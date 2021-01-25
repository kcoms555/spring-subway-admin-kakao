package subway.line;

import subway.Response;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse extends Response {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        if(stations == null){
            return new ArrayList<>();
        }
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }
}
