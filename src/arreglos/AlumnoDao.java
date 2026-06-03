package arreglos;

import clases.Conexion;
import clases.Alumno;
import java.sql.*;
import java.util.ArrayList;

public class AlumnoDao {
  public void adicionar(Alumno a) throws Exception {
    String sql = "INSERT INTO alumnos (cod_alumno, nombres, apellidos, dni, edad, celular, estado) VALUES (?,?,?,?,?,?,?)";
    
    try (Connection cn = Conexion.getConnection(); 
            PreparedStatement ps = cn.prepareStatement(sql)) {
      
      ps.setInt(1, a.getCodAlumno());
      ps.setString(2, a.getNombres());
      ps.setString(3, a.getApellidos());
      ps.setString(4, a.getDni());
      ps.setInt(5, a.getEdad());
      ps.setString(6, a.getCelular());
      ps.setInt(7, a.getEstado());
      ps.executeUpdate();
    }
  }

  public void eliminar(int codAlumno) throws Exception {
    String sql = "DELETE FROM alumnos WHERE cod_alumno = ?";
    try (Connection cn = Conexion.getConnection(); 
            PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setInt(1, codAlumno);
      ps.executeUpdate();
    }
  }

  public void actualizar(Alumno a) throws Exception {
    String sql = "UPDATE alumnos SET nombres=?, apellidos=?, edad=?, celular=?, estado=? WHERE cod_alumno=?";
    try (Connection cn = Conexion.getConnection(); 
            PreparedStatement ps = cn.prepareStatement(sql)) {
     
      ps.setString(1, a.getNombres());
      ps.setString(2, a.getApellidos());
      ps.setInt(3, a.getEdad()); 
      ps.setString(4, a.getCelular());
      ps.setInt(5, a.getEstado());
      ps.setInt(6, a.getCodAlumno());
      ps.executeUpdate();
    }
  }

  //RARO BUSCAR
  public Alumno buscar(int codAlumno) throws Exception {
    String sql = "SELECT * FROM alumnos WHERE cod_alumno = ?";
    try (Connection cn = Conexion.getConnection(); 
            PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setInt(1, codAlumno);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return mapear(rs);
      }
    }
    return null;
  }

  public ArrayList<Alumno> listar() throws Exception {
    ArrayList<Alumno> lista = new ArrayList<>();
    String sql = "SELECT * FROM alumnos ORDER BY cod_alumno";
    try (Connection cn = Conexion.getConnection(); 
            Statement st = cn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
      while (rs.next()) lista.add(mapear(rs));
    }
    return lista;
  }

  public int codigoCorrelativo() throws Exception {
    String sql = "SELECT MAX(cod_alumno) AS maxcod FROM alumnos";
    try (Connection cn = Conexion.getConnection(); 
            Statement st = cn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
       if (rs.next()) return rs.getInt("maxcod") + 1;
    }
    return 202010001; // inicio por defecto
  }

  public boolean existeDNI(String dni) throws Exception {
    String sql = "SELECT 1 FROM alumnos WHERE dni = ?";
    try (Connection cn = Conexion.getConnection(); 
            PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, dni);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    }
  }

    private Alumno mapear(ResultSet rs) throws SQLException{
      return new Alumno(
      rs.getInt("cod_alumno"),
      rs.getString("nombres"),
      rs.getString("apellidos"),
      rs.getString("dni"),          
      rs.getInt("edad"),
      rs.getString("celular"),          
      rs.getInt("estado")        
      );
    }
}