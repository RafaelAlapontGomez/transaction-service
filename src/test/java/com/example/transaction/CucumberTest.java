package com.example.transaction;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "classpath:microtransaction.feature",
		plugin = { 
				"pretty",
				"html:target/cucumber",
				"json:target/cucumber.json"
		},
		glue = "com.example.transaction.cucumber"
)
public class CucumberTest {

}
