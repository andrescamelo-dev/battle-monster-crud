package co.fullstacklabs.battlemonsters.challenge.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class ValidationError implements Serializable {
    public static final long serialVersionUID = 4328845;
    private transient List<ErrorDetails> violations = new ArrayList<>();

    public List<ErrorDetails> getViolations() {
        return violations;
    }

    public void addViolations(ErrorDetails violation) {
        this.violations.add(violation);
    }
}
