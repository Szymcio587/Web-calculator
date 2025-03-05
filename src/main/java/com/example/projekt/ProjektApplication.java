package com.example.projekt;

import org.mariuszgromada.math.mxparser.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjektApplication {

    public static void main(String[] args) {
        License.iConfirmNonCommercialUse("szymon.talar2882@gmail.com");
        SpringApplication.run(ProjektApplication.class, args);
    }

}
