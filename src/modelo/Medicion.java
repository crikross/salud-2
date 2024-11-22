package Modelo;

import java.util.Date;

public class Medicion {
    // Atributos de la clase
    private int pulsaciones;
    private int pasos;
    private String nivelEstres;
    private Date fechaRegistro;

    // Constructor
    public Medicion(int pulsaciones, int pasos, String nivelEstres, Date fechaRegistro) {
        this.pulsaciones = pulsaciones;
        this.pasos = pasos;
        this.nivelEstres = nivelEstres;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getPulsaciones() {
        return pulsaciones;
    }

    public void setPulsaciones(int pulsaciones) {
        this.pulsaciones = pulsaciones;
    }

    public int getPasos() {
        return pasos;
    }

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }

    public String getNivelEstres() {
        return nivelEstres;
    }

    public void setNivelEstres(String nivelEstres) {
        this.nivelEstres = nivelEstres;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Medicion{" +
                "pulsaciones=" + pulsaciones +
                ", pasos=" + pasos +
                ", nivelEstres='" + nivelEstres + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
