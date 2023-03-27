package co.fullstacklabs.battlemonsters.challenge.exceptions;

import java.io.Serializable;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class UnprocessableFileException extends RuntimeException implements Serializable {
    public static final long serialVersionUID = 4328844;
    public UnprocessableFileException(String message){
        super(message);
    }
}
