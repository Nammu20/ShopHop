package com.example.ShopHop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @GetMapping("/Index")
    public String Index(){
        return "Index";
    }

    @GetMapping("Cart")
    public String Cart(){
        return "cart";
    }
    @GetMapping("Home")
    public String HomeNew(){
        return "Home";
    }

    @GetMapping("examples/echo")
    @ResponseBody
    public String makeGetEcho(@RequestParam("text") String text) {
        return text;
    }
}


