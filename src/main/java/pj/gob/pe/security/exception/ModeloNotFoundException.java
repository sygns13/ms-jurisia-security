package pj.gob.pe.security.exception;

public class ModeloNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 7513221174646872318L;

    public ModeloNotFoundException(String mensaje) {
        super(mensaje);
    }
}
