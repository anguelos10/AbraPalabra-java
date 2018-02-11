package bo.org.abrapalabra.abrapalabra.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bo.org.abrapalabra.abrapalabra.Fragments.Fragment_Inicio;
import bo.org.abrapalabra.abrapalabra.MainActivity;
import bo.org.abrapalabra.abrapalabra.Objetos.ObjEvento;
import bo.org.abrapalabra.abrapalabra.R;

/**
 * Created by Mikypedia on 20/07/2017.
 */

public class AdaptadorEventoTipo extends RecyclerView.Adapter<AdaptadorEventoTipo.ViewHolder>{

    List<ObjEvento> ListEvento;
    private Context context;

    public AdaptadorEventoTipo(List<ObjEvento> listEvento, Context context){
        this.ListEvento = listEvento;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtCalif, txtDetalle, txtFecha, txtHora, txtPeso,txtTipo;
        ImageView imagen, imgStars;
        LinearLayout barColor, linearEvento;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCalif = (TextView)itemView.findViewById(R.id.txtCalif);
            txtDetalle = (TextView)itemView.findViewById(R.id.txtDetalle);
            txtFecha = (TextView)itemView.findViewById(R.id.txtFecha);
            txtHora = (TextView)itemView.findViewById(R.id.txtHora);
            txtPeso = (TextView)itemView.findViewById(R.id.txtPeso);
            txtTipo = (TextView)itemView.findViewById(R.id.txtTipo);
            linearEvento = (LinearLayout)itemView.findViewById(R.id.linearEvento);

            imagen = (ImageView)itemView.findViewById(R.id.ico_evento);
            imgStars = (ImageView)itemView.findViewById(R.id.img_stars);

            barColor = (LinearLayout)itemView.findViewById(R.id.bar_evento);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ini_item, parent, false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ObjEvento evento = ListEvento.get(position);
        holder.txtCalif.setText(evento.getCalificacion());
        holder.txtDetalle.setText(Html.fromHtml(evento.getDetalle()));
        holder.txtFecha.setText(evento.getFecha());
        holder.txtHora.setText(evento.getHora());
        holder.txtPeso.setText(evento.getPeso());
        holder.txtTipo.setText(evento.getTipo());

        int num_evento = Integer.parseInt(evento.getTipo());

        switch (num_evento){
            case 1:
                holder.barColor.setBackgroundColor(Color.parseColor("#aF4081"));
                holder.imagen.setImageResource(R.drawable.icon_90_asistencia);
                break;
            case 2:
                holder.barColor.setBackgroundColor(Color.parseColor("#e6b800"));
                holder.imagen.setImageResource(R.drawable.icon_90_star);
                break;
            case 3:
                holder.barColor.setBackgroundColor(Color.parseColor("#88c45f"));
                holder.imagen.setImageResource(R.drawable.icon_90_alimentacion);
                break;
            case 4:
                holder.barColor.setBackgroundColor(Color.parseColor("#44c4dd"));
                holder.imagen.setImageResource(R.drawable.icon_90_healt);
                break;
            case 5:
                holder.barColor.setBackgroundColor(Color.parseColor("#44c4dd"));
                holder.imagen.setImageResource(R.drawable.icon_90_termometro);
                break;
            case 6:
                holder.barColor.setBackgroundColor(Color.parseColor("#f34e4a"));
                holder.imagen.setImageResource(R.drawable.icon_90_academico);
                break;
            case 7:
                holder.barColor.setBackgroundColor(Color.parseColor("#3fbb65"));
                holder.imagen.setImageResource(R.drawable.icon_90_cuentas);
                break;
            case 8:
                holder.barColor.setBackgroundColor(Color.parseColor("#692c8b"));
                holder.imagen.setImageResource(R.drawable.icon_90_agenda);
                break;
            case 9:
                holder.barColor.setBackgroundColor(Color.parseColor("#007ba3"));
                holder.imagen.setImageResource(R.drawable.icon_90_avisos);
                break;
            default:
                holder.barColor.setBackgroundColor(Color.parseColor("#aF4081"));
                holder.imagen.setImageResource(R.drawable.icon_90_default);
                break;
        }

        int num_stars = Integer.parseInt(evento.getCalificacion());
        switch (num_stars){
            case 0:
                holder.imgStars.setImageResource(R.drawable.stars_0);
                break;
            case 1:
                holder.imgStars.setImageResource(R.drawable.stars_iz_1);
                break;
            case 2:
                holder.imgStars.setImageResource(R.drawable.stars_iz_2);
                break;
            case 3:
                holder.imgStars.setImageResource(R.drawable.stars_iz_3);
                break;
            case 4:
                holder.imgStars.setImageResource(R.drawable.stars_iz_4);
                break;
            case 5:
                holder.imgStars.setImageResource(R.drawable.stars_5);
                break;
            default:
                holder.imgStars.setImageResource(R.drawable.stars_default);
                break;
        }

        holder.linearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemSelectFecha = evento.getFecha().toString();
                String itemSelectTipo = evento.getTipo().toString();

                int tipoEvento = Integer.parseInt(itemSelectTipo);

                SharedPreferences preferencias = context.getSharedPreferences("AbraData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("fechaConsulta",itemSelectFecha);
                editor.commit();

                //Toast.makeText(context,"La Fecha selecionada es: "+itemSelectFecha+"\ny la actividad es: "+itemSelectTipo, Toast.LENGTH_SHORT).show();

                Fragment fragment = new Fragment_Inicio();
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return ListEvento.size();
    }
}
