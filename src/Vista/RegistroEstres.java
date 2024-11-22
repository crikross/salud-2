package Vista;

import com.toedter.calendar.JDateChooser;
import controlador.RegistroObserver;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class RegistroEstres extends JFrame {
    private JTextField txtNombre, txtEdad;
    private JComboBox<String> comboNivelEstres;
    private JTextField txtMotivo;
    private JDateChooser dateChooser;
    private JButton btnRegistrar, btnCancelar, btnSiguiente;

    private RegistroObserver observer;  // Declarar el observer

    public RegistroEstres(String nombreUsuario, int edadUsuario) {
        setTitle("Registro de Estrés");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Crear y configurar los componentes de la interfaz
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 200, 25);
        txtNombre.setText(nombreUsuario); 
        add(txtNombre);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(20, 60, 100, 25);
        add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setBounds(120, 60, 200, 25);
        txtEdad.setText(String.valueOf(edadUsuario)); 
        add(txtEdad);

        JLabel lblNivelEstres = new JLabel("Nivel de Estrés:");
        lblNivelEstres.setBounds(20, 100, 100, 25);
        add(lblNivelEstres);

        comboNivelEstres = new JComboBox<>();
        comboNivelEstres.setBounds(120, 100, 200, 25);
        inicializarComboNivelEstres(); 
        add(comboNivelEstres);

        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setBounds(20, 140, 100, 25);
        add(lblMotivo);

        txtMotivo = new JTextField();
        txtMotivo.setBounds(120, 140, 200, 25);
        add(txtMotivo);

        JLabel lblFechaRegistro = new JLabel("Fecha de Registro:");
        lblFechaRegistro.setBounds(20, 180, 100, 25);
        add(lblFechaRegistro);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(120, 180, 200, 25);
        add(dateChooser);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(120, 230, 100, 30);
        add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 230, 100, 30);
        add(btnCancelar);

        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setBounds(120, 270, 100, 30);  // Posición del botón "Siguiente"
        add(btnSiguiente);

        // Acción del botón Registrar
        btnRegistrar.addActionListener((ActionEvent e) -> {
            registerEstres();
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });

        // Acción del botón Siguiente
        btnSiguiente.addActionListener((ActionEvent e) -> {
            // Crear la nueva instancia de RegistroMedicion y mostrarla
            RegistroMedicion registroMedicion = new RegistroMedicion();
            registroMedicion.setVisible(true);
            dispose(); // Cerrar la ventana actual
        });
    }

    // Método para asignar el observer
    public void setObserver(RegistroObserver observer) {
        this.observer = observer;
    }

    // Método para inicializar el JComboBox con los niveles de estrés
    private void inicializarComboNivelEstres() {
        String[] nivelesEstres = {"Bajo", "Medio", "Alto"};
        for (String nivel : nivelesEstres) {
            comboNivelEstres.addItem(nivel);
        }
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

    private void registerEstres() {
        String nombre = txtNombre.getText();
        int edad = Integer.parseInt(txtEdad.getText());
        String nivelEstres = comboNivelEstres.getSelectedItem().toString();
        String motivo = txtMotivo.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaRegistro = dateFormat.format(dateChooser.getDate());

        String query = "INSERT INTO Estres (nombre, edad, nivelEstres, motivo, fechaRegistro) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setString(1, nombre);
            ps.setInt(2, edad);
            ps.setString(3, nivelEstres); 
            ps.setString(4, motivo);
            ps.setString(5, fechaRegistro);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Registro de estrés guardado con éxito.");

                // Notificar al observador
                if (observer != null) {
                    observer.onRegistroCompletado("Registro de estrés completado con éxito.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el estrés.");
            }
               

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar estrés: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroEstres form = new RegistroEstres("Juan Pérez", 25); // Proporcionar el nombre y edad
            form.setVisible(true);
        });
    }
}
