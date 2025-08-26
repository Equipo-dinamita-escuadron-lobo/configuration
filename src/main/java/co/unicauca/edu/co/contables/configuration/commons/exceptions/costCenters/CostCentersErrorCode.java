package co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.ErrorCodeDefinition;
import lombok.Getter;

/**
 * Códigos de error específicos del dominio de Centros de Costo.
 */
@Getter
public enum CostCentersErrorCode implements ErrorCodeDefinition {

    COST_CENTER_NOT_FOUND("COST_CENTER_NOT_FOUND", "Centro de costo no encontrado"),
    COST_CENTER_ALREADY_EXISTS("COST_CENTER_ALREADY_EXISTS", "Centro de costo ya existe"),
    COST_CENTER_HAS_CHILDREN("COST_CENTER_HAS_CHILDREN", "No se puede eliminar un centro de costo que tiene hijos");

    private final String code;
    private final String message;

    CostCentersErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}


