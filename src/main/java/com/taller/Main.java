package com.taller;

import com.formdev.flatlaf.FlatLightLaf;
import com.taller.config.DatabaseManager;
import com.taller.view.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
        logger.info("=== Iniciando Sistema de Inventario ===");

        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            logger.error("Error al configurar FlatLaf", e);
        }

        // Inicializar base de datos
        DatabaseManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            logger.info("Pantalla de login mostrada");
        });

        // Cerrar BD al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.getInstance().closeConnection();
            logger.info("=== Aplicación finalizada ===");
        }));
    }
}
