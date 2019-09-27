package ru.net.arh.mpd.connection.rw;

import org.junit.Test;

public class MyTest {

    @Test
    public void test( ) {
        MpdReaderWriterMockImpl rw = new MpdReaderWriterMockImpl();
        rw.loadYaml("mpd.yaml");
    }
}
