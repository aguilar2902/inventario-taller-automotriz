package com.taller.view;

import com.taller.controller.UsuarioController;
import com.taller.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UsuariosView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(UsuariosView.class);
    private final UsuarioController controller;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnEditar, btnEliminar, btnCambiarPass, btnResetearPass;
    private JLabel lblTotal;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public UsuariosView() {
        this.controller = new UsuarioController(this);
        initComponents();
        configurarListeners();
        cargarUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel superior con título y estadísticas
        add(createTopPanel(), BorderLayout.NORTH);

        // Panel central con tabla
        add(createTablePanel(), BorderLayout.CENTER);

        // Panel inferior con botones de acción
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Título
        JLabel title = new JLabel("👥 Gestión de Usuarios");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(52, 58, 64));

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(Color.WHITE);

        lblTotal = new JLabel("Total: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(40, 167, 69));

        statsPanel.add(lblTotal);

        panel.add(title, BorderLayout.WEST);
        panel.add(statsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Tabla de usuarios
        String[] columnas = {"ID", "Usuario", "Nombre Completo", "Rol", "Activo", "Fecha Creación"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { // Columna Activo
                    return Boolean.class;
                }
                return String.class;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(32);
        tablaUsuarios.setShowGrid(true);
        tablaUsuarios.setGridColor(new Color(230, 230, 230));
        tablaUsuarios.setSelectionBackground(new Color(184, 207, 229));
        tablaUsuarios.setSelectionForeground(Color.BLACK);

        // Configurar header
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columna ID
        tablaUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        tablaUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaUsuarios.getColumnModel().getColumn(0).setWidth(0);

        // Ajustar anchos de columnas
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150); // Usuario
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(250); // Nombre Completo
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100); // Rol
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(80);  // Activo
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(180); // Fecha

        // Renderer personalizado para columnas
        tablaUsuarios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                // Centrar columnas específicas
                if (column == 3 || column == 4) { // Rol y Activo
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);

        btnNuevo = createButton("➕ Nuevo Usuario", new Color(40, 167, 69), "Crear un nuevo usuario");
        btnEditar = createButton("✏️ Editar", new Color(255, 193, 7), "Editar usuario seleccionado");
        btnCambiarPass = createButton("🔑 Cambiar Contraseña", new Color(0, 123, 255), "Cambiar contraseña del usuario");
        btnResetearPass = createButton("🔄 Blanquear Contraseña", new Color(108, 117, 125), "Blanquear contraseña (primer login)");
        btnEliminar = createButton("🗑️ Eliminar", new Color(220, 53, 69), "Desactivar usuario");

        // Deshabilitar botones de edición inicialmente
        btnEditar.setEnabled(false);
        btnCambiarPass.setEnabled(false);
        btnResetearPass.setEnabled(false);
        btnEliminar.setEnabled(false);

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnCambiarPass);
        panel.add(btnResetearPass);
        panel.add(btnEliminar);

        return panel;
    }

    private JButton createButton(String texto, Color color, String tooltip) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        button.setToolTipText(tooltip);

        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(color.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void configurarListeners() {
        // Listener para selección de fila
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tablaUsuarios.getSelectedRow() != -1;
                btnEditar.setEnabled(haySeleccion);
                btnCambiarPass.setEnabled(haySeleccion);
                btnResetearPass.setEnabled(haySeleccion);
                btnEliminar.setEnabled(haySeleccion);
            }
        });

        // Doble click para editar
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaUsuarios.getSelectedRow() != -1) {
                    abrirFormularioEditar();
                }
            }
        });

        // Listeners de botones
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        btnEditar.addActionListener(e -> abrirFormularioEditar());
        btnCambiarPass.addActionListener(e -> cambiarContrasena());
        btnResetearPass.addActionListener(e -> resetearContrasena());
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    public void cargarUsuarios() {
        try {
            logger.info("Cargando usuarios...");
            controller.cargarUsuarios();
        } catch (Exception e) {
            logger.error("Error al cargar usuarios", e);
            mostrarError("Error al cargar los usuarios");
        }
    }

    public void actualizarTabla(List<Usuario> usuarios) {
        modeloTabla.setRowCount(0);

        for (Usuario u : usuarios) {
            String fechaFormateada = u.getFechaCreacion() != null ?
                    u.getFechaCreacion().format(formatoFecha) : "N/A";

            Object[] fila = {
                    u.getId(),
                    u.getUsuario(),
                    u.getNombreCompleto(),
                    u.getRol().name(),
                    u.getActivo(),
                    fechaFormateada
            };
            modeloTabla.addRow(fila);
        }

        logger.info("Tabla actualizada con " + usuarios.size() + " usuarios");
    }

    public void actualizarEstadisticas(int total) {
        lblTotal.setText("Total: " + total);
    }

    private void abrirFormularioNuevo() {
        try {
            UsuarioFormDialog dialog = new UsuarioFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    null
            );
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                Usuario usuario = dialog.getUsuario();
                if (controller.guardarUsuario(usuario)) {
                    mostrarExito("Usuario creado exitosamente");
                    cargarUsuarios();
                } else {
                    mostrarAdvertencia("Error al crear el usuario. Verifique que el nombre de usuario no exista.");
                }
            }
        } catch (Exception e) {
            logger.error("Error al abrir formulario nuevo", e);
            mostrarError("Error al abrir el formulario");
        }
    }

    private void abrirFormularioEditar() {
        int selectedRow = tablaUsuarios.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un usuario para editar");
            return;
        }

        try {
            Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);
            Usuario usuario = controller.obtenerUsuarioPorId(id);

            if (usuario != null) {
                UsuarioFormDialog dialog = new UsuarioFormDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        usuario
                );
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    Usuario usuarioEditado = dialog.getUsuario();
                    if (controller.actualizarUsuario(usuarioEditado)) {
                        mostrarExito("Usuario actualizado exitosamente");
                        cargarUsuarios();
                    } else {
                        mostrarError("Error al actualizar el usuario");
                    }
                }
            } else {
                mostrarError("No se pudo cargar el usuario seleccionado");
            }
        } catch (Exception e) {
            logger.error("Error al editar usuario", e);
            mostrarError("Error al editar el usuario");
        }
    }

    private void cambiarContrasena() {
        int selectedRow = tablaUsuarios.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un usuario para cambiar la contraseña");
            return;
        }

        try {
            Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);
            String nombreUsuario = (String) modeloTabla.getValueAt(selectedRow, 1);

            JPasswordField passField1 = new JPasswordField(20);
            JPasswordField passField2 = new JPasswordField(20);

            JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
            panel.add(new JLabel("Nueva contraseña para: " + nombreUsuario));
            panel.add(passField1);
            panel.add(new JLabel("Confirmar contraseña:"));
            panel.add(passField2);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Cambiar Contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String pass1 = new String(passField1.getPassword());
                String pass2 = new String(passField2.getPassword());

                if (pass1.isEmpty()) {
                    mostrarAdvertencia("La contraseña no puede estar vacía");
                    return;
                }

                if (!pass1.equals(pass2)) {
                    mostrarAdvertencia("Las contraseñas no coinciden");
                    return;
                }

                if (controller.cambiarContrasena(id, pass1)) {
                    mostrarExito("Contraseña cambiada exitosamente");
                } else {
                    mostrarError("Error al cambiar la contraseña");
                }
            }
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña", e);
            mostrarError("Error al cambiar la contraseña");
        }
    }

    private void resetearContrasena() {
        int selectedRow = tablaUsuarios.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un usuario para blanquear la contraseña");
            return;
        }

        try {
            Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);
            String nombreUsuario = (String) modeloTabla.getValueAt(selectedRow, 1);

            // Opciones: Blanquear (NULL) o Resetear (password temporal)
            Object[] opciones = {"Blanquear (sin contraseña)", "Resetear a temporal", "Cancelar"};

            int opcion = JOptionPane.showOptionDialog(this,
                    "Seleccione cómo desea resetear la contraseña del usuario:\n\"" + nombreUsuario + "\"\n\n" +
                            "• Blanquear: Elimina la contraseña (debe establecer una nueva en el próximo login)\n" +
                            "• Resetear: Establece contraseña temporal '" + nombreUsuario + "123' (debe cambiarla en el próximo login)",
                    "Blanquear/Resetear Contraseña",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (opcion == 0) {
                // Blanquear (contraseña NULL)
                if (controller.blanquearContrasena(id)) {
                    mostrarExito("Contraseña blanqueada exitosamente.\n" +
                            "El usuario deberá establecer una nueva contraseña en su próximo login.");
                } else {
                    mostrarError("Error al blanquear la contraseña.\n" +
                            "No puede blanquear su propia contraseña.");
                }
            } else if (opcion == 1) {
                // Resetear a temporal
                if (controller.resetearContrasena(id)) {
                    mostrarExito("Contraseña reseteada exitosamente a: " + nombreUsuario + "123\n" +
                            "El usuario deberá cambiarla en su próximo login.");
                } else {
                    mostrarError("Error al resetear la contraseña.\n" +
                            "No puede resetear su propia contraseña.");
                }
            }
            // Si opcion == 2 o CANCEL, no hacer nada

        } catch (Exception e) {
            logger.error("Error al resetear/blanquear contraseña", e);
            mostrarError("Error al procesar la operación");
        }
    }

    private void eliminarUsuario() {
        int selectedRow = tablaUsuarios.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un usuario para eliminar");
            return;
        }

        String nombreUsuario = (String) modeloTabla.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea desactivar el usuario:\n\"" + nombreUsuario + "\"?",
                "Confirmar desactivación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);

                if (controller.eliminarUsuario(id)) {
                    mostrarExito("Usuario desactivado exitosamente");
                    cargarUsuarios();
                } else {
                    mostrarError("Error al desactivar el usuario");
                }
            } catch (Exception e) {
                logger.error("Error al eliminar usuario", e);
                mostrarError("Error al desactivar el usuario");
            }
        }
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }
}