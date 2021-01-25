package subway.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import subway.Response;
import subway.ValueObject;
import subway.exceptions.exception.SectionConnectException;
import subway.exceptions.exception.SectionRemoveException;
import subway.section.Section;
import subway.section.SectionDao;
import subway.station.StationDao;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class Line implements ValueObject {
    private Long id;
    private String name;
    private String color;
    private Integer extraFare;

    public Line(Long id, String name, String color, Integer extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public Integer getExtraFare() {
        return extraFare;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setExtraFare(Integer extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    public LineResponse toResponse() {
        return new LineResponse(id, name, color, null);     /* Line 은 stations 정보를 가지고 있지 않기에 null을 전달. 필요한 경우 LineResponse에 직접 setter를 이용하여 넣어주어야 함 */
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
