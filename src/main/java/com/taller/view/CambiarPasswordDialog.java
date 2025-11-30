package com.taller.view;

import com.taller.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CambiarPasswordDialog extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(CambiarPasswordDialog.class);
    private final AuthService authService;

    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JLabel lblMensaje;

    private boolean passwordEstablecida = false;
    private final boolean esPrimerLogin;

    public CambiarPasswordDialog(Frame parent, boolean esPrimerLogin) {
        super(parent, esPrimerLogin ? "Establecer Contraseña" : "Cambiar Contraseña", true);
        this.esPrimerLogin = esPrimerLogin;
        this.authService = AuthService.getInstance();

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setDefaultCloseOperation(esPrimerLogin ? DO_NOTHING_ON_CLOSE : DISPOSE_ON_CLOSE);
        setSize(480, esPrimerLogin ? 420 : 380);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Panel superior con información
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Panel central con formulario
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);

        // Panel inferior con botones
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Icono
        JLabel iconLabel = new JLabel("🔐");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        String titulo = esPrimerLogin ? "Primer Acceso" : "Cambiar Contraseña";
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Mensaje informativo
        String mensaje = esPrimerLogin
                ? "Por seguridad, debe establecer una contraseña para continuar"
                : "Ingrese su nueva contraseña";

        JLabel messageLabel = new JLabel("<html><center>" + mensaje + "</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(Color.GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(titleLabel);
        panel.add(messageLabel);

        // Mensaje adicional para primer login
        if (esPrimerLogin) {
            JLabel warningLabel = new JLabel("<html><center><b>⚠️ Esta acción es obligatoria</b></center></html>");
            warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            warningLabel.setForeground(new Color(220, 53, 69));
            warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            warningLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
            panel.add(warningLabel);
        }

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Usuario actual (informativo)
        if (authService.getUsuarioActual() != null) {
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            JLabel lblUsuarioActual = new JLabel("Usuario: " + authService.getUsuarioActual().getUsuario());
            lblUsuarioActual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblUsuarioActual.setForeground(Color.GRAY);
            lblUsuarioActual.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(lblUsuarioActual, gbc);
            gbc.gridwidth = 1;
        }

        // Nueva Contraseña
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblNueva = new JLabel("Nueva Contraseña:");
        lblNueva.setFont(labelFont);
        panel.add(lblNueva, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtNuevaPassword = new JPasswordField(20);
        txtNuevaPassword.setFont(fieldFont);
        txtNuevaPassword.setPreferredSize(new Dimension(250, 35));
        txtNuevaPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtNuevaPassword, gbc);

        // Confirmar Contraseña
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel lblConfirmar = new JLabel("Confirmar Contraseña:");
        lblConfirmar.setFont(labelFont);
        panel.add(lblConfirmar, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setFont(fieldFont);
        txtConfirmarPassword.setPreferredSize(new Dimension(250, 35));
        txtConfirmarPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtConfirmarPassword, gbc);

        // Requisitos de contraseña
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel lblRequisitos = new JLabel("<html><i>• Mínimo 6 caracteres<br>• Se recomienda usar letras y números</i></html>");
        lblRequisitos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRequisitos.setForeground(Color.GRAY);
        panel.add(lblRequisitos, gbc);

        // Panel de mensajes
        gbc.gridy = 4;
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblMensaje, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);

        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarPassword());

        // Solo mostrar cancelar si NO es primer login
        if (!esPrimerLogin) {
            btnCancelar = new JButton("❌ Cancelar");
            btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnCancelar.setPreferredSize(new Dimension(150, 40));
            btnCancelar.setBackground(new Color(240, 240, 240));
            btnCancelar.setForeground(Color.DARK_GRAY);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.addActionListener(e -> cancelar());
            panel.add(btnCancelar);
        }

        panel.add(btnGuardar);

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

        return panel;
    }

    private void guardarPassword() {
        String nuevaPassword = new String(txtNuevaPassword.getPassword());
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());

        // Validaciones
        if (nuevaPassword.isEmpty()) {
            mostrarError("La contraseña no puede estar vacía");
            txtNuevaPassword.requestFocus();
            return;
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            mostrarError("Las contraseñas no coinciden");
            txtConfirmarPassword.requestFocus();
            txtConfirmarPassword.selectAll();
            return;
        }

        // Deshabilitar botón mientras procesa
        btnGuardar.setEnabled(false);
        btnGuardar.setText("Guardando...");

        // Procesar en segundo plano
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authService.establecerContrasenaPrimerLogin(nuevaPassword);
            }

            @Override
            protected void done() {
                try {
                    boolean resultado = get();

                    if (resultado) {
                        mostrarExito("Contraseña establecida correctamente");
                        passwordEstablecida = true;

                        // Pequeña pausa antes de cerrar
                        Timer timer = new Timer(800, evt -> dispose());
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        mostrarError("Error al guardar la contraseña");
                        btnGuardar.setEnabled(true);
                        btnGuardar.setText("💾 Guardar");
                    }
                } catch (Exception e) {
                    logger.error("Error al establecer contraseña", e);
                    mostrarError("Error inesperado");
                    btnGuardar.setEnabled(true);
                    btnGuardar.setText("💾 Guardar");
                }
            }
        };

        worker.execute();
    }

    private void cancelar() {
        if (!esPrimerLogin) {
            dispose();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setForeground(new Color(220, 53, 69));
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText("✓ " + mensaje);
        lblMensaje.setForeground(new Color(40, 167, 69));
    }

    public boolean isPasswordEstablecida() {
        return passwordEstablecida;
    }
}
