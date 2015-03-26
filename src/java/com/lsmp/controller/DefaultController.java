/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

/**
 *
 * @author TiDy
 */
import com.lsmp.manager.UtilisateurManager;
import java.text.MessageFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("default/index");
        return mv;
    }

    @RequestMapping("/template")
    public ModelAndView template() {
        ModelAndView mv = new ModelAndView("default/template");
        return mv;
    }

    @RequestMapping("/bonjour")
    public ModelAndView helloWorld() {
        UtilisateurManager utilisateurManager = new UtilisateurManager();
        ModelAndView mv = new ModelAndView("default/bonjour");
        String out = "All User Details: ";
        mv.addObject("utilisateur", utilisateurManager.findAllUsers());
        mv.addObject("message", out);
        return mv;
    }

    @RequestMapping("/error")
    public String customError(HttpServletRequest request, HttpServletResponse response, Model model) {
        // retrieve some useful information from the request
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        // String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String exceptionMessage = getExceptionMessage(throwable, statusCode);

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        String message = MessageFormat.format("Error {0} returned for {1} with message {2}",
                statusCode, requestUri, exceptionMessage
        );

        model.addAttribute("errorMessage", message);
        return "default/error";
    }

    private String getExceptionMessage(Throwable throwable, Integer statusCode) {
        if (throwable != null) {
            return throwable.getMessage();
        }
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return httpStatus.getReasonPhrase();
    }
}
