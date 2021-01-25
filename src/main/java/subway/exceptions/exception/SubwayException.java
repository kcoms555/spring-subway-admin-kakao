package subway.exceptions.exception;

public class SubwayException extends RuntimeException{
    public SubwayException(){super();}
    public SubwayException(String str){
        super(str);
    }
}
