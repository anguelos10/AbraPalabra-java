package bo.org.abrapalabra.abrapalabra.Objetos;

import android.support.annotation.NonNull;

/**
 * Created by Mikypedia on 13/07/2017.
 */

public class ObjEvento implements Comparable {

    String calificacion;
    String detalle;
    String fecha;
    String hora;
    String peso;
    String tipo;

    public ObjEvento(){
    }

    public ObjEvento(String calificacion, String detalle, String fecha, String hora, String peso, String tipo){

        this.calificacion = calificacion;
        this.detalle = detalle;
        this.fecha = fecha;
        this.hora = hora;
        this.peso = peso;
        this.tipo = tipo;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        int comparePeso= Integer.parseInt(((ObjEvento)o).getPeso());
        /* For Ascending order*/
        return  Integer.parseInt(this.peso)-comparePeso;
    }


}