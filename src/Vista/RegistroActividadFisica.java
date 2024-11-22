package Vista;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class RegistroActividadFisica extends JFrame {
    // Definir los componentes
    private JTextField txtPasos;
    private JTextField txtDistancia;
    private JTextField txtCalorias;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JButton btnSiguiente;
    private JDateChooser dateChooser;

    // Constructor que recibe los pasos previos desde RegistroMedicion
    public RegistroActividadFisica(int pasosPrevios) {
        this(); // Llama al constructor predeterminado para inicializar la interfaz
        txtPasos.setText(String.valueOf(pasosPrevios)); // Precargar los pasos
    }

    // Constructor por defecto
    public RegistroActividadFisica() {
        // Configuración de la ventana
        setTitle("Registro de Actividad Física");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Crear y añadir componentes
        JLabel lblPasos = new JLabel("Pasos:");
        lblPasos.setBounds(20, 20, 100, 25);
        add(lblPasos);

        txtPasos = new JTextField();
        txtPasos.setBounds(130, 20, 200, 25);
        add(txtPasos);

        JLabel lblDistancia = new JLabel("Distancia (km):");
        lblDistancia.setBounds(20, 60, 100, 25);
        add(lblDistancia);

        txtDistancia = new JTextField();
        txtDistancia.setBounds(130, 60, 200, 25);
        add(txtDistancia);

        JLabel lblCalorias = new JLabel("Calorías quemadas:");
        lblCalorias.setBounds(20, 100, 130, 25);
        add(lblCalorias);

        txtCalorias = new JTextField();
        txtCalorias.setBounds(160, 100, 170, 25);
        add(txtCalorias);

        JLabel lblFecha = new JLabel("Fecha de Registro:");
        lblFecha.setBounds(20, 140, 120, 25);
        add(lblFecha);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(130, 140, 200, 25);
        add(dateChooser);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(130, 180, 100, 30);
        add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 180, 100, 30);
        add(btnCancelar);

        // Acciones de los botones
        btnRegistrar.addActionListener((ActionEvent e) -> {
            registrarActividad();
        });
        
        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setBounds(120, 270, 100, 30);  // Posición del botón "Siguiente"
        add(btnSiguiente);
        
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        btnSiguiente.addActionListener((ActionEvent e) -> {
        RegistroSignosVitales registroSignosVitales = new RegistroSignosVitales();  // Aquí agregamos los paréntesis
        registroSignosVitales.setVisible(true);
        dispose();
        });

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
        String query = "SELECT fechaRegistro FROM ActividadFisica ORDER BY fechaRegistro DESC LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Date ultimaFecha = rs.getDate("fechaRegistro");
                dateChooser.setDate(ultimaFecha);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la fecha: " + e.getMessage());
        }
    }

    private void registrarActividad() {
        try {
            int pasos = Integer.parseInt(txtPasos.getText());
            float distancia = Float.parseFloat(txtDistancia.getText());
            float calorias = Float.parseFloat(txtCalorias.getText());
            Date fechaRegistro = dateChooser.getDate();

            String query = "INSERT INTO ActividadFisica (pasos, distancia, caloriasQuemadas, fechaRegistro) VALUES (?, ?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, pasos);
                ps.setFloat(2, distancia);
                ps.setFloat(3, calorias);
                ps.setDate(4, new java.sql.Date(fechaRegistro.getTime()));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Actividad registrada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar la actividad.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar actividad: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroActividadFisica form = new RegistroActividadFisica();
            form.setVisible(true);
        });
    }
}
