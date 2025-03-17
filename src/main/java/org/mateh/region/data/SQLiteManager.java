package org.mateh.region.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLiteManager {
    private Connection connection;
    private String dbPath;

    public SQLiteManager(String dbPath) {
        this.dbPath = dbPath;
        try {
            File dbFile = new File(dbPath);
            File parent = dbFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS regions (" +
                "name TEXT PRIMARY KEY," +
                "world TEXT NOT NULL," +
                "x1 REAL NOT NULL, y1 REAL NOT NULL, z1 REAL NOT NULL," +
                "x2 REAL NOT NULL, y2 REAL NOT NULL, z2 REAL NOT NULL," +
                "whitelist TEXT," +
                "flags TEXT," +
                "particles INTEGER" +
                ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
