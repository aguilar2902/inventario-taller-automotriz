package com.taller.view;

import com.taller.controller.ProductoController;
import com.taller.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductosView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ProductosView.class);
    private final ProductoController controller;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnNuevo, btnEditar, btnEliminar, btnRefrescar;
    private JLabel lblTotal, lblBajoStock;

    private final NumberFormat formatoPrecio;

    public ProductosView() {
        this.controller = new ProductoController(this);
        this.formatoPrecio = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        initComponents();
        configurarListeners();
        cargarProductos();
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
        JLabel title = new JLabel("📦 Gestión de Productos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(52, 58, 64));

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(Color.WHITE);

        lblTotal = new JLabel("Total: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(40, 167, 69));

        lblBajoStock = new JLabel("Bajo Stock: 0");
        lblBajoStock.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBajoStock.setForeground(new Color(220, 53, 69));

        JLabel separador = new JLabel("|");
        separador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        separador.setForeground(Color.GRAY);

        statsPanel.add(lblTotal);
        statsPanel.add(separador);
        statsPanel.add(lblBajoStock);

        panel.add(title, BorderLayout.WEST);
        panel.add(statsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Barra de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtBuscar = new JTextField(30);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setToolTipText("Buscar por nombre de producto");

        btnRefrescar = new JButton("↻ Refrescar");
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setToolTipText("Recargar lista de productos");

        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnRefrescar);

        // Tabla de productos
        String[] columnas = {"ID", "Código", "Nombre", "Descripción", "Stock", "Stock Mín.", "Precio", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5) { // Stock columns
                    return Integer.class;
                }
                return String.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(32);
        tablaProductos.setShowGrid(true);
        tablaProductos.setGridColor(new Color(230, 230, 230));
        tablaProductos.setSelectionBackground(new Color(184, 207, 229));
        tablaProductos.setSelectionForeground(Color.BLACK);

        // Configurar header
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columna ID
        tablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaProductos.getColumnModel().getColumn(0).setWidth(0);

        // Ajustar anchos de columnas
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100); // Código
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(180); // Nombre
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(200); // Descripción
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Stock
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(90);  // Stock Mín
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(100); // Precio
        tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(120); // Categoría

        // Renderer para filas con bajo stock
        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    int stockActual = (Integer) table.getModel().getValueAt(row, 4);
                    int stockMinimo = (Integer) table.getModel().getValueAt(row, 5);

                    if (stockActual <= stockMinimo) {
                        c.setBackground(new Color(255, 230, 230)); // Rojo claro
                        c.setForeground(new Color(139, 0, 0)); // Rojo oscuro
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }

                // Centrar columnas de stock
                if (column == 4 || column == 5) {
                    setHorizontalAlignment(CENTER);
                } else if (column == 6) {
                    setHorizontalAlignment(RIGHT);
                } else {
                    setHorizontalAlignment(LEFT);
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);

        btnNuevo = createButton("➕ Nuevo Producto", new Color(40, 167, 69), "Crear un nuevo producto");
        btnEditar = createButton("✏️ Editar", new Color(255, 193, 7), "Editar producto seleccionado");
        btnEliminar = createButton("🗑️ Eliminar", new Color(220, 53, 69), "Eliminar producto seleccionado");

        // Deshabilitar botones de edición y eliminación inicialmente
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        return panel;
    }

    private JButton createButton(String texto, Color color, String tooltip) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 40));
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
        // Listener para búsqueda en tiempo real
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarProductos();
            }
        });

        // Listener para selección de fila
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tablaProductos.getSelectedRow() != -1;
                btnEditar.setEnabled(haySeleccion);
                btnEliminar.setEnabled(haySeleccion);
            }
        });

        // Doble click para editar
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaProductos.getSelectedRow() != -1) {
                    abrirFormularioEditar();
                }
            }
        });

        // Listeners de botones
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        btnEditar.addActionListener(e -> abrirFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    public void cargarProductos() {
        try {
            logger.info("Cargando productos...");
            controller.cargarProductos();
            txtBuscar.setText(""); // Limpiar búsqueda
        } catch (Exception e) {
            logger.error("Error al cargar productos", e);
            mostrarError("Error al cargar los productos");
        }
    }

    private void buscarProductos() {
        try {
            String textoBusqueda = txtBuscar.getText().trim();
            if (textoBusqueda.isEmpty()) {
                cargarProductos();
            } else {
                controller.buscarProductos(textoBusqueda);
            }
        } catch (Exception e) {
            logger.error("Error al buscar productos", e);
            mostrarError("Error al realizar la búsqueda");
        }
    }

    public void actualizarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);

        for (Producto p : productos) {
            String categoriaNombre = p.getCategoria() != null ?
                    p.getCategoria().getNombre() : "Sin categoría";

            Object[] fila = {
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion() != null ? p.getDescripcion() : "",
                    p.getStockActual(),
                    p.getStockMinimo(),
                    formatoPrecio.format(p.getPrecioUnitario()),
                    categoriaNombre
            };
            modeloTabla.addRow(fila);
        }

        logger.info("Tabla actualizada con " + productos.size() + " productos");
    }

    public void actualizarEstadisticas(int total, int bajoStock) {
        lblTotal.setText("Total: " + total);
        lblBajoStock.setText("Bajo Stock: " + bajoStock);
    }

    private void abrirFormularioNuevo() {
        try {
            ProductoFormDialog dialog = new ProductoFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    null
            );
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                Producto producto = dialog.getProducto();
                if (controller.guardarProducto(producto)) {
                    mostrarExito("Producto creado exitosamente");
                    cargarProductos();
                } else {
                    mostrarAdvertencia("Error al crear el producto. Verifique que el código no exista.");
                }
            }
        } catch (Exception e) {
            logger.error("Error al abrir formulario nuevo", e);
            mostrarError("Error al abrir el formulario");
        }
    }

    private void abrirFormularioEditar() {
        int selectedRow = tablaProductos.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un producto para editar");
            return;
        }

        try {
            Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);
            Producto producto = controller.obtenerProductoPorId(id);
            System.out.println(producto.getId());
            if (producto != null) {
                ProductoFormDialog dialog = new ProductoFormDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        producto
                );
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    Producto productoEditado = dialog.getProducto();
                    if (controller.actualizarProducto(productoEditado)) {
                        mostrarExito("Producto actualizado exitosamente");
                        cargarProductos();
                    } else {
                        mostrarError("Error al actualizar el producto");
                    }
                }
            } else {
                mostrarError("No se pudo cargar el producto seleccionado");
            }
        } catch (Exception e) {
            logger.error("Error al editar producto", e);
            mostrarError("Error al editar el producto");
        }
    }

    private void eliminarProducto() {
        int selectedRow = tablaProductos.getSelectedRow();

        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un producto para eliminar");
            return;
        }

        String nombreProducto = (String) modeloTabla.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el producto:\n\"" + nombreProducto + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Integer id = (Integer) modeloTabla.getValueAt(selectedRow, 0);

                if (controller.eliminarProducto(id)) {
                    mostrarExito("Producto eliminado exitosamente");
                    cargarProductos();
                } else {
                    mostrarError("Error al eliminar el producto");
                }
            } catch (Exception e) {
                logger.error("Error al eliminar producto", e);
                mostrarError("Error al eliminar el producto");
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