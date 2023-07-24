package com.example.projekt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private String message;

    public Result(String message) {
        this.message = message;
    }
}
