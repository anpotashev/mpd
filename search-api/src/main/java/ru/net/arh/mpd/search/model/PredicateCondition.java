package ru.net.arh.mpd.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PredicateCondition extends Condition {

    private List<Condition> conditions;
    private PredicateType type;

    enum PredicateType {
        AND,
        OR,
        NOT
    }

    @Override
    public String toString() {
        switch (type) {
            case OR:
                return conditions.stream()
                        .map(s -> "(" + s + ")")
                        .collect(Collectors.joining(" || "));
            case AND:
                return conditions.stream()
                        .map(s -> "(" + s + ")")
                        .collect(Collectors.joining(" && "));
            default:
                return "!(" + conditions.get(0).toString() + ")";

        }
    }
}
