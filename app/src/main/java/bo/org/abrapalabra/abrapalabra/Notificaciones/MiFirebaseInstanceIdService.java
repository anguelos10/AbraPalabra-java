package bo.org.abrapalabra.abrapalabra.Notificaciones;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * esta clase se encarga de gestionar el token
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "Mensajes";//Declaramos el tag

    //Metodo que se ejecuta cuando nos asignan un token o cuando se actualiza(puede cambiar con el tiempo    )
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //Accedemos al token
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: "+token);

    }
}
