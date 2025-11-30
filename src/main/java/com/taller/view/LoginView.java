package com.taller.view;

import com.taller.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class LoginView extends JFrame{
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelar;
    private JLabel lblMensaje;
    private LoginController controller;

    public LoginView() {
        controller = new LoginController(this);
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        setupUI();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión - Sistema de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel superior con logo y título
        JPanel headerPanel = createHeaderPanel();

        // Panel central con formulario
        JPanel formPanel = createFormPanel();

        // Panel inferior con botones
        JPanel buttonPanel = createButtonPanel();

        // Panel para mensaje de error/éxito
        JPanel messagePanel = createMessagePanel();

        // Agregar paneles al panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        // Panel contenedor para botones
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Icono (puedes reemplazarlo con un icono real)
        JLabel iconLabel = new JLabel("🔧");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel titleLabel = new JLabel("Sistema de Inventario");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(15, 0, 5, 0));

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Taller de Electricidad Automotriz");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(titleLabel);
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Label Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblUsuario, gbc);

        // Campo Usuario
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);

        // Label Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 8, 0);
        panel.add(lblPassword, gbc);

        // Campo Contraseña
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 0, 8, 0);
        panel.add(txtPassword, gbc);

        // Enter en los campos para login
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        };

        txtUsuario.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setBackground(Color.WHITE);

        // Botón Iniciar Sesión
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(150, 40));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(60, 120, 170));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(70, 130, 180));
            }
        });

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(Color.DARK_GRAY);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(btnLogin);
        panel.add(btnCancelar);

        return panel;
    }

    private JPanel createMessagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 40));

        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(lblMensaje, BorderLayout.CENTER);

        return panel;
    }

    private void setupUI() {
        // Acción del botón Login
        btnLogin.addActionListener(e -> handleLogin());

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                logger.info("Usuario canceló el login");
                System.exit(0);
            }
        });

        // Mostrar credenciales por defecto (solo para desarrollo)
        mostrarCredencialesPorDefecto();
    }

    private void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Validación básica
        if (usuario.isEmpty()) {
            mostrarError("Por favor ingrese su usuario");
            txtUsuario.requestFocus();
            return;
        }

        // Deshabilitar botón mientras procesa
        btnLogin.setEnabled(false);
        btnLogin.setText("Validando...");
        lblMensaje.setText("Validando credenciales...");
        lblMensaje.setForeground(Color.GRAY);

        // Procesar login en segundo plano
        SwingWorker<LoginController.LoginResult, Void> worker = new SwingWorker<>() {
            @Override
            protected LoginController.LoginResult doInBackground() throws Exception {
                return controller.login(usuario, password);
            }

            @Override
            protected void done() {
                try {
                    LoginController.LoginResult resultado = get();

                    switch (resultado) {
                        case EXITOSO:
                            mostrarExito("¡Bienvenido!");
                            Timer timer = new Timer(500, evt -> abrirVentanaPrincipal());
                            timer.setRepeats(false);
                            timer.start();
                            break;

                        case PRIMER_ACCESO:
                            mostrarInfo("Primer acceso detectado");
                            abrirCambioPasswordPrimerAcceso();
                            break;

                        case REQUIERE_CAMBIO_PASSWORD:
                            mostrarInfo("Debe cambiar su contraseña");
                            abrirCambioPasswordObligatorio();
                            break;

                        case CREDENCIALES_INVALIDAS:
                            mostrarError("Usuario o contraseña incorrectos");
                            txtPassword.setText("");
                            txtPassword.requestFocus();
                            btnLogin.setEnabled(true);
                            btnLogin.setText("Iniciar Sesión");
                            break;

                        case ERROR:
                            mostrarError("Error al procesar el login");
                            btnLogin.setEnabled(true);
                            btnLogin.setText("Iniciar Sesión");
                            break;
                    }
                } catch (Exception e) {
                    logger.error("Error durante el login", e);
                    mostrarError("Error al procesar el login");
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar Sesión");
                }
            }
        };

        worker.execute();
    }

    private void abrirCambioPasswordPrimerAcceso() {
        logger.info("Abriendo diálogo de cambio de contraseña (primer acceso)");

        SwingUtilities.invokeLater(() -> {
            CambiarPasswordDialog dialog = new CambiarPasswordDialog(this, true);
            dialog.setVisible(true);

            if (dialog.isPasswordEstablecida()) {
                mostrarExito("¡Contraseña establecida! Iniciando sesión...");
                Timer timer = new Timer(800, evt -> abrirVentanaPrincipal());
                timer.setRepeats(false);
                timer.start();
            } else {
                // Si canceló o falló, limpiar formulario
                txtUsuario.setText("");
                txtPassword.setText("");
                btnLogin.setEnabled(true);
                btnLogin.setText("Iniciar Sesión");
                lblMensaje.setText("");
            }
        });
    }

    private void abrirCambioPasswordObligatorio() {
        logger.info("Abriendo diálogo de cambio de contraseña (obligatorio)");

        SwingUtilities.invokeLater(() -> {
            CambiarPasswordDialog dialog = new CambiarPasswordDialog(this, true);
            dialog.setVisible(true);

            if (dialog.isPasswordEstablecida()) {
                mostrarExito("¡Contraseña actualizada! Iniciando sesión...");
                Timer timer = new Timer(800, evt -> abrirVentanaPrincipal());
                timer.setRepeats(false);
                timer.start();
            } else {
                txtPassword.setText("");
                btnLogin.setEnabled(true);
                btnLogin.setText("Iniciar Sesión");
                lblMensaje.setText("");
            }
        });
    }

    private void mostrarInfo(String mensaje) {
        lblMensaje.setText("ℹ️ " + mensaje);
        lblMensaje.setForeground(new Color(0, 123, 255));
    }
    private void abrirVentanaPrincipal() {
        logger.info("Abriendo ventana principal...");

        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
            this.dispose();
        });
    }

    public void mostrarError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setForeground(new Color(220, 53, 69));

        // Efecto de vibración
        Point punto = getLocation();
        for (int i = 0; i < 3; i++) {
            try {
                setLocation(punto.x - 5, punto.y);
                Thread.sleep(50);
                setLocation(punto.x + 5, punto.y);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        setLocation(punto);
    }

    public void mostrarExito(String mensaje) {
        lblMensaje.setText("✓ " + mensaje);
        lblMensaje.setForeground(new Color(40, 167, 69));
    }

    private void mostrarCredencialesPorDefecto() {
        // Solo para desarrollo - REMOVER en producción
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(255, 250, 205));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel infoTitle = new JLabel("💡 Credenciales por defecto:");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoUser = new JLabel("Usuario: admin");
        infoUser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoPass = new JLabel("Contraseña: admin123");
        infoPass.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(infoUser);
        infoPanel.add(infoPass);

        ((JPanel) getContentPane().getComponent(0)).add(infoPanel, BorderLayout.NORTH);
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }
}
