package clases;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
  // Configuración: variables de entorno DB_URL, DB_USER, DB_PASS (o edita los valores por defecto)
  private static final String URL  = env("DB_URL",
      "jdbc:mysql://localhost:3306/registro_matricula?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
  private static final String USER = env("DB_USER", "root");
  private static final String PASS = env("DB_PASS", "admin");

    private static class SQLException extends Exception {

        public SQLException(String no_se_encontró_el_Driver_JDBC_de_MySQLMar, ClassNotFoundException e) {
        }
    }

  /** Bloque de trabajo que se ejecuta dentro de una transacción. */
  @FunctionalInterface
  public interface Trabajo<T> {
    T ejecutar(Connection cn) throws Exception;
  }

public static Connection getConnection() throws Exception {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException("No se encontró el Driver JDBC de MySQL/MariaDB", e);
    }
    return DriverManager.getConnection(URL, USER, PASS);
}

  /**
   * Ejecuta varias operaciones como una sola unidad: o se guardan todas (commit)
   * o no se guarda ninguna (rollback).
   */
  public static <T> T enTransaccion(Trabajo<T> trabajo) throws Exception {
    try (Connection cn = getConnection()) {
      cn.setAutoCommit(false);
      try {
        T resultado = trabajo.ejecutar(cn);
        cn.commit();
        return resultado;
      } catch (Exception ex) {
        cn.rollback();
        throw ex;
      }
    }
  }

  private static String env(String clave, String porDefecto) {
    String v = System.getenv(clave);
    return (v == null || v.isEmpty()) ? porDefecto : v;
  }
}
