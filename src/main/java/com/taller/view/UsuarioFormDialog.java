package com.taller.view;

import com.taller.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UsuarioFormDialog extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioFormDialog.class);

    private JTextField txtUsuario;
    private JTextField txtNombreCompleto;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JComboBox<Usuario.Rol> cmbRol;
    private JCheckBox chkActivo;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Usuario usuario;
    private boolean confirmado = false;
    private boolean esEdicion;

    public UsuarioFormDialog(Frame parent, Usuario usuario) {
        super(parent, usuario == null ? "Nuevo Usuario" : "Editar Usuario", true);
        this.usuario = usuario;
        this.esEdicion = (usuario != null);

        initComponents();

        if (usuario != null) {
            cargarDatosUsuario();
        }

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, esEdicion ? 420 : 520);
        setResizable(false);

        // Panel principal con formulario
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel lblTitulo = new JLabel(esEdicion ? "✏️ Editar Usuario" : "👤 Nuevo Usuario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Panel de formulario
        JPanel formPanel = createFormPanel();

        // Panel de botones
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        int row = 0;

        // Usuario
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblUsuario = new JLabel("Usuario: *");
        lblUsuario.setFont(labelFont);
        panel.add(lblUsuario, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(fieldFont);
        txtUsuario.setPreferredSize(new Dimension(250, 30));
        panel.add(txtUsuario, gbc);

        row++;

        // Nombre Completo
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblNombre = new JLabel("Nombre Completo: *");
        lblNombre.setFont(labelFont);
        panel.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtNombreCompleto = new JTextField(20);
        txtNombreCompleto.setFont(fieldFont);
        txtNombreCompleto.setPreferredSize(new Dimension(250, 30));
        panel.add(txtNombreCompleto, gbc);

        row++;

        // Rol
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblRol = new JLabel("Rol: *");
        lblRol.setFont(labelFont);
        panel.add(lblRol, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        cmbRol = new JComboBox<>(Usuario.Rol.values());
        cmbRol.setFont(fieldFont);
        cmbRol.setPreferredSize(new Dimension(250, 30));
        panel.add(cmbRol, gbc);

        row++;

        // Campos de contraseña (solo al crear)
        if (!esEdicion) {
            // Crear componentes de contraseña
            JLabel lblContrasena = new JLabel("Contraseña: *");
            lblContrasena.setFont(labelFont);

            txtContrasena = new JPasswordField(20);
            txtContrasena.setFont(fieldFont);
            txtContrasena.setPreferredSize(new Dimension(250, 30));

            JLabel lblConfirmar = new JLabel("Confirmar Contraseña: *");
            lblConfirmar.setFont(labelFont);

            txtConfirmarContrasena = new JPasswordField(20);
            txtConfirmarContrasena.setFont(fieldFont);
            txtConfirmarContrasena.setPreferredSize(new Dimension(250, 30));

            JLabel lblInfoPassword = new JLabel("<html><i>Solo ADMIN requiere contraseña al crear.<br>EMPLEADO la establecerá en su primer login.</i></html>");
            lblInfoPassword.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblInfoPassword.setForeground(Color.GRAY);

            // Listener para mostrar/ocultar campos según rol
            cmbRol.addActionListener(e -> {
                Usuario.Rol rolSeleccionado = (Usuario.Rol) cmbRol.getSelectedItem();
                boolean esAdmin = rolSeleccionado == Usuario.Rol.ADMIN;

                // Mostrar/ocultar componentes de contraseña
                lblContrasena.setVisible(esAdmin);
                txtContrasena.setVisible(esAdmin);
                lblConfirmar.setVisible(esAdmin);
                txtConfirmarContrasena.setVisible(esAdmin);
                lblInfoPassword.setVisible(!esAdmin);

                // Revalidar el panel
                panel.revalidate();
                panel.repaint();
            });

            // Agregar campos de contraseña al panel
            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
            panel.add(lblContrasena, gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(txtContrasena, gbc);

            row++;

            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
            panel.add(lblConfirmar, gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(txtConfirmarContrasena, gbc);

            row++;

            // Información sobre contraseñas
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            panel.add(lblInfoPassword, gbc);
            gbc.gridwidth = 1;

            row++;

            // Inicialmente ocultar campos (por defecto ADMIN está seleccionado, así que mostrar)
            boolean inicialmenteAdmin = cmbRol.getSelectedItem() == Usuario.Rol.ADMIN;
            lblContrasena.setVisible(inicialmenteAdmin);
            txtContrasena.setVisible(inicialmenteAdmin);
            lblConfirmar.setVisible(inicialmenteAdmin);
            txtConfirmarContrasena.setVisible(inicialmenteAdmin);
            lblInfoPassword.setVisible(!inicialmenteAdmin);

        } else {
            // Mensaje informativo en edición
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            JLabel lblInfo = new JLabel("<html><i>Para cambiar la contraseña, use el botón \"Cambiar Contraseña\"</i></html>");
            lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblInfo.setForeground(Color.GRAY);
            panel.add(lblInfo, gbc);
            gbc.gridwidth = 1;

            row++;
        }

        // Activo
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblActivo = new JLabel("Activo:");
        lblActivo.setFont(labelFont);
        panel.add(lblActivo, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        chkActivo.setBackground(Color.WHITE);
        chkActivo.setFont(fieldFont);
        panel.add(chkActivo, gbc);

        row++;

        // Nota de campos obligatorios
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);
        panel.add(lblNota, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);

        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setPreferredSize(new Dimension(130, 38));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> cancelar());

        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(130, 38));
        btnGuardar.addActionListener(e -> guardar());

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

        panel.add(btnCancelar);
        panel.add(btnGuardar);

        return panel;
    }

    private void cargarDatosUsuario() {
        try {
            txtUsuario.setText(usuario.getUsuario());
            txtUsuario.setEditable(false); // No permitir cambiar el usuario en edición
            txtNombreCompleto.setText(usuario.getNombreCompleto());
            cmbRol.setSelectedItem(usuario.getRol());
            chkActivo.setSelected(usuario.getActivo());

            logger.info("Datos del usuario cargados: " + usuario.getUsuario());

        } catch (Exception e) {
            logger.error("Error al cargar datos del usuario", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos del usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        // Validaciones
        if (txtUsuario.getText().trim().isEmpty()) {
            mostrarError("El nombre de usuario es obligatorio", txtUsuario);
            return;
        }

        if (txtNombreCompleto.getText().trim().isEmpty()) {
            mostrarError("El nombre completo es obligatorio", txtNombreCompleto);
            return;
        }

        if (cmbRol.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un rol",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            cmbRol.requestFocus();
            return;
        }

        try {
            if (esEdicion) {
                // Actualizar usuario existente (sin cambiar contraseña)
                usuario = new Usuario(
                        usuario.getId(),
                        txtUsuario.getText().trim(),
                        usuario.getContrasena(), // Mantener contraseña actual
                        txtNombreCompleto.getText().trim(),
                        (Usuario.Rol) cmbRol.getSelectedItem(),
                        chkActivo.isSelected(),
                        usuario.getRequiereCambioPassword(), // Mantener estado
                        usuario.getFechaCreacion()
                );
                logger.info("Usuario actualizado: " + usuario.getUsuario());
            } else {
                // Crear nuevo usuario
                Usuario.Rol rolSeleccionado = (Usuario.Rol) cmbRol.getSelectedItem();

                if (rolSeleccionado == Usuario.Rol.ADMIN) {
                    // ADMIN: Requiere contraseña obligatoria
                    String contrasena = new String(txtContrasena.getPassword());
                    String confirmar = new String(txtConfirmarContrasena.getPassword());

                    if (contrasena.isEmpty()) {
                        mostrarError("La contraseña es obligatoria para usuarios ADMIN", txtContrasena);
                        return;
                    }

                    if (contrasena.length() < 6) {
                        mostrarError("La contraseña debe tener al menos 6 caracteres", txtContrasena);
                        return;
                    }

                    if (!contrasena.equals(confirmar)) {
                        JOptionPane.showMessageDialog(this,
                                "Las contraseñas no coinciden",
                                "Validación",
                                JOptionPane.WARNING_MESSAGE);
                        txtConfirmarContrasena.requestFocus();
                        txtConfirmarContrasena.selectAll();
                        return;
                    }

                    // Crear ADMIN con contraseña
                    usuario = new Usuario(
                            txtUsuario.getText().trim(),
                            contrasena,
                            txtNombreCompleto.getText().trim(),
                            Usuario.Rol.ADMIN
                    );
                    logger.info("Usuario ADMIN creado con contraseña: " + usuario.getUsuario());

                } else {
                    // EMPLEADO: Sin contraseña (primer login)
                    usuario = new Usuario(
                            txtUsuario.getText().trim(),
                            txtNombreCompleto.getText().trim(),
                            Usuario.Rol.EMPLEADO
                    );
                    logger.info("Usuario EMPLEADO creado sin contraseña: " + usuario.getUsuario());
                }
            }

            confirmado = true;
            dispose();

        } catch (Exception e) {
            logger.error("Error al guardar usuario", e);
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al guardar el usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
        campo.selectAll();
    }

    private void cancelar() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cancelar?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            confirmado = false;
            dispose();
            logger.info("Formulario cancelado");
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
