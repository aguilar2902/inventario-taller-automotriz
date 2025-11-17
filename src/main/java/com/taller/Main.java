package com.taller;

import com.formdev.flatlaf.FlatLightLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main( String[] args )
    {
        logger.info("Iniciando Sistema de Inventario...");

        // Configurar Look and Feel moderno
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            logger.error("Error al configurar FlatLaf", e);
        }

        // Iniciar aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Inventario - Taller Automotriz");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JLabel label = new JLabel("¡Proyecto iniciado correctamente!", SwingConstants.CENTER);
            frame.add(label);

            frame.setVisible(true);

            logger.info("Aplicación iniciada exitosamente");
        });
    }
}
