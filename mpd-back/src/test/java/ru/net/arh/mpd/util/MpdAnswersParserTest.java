package ru.net.arh.mpd.util;

import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MpdAnswersParserTest {

    @Test
    public void parseAll() {
        PodamFactory factory = new PodamFactoryImpl();
        List<MpdPojo4Test> expected = IntStream.range(0, 20)
                .mapToObj(n -> factory.manufacturePojo(MpdPojo4Test.class))
                .collect(Collectors.toList());
        ;
        List<String> list = expected.stream()
                .map(MpdPojo4Test::stringList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        list.add("OK");
        List<MpdPojo4Test> actual = MpdAnswersParser.parseAll(MpdPojo4Test.class, list);
        assertThat(actual.size()).isEqualTo(expected.size());
        IntStream.range(0, actual.size()).forEach(i -> assertThat(expected.get(i)).isEqualToComparingFieldByFieldRecursively(actual.get(i)));
    }

    @Test
    public void parse() {
        PodamFactory factory = new PodamFactoryImpl();
        MpdPojo4Test expected = factory.manufacturePojo(MpdPojo4Test.class);
        MpdPojo4Test actual = MpdAnswersParser.parse(MpdPojo4Test.class, expected.stringList());
        assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
    }
}