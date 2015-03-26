/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author TiDy
 */
@Controller
public class CompteController {

    @RequestMapping("/account/inscription")
    public ModelAndView inscription() {
        ModelAndView mv = new ModelAndView("account/inscription");
        return mv;
    }

}
