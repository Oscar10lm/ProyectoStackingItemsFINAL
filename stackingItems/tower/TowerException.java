package tower;

/**
 * Define las excepciones personalizadas para los errores de lógica en la torre.
 * @author gaitan - lasso
 */

public class TowerException extends Exception {
    
    public static final String HEIGHT_EXCEEDED = "No se puede agregar el elemento. Supera la altura máxima.";
    public static final String DUPLICATE_ITEM = "El elemento ya existe en la torre.";
    public static final String MISSING_COMPANION = "El elemento requiere su compañero para ser insertado.";
    public static final String ITEM_LOCKED = "El elemento está bloqueado y no puede ser removido.";
    public static final String ITEM_NOT_FOUND = "El elemento no se encuentra en la torre.";

    public TowerException(String message) {
        super(message);
    }
}
