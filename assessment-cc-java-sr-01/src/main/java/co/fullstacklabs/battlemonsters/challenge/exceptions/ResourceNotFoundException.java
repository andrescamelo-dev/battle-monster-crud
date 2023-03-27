package co.fullstacklabs.battlemonsters.challenge.exceptions;

import java.io.Serializable;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class ResourceNotFoundException extends RuntimeException implements Serializable {
    public static final long serialVersionUID = 4328843;
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
