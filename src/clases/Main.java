package clases;
import java.sql.Connection;
import clases.Conexion;

public class Main {

    public static void main(String[] args) {
      try {
            Connection cn = Conexion.getConnection();

            System.out.println("Conectado correctamente");

            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
