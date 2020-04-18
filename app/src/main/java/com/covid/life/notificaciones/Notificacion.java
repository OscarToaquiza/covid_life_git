package com.covid.life.notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.covid.life.MainActivity;
import com.covid.life.R;
import com.covid.life.login.activity_login;
import com.covid.life.menu.activity_menu;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class Notificacion extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        getSharedPreferences("notificacion", MODE_PRIVATE).edit().putString("token", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
            String titulo = remoteMessage.getNotification().getTitle();
            String descripcion = remoteMessage.getNotification().getBody();
            String enlace = remoteMessage.getData().get("enlace");
            superiorOreo(titulo,descripcion,enlace);
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("notificacion", MODE_PRIVATE).getString("token", "empty");
    }

    private void superiorOreo(String titulo,String descripcion,String enlace){

        String id ="mensaje";
        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            nm.createNotificationChannel(nc);

        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(descripcion)
                .setContentIntent(clickNotificacion(enlace))
                .setContentInfo("nuevo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        Random random = new Random();
        int idNotify = random.nextInt(8000);
        assert nm != null;
        nm.notify(idNotify,builder.build());

    }

    public PendingIntent clickNotificacion(String enlace){
        Intent nf = new Intent(Intent.makeMainActivity(new ComponentName(this, activity_llamada.class)));
        nf.putExtra("enlace",enlace);
        nf.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this,0,nf,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
