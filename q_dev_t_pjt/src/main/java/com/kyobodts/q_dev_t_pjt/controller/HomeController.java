package com.kyobodts.q_dev_t_pjt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {
    	
        return "안녕";
    }
}
