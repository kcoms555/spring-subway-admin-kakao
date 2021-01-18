package subway.line;

import subway.section.Section;
import subway.section.SectionDao;

public class Line {

    private Long id;
    private String name;
    private String color;

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }

    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
