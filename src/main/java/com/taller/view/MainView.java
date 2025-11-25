package com.taller.view;

import com.taller.model.Usuario;
import com.taller.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    private final AuthService authService;

    private CardLayout cardLayout;
    private JPanel panelCentral;
    private JButton btnActivo; // Para resaltar el botón activo

    public MainView() {
        this.authService = AuthService.getInstance();
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Inventario - Taller Automotriz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Barra superior
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Menú lateral
        JPanel sidebar = createNavigationMenu();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Sistema de tarjetas (vistas)
        createCardSystem(mainPanel);

        add(mainPanel);
        registrarVistas();

        // Mostrar vista inicial
        mostrarVista("inicio");

        logger.info("Ventana principal inicializada");
    }

    // ============================================================
    //  NAVBAR LATERAL / MENU
    // ============================================================
    private JPanel createNavigationMenu() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 750));
        sidebar.setBackground(new Color(52, 58, 64));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Logo/Título del menú
        JLabel lblTitulo = new JLabel("MENÚ PRINCIPAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        sidebar.add(lblTitulo);

        // Separador
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(200, 1));
        separator.setForeground(Color.GRAY);
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(10));

        // Botones del menú
        JButton btnInicio = crearBotonMenu("🏠 Inicio", "inicio");
        JButton btnProductos = crearBotonMenu("📦 Productos", "productos");
        JButton btnUsuarios = crearBotonMenu("👥 Usuarios", "usuarios");

        sidebar.add(btnInicio);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnProductos);
        sidebar.add(Box.createVerticalStrut(5));

        // Solo mostrar usuarios si es admin
        if (authService.isAdmin()) {
            sidebar.add(btnUsuarios);
            sidebar.add(Box.createVerticalStrut(5));
        }

        // Espacio flexible para empujar hacia arriba
        sidebar.add(Box.createVerticalGlue());

        // Información en la parte inferior
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(52, 58, 64));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblVersion = new JLabel("Versión 1.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersion.setForeground(Color.LIGHT_GRAY);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(lblVersion);
        sidebar.add(infoPanel);

        return sidebar;
    }

    private JButton crearBotonMenu(String texto, String vista) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 58, 64));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Efecto hover y click
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnActivo != button) {
                    button.setBackground(new Color(73, 80, 87));
                    button.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnActivo != button) {
                    button.setContentAreaFilled(false);
                }
            }
        });

        button.addActionListener(e -> {
            mostrarVista(vista);
            actualizarBotonActivo(button);
        });

        return button;
    }

    private void actualizarBotonActivo(JButton button) {
        // Resetear botón anterior
        if (btnActivo != null) {
            btnActivo.setBackground(new Color(52, 58, 64));
            btnActivo.setContentAreaFilled(false);
            btnActivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        // Activar nuevo botón
        btnActivo = button;
        btnActivo.setBackground(new Color(70, 130, 180));
        btnActivo.setContentAreaFilled(true);
        btnActivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    // ============================================================
    //  SISTEMA DE CARDLAYOUT
    // ============================================================
    private void createCardSystem(JPanel mainPanel) {
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(Color.WHITE);

        mainPanel.add(panelCentral, BorderLayout.CENTER);
    }

    private void registrarVistas() {
        // Vista de inicio
        panelCentral.add(createInicioPanel(), "inicio");

        // Vista de productos
        panelCentral.add(new ProductosView(), "productos");

        // Vista de usuarios (solo si es admin)
        if (authService.isAdmin()) {
            panelCentral.add(new UsuariosView(), "usuarios");
        }

        // Aquí puedes agregar más vistas:
        // panelCentral.add(new ClientesView(), "clientes");
        // panelCentral.add(new ReportesView(), "reportes");
        // panelCentral.add(new InventarioView(), "inventario");
    }

    public void mostrarVista(String nombre) {
        cardLayout.show(panelCentral, nombre);
        logger.info("Cambiando vista a: " + nombre);
    }

    // ============================================================
    //  BARRA SUPERIOR
    // ============================================================
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

        Usuario usuarioActual = authService.getUsuarioActual();
        String nombreUsuario = usuarioActual.getNombreCompleto();
        String rol = usuarioActual.getRol().name();

        JLabel userLabel = new JLabel("👤 " + nombreUsuario + " (" + rol + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("🚪 Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(140, 35));
        btnLogout.addActionListener(e -> handleLogout());

        // Efecto hover en logout
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(200, 35, 51));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(220, 53, 69));
            }
        });

        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // ============================================================
    //  PANEL DE INICIO (DASHBOARD)
    // ============================================================
    private JPanel createInicioPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Título de bienvenida
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("¡Bienvenido al Sistema de Inventario!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(52, 58, 64));

        Usuario usuarioActual = authService.getUsuarioActual();
        JLabel userGreeting = new JLabel("👋 Hola, " + usuarioActual.getNombreCompleto());
        userGreeting.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userGreeting.setForeground(Color.GRAY);
        userGreeting.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(welcomeLabel);
        textPanel.add(userGreeting);

        headerPanel.add(textPanel, BorderLayout.WEST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con tarjetas de acceso rápido
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(Color.WHITE);

        // Tarjetas de módulos
        cardsPanel.add(crearTarjetaModulo(
                "📦 Productos",
                "Gestión de productos e inventario",
                new Color(40, 167, 69),
                "productos"
        ));

        if (authService.isAdmin()) {
            cardsPanel.add(crearTarjetaModulo(
                    "👥 Usuarios",
                    "Administración de usuarios del sistema",
                    new Color(0, 123, 255),
                    "usuarios"
            ));
        } else {
            cardsPanel.add(crearTarjetaInfo(
                    "🔒 Acceso Restringido",
                    "Solo administradores",
                    Color.LIGHT_GRAY
            ));
        }

        cardsPanel.add(crearTarjetaInfo(
                "📊 Reportes",
                "Próximamente disponible",
                new Color(255, 193, 7)
        ));

        cardsPanel.add(crearTarjetaInfo(
                "⚙️ Configuración",
                "Próximamente disponible",
                new Color(108, 117, 125)
        ));

        panel.add(cardsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaModulo(String titulo, String descripcion, Color color, String vista) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(lblTitulo);
        card.add(lblDescripcion);

        // Click en la tarjeta
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarVista(vista);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 249, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private JPanel crearTarjetaInfo(String titulo, String descripcion, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(lblTitulo);
        card.add(lblDescripcion);

        return card;
    }

    // ============================================================
    //  LOGOUT
    // ============================================================
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