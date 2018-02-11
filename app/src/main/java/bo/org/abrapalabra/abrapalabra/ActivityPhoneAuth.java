package bo.org.abrapalabra.abrapalabra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

import bo.org.abrapalabra.abrapalabra.Objetos.ObjRegistro;

/**  *********************************************************  No esta en uso
 * Created by Mikypedia on 16/07/2017.
 */

public class ActivityPhoneAuth extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone, etPhoneCode;
    private Button btnPhone, btnCodePhone;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String mVerificationId;
    String phoneNumber;
    String token;

    DatePicker picker;
    LinearLayout pantalla;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        //Instanciar
        etPhone = (EditText)findViewById(R.id.etPhoneNumber);
        etPhoneCode = (EditText)findViewById(R.id.etCodeNumber);
        btnPhone = (Button)findViewById(R.id.btnNumberPhone);
        btnCodePhone = (Button)findViewById(R.id.btnCodePhone);
        pantalla = (LinearLayout)findViewById(R.id.pantalla);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    Toast.makeText(getApplicationContext(),firebaseAuth.getCurrentUser().getProviderId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityPhoneAuth.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    pantalla.setVisibility(View.VISIBLE);
                }
            }
        };

        btnPhone.setOnClickListener(this);
        btnCodePhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNumberPhone:
                requestCode(v);
                break;
            case R.id.btnCodePhone:
                signIn(v);
                break;
        }
    }

    public void requestCode(View view){
        String phoneNumber = phoneEdit(etPhone.getText().toString());

        if (TextUtils.isEmpty(phoneNumber))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60, TimeUnit.SECONDS,ActivityPhoneAuth.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            registrarDB();
                            // Acccion para recoget numero de telefono

                        }else {
                            Toast.makeText(ActivityPhoneAuth.this,"Error al cargar registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view){
        String code = etPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;
        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }

    public void registrarDB(){
        token = FirebaseInstanceId.getInstance().getToken();
        phoneNumber = phoneEdit(etPhone.getText().toString());

        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;  // quita el simbolo +

        writeShared("phone",phoneNumber);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuarios = root.child("register");
        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phoneNumber).exists()){

                    //------------------------ remplazar base de datos -----------------------------
                    ObjRegistro objRegistro = dataSnapshot.getValue(ObjRegistro.class);
                    String id_family = objRegistro.getId_family();
                    String kid1 = objRegistro.getKid1();

                    String fecha = fechaSistema();

                    SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = dataShared.edit();
                    editor.putString("phone", phoneNumber);
                    editor.putString("fechaConsulta", fecha);
                    editor.putString("id_family", id_family);
                    editor.putString("id_kid", kid1);
                    editor.commit();

                    Toast.makeText(ActivityPhoneAuth.this,"Estoy reescribiendo "+phoneNumber  , Toast.LENGTH_SHORT).show();

                    //------------------------------------------------------------------------------

                    DatabaseReference dfrToken = FirebaseDatabase.getInstance().getReference();
                    dfrToken.child("register").child(phoneNumber).child("token").setValue(token);

                }else {
                    alertaPhone();
                    SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = dataShared.edit();
                    editor.putString("phone", "0");
                    editor.putString("fechaConsulta", "12/12/2012");
                    editor.putString("id_family", "0");
                    editor.putString("id_kid", "0");
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        /*if (dbref != null) {

            //Leer base de datos
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ObjRegistro objRegistro = dataSnapshot.getValue(ObjRegistro.class);
                    String id_family = objRegistro.getId_family();
                    String kid1 = objRegistro.getKid1();
                    String token = objRegistro.getToken();

                    String fecha = fechaSistema();

                    SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = dataShared.edit();
                    editor.putString("phone", phoneNumber);
                    editor.putString("fechaConsulta", fecha);
                    editor.putString("id_family", id_family);
                    editor.putString("id_kid", kid1);
                    editor.commit();


//                    Toast.makeText(ActivityPhoneAuth.this,id_family+"\n"+kid1,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Escribir token base de datos
            dbref.child("token").setValue(token);
        }else{
            SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = dataShared.edit();
            editor.putString("phone", "");
            editor.putString("fechaConsulta", "12/12/2012");
            editor.putString("id_family", "0");
            editor.putString("id_kid", "0");
            editor.commit();
        }*/

    }

    public String fechaSistema(){

        String dateResponse = "";

        int dia = 0, mes = 0, anio = 0;

        picker = new DatePicker(this);
        dia = picker.getDayOfMonth();
        mes = picker.getMonth() + 1;
        anio = picker.getYear();
//        String.format("%010d", Integer.parseInt(mystring));

        dateResponse = ArreglarFecha(dia)+ "/" + ArreglarFecha(mes) +"/" +anio;
        return dateResponse;
    }

    public String ArreglarFecha(int numero){
        String resultado = "";

        if (numero < 10){
            resultado = "0"+numero;
        }else{
            resultado = numero+"";
        }

        return resultado;
    }

    public String phoneEdit(String phone){

        String number;
//        phone = phone.startsWith("+") ? phone.substring(1) : phone;
        if ( phone.length()==8){
            number = "+591"+phone;
        } else {
            number = phone;
        }

        return number;
    }

    public void alertaPhone(){
        AlertDialog.Builder alertphone = new AlertDialog.Builder(this);
        alertphone.setTitle("Teléfono no registrado");
        alertphone.setMessage("El telefono no esta registrado en la base de datos, por favor contactese con Abra Palabra");
        alertphone.setCancelable(false);
        alertphone.setPositiveButton("Salir", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertphone.show();
    }

    public void writeShared (String campo, String valor){
        SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataShared.edit();
        editor.putString(campo,valor);
        editor.commit();
    }
}
