package com.bajaj.hiring;

import com.bajaj.hiring.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BajajTestApplication implements CommandLineRunner {

    @Autowired
    private ProcessService processService;

    public static void main(String[] args) {
        SpringApplication.run(BajajTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        processService.executeProcess();
    }
}
