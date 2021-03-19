package exceptions;

public class InvalidArrayLength extends RuntimeException {
    public InvalidArrayLength() {
        super();
    }

    public InvalidArrayLength(String str) {
        super(str);
    }
}
