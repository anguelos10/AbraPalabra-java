package bo.org.abrapalabra.abrapalabra.Objetos;

/**
 * Created by scorp7 on 21/07/2017.
 */

public class ObjRegistro {
    String token;
    String kid1;
    String id_family;

    public ObjRegistro(){
    }

    public ObjRegistro(String token, String kid1, String id_family){
        this.token = token;
        this.kid1 = kid1;
        this.id_family = id_family;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKid1() {
        return kid1;
    }

    public void setKid1(String hijo1) {
        this.kid1 = hijo1;
    }

    public String getId_family() {
        return id_family;
    }

    public void setId_family(String id_family) {
        this.id_family = id_family;
    }


}
