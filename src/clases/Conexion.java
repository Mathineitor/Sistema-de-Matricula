package clases;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
  private static final String URL = "jdbc:mysql://localhost:3306/registro_matricula?useSSL=false&serverTimezone=UTC";
  private static final String USER = "root";
  
  private static final String PASS = "TU_CONTRASEÑA_AQUI";
  //private static final String PASS = System.getenv("DB_PASS");

  public static Connection getConnection() throws Exception {
    return DriverManager.getConnection(URL, USER, PASS);
  }
}