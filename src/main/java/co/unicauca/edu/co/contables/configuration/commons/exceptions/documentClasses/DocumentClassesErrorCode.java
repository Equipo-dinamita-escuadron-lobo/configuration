package co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.ErrorCodeDefinition;
import lombok.Getter;

@Getter
public enum DocumentClassesErrorCode implements ErrorCodeDefinition {

    DOCUMENT_CLASS_NOT_FOUND("DOCUMENT_CLASS_NOT_FOUND", "Clase de documento no encontrada"),
    DOCUMENT_CLASS_ALREADY_EXISTS("DOCUMENT_CLASS_ALREADY_EXISTS", "Clase de documento ya existe");

    private final String code;
    private final String message;

    DocumentClassesErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}


