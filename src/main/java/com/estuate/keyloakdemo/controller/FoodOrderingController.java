package com.estuate.keyloakdemo.controller;

import com.estuate.keyloakdemo.service.KeycloakAdminService;
import com.estuate.keyloakdemo.service.UserRegistrationRecord;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * FoodOrderingController handles web requests for the food ordering application.
 */
@Controller
public class FoodOrderingController {

    private final KeycloakAdminService keycloakAdminService;

    // The controller now requires the KeycloakAdminService to be injected.
    public FoodOrderingController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * This method handles displaying the registration page.
     * It was likely missing from your controller.
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * This method handles the form submission from the registration page.
     */
    @PostMapping("/register")
    public String processRegistration(UserRegistrationRecord registrationRecord, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to create the user in Keycloak
            keycloakAdminService.createUser(registrationRecord);
            // On success, redirect to the login page with a success message
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            // Handle errors, such as if the user already exists
            if (e.getStatusCode().value() == 409) { // 409 Conflict
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("errorMsg", "Username or email already exists.");
            } else {
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("errorMsg", "An unexpected error occurred during registration.");
            }
            return "redirect:/register";
        }
    }

    @GetMapping("/menu")
    public String menu(Authentication authentication, Model model) {
        // Updated to handle a standard Authentication principal
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "menu";
    }
}

