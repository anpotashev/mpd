package ru.net.arh.mpd.util;

import ru.net.arh.mpd.model.MpdAnswer;
import ru.net.arh.mpd.model.exception.MpdException;
import ru.net.arh.mpd.model.status.MpdSongTime;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * Парсер ответов mpd-сервиса. POJO классы и поля должны иметь аннотацию @see ru.net.arh.mpd.model.MpdAnswer
 */
public class MpdAnswersParser {

    /**
     * Парсинг ответа в коллекцию объектов.
     *
     * @param clazz - класс элемента коллекции ответа
     * @param list  - массив строк, полученный в ответ от сервера на команду
     */
    public static <T> List<T> parseAll(Class<T> clazz, List<String> list) {
        if (!clazz.isAnnotationPresent(MpdAnswer.class)) {
            throw new MpdException("Class does not have MpdAnswer-annotation");
        }
        List<List<String>> lists = groupList(list, clazz.getAnnotation(MpdAnswer.class).preffix());
        Map<String, Field> fieldsMap = getFieldsMap(clazz);
        return lists.stream()
                .map(strings -> parse(clazz, strings, fieldsMap))
                .collect(Collectors.toList());
    }

    private static final String UTF8_BOM = "\uFEFF";

    private static String removeBom(String s) {
        return s.startsWith(UTF8_BOM)
                ? s.substring(1)
                : s;
    }

    private static <T> T parse(Class<T> clazz, List<String> list, Map<String, Field> fieldsMap) {
        try {
            T result = clazz.newInstance();
            for (String line : list) {
                for (Entry<String, Field> entry : fieldsMap.entrySet()) {
                    if (line.startsWith(entry.getKey())) {
                        setFieldValue(result, entry.getValue(), removeBom(line.substring(entry.getKey().length()).trim()));
                        break;
                    }
                }
            }
            return result;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new MpdException("Error parsing result");
        }
    }


    /**
     * Парсинг ответа в единичный объект.
     *
     * @param clazz - класс ответа
     * @param list  - массив строк, полученный в ответ от сервера на команду
     */
    public static <T> T parse(Class<T> clazz, List<String> list) {
        return parse(clazz, list, getFieldsMap(clazz));
    }

    private static <T> void setFieldValue(T result, Field field, String value)
            throws IllegalAccessException {
        Class<?> type = field.getType();
        field.setAccessible(true);
        field.set(result, toObject(type, value));
    }

    private static Map<String, Field> getFieldsMap(Class<?> clazz) {
        return Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MpdAnswer.class))
                .collect(toMap(f -> f.getAnnotation(MpdAnswer.class).preffix()[0], f -> f));
    }

    private static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz || boolean.class == clazz) {
            return value.equals("1");
        }
        if (Byte.class == clazz || byte.class == clazz) {
            return Byte.parseByte(value);
        }
        if (Short.class == clazz || short.class == clazz) {
            return Short.parseShort(value);
        }
        if (Integer.class == clazz || int.class == clazz) {
            return Integer.parseInt(value);
        }
        if (Long.class == clazz || long.class == clazz) {
            return Long.parseLong(value);
        }
        if (Float.class == clazz || float.class == clazz) {
            return Float.parseFloat(value);
        }
        if (Double.class == clazz || double.class == clazz) {
            return Double.parseDouble(value);
        }
        if (LocalDateTime.class == clazz) {
            Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(value));
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//      return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
        }
        if (MpdSongTime.class == clazz) {
            int[] ints = Arrays.stream(value.split(":")).mapToInt(Integer::parseInt).toArray();
            return new MpdSongTime(ints[0], ints[1]);
        }
        return value;
    }

    /**
     * Группирует массив строк в массив массивов строк. Строка начинающаяся с "beginLine" служит индикатором начала нового элемента
     */
    private static List<List<String>> groupList(List<String> answer, String... beginLine) {
        AtomicInteger counter = new AtomicInteger(0);
        return new ArrayList(
                answer.stream()
                        .collect(
                                groupingBy(
                                        s -> counter.addAndGet(isNew(s, beginLine) ? 1 : 0)
                                        , LinkedHashMap::new
                                        , Collectors.toList()
                                )
                        ).values());
    }

    private static boolean isNew(String value, String... values) {
        return Arrays.stream(values).map(value::startsWith).reduce(Boolean::logicalOr).get();
    }
}
