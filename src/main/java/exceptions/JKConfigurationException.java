package exceptions;

public class JKConfigurationException extends RuntimeException {
    public JKConfigurationException() {
    }

    public JKConfigurationException(String message) {
        super(message);
    }
}
