package com.peisia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/hihi/*", produces = "text/html; charset=UTF-8")
@Controller					
public class MyController {					
	@GetMapping("/getData")				
	@ResponseBody				
	public String getData() {				
		// 데이터를 생성하거나 가져옴			
        String data = "<p>가나다라마바사.</p>";
        return data;	
	}				
}					