package pj.gob.pe.security.exception;

public class AuthLoginException extends RuntimeException{

    private static final long serialVersionUID = 7513221174646872318L;

    public AuthLoginException(String mensaje) {
        super(mensaje);
    }
}
