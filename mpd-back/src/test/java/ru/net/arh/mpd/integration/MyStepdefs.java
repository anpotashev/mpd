package ru.net.arh.mpd.integration;

import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import ru.net.arh.mpd.connection.ConnectionService;

import static org.junit.Assert.assertTrue;

public class MyStepdefs extends BaseIntegrationTest implements En  {

    @Autowired
    private ConnectionService connectionService;
    public MyStepdefs() {
        Given("^клиент установил web-socket соединение$", () -> {
        });
        And("^соединение с mpd отсутствует$", () -> {
            connectionService.disconnect();
        });
        When("^клиент отправляет команду 'connect'$", () -> {
        });
        Then("^соединение с mpd установлено$", () -> {
            assertTrue(connectionService.isConnected());
        });
    }
}
