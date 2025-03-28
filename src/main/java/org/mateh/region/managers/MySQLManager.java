package org.mateh.region.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {
    private Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public MySQLManager(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        connect();
        createTables();
    }

    private void connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS regions ("
                + "id VARCHAR(36) PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "owner VARCHAR(36) NOT NULL,"
                + "world VARCHAR(255) NOT NULL,"
                + "x1 DOUBLE NOT NULL, y1 DOUBLE NOT NULL, z1 DOUBLE NOT NULL,"
                + "x2 DOUBLE NOT NULL, y2 DOUBLE NOT NULL, z2 DOUBLE NOT NULL,"
                + "whitelist TEXT,"
                + "flags TEXT,"
                + "particles INT"
                + ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
