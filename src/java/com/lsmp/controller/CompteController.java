/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

import com.lsmp.manager.TypeUtilisateurDao;
import com.lsmp.manager.UtilisateurDao;
import com.lsmp.model.Utilisateur;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author TiDy
 */
@Controller
public class CompteController {
    @Autowired
    TypeUtilisateurDao typeUtilisateurManager;
    
    @Autowired
    UtilisateurDao utilisateurManager;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("/account/login");

        return model;

    }

    // customize the error message
    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession().getAttribute(key);

        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }

        return error;
    }

    @RequestMapping(value = "/account/inscription", method = RequestMethod.GET)
    public ModelAndView inscriptionForm() {
        ModelAndView mv = new ModelAndView("account/inscription");
        mv.addObject("newUtilisateur", new Utilisateur());
        mv.addObject("typeUtilisateurs", typeUtilisateurManager.findAllTypeUtilisateur());
        return mv;
    }

    @RequestMapping(value = "/account/inscription", method = RequestMethod.POST)
    public ModelAndView inscriptionSubmit(@ModelAttribute Utilisateur newUtilisateur) {
        String generatedSecuredPasswordHash = BCrypt.hashpw(newUtilisateur.getMdpUtilisateur(), BCrypt.gensalt(10));
        newUtilisateur.setMdpUtilisateur(generatedSecuredPasswordHash);
        System.out.println(newUtilisateur.getIdentifiantUtilisateur());
        System.out.println(newUtilisateur.getMailUtilisateur());
        System.out.println(newUtilisateur.getPrenomUtilisateur());
        System.out.println(newUtilisateur.getNomUtilisateur());
        System.out.println(newUtilisateur.getTypeUtilisateur());
//        TypeUtilisateurManager typeUtilisateurManager = new TypeUtilisateurManager();
//        TypeUtilisateur typeUtilisateur = typeUtilisateurManager.find(Integer.parseInt(newUtilisateur.getTypeUtilisateur().toString()));
//        System.out.println(typeUtilisateur.getIdTypeUtilisateur());
//        System.out.println(typeUtilisateur.getLibelle());
//        newUtilisateur.setTypeUtilisateur(typeUtilisateur);
        utilisateurManager.saveUtilisateur(newUtilisateur);
        ModelAndView mv = new ModelAndView("account/created");
        return mv;
    }
}
