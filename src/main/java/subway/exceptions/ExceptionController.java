package subway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.exceptions.exception.*;
import subway.line.Line;
import subway.line.LineDao;
import subway.section.SectionDao;
import subway.station.StationDao;

@ControllerAdvice
public class ExceptionController {
    private JdbcTemplate jdbcTemplate;

    ExceptionController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity lineNotFoundHandle() {
        return ResponseEntity.badRequest().body("해당 노선을 찾을 수 없습니다");
    }

    @ExceptionHandler(LineDuplicatedException.class)
    public ResponseEntity lineDuplicatedHandle() {
        return ResponseEntity.badRequest().body("중복된 노선입니다");
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity subwayExceptionHandle(SubwayException e) {
        return ResponseEntity.badRequest().body(e.getMessage()) ;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity Exception(Exception e) {
        System.out.println("==================================에러 처리==================================");
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.out.println("==================================데이터베이스==================================");
        jdbcTemplate.query("SELECT * FROM STATION", StationDao.stationMapper).forEach(System.out::println);
        jdbcTemplate.query("SELECT * FROM LINE", LineDao.lineMapper).forEach(System.out::println);
        jdbcTemplate.query("SELECT * FROM SECTION", SectionDao.sectionMapper).forEach(System.out::println);
        System.out.println("==================================에러 처리 끝==================================");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
