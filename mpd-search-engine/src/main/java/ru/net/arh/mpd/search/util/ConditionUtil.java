package ru.net.arh.mpd.search.util;

import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.Id3Predicate;
import ru.net.arh.mpd.search.model.Id3Tag;
import ru.net.arh.mpd.search.model.PredicateCondition;
import ru.net.arh.mpd.search.model.SearchCondition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;
import java.util.function.Predicate;

public class ConditionUtil {

    public static Predicate<TreeItem> getPredicate(Condition condition) {
        if (condition instanceof PredicateCondition) {
            List<Condition> conditions = ((PredicateCondition) condition).getConditions();
            switch (((PredicateCondition) condition).getType()) {
                case AND:
                    return conditions
                            .stream()
                            .map(ConditionUtil::getPredicate)
                            .reduce(Predicate::and).get();
                case OR:
                    return conditions
                            .stream()
                            .map(ConditionUtil::getPredicate)
                            .reduce(Predicate::or).get();
                default: //case NOT:
                    return getPredicate(conditions.get(0)).negate();
            }
        } else {
            SearchCondition searchCondition = (SearchCondition) condition;
            return treeItem -> {
                Id3Tag id3Tag = searchCondition.getId3Tag();
                Id3Predicate operation = searchCondition.getOperation();
                String value = searchCondition.getValue();
                return operation.getFunction().apply(field(treeItem, id3Tag), value);
            };
        }

    }

    private static String field(TreeItem item, Id3Tag tag) {
        switch (tag) {
            case ALBUM:
                return item.getAlbum();
            default: //case ARTIST:
                return item.getArtist();
        }
    }
}
