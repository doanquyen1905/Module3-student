package com.example.student.repositories.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseRepository {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/student_demo";
    private static final String jdbcUsername = "root";
    private static final String jdbcPassword = "doanquyen1905";
    private static Connection connection = null;
    private BaseRepository(){}
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static synchronized Connection getConnection(){
            try {
                if (connection == null) {
                    connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
                }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return connection;
    }
}
