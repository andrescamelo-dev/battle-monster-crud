package co.fullstacklabs.problemsolving.challenge2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge2 {

    private static List<Integer> diceSides = Arrays.asList(1,2,3,4,5,6);
    private static int number3 = 3;
    private static int number1 = 1;

    public static int diceFacesCalculator(int dice1, int dice2, int dice3) {
        List<Integer> dice = Arrays.asList(dice1,dice2,dice3);

        dice.forEach(d -> {
            if (!diceSides.contains(d)){
                throw new IllegalArgumentException();
            }
        });

        Map<Integer, Long> groupNumber = dice.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        if (groupNumber.entrySet().size() == number3){
            List<Integer> listNumber = new ArrayList<>(groupNumber.keySet());
            Optional<Integer> max = listNumber.stream().max(Comparator.comparing(Integer::intValue));
            if (max.isPresent()){
                return max.get();
            }
        } else if (groupNumber.entrySet().size() == number1){
            Optional<Integer> first = groupNumber.keySet().stream().findFirst();
            if (first.isPresent()){
                return first.get() * 3;
            }
        } else {
            Optional<Integer> two = groupNumber.entrySet().stream().filter(diceValue -> diceValue.getValue() == 2).findFirst().map(Map.Entry::getKey);
            if (two.isPresent()){
                return  two.get() * 2;
            }
        }

        return 0;        
    }
}
