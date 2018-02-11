package bo.org.abrapalabra.abrapalabra.Objetos;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bo.org.abrapalabra.abrapalabra.R;

/**
 * Created by scorp7 on 12/08/2017.
 */

public class No_Register extends AppCompatActivity {

    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Restringimos la orientacion de la pantalla
        setContentView(R.layout.no_register);

        btnExit = (Button)findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
