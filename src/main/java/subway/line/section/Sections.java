package subway.line.section;

import java.util.Map;

public class Sections {
    Map<Long, Section> sections;

    public Sections(Map<Long, Section> sections){
        this.sections = sections;
    }

    public boolean has(Long id){
        return sections.containsKey(id);
    }

    public boolean has(Section section) {
        return sections.containsValue(section);
    }

    public Section getBy(Long id){
        return sections.get(id);
    }

    public int size(){
        return sections.size();
    }

    public void remove(Long stationId) {
        Section.remove(getBy(stationId));
        sections.remove(stationId);
    }
}
