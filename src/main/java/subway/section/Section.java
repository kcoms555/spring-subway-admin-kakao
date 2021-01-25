package subway.section;

import subway.Response;
import subway.ValueObject;

public class Section implements ValueObject {
    private Long id;
    private Long lineId;
    private Long stationId;

    private Long upId;
    private Long downId;

    private int upDistance;
    private int downDistance;

    public Section(Long id, Long lineId, Long stationId, Long upId, Long downId, int upDistance, int downDistance){
        this(id, lineId, stationId);
        this.upId = upId;
        this.downId = downId;
        this.upDistance = upDistance;
        this.downDistance = downDistance;
    }
    public Section(Long id, Long lineId, Long stationId) {
        this.id = id;
        this.lineId = lineId;
        this.stationId = stationId;
        this.upId = null;
        this.downId = null;
        this.upDistance = 0;
        this.downDistance = 0;
    }

    public boolean hasUpId(){
        return this.upId != null;
    }

    public boolean hasDownId(){
        return this.downId != null;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getUpId() {
        return upId;
    }

    public Long getDownId() {
        return downId;
    }

    public int getUpDistance() {
        return upDistance;
    }

    public int getDownDistance() {
        return downDistance;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public void setUpId(Long upId) {
        this.upId = upId;
    }

    public void setDownId(Long downId) {
        this.downId = downId;
    }

    public void setUpDistance(int upDistance) {
        this.upDistance = upDistance;
    }

    public void setDownDistance(int downDistance) {
        this.downDistance = downDistance;
    }

    @Override
    public Response toResponse() {
        return null;    /* Section 에 대응하는 SectionResponse 가 필요 없기에 구현하지 않음 */
    }
}
