package subway.line;

import org.springframework.stereotype.Service;
import subway.exceptions.exception.LineCreateException;
import subway.section.SectionDao;
import subway.station.Stations;

@Service
public class LineService {
    private LineDao lineDao;
    private SectionDao sectionDao;

    public LineService(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line createLine(LineRequest lineRequest) {
        if (lineDao.existByName(lineRequest.getName())) {
            throw new LineCreateException("이미 존재하는 노선입니다");
        }
        //TODO 섹션 연결 완성
        return lineDao.create(lineRequest.getName(), lineRequest.getColor(), null);
    }

    public Line getLine(Long lineId) {
        return lineDao.getLineBy(lineId);
    }

    public Lines getLines() {
        return new Lines(lineDao.getAllLines());
    }

    public Stations getStations(Long lineId) {
        return new Stations(null);
    }

    public void deleteLine(Long lineId) {
        sectionDao.deleteByLineId(lineId);
        lineDao.deleteBy(lineId);
    }

    public void putLine(Long lineId, LineRequest lineRequest) {
        Line line = new Line(lineId, lineRequest.getName(), lineRequest.getColor(), null);
        lineDao.update(line);
    }

}
