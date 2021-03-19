package exceptions;

public class NoGraphsToPlotException extends RuntimeException {
    public NoGraphsToPlotException() {
        super();
    }

    public NoGraphsToPlotException(String str) {
        super(str);
    }
}
