package com.taller.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:inventario.db";

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(DB_URL);
            logger.info("Conexión a base de datos establecida");
            initializeDatabase();
            createDefaultAdmin();
            createDefaultCategorias();
            createDefaultAdmin();
            createDefaultCategorias();
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error al conectar con la base de datos", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener conexión", e);
        }
        return connection;
    }

    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Tabla usuarios
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255),
                    nombre_completo VARCHAR(100) NOT NULL,
                    rol VARCHAR(20) NOT NULL,
                    activo BOOLEAN NOT NULL DEFAULT TRUE,
                    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Tabla categorías
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categorias (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(100) NOT NULL UNIQUE,
                    activo BOOLEAN NOT NULL DEFAULT TRUE
                )
            """);

            // Tabla productos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS productos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    codigo VARCHAR(50) UNIQUE NOT NULL,
                    nombre VARCHAR(200) NOT NULL,
                    descripcion TEXT,
                    categoria_id INTEGER,
                    stock_actual INTEGER DEFAULT 0,
                    stock_minimo INTEGER DEFAULT 5,
                    precio_unitario DECIMAL(10,2),
                    activo BOOLEAN DEFAULT 1,
                    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
                )
            """);

            // Tabla movimientos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS movimientos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    producto_id INTEGER NOT NULL,
                    tipo VARCHAR(20) NOT NULL,
                    cantidad INTEGER NOT NULL,
                    stock_anterior INTEGER,
                    stock_nuevo INTEGER,
                    motivo TEXT,
                    usuario_id INTEGER NOT NULL,
                    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (producto_id) REFERENCES productos(id),
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """);

            logger.info("Tablas de base de datos inicializadas correctamente");

            // Crear usuario admin por defecto
            createDefaultAdmin();
            createDefaultCategorias();

        } catch (SQLException e) {
            logger.error("Error al inicializar base de datos", e);
        }
    }

    private void createDefaultAdmin() {
        String checkUser = "SELECT COUNT(*) FROM usuarios WHERE username = 'admin'";
        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery(checkUser)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Usuario admin no existe, crearlo
                String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt());
                String insertAdmin = String.format(
                        "INSERT INTO usuarios (username, password, nombre_completo, rol) VALUES ('admin', '%s', 'Administrador', 'ADMIN')",
                        hashedPassword
                );
                stmt.execute(insertAdmin);
                logger.info("Usuario admin creado (username: admin, password: admin123)");
            }
        } catch (SQLException e) {
            logger.error("Error al crear usuario admin por defecto", e);
        }
    }

    private void createDefaultCategorias() {
        String checkCategorias = "SELECT COUNT(*) FROM categorias";
        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery(checkCategorias)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No hay categorías, crear algunas por defecto
                String[] categoriasDefault = {
                        "INSERT INTO categorias (nombre, activo) VALUES ('Baterías', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Alternadores', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Arrancadores', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Cables', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Fusibles', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Iluminación', 1)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Sensores', 0)",
                        "INSERT INTO categorias (nombre, activo) VALUES ('Otros', 1)"
                };

                for (String sql : categoriasDefault) {
                    stmt.execute(sql);
                }
                logger.info("Categorías por defecto creadas");
            }
        } catch (SQLException e) {
            logger.error("Error al crear categorías por defecto", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Conexión a base de datos cerrada");
            }
        } catch (SQLException e) {
            logger.error("Error al cerrar conexión", e);
        }
    }
}