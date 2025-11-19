package com.taller.controller;

import com.taller.service.AuthService;
import com.taller.view.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final LoginView view;
    private final AuthService authService;

    public LoginController(LoginView view) {
        this.view = view;
        this.authService = AuthService.getInstance();
    }

    public boolean login(String usuario, String password) {
        logger.info("Intento de login para usuario: " + usuario);

        try {
            boolean resultado = authService.login(usuario, password);

            if (resultado) {
                logger.info("Login exitoso para: " + usuario);
            } else {
                logger.warn("Login fallido para: " + usuario);
            }

            return resultado;

        } catch (Exception e) {
            logger.error("Error durante el login", e);
            return false;
        }
    }

    public void logout() {
        authService.logout();
    }
}
