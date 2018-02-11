package bo.org.abrapalabra.abrapalabra.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bo.org.abrapalabra.abrapalabra.Adaptadores.AdaptadorEventoTipo;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjEvento;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjKid;
import bo.org.abrapalabra.abrapalabra.R;

//import com.squareup.picasso.Picasso;

/**
 * Created by Mikypedia on 14/07/2017.
 */

public class Fragment_Cuentas extends Fragment {

    View rootView;
    RecyclerView rv;
    List<ObjEvento> ListEvento;
    RecyclerView.Adapter adaptador;

    TextView nombre_kid,grupo_kid;
    ImageView fotoKid;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_cuentas, viewGroup, false);

        //Shared preferences
        SharedPreferences dataShared = getContext().getSharedPreferences("AbraData", Context.MODE_PRIVATE);
        String idKid = dataShared.getString("id_kid","100028");
        String idFamily = dataShared.getString("id_family","955");
        String family = dataShared.getString("family","family");
        final String fechaConsulta = dataShared.getString("fechaConsulta","family");


        //Instanciamos los elementos
        nombre_kid = (TextView)rootView.findViewById(R.id.txtNombre);
        grupo_kid = (TextView)rootView.findViewById(R.id.txtGrupo);
        fotoKid = (ImageView)rootView.findViewById(R.id.img_nino);


        //Obtenemos la intacia de firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //----------------------------- Obntener datos del chamaco ---------------------------------
        final DatabaseReference nombre = database.getReference().child(family).child(idFamily).child("kids").child(idKid);
        nombre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObjKid datosKid = dataSnapshot.getValue(ObjKid.class);
                String nombre = datosKid.getNombre();
                String grupo = datosKid.getGrupo();
                String imagenKid = datosKid.getImagen();

                nombre_kid.setText(nombre);
                grupo_kid.setText(grupo);

                Bitmap fotoKid64 = decodeBase64(imagenKid);
                fotoKid.setImageBitmap(fotoKid64);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Nombre", "No se pudo cargar el nombre");
            }
        });

        //---------------------------- Llenar el recicleview ---------------------------------------
        rv = (RecyclerView)rootView.findViewById(R.id.recicle);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        ListEvento = new ArrayList<>();
        adaptador = new AdaptadorEventoTipo(ListEvento, getContext());
        rv.setAdapter(adaptador);

        database.getReference().child(family).child(idFamily).child(idKid).orderByChild("tipo").equalTo("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListEvento.removeAll(ListEvento);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ObjEvento Evento = snapshot.getValue(ObjEvento.class);
                    ListEvento.add(Evento);
                }
                adaptador.notifyDataSetChanged();
                Collections.reverse(ListEvento);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
    public static Bitmap decodeBase64(String input){
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }
}
