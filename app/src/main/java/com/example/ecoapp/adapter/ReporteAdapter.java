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

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private final Context context;
    private final List<TopPlanta> list;

    public ReporteAdapter(Context context, List<TopPlanta> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reporte, parent, false);
        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        TopPlanta item = list.get(position);

        holder.tvPosicion.setText("#" + (position + 1));
        holder.tvNombre.setText(item.nombre);
        holder.tvEspecie.setText(item.especie);
        holder.tvRiegos.setText(String.valueOf(item.riegos));

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

    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosicion, tvNombre, tvEspecie, tvRiegos;
        ImageView ivIcono;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosicion = itemView.findViewById(R.id.tvPosicionRank);
            tvNombre = itemView.findViewById(R.id.tvNombrePlantaReporte);
            tvEspecie = itemView.findViewById(R.id.tvEspecieReporte);
            tvRiegos = itemView.findViewById(R.id.tvDiasRiego);
            ivIcono = itemView.findViewById(R.id.ivIconoReporte);
        }
    }

    public static class TopPlanta {
        public String nombre;
        public String especie;
        public int riegos;

        public TopPlanta(String nombre, String especie, int riegos) {
            this.nombre = nombre;
            this.especie = especie;
            this.riegos = riegos;
        }
    }
}
