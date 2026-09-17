package com.example.ecoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.R;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private final Context context;
    private final List<HistorialRiego> list;

    public HistorialAdapter(Context context, List<HistorialRiego> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial_riego, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialRiego item = list.get(position);

        holder.tvNombre.setText(item.nombre);
        holder.tvFecha.setText(item.fecha);
        holder.tvHumedad.setText(" Tierra: " + item.humedadTierra + " ");
        holder.tvPoda.setText(" " + item.podada + " ");
        holder.tvClima.setText(item.climaDia);

        if (item.nombre.toLowerCase().contains("monstera") || item.nombre.toLowerCase().contains("helecho")) {
            holder.ivIcono.setImageResource(R.drawable.helecho);
        } else {
            holder.ivIcono.setImageResource(R.drawable.suculenta);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvHumedad, tvPoda, tvClima;
        ImageView ivIcono;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePlantaHistorial);
            tvFecha = itemView.findViewById(R.id.tvFechaHistorial);
            tvHumedad = itemView.findViewById(R.id.tvHumedadTierra);
            tvPoda = itemView.findViewById(R.id.tvEstadoPoda);
            tvClima = itemView.findViewById(R.id.tvClimaDia);
            ivIcono = itemView.findViewById(R.id.ivIconoHistorial);
        }
    }

    public static class HistorialRiego {
        public String nombre;
        public String fecha;
        public String humedadTierra;
        public String climaDia;
        public String podada;

        public HistorialRiego(String nombre, String fecha, String humedadTierra, String climaDia, String podada) {
            this.nombre = nombre;
            this.fecha = fecha;
            this.humedadTierra = humedadTierra;
            this.climaDia = climaDia;
            this.podada = podada;
        }
    }
}
