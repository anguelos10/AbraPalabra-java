package bo.org.abrapalabra.abrapalabra.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Inicio;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjKid;
import bo.org.abrapalabra.abrapalabra.R;

//import com.squareup.picasso.Picasso;

/**
 * Created by Mikypedia on 15/07/2017.
 */

public class AdaptadorKids extends RecyclerView.Adapter<AdaptadorKids.ViewHolder>{

    List<ObjKid> listKids;
    private Context context;

    public AdaptadorKids(List<ObjKid> listKids, Context context){
        this.listKids = listKids;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView nombreKid, id_kid;
        ImageView imgKid;
        LinearLayout linearKid;

        public ViewHolder(View itemKidView){
            super(itemKidView);
            nombreKid = (TextView)itemKidView.findViewById(R.id.nombreLog);
            id_kid = (TextView)itemKidView.findViewById(R.id.I_KID);
            imgKid = (ImageView)itemKidView.findViewById(R.id.imageLog);
            linearKid = (LinearLayout)itemKidView.findViewById(R.id.linearKid);
        }


    }

    @Override
    public AdaptadorKids.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowKids = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hijo, parent, false);
        ViewHolder holder = new ViewHolder(rowKids);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdaptadorKids.ViewHolder holder, int position) {
        final ObjKid objectKid = listKids.get(position);
        holder.nombreKid.setText(objectKid.getNombre());
        holder.id_kid.setText(objectKid.getIdKid());

        //Picasso.with(context).load(objectKid.getImagen()).into(holder.imgKid);

        String imagenKid = objectKid.getImagen();
        Bitmap fotoKid64 = decodeBase64(imagenKid);
        holder.imgKid.setImageBitmap(fotoKid64);

        /*Bitmap fotoKid64 = decodeBase64(imagenKid);
        fotoKid.setImageBitmap(fotoKid64);*/

        holder.linearKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelectNombre = objectKid.getNombre().toString();
                String itemSelectId = objectKid.getIdKid().toString();

                SharedPreferences preferencias = context.getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("id_kid",itemSelectId);
                editor.commit();
                Log.e("Estado", "He replazado la fecha");
                //Toast.makeText(context,"Has selecionado a:  "+itemSelectNombre+"\ncon el id: "+itemSelectId, Toast.LENGTH_SHORT).show();


                Fragment fragment = new Fragment_Inicio();
                Toast.makeText(context, itemSelectNombre+" seleccionado", Toast.LENGTH_SHORT).show();

                /*FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                //fragmentManager.beginTransaction().remove(fragment).commit();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return listKids.size();
    }

    public static Bitmap decodeBase64(String input){
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }

}
