package Vista;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class RegistroMedicion extends JFrame {
    // Definir los componentes
    private JTextField txtPulsaciones;
    private JTextField txtPasos;
    private JComboBox<String> comboNivelEstres;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JButton btnSiguiente;
    private JDateChooser dateChooser;  // Agregar JDateChooser

    public RegistroMedicion() {
        // Configurar la ventana
        setTitle("Registro de Mediciones");
        setSize(400, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Crear y añadir componentes
        JLabel lblPulsaciones = new JLabel("Pulsaciones:");
        lblPulsaciones.setBounds(20, 20, 100, 25);
        add(lblPulsaciones);

        txtPulsaciones = new JTextField();
        txtPulsaciones.setBounds(130, 20, 200, 25);
        add(txtPulsaciones);

        JLabel lblPasos = new JLabel("Pasos:");
        lblPasos.setBounds(20, 60, 100, 25);
        add(lblPasos);
        
        txtPasos = new JTextField();
        txtPasos.setBounds(130, 60, 200, 25);
        add(txtPasos);

        JLabel lblNivelEstres = new JLabel("Nivel de Estrés:");
        lblNivelEstres.setBounds(20, 100, 100, 25);
        add(lblNivelEstres);

        comboNivelEstres = new JComboBox<>(new String[]{"Bajo", "Medio", "Alto"});
        comboNivelEstres.setBounds(130, 100, 200, 25);
        add(comboNivelEstres);

        // Añadir el JDateChooser para seleccionar la fecha
        JLabel lblFecha = new JLabel("Fecha de Registro:");
        lblFecha.setBounds(20, 140, 120, 25);
        add(lblFecha);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(130, 140, 200, 25);
        add(dateChooser);

        // Acción del botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(130, 180, 100, 30);
        add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 180, 100, 30);
        add(btnCancelar);

        // Acción del botón Registrar
        btnRegistrar.addActionListener((ActionEvent e) -> {
            registrarMedicion();
        });
        
        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setBounds(120, 270, 100, 30);  // Posición del botón "Siguiente"
        add(btnSiguiente);
        
        // Acción del botón Cancelar
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        btnSiguiente.addActionListener((ActionEvent e) -> {
        int pasos = Integer.parseInt(txtPasos.getText());
        RegistroActividadFisica registroActividadFisica = new RegistroActividadFisica(pasos);
        registroActividadFisica.setVisible(true);
        dispose();
        });
        
        // Cargar la última fecha de registro
        cargarUltimaFecha();
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salud", "root", "root");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
        }
        return conn;
    }

    private void cargarUltimaFecha() {
        String query = "SELECT fechaRegistro FROM Medicion ORDER BY fechaRegistro DESC LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Obtener la fecha de la base de datos
                Date ultimaFecha = rs.getDate("fechaRegistro");
                // Establecer la fecha en el JDateChooser
                dateChooser.setDate(ultimaFecha);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la fecha: " + e.getMessage());
        }
    }

    private void registrarMedicion() {
        int pulsaciones = Integer.parseInt(txtPulsaciones.getText());
        int pasos = Integer.parseInt(txtPasos.getText());
        String nivelEstres = comboNivelEstres.getSelectedItem().toString();
        
        // Obtener la fecha seleccionada del JDateChooser
        Date fechaRegistro = dateChooser.getDate();

        String query = "INSERT INTO Medicion (pulsaciones, pasos, nivelEstres, fechaRegistro) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, pulsaciones);
            ps.setInt(2, pasos);
            ps.setString(3, nivelEstres);
            ps.setDate(4, new java.sql.Date(fechaRegistro.getTime())); // Convertir a formato SQL Date

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Medición registrada con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar la medición.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar medición: " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroMedicion form = new RegistroMedicion();
            form.setVisible(true);
        });
    }
}
