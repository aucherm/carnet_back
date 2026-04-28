package com.ma.carnet.journal.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CoucouController {

    @GetMapping("/chewinggum")
    public String coucou() {
        return "Le chewing gum c'est très vulgaire, le chewing gum ça m'exaspère";
    }
}