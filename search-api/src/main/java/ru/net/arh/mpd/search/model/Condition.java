package ru.net.arh.mpd.search.model;

import java.io.Serializable;
import java.util.Arrays;

public class Condition implements Serializable {

    public static Condition or(Condition...conditions) {
        if (conditions.length < 2) {
            throw new RuntimeException("Wrong conditions count");
        }
        return new PredicateCondition(Arrays.asList(conditions), PredicateCondition.PredicateType.OR);
    }

    public static Condition and(Condition...conditions) {
        if (conditions.length < 2) {
            throw new RuntimeException("Wrong conditions count");
        }
        return new PredicateCondition(Arrays.asList(conditions), PredicateCondition.PredicateType.AND);
    }

    public static Condition not(Condition condition) {
        return new PredicateCondition(Arrays.asList(condition), PredicateCondition.PredicateType.NOT);
    }

}
