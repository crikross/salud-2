
package modelo;
import java.sql.SQLException;

public interface Operaciones {
    
    Operaciones clonar();
    
    void insertar() throws SQLException;
    void seleccionar () throws SQLException;
    void eliminar () throws SQLException;
   
}
