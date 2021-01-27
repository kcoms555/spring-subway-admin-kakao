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
    private List<StationResponse> stationResponses;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stations;
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

    public List<StationResponse> getStationResponses() {
        if (stationResponses == null) {
            return new ArrayList<>();
        }
        return stationResponses;
    }

    public void setStationResponses(List<StationResponse> stationResponses) {
        this.stationResponses = stationResponses;
    }
}
