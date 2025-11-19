package com.taller.view;

import com.taller.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame{
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    private final AuthService authService;

    public MainView() {
        this.authService = AuthService.getInstance();
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Inventario - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Barra superior
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Panel central (aquí irán las funcionalidades)
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        logger.info("Ventana principal inicializada");
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Título
        JLabel titleLabel = new JLabel("🔧 Sistema de Inventario - Taller Automotriz");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Panel derecho con info de usuario
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        String nombreUsuario = authService.getUsuarioActual().getNombreCompleto();
        String rol = authService.getUsuarioActual().getRol().name();

        JLabel userLabel = new JLabel("👤 " + nombreUsuario + " (" + rol + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> handleLogout());

        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Mensaje de bienvenida
        JLabel welcomeLabel = new JLabel("✓ Login exitoso");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(40, 167, 69));
        panel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        JLabel messageLabel = new JLabel("Sistema listo para usar");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        messageLabel.setForeground(Color.GRAY);
        panel.add(messageLabel, gbc);

        gbc.gridy = 2;
        JLabel infoLabel = new JLabel("<html><center>Próximamente aquí estarán:<br>" +
                "• Gestión de Productos<br>" +
                "• Control de Inventario<br>" +
                "• Reportes y Estadísticas</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setBorder(new EmptyBorder(30, 0, 0, 0));
        panel.add(infoLabel, gbc);

        return panel;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            logger.info("Cerrando sesión...");
            authService.logout();

            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                this.dispose();
            });
        }
    }
}
