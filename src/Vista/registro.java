package Vista;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class registro extends JFrame {
    // Definir los componentes
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JComboBox<String> comboNivelAcademico;
    private JDateChooser dateChooser;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public registro() {
        // Configurar la ventana
        setTitle("Nuevo Estudiante");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Crear y añadir componentes
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 20, 200, 25);
        add(txtNombre);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(20, 60, 100, 25);
        add(lblEdad);
        txtEdad = new JTextField();
        txtEdad.setBounds(130, 60, 200, 25);
        add(txtEdad);

        JLabel lblNivel = new JLabel("Nivel Académico:");
        lblNivel.setBounds(20, 100, 100, 25);
        add(lblNivel);

        comboNivelAcademico = new JComboBox<>(new String[]{"Universitario", "Técnico"});
        comboNivelAcademico.setBounds(130, 100, 200, 25);
        add(comboNivelAcademico);

        JLabel lblFecha = new JLabel("Fecha de Inscripción:");
        lblFecha.setBounds(20, 140, 100, 25);
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

        // Acción del botón Registrar
        btnRegistrar.addActionListener((ActionEvent e) -> {
            registrarusuario();
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();  // Cierra la ventana
        });
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

    private void registrarusuario() {
        String nombre = txtNombre.getText();
        int edad = Integer.parseInt(txtEdad.getText());
        String nivelAcademico = comboNivelAcademico.getSelectedItem().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInscripcion = dateFormat.format(dateChooser.getDate());

        String query = "INSERT INTO tbl_usuarios (nombre, edad, nivel_academico, fecha_inscripcion) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nombre);
            ps.setInt(2, edad);
            ps.setString(3, nivelAcademico);
            ps.setString(4, fechaInscripcion);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                
                // Ocultar la ventana actual y abrir la ventana de registro de estrés
                this.setVisible(false);
                
                // Crear y configurar la instancia de RegistroEstres
                RegistroEstres registroEstres = new RegistroEstres(nombre, edad);
                registroEstres.setObserver(mensaje -> JOptionPane.showMessageDialog(null, mensaje));
                registroEstres.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el usuario.");
            }

        } catch (Exception e) { // Esto te ayudará a ver detalles del error en la consola
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            registro form = new registro();
            form.setVisible(true);
        });
    }
}
