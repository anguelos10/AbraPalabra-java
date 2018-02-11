package bo.org.abrapalabra.abrapalabra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
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

/**
 * Created by scorp7 on 23/07/2017.
 */

public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mPhoneNumberField, mVerificationField;
    Button mStartButton, mVerifyButton, mResendButton;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    String token;
    String phoneNumber;

    DatePicker picker;
    LinearLayout pantalla;

    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mPhoneNumberField = (EditText) findViewById(R.id.etPhoneNumber);
        mVerificationField = (EditText) findViewById(R.id.etCodeNumber);

        mStartButton = (Button) findViewById(R.id.btnNumberPhone);
        mVerifyButton = (Button) findViewById(R.id.btnCodePhone);
        mResendButton = (Button) findViewById(R.id.btnReSendCode);

        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Cuota excedida.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            registrarDB();//-----------------------------------------------------------------------------------------------------------ç

                            EnviarToken();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, 2000);





                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Código invalido.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validarPhoneNumber() {
        String phoneNumber = phoneEdit(mPhoneNumberField.getText().toString());
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Por favor ingrese un número de telefono");
            return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNumberPhone:
                if (!validarPhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(phoneEdit(mPhoneNumberField.getText().toString()));
                break;
            case R.id.btnCodePhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Ingrese un nro valido.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btnReSendCode:
                resendVerificationCode(phoneEdit(mPhoneNumberField.getText().toString()), mResendToken);
                break;
        }

    }

    public void registrarDB(){
        //token = FirebaseInstanceId.getInstance().getToken();
        phoneNumber = phoneEdit(mPhoneNumberField.getText().toString());

        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;  // quitar el + del inicio del nro

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
                    String token = FirebaseInstanceId.getInstance().getToken();

                    SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = dataShared.edit();
                    editor.putString("token",token);
                    editor.putString("phone", phoneNumber);
                    editor.putString("fechaConsulta", fecha);
                    editor.putString("id_family", id_family);
                    editor.putString("id_kid", kid1);
                    editor.commit();

                    //Toast.makeText(this,"Estoy reescribiendo el nro "+phoneNumber ,Toast.LENGTH_SHORT).show();

//                    Toast.makeText(this, "Guardando: "+phoneNumber+" por favor, espere...." ,Toast.LENGTH_LONG).show();
                    //------------------------------------------------------------------------------

                    EnviarToken();
                    /*DatabaseReference dfrToken = FirebaseDatabase.getInstance().getReference();
                    dfrToken.child("register").child(phoneNumber).child("token").setValue(token);*/

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


        Toast.makeText(this, "Registrando: ("+number+") por favor, espere...."
                , Toast.LENGTH_LONG).show();
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

    public void EnviarToken(){
        token = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference dfrToken = FirebaseDatabase.getInstance().getReference();
        dfrToken.child("register").child(phoneNumber).child("token").setValue(token);
    }
}
