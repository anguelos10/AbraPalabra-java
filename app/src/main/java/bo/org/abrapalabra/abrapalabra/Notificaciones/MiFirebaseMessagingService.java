package bo.org.abrapalabra.abrapalabra.Notificaciones;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import bo.org.abrapalabra.abrapalabra.MainActivity;
import bo.org.abrapalabra.abrapalabra.R;


public class MiFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG= "Mensajes";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Aqui llegaran todos losmensajes o notificaciones que nos manden
        String from = remoteMessage.getFrom();

        Log.d(TAG, "mensaje recibido de: "+from);

        //Accedemos al contenido de la notificacion  Como existe la posibilidad q sea nulo lo validamos
        if (remoteMessage.getNotification()!=null){
            Log.d(TAG, "Notificacion: "+remoteMessage.getNotification().getBody());

            //Toast.makeText(getApplicationContext(),"Actualizando",Toast.LENGTH_LONG).show();


            //Notificacion q recibe el titulo y el cuerpo
            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());


        }

        //eviar informacion extra (Clave Valor)
        if (remoteMessage.getData().size()>0){
            Log.d(TAG, "Data: "+remoteMessage.getData());
        }

    }

     private void mostrarNotificacion(String title, String body) {

        //Accion cuando lo presionen
         Intent intent = new Intent(this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);//activar


        //Solicitamos la uri del sonido
        Uri doundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //NotificationCompatbuilder selecciona el icono de la aplicacion
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_90_inicio_notificacion1)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.icon_90_inicio))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)//desaparece cuando lo tocamos
                .setSound(doundUri)//sonido de la notificacion
                .setVibrate(new long[] {100, 250, 100, 500})
                .setContentIntent(pendingIntent)//lo q hara cuando lo presionemos
                .setPriority(Notification.PRIORITY_HIGH);


        //Hacer que el notificador se encargue (codigo por defecto)

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}
