package com.salesmanager.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/un1234")
public class ImUnique {
	@RequestMapping(value="/ram",method=RequestMethod.GET)
	public String getData(){
		return "Hi Data";
	}
}
