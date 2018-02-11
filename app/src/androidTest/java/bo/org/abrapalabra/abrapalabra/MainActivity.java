package bo.org.abrapalabra.abrapalabra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;

import bo.org.abrapalabra.abrapalabra.Adaptadores.AdaptadorKids;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Agenda;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Alimentacion;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Asistencia;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Avisos;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Comportamiento;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Cuentas;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Default;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Higiene;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Inicio;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Salud;
import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_academico;
import bo.org.abrapalabra.abrapalabra.Objetos.No_Register;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjKid;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjMenu;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjRegistro;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Intentar_Fechas/*, Intentar, Intentar_Fechas, Conexion */{


    NavigationView navigationView;



    //------------------------------------- Ajustes ------------------------------------------------

    DatePicker picker;

    //----------------------------------- Recycle Hijos --------------------------------------------
    RecyclerView rvHijos;
    List<ObjKid> listKids;
    RecyclerView.Adapter adaptadorHijos;
    FirebaseAuth.AuthStateListener mAuthListener;

    String telefonoVerif;
    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Restringimos la orientacion de la pantalla
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras() != null){
            for (String key : getIntent().getExtras().keySet()) {
                Toast.makeText(getApplicationContext(),"Has recibido una notificiion", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(),"Has recibido una notificiion", Toast.LENGTH_SHORT).show();
        }

        /*

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent ireg= new Intent(MainActivity.this,ActivityPhoneAuth.class);
                    startActivity(ireg);
                    finish();
                }
            }
        };*/

        //escribirRegistro();

        //---------------------------- Inicia el onCresate -----------------------------------------
        //toolbar();


        String fecha = fechaSistema();

        //---------------------------  Cargar las shared -------------------------------------------
        /*SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataShared.edit();
        editor.putString("fechaConsulta",fecha);
        editor.putString("id_family", "955");
        editor.putString("id_kid","100028");
        editor.commit();*/

        //----------------------------- Toast de verificacion --------------------------------------
//        Toast.makeText(this, "id family: "+readShared("id_family")+
//                            "\nid kid: "+readShared("id_kid")+
//                            "\nfecha de consulta: "+readShared("fechaConsulta")+
//                            "\ntelefono: "+readShared("phone")
//                            ,Toast.LENGTH_LONG).show();

        //----------------------------- Carga del fragment -----------------------------------------

        verificacion();

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //toolbar();
                Fragment fragment = new Fragment_Inicio();
                LlamarFragment(fragment);
            }
        }, 500);*/
    }

    //-------------------------------------- Interfaces --------------------------------------------
    public void toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        displaySelectScreen(id);

        return true;
    }

    private void displaySelectScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_ini:
                fragment = new Fragment_Inicio();
                LlamarFragment(fragment);
                break;
            case R.id.nav_asist:
                fragment = new Fragment_Asistencia();
                LlamarFragment(fragment);
                break;
            case R.id.nav_comp:
                fragment = new Fragment_Comportamiento();
                LlamarFragment(fragment);
                break;
            case R.id.nav_alim:
                fragment = new Fragment_Alimentacion();
                LlamarFragment(fragment);
                break;
            case R.id.nav_higi:
                fragment = new Fragment_Higiene();
                LlamarFragment(fragment);
                break;
            case R.id.nav_salud:
                fragment = new Fragment_Salud();
                LlamarFragment(fragment);
                break;
            case R.id.nav_acad:
                fragment = new Fragment_academico();
                LlamarFragment(fragment);
                break;
            case R.id.nav_cuentas:
                fragment = new Fragment_Cuentas();
                LlamarFragment(fragment);
                break;
            case R.id.nav_agenda:
                fragment = new Fragment_Agenda();
                LlamarFragment(fragment);
                break;
            case R.id.nav_avisos:
                fragment = new Fragment_Avisos();
                LlamarFragment(fragment);
                break;
            case R.id.nav_default:
                fragment = new Fragment_Default();
                LlamarFragment(fragment);
        }
    }

    private void LlamarFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //----------------------- Mostrar, ocultar opcones del menu ------------------------------------
    private void cargarMenu(){


        //Obtenemos la instancia de firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuDrawel = database.getReference().child("config").child("menu");
        menuDrawel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MenuItem nav;
                Menu menu = navigationView.getMenu();

                ObjMenu objMenu = dataSnapshot.getValue(ObjMenu.class);
                String asistencia = objMenu.getAsistencia();
                String comp = objMenu.getComportamiento();
                String alim = objMenu.getAlimentacion();
                String higi = objMenu.getHigiene();
                String salud = objMenu.getSalud();
                String acad = objMenu.getAcademico();
                String cuentas = objMenu.getCuentas();
                String agenda = objMenu.getAgenda();
                String avisos = objMenu.getAvisos();
                String otros = objMenu.getOtros();

                //Toast.makeText(getApplicationContext(), asistencia+comp+alim+higi+salud+acad+cuentas+agenda+avisos+otros,Toast.LENGTH_SHORT ).show();

                for (int i = 1; i <= 10; i++){
                    switch (i){
                        case 1:
                            nav = menu.findItem(R.id.nav_asist);
                            if(asistencia.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 2:
                            nav = menu.findItem(R.id.nav_comp);
                            if(comp.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 3:
                            nav = menu.findItem(R.id.nav_alim);
                            if(alim.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 4:
                            nav = menu.findItem(R.id.nav_higi);
                            if(higi.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 5:
                            nav = menu.findItem(R.id.nav_salud);
                            if(salud.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 6:
                            nav = menu.findItem(R.id.nav_acad);
                            if(acad.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 7:
                            nav = menu.findItem(R.id.nav_cuentas);
                            if(cuentas.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 8:
                            nav = menu.findItem(R.id.nav_agenda);
                            if(agenda.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 9:
                            nav = menu.findItem(R.id.nav_avisos);
                            if(avisos.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                        case 10:
                            nav = menu.findItem(R.id.nav_default);
                            if(otros.equals("0")){
                                nav.setVisible(false);
                            }else{nav.setVisible(true);}
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //----------------------- Opcines del menu calendario e hijos ----------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendario:

                DialogFragment dFragment = new DatePickerFragment();
                dFragment.show(getSupportFragmentManager(), "INTENTO");

                return true;

            case R.id.select_kid:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();

                View view1 = layoutInflater.inflate(R.layout.dialog_hijos, null);
                rvHijos = (RecyclerView)view1.findViewById(R.id.recicle_hijos);
                rvHijos.setLayoutManager(new LinearLayoutManager(this));

                listKids = new ArrayList<>();
                adaptadorHijos = new AdaptadorKids(listKids, getApplicationContext());
                rvHijos.setAdapter(adaptadorHijos);

                FirebaseDatabase database = FirebaseDatabase.getInstance();


                SharedPreferences dataShared = this.getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                String idfamily = dataShared.getString("id_family","0");
                database.getReference().child("family").child(idfamily).child("kids").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listKids.removeAll(listKids);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ObjKid objKid = snapshot.getValue(ObjKid.class);
                            listKids.add(objKid);
                        }
                        adaptadorHijos.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //builder.setMessage("Seleccion de kid");
                builder.setPositiveButton("Seleccionar",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface builder, int id) {
                        Fragment fragment = new Fragment_Inicio();
                        LlamarFragment(fragment);
                    }
                });
                builder.setView(view1);



                final AlertDialog dialog = builder.show();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void Enviar_Fechas(Fragment fragment) {
        LlamarFragment(fragment);
    }

    //---------------------------- Funciones de fecha y hora ---------------------------------------
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        Intentar_Fechas intentarFecha;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),android.app.AlertDialog.THEME_HOLO_LIGHT , this, year, month, day);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, this,year,month,day);
            return dpd;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int mont = month + 1;
            String fecha = mont + "/" + day + "/" + year;
            String fechaConsulta = ArreglarFecha(day)+ "/" + ArreglarFecha(mont) + "/" +year  ;
            Log.d("Dia", "" + day);
            Log.d("Mes", "" + mont);
            Log.d("Anio", "" + year);
            Log.d("Fecha Pick", "" + fechaConsulta);

            SharedPreferences preferencias = getContext().getSharedPreferences("AbraData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("fechaConsulta",fechaConsulta);
            editor.commit();


            Fragment fragment = new Fragment_Inicio();
            intentarFecha.Enviar_Fechas(fragment);
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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            intentarFecha = (Intentar_Fechas) activity;
        }
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

    public void verificacion (){

        telefonoVerif = readShared("phone");

        boolean resutado = false;

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuarios = root.child("register");
        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(telefonoVerif).exists()){

                    escribirRegistro();
                    toolbar();
                    cargarMenu();
                    verifToken();
                    //Toast.makeText(getApplicationContext(), "El teléfono esta verificado", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new Fragment_Inicio();
                    LlamarFragment(fragment);
                }else {
                    Intent inteto = new Intent(getApplication(), No_Register.class);
                    startActivity(inteto);
                    finish();
                    //alertaPhone(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alertaPhone(int tipo){
        AlertDialog.Builder alertphone = new AlertDialog.Builder(this);
        alertphone.setTitle("Teléfono no registrado");
        alertphone.setMessage("El telefono no esta registrado en la base de datos, por favor contactese con Abra Palabra");
        alertphone.setCancelable(false);
        alertphone.setPositiveButton("Salir", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                finish();
            }
        });

        alertphone.show();
    }

    public void escribirRegistro(){

        /*final ProgressDialog load = new ProgressDialog(this);
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.setMessage("Cargado...");
        load.setIndeterminate(true);
        load.setCanceledOnTouchOutside(false);
        load.show();*/

        //---------------------------- Consulta base de datos --------------------------------------
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String phonetemens = readShared("phone");

        final DatabaseReference nombre = database.getReference().child("register").child(phonetemens);
        nombre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObjRegistro datosFamily = dataSnapshot.getValue(ObjRegistro.class);
                String id_family = datosFamily.getId_family();
                String kid_1= datosFamily.getKid1();

                String fecha = fechaSistema();

                SharedPreferences dataShared = getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = dataShared.edit();
                editor.putString("fechaConsulta",fecha);
                editor.putString("id_family",id_family);
                editor.putString("id_kid",kid_1);
                editor.commit();
                String phonetemens = readShared("phone");
//                Toast.makeText(getApplicationContext(),"Familia:"+id_family+"telefono"+phonetemens,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Nombre", "No se pudo cargar el nombre");
            }
        });

        /*load.dismiss();*/
    }

    public String readShared(String valor){
        SharedPreferences dataShared = this.getSharedPreferences("AbraData", Context.MODE_PRIVATE);
        String respuesta = dataShared.getString(valor,"0");
        return respuesta;
    }

    public void verifToken(){
        String tokenSys= FirebaseInstanceId.getInstance().getToken();
        String tokenShared = readShared("token");
        String phone = readShared("phone");

        if (tokenSys.equals(tokenShared)){

        }else {
            DatabaseReference dfrToken = FirebaseDatabase.getInstance().getReference();
            dfrToken.child("register").child(phone).child("token").setValue(tokenSys);
        }
    }
}