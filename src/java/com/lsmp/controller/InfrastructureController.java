/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

import com.lsmp.manager.InfrastructureDao;
import com.lsmp.model.Infrastructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(value = "infrastructure", types = {Infrastructure.class})
public class InfrastructureController {
    
    @Autowired
    InfrastructureDao infrastructureManager;
    
    @RequestMapping("/infrastructure")
    public ModelAndView infrastructure() {
        ModelAndView mv = new ModelAndView("infrastructure/index");
        String out = "All Infrastructure Details: ";
        mv.addObject("infrastructure", infrastructureManager.findAllInfrastructures());
        mv.addObject("message", out);
        return mv;
    }
}
