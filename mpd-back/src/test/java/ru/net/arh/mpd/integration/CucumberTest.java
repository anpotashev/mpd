package ru.net.arh.mpd.integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features"
, glue = {"ru.net.arh.mpd.integration.steps"})
public class CucumberTest {
}
