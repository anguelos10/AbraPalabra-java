package bo.org.abrapalabra.abrapalabra.Objetos;

/**
 * Created by scorp7 on 12/08/2017.
 */

public class ObjMenu {

    String asistencia;
    String comportamiento;
    String alimentacion;
    String higiene;
    String salud;
    String academico;
    String cuentas;
    String agenda;
    String avisos;
    String otros;

    public ObjMenu(){
    }

    public ObjMenu(String asistencia, String comportamiento, String alimentacion, String higiene, String salud, String academico, String cuentas, String agenda, String avisos, String otros){
        this.asistencia = asistencia;
        this.comportamiento = comportamiento;
        this.alimentacion = alimentacion;
        this.higiene = higiene;
        this.salud = salud;
        this.academico = academico;
        this.cuentas = cuentas;
        this.agenda = agenda;
        this.avisos = avisos;
        this.otros = otros;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public String getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(String comportamiento) {
        this.comportamiento = comportamiento;
    }

    public String getAlimentacion() {
        return alimentacion;
    }

    public void setAlimentacion(String alimentacion) {
        this.alimentacion = alimentacion;
    }

    public String getHigiene() {
        return higiene;
    }

    public void setHigiene(String higiene) {
        this.higiene = higiene;
    }

    public String getSalud() {
        return salud;
    }

    public void setSalud(String salud) {
        this.salud = salud;
    }

    public String getAcademico() {
        return academico;
    }

    public void setAcademico(String academico) {
        this.academico = academico;
    }

    public String getCuentas() {
        return cuentas;
    }

    public void setCuentas(String cuentas) {
        this.cuentas = cuentas;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getAvisos() {
        return avisos;
    }

    public void setAvisos(String avisos) {
        this.avisos = avisos;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }


}
