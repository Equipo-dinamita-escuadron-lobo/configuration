package co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

/**
 * Excepción para cuando se intenta eliminar una clase de documento que está siendo utilizada por tipos de documentos.
 */
public class DocumentClassInUseException extends BaseBusinessException {

    public DocumentClassInUseException() {
        super(DocumentClassesErrorCode.DOCUMENT_CLASS_IN_USE);
    }

    public DocumentClassInUseException(String className) {
        super(
            DocumentClassesErrorCode.DOCUMENT_CLASS_IN_USE,
            String.format("No se puede eliminar la clase de documento '%s' porque está siendo utilizada por tipos de documentos activos", className)
        );
    }

    public DocumentClassInUseException(Long id, String className) {
        super(
            DocumentClassesErrorCode.DOCUMENT_CLASS_IN_USE,
            String.format("No se puede eliminar la clase de documento con ID %d ('%s') porque está siendo utilizada por tipos de documentos activos", id, className)
        );
    }
}
