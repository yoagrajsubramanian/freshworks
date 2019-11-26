package com.yogaraj.datastore;

import com.yogaraj.datastore.component.DataStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class DataStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataStoreApplication.class, args);
	}
}
