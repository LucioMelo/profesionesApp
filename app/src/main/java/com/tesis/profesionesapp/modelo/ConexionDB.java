package com.tesis.profesionesapp.modelo;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Lucio on 16/10/2018.
 */

public class ConexionDB {
    public Connection conexionBD() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            String classs = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(classs);
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.76;port=1433;databaseName=android2;user=usuario;password=tesis123;");
        } catch (SQLException se) {
            Log.e("ERROR:1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR:2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR:3", e.getMessage());
        }
        return conn;
    }
}

