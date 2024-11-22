package conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Conexion {

    // Método para conectar a la base de datos
    public static Connection conectar() {

        try {
            // Cambiamos la conexión para que apunte a la base de datos `salud`
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/salud?useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
            return cn;

        } catch (SQLException e) {
            System.out.println("Error de Conexión " + e);
        }
        return null;
    }
}
