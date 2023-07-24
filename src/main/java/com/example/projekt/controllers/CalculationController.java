package com.example.projekt.controllers;

import com.example.projekt.model.Data;
import com.example.projekt.model.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
public class CalculationController {

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/calculations/interpolation")
    public Result Interpolate(@RequestBody Data data) {
        return new Result("Interpolates!");
    }
}
