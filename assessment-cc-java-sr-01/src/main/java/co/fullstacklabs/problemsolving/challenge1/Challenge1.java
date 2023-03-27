package co.fullstacklabs.problemsolving.challenge1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge1 {

    public static final String POSITIVES = "positives";
    public static final String NEGATIVE = "negative";
    public static final String ZEROS = "zeros";

    public static Map<String, Float> numbersFractionCalculator(Integer[] numbers) {
        Map<String , Float> result = new HashMap<>();
        Long countPositives = Arrays.stream(numbers).filter(b -> b > 0).count();
        Float positives = getTypeNumber(countPositives, numbers.length);
        result.put(POSITIVES,positives);

        Long countNegative = Arrays.stream(numbers).filter(b -> b < 0).count();
        Float negative = getTypeNumber(countNegative, numbers.length);
        result.put(NEGATIVE,negative);

        Long countZeros = Arrays.stream(numbers).filter(b -> b.equals(0)).count();
        Float zeros = getTypeNumber(countZeros, numbers.length);
        result.put(ZEROS,zeros);

        return result;
    }

    private static Float round(Float number, int decimlas) {
        double roundFactor = Math.pow(10, decimlas);
        return Double.valueOf
                        (Math.round((double) number * roundFactor) / roundFactor)
                .floatValue();
    }

    private static Float getTypeNumber(Long number, int length){
        return round(Optional.of(number).map(p -> p.floatValue() / length).orElse(0f),6);
    }
}
