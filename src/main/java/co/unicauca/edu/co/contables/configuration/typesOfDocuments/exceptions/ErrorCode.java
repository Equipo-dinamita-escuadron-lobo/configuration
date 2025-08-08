package co.unicauca.edu.co.contables.configuration.costCenters.exceptions;

import lombok.Getter;

/**
 * Catálogo de errores utilizado para identificar y describir errores comunes.
 */
@Getter
public enum ErrorCode {

    GENERIC_ERROR("GENERIC_ERROR", "Ha ocurrido un error"),

    //Codigos de Centro de Costos
    COST_CENTER_NOT_FOUND("COST_CENTER_NOT_FOUND", "Centro de costo no encontrado");
  
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
