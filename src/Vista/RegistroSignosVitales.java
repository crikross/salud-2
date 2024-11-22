package Vista;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class RegistroSignosVitales extends JFrame {
    private JTextField txtPulsaciones;
    private JTextField txtPresionArterial;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JDateChooser dateChooser;

    public RegistroSignosVitales() {
        // Configuración de la ventana
        setTitle("Registro de Signos Vitales");
        setSize(400, 300);
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

        JLabel lblPresionArterial = new JLabel("Presión Arterial:");
        lblPresionArterial.setBounds(20, 60, 100, 25);
        add(lblPresionArterial);

        txtPresionArterial = new JTextField();
        txtPresionArterial.setBounds(130, 60, 200, 25);
        add(txtPresionArterial);

        JLabel lblFecha = new JLabel("Fecha de Registro:");
        lblFecha.setBounds(20, 100, 120, 25);
        add(lblFecha);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(130, 100, 200, 25);
        add(dateChooser);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(130, 180, 100, 30);
        add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 180, 100, 30);
        add(btnCancelar);

        // Acciones de los botones
        btnRegistrar.addActionListener((ActionEvent e) -> {
            registrarSignosVitales();
        });

        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });

        // Cargar la última fecha registrada
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
        String query = "SELECT fechaRegistro FROM SignosVitales ORDER BY fechaRegistro DESC LIMIT 1";

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

    private void registrarSignosVitales() {
        try {
            int pulsaciones = Integer.parseInt(txtPulsaciones.getText());
            int presionArterial = Integer.parseInt(txtPresionArterial.getText());
            Date fechaRegistro = dateChooser.getDate();

            String query = "INSERT INTO SignosVitales (pulsaciones, presionArterial, fechaRegistro) VALUES (?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, pulsaciones);
                ps.setInt(2, presionArterial);
                ps.setDate(3, new java.sql.Date(fechaRegistro.getTime()));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Signos vitales registrados con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar los signos vitales.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar signos vitales: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroSignosVitales form = new RegistroSignosVitales();
            form.setVisible(true);
        });
    }
}


