package com.example.projekt.controllers;

import com.example.projekt.model.data.Savable;
import com.example.projekt.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private final DataService dataService;

    public HistoryController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("")
    public List<Savable> TreatResultsHistorySearch(@RequestBody String username) {
        List<Savable> records = dataService.getDataByUserId(username);
        System.out.println(records);
        return records;
    }
}
