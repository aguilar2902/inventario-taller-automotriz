package com.taller.view;

import com.taller.model.Categoria;
import com.taller.model.Producto;
import com.taller.service.CategoriaService;
import com.taller.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductoFormDialog extends JDialog{
    private static final Logger logger = LoggerFactory.getLogger(ProductoFormDialog.class);
    private final CategoriaService categoriaService;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JSpinner spinStockActual;
    private JSpinner spinStockMinimo;
    private JTextField txtPrecio;
    private JComboBox<Categoria> cmbCategoria;
    private JCheckBox chkActivo;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Producto producto;
    private boolean confirmado = false;

    public ProductoFormDialog(Frame parent, Producto producto) {
        super(parent, producto == null ? "Nuevo Producto" : "Editar Producto", true);
        this.producto = producto;
        this.categoriaService = CategoriaService.getInstance();

        initComponents();
        cargarCategorias();

        if (producto != null) {
            cargarDatosProducto();
        }

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(520, 620);
        setResizable(false);

        // Panel principal con formulario
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel lblTitulo = new JLabel(producto == null ? "📦 Nuevo Producto" : "✏️ Editar Producto");
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

        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblCodigo = new JLabel("Código: *");
        lblCodigo.setFont(labelFont);
        panel.add(lblCodigo, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtCodigo = new JTextField(20);
        txtCodigo.setFont(fieldFont);
        txtCodigo.setPreferredSize(new Dimension(250, 30));
        panel.add(txtCodigo, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblNombre = new JLabel("Nombre: *");
        lblNombre.setFont(labelFont);
        panel.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setFont(fieldFont);
        txtNombre.setPreferredSize(new Dimension(250, 30));
        panel.add(txtNombre, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(labelFont);
        panel.add(lblDescripcion, gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.gridheight = 1; gbc.anchor = GridBagConstraints.CENTER;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(fieldFont);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(250, 80));
        panel.add(scrollDesc, gbc);

        // Stock Actual
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.gridheight = 1;
        JLabel lblStockActual = new JLabel("Stock Actual: *");
        lblStockActual.setFont(labelFont);
        panel.add(lblStockActual, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        spinStockActual = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        spinStockActual.setFont(fieldFont);
        ((JSpinner.DefaultEditor) spinStockActual.getEditor()).getTextField().setFont(fieldFont);
        spinStockActual.setPreferredSize(new Dimension(250, 30));
        panel.add(spinStockActual, gbc);

        // Stock Mínimo
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        JLabel lblStockMinimo = new JLabel("Stock Mínimo: *");
        lblStockMinimo.setFont(labelFont);
        panel.add(lblStockMinimo, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        spinStockMinimo = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        spinStockMinimo.setFont(fieldFont);
        ((JSpinner.DefaultEditor) spinStockMinimo.getEditor()).getTextField().setFont(fieldFont);
        spinStockMinimo.setPreferredSize(new Dimension(250, 30));
        panel.add(spinStockMinimo, gbc);

        // Precio
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        JLabel lblPrecio = new JLabel("Precio ($): *");
        lblPrecio.setFont(labelFont);
        panel.add(lblPrecio, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtPrecio = new JTextField(20);
        txtPrecio.setFont(fieldFont);
        txtPrecio.setPreferredSize(new Dimension(250, 30));
        panel.add(txtPrecio, gbc);

        // Categoría
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        JLabel lblCategoria = new JLabel("Categoría: *");
        lblCategoria.setFont(labelFont);
        panel.add(lblCategoria, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setFont(fieldFont);
        cmbCategoria.setPreferredSize(new Dimension(250, 30));
        panel.add(cmbCategoria, gbc);

        // Activo
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        JLabel lblActivo = new JLabel("Activo:");
        lblActivo.setFont(labelFont);
        panel.add(lblActivo, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        chkActivo.setBackground(Color.WHITE);
        chkActivo.setFont(fieldFont);
        panel.add(chkActivo, gbc);

        // Nota de campos obligatorios
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
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

    private void cargarCategorias() {
        try {
            cmbCategoria.removeAllItems();
            ArrayList<Categoria> categorias = categoriaService.obtenerTodas();

            if (categorias.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay categorías disponibles. Por favor, cree una categoría primero.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                logger.warn("No hay categorías disponibles para cargar");
                return;
            }

            for (Categoria cat : categorias) {
                if (cat.getActivo()) {
                    cmbCategoria.addItem(cat);
                }
            }

            logger.info("Se cargaron " + cmbCategoria.getItemCount() + " categorías activas");

        } catch (Exception e) {
            logger.error("Error al cargar categorías", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las categorías",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosProducto() {
        try {
            txtCodigo.setText(producto.getCodigo());
            txtCodigo.setEditable(false); // No permitir cambiar el código en edición
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            spinStockActual.setValue(producto.getStockActual());
            spinStockMinimo.setValue(producto.getStockMinimo());
            txtPrecio.setText(producto.getPrecioUnitario().toString());
            chkActivo.setSelected(producto.getActivo());

            // Seleccionar categoría
            if (producto.getCategoria() != null) {
                for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                    Categoria cat = cmbCategoria.getItemAt(i);
                    if (cat.getId().equals(producto.getCategoria().getId())) {
                        cmbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
            }

            logger.info("Datos del producto cargados: " + producto.getCodigo());

        } catch (Exception e) {
            logger.error("Error al cargar datos del producto", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos del producto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        // Validaciones
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarError("El código es obligatorio", txtCodigo);
            return;
        }

        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio", txtNombre);
            return;
        }

        if (txtPrecio.getText().trim().isEmpty()) {
            mostrarError("El precio es obligatorio", txtPrecio);
            return;
        }

        if (cmbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una categoría",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            cmbCategoria.requestFocus();
            return;
        }

        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());

            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                mostrarError("El precio no puede ser negativo", txtPrecio);
                return;
            }

            int stockActual = (Integer) spinStockActual.getValue();
            int stockMinimo = (Integer) spinStockMinimo.getValue();

            //  VALIDACIÓN DE CÓDIGO DUPLICADO ANTES DE CERRAR
            if (producto == null) {  // Solo para productos nuevos
                ProductoService productoService = ProductoService.getInstance();
                if (productoService.existeCodigo(txtCodigo.getText().trim())) {
                    JOptionPane.showMessageDialog(this,
                            "El código '" + txtCodigo.getText().trim() + "' ya está registrado.\n\n" +
                                    "Por favor, ingrese un código diferente.",
                            "Código duplicado",
                            JOptionPane.WARNING_MESSAGE);
                    txtCodigo.requestFocus();
                    txtCodigo.selectAll();
                    return;  // No cerrar el diálogo
                }
            }
            // Crear o actualizar producto
            if (producto == null) {
                // Nuevo producto
                producto = new Producto(
                        txtCodigo.getText().trim(),
                        txtNombre.getText().trim(),
                        txtDescripcion.getText().trim(),
                        stockActual,
                        stockMinimo,
                        precio,
                        chkActivo.isSelected(),
                        (Categoria) cmbCategoria.getSelectedItem()
                );

                logger.info("Producto nuevo creado: " + producto.getCodigo());
            } else {
                // Actualizar producto existente
                producto = new Producto(
                        producto.getId(),
                        txtCodigo.getText().trim(),
                        txtNombre.getText().trim(),
                        txtDescripcion.getText().trim(),
                        stockActual,
                        stockMinimo,
                        precio,
                        chkActivo.isSelected(),
                        producto.getFechaCreacion(),
                        (Categoria) cmbCategoria.getSelectedItem()
                );
                logger.info("Producto actualizado: " + producto.getCodigo());
            }

            confirmado = true;
            dispose();

        } catch (NumberFormatException e) {
            logger.error("Error al parsear el precio", e);
            mostrarError("El precio debe ser un número válido (ej: 150.50)", txtPrecio);
        } catch (Exception e) {
            logger.error("Error al guardar producto", e);
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al guardar el producto",
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

    public Producto getProducto() {
        return producto;
    }
}
