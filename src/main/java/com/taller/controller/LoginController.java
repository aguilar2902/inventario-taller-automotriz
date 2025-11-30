package com.taller.controller;

import com.taller.model.Usuario;
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

    /**
     * Intenta login normal o detecta primer acceso
     */
    public LoginResult login(String usuario, String password) {
        logger.info("Intento de login para usuario: " + usuario);

        try {
            // Si tiene contraseña ingresada, SOLO intentar login normal
            if (!password.isEmpty()) {
                boolean loginExitoso = authService.login(usuario, password);

                if (loginExitoso) {
                    logger.info("Login exitoso para: " + usuario);

                    // Verificar si requiere cambio de contraseña (solo EMPLEADOS)
                    if (authService.requiereCambioPassword() && !authService.isAdmin()) {
                        logger.info("Usuario requiere cambio de contraseña: " + usuario);
                        return LoginResult.REQUIERE_CAMBIO_PASSWORD;
                    }

                    return LoginResult.EXITOSO;
                }

                // Si falló con contraseña, simplemente es credencial inválida
                logger.warn("Login fallido para: " + usuario);
                return LoginResult.CREDENCIALES_INVALIDAS;
            }

            // Si NO tiene contraseña, intentar primer acceso (SOLO EMPLEADOS)
            boolean primerAcceso = authService.loginPrimerAcceso(usuario);

            if (primerAcceso) {
                logger.info("Primer acceso detectado para EMPLEADO: " + usuario);
                return LoginResult.PRIMER_ACCESO;
            }

            logger.warn("Login fallido para: " + usuario);
            return LoginResult.CREDENCIALES_INVALIDAS;

        } catch (Exception e) {
            logger.error("Error durante el login", e);
            return LoginResult.ERROR;
        }
    }

    public void logout() {
        authService.logout();
    }

    // Enum para resultados de login
    public enum LoginResult {
        EXITOSO,
        PRIMER_ACCESO,
        REQUIERE_CAMBIO_PASSWORD,
        CREDENCIALES_INVALIDAS,
        ERROR
    }
}
