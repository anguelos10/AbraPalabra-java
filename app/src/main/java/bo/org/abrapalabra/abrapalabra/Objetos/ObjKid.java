package bo.org.abrapalabra.abrapalabra.Objetos;

/**
 * Created by Mikypedia on 13/07/2017.
 */

public class ObjKid {

    String genero;
    String grupo;
    String imagen;
    String nombre;
    String idKid;


    public ObjKid(){

    }

    public ObjKid(String genero, String grupo, String imagen, String nombre, String idKid){
        this.genero = genero;
        this.grupo = grupo;
        this.imagen = imagen;
        this.nombre = nombre;
        this.idKid = idKid;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdKid() {
        return idKid;
    }

    public void setIdKid(String idKid) {
        this.idKid = idKid;
    }

}
