package com.example.ecoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.PlantaDetalleActivity;
import com.example.ecoapp.R;
import com.example.ecoapp.model.Planta;

import java.util.List;

public class PlantaAdapter extends RecyclerView.Adapter<PlantaAdapter.PlantaViewHolder> {

    public interface OnPlantaListener {
        void onPlantaClick(Planta planta);
        void onEditarClick(Planta planta);
        void onEliminarClick(Planta planta);
    }

    private final Context context;
    private List<Planta> plantaList;
    private OnPlantaListener listener;

    public PlantaAdapter(Context context, List<Planta> plantaList) {
        this.context = context;
        this.plantaList = plantaList;
    }

    public PlantaAdapter(Context context, List<Planta> plantaList, OnPlantaListener listener) {
        this.context = context;
        this.plantaList = plantaList;
        this.listener = listener;
    }

    public void actualizarLista(List<Planta> nuevaLista) {
        this.plantaList = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_planta, parent, false);
        return new PlantaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantaViewHolder holder, int position) {
        Planta planta = plantaList.get(position);
        
        if (holder.tvNombre != null) holder.tvNombre.setText(planta.getNombre());
        if (holder.tvEspecie != null) holder.tvEspecie.setText(planta.getEspecie() + " • " + planta.getUbicacion());
        if (holder.tvRiego != null) holder.tvRiego.setText("Riego cada " + planta.getFrecuenciaRiegoDias() + " días | Siembra: " + planta.getFechaSiembra());

        if (holder.ivPlant != null) {
            String url = "";
            String especieLower = planta.getEspecie().toLowerCase();
            
            if (especieLower.contains("monstera")) {
                url = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=600&q=80";
            } else if (especieLower.contains("spathiphyllum") || especieLower.contains("lirio")) {
                url = "https://images.unsplash.com/photo-1593482834024-f513f56d0d5d?auto=format&fit=crop&w=600&q=80";
            } else if (especieLower.contains("sansevieria")) {
                url = "https://images.unsplash.com/photo-1509423350716-97f9360b4e09?auto=format&fit=crop&w=600&q=80";
            } else if (especieLower.contains("ficus")) {
                url = "https://images.unsplash.com/photo-1525498128493-380d1990a112?auto=format&fit=crop&w=600&q=80";
            } else if (especieLower.contains("phalaenopsis") || especieLower.contains("orquídea")) {
                url = "https://images.unsplash.com/photo-1525310072745-f49212b5ac6d?auto=format&fit=crop&w=600&q=80";
            } else if (especieLower.contains("epipremnum") || especieLower.contains("potus")) {
                url = "https://images.unsplash.com/photo-1596547609652-9cf5d8d76921?auto=format&fit=crop&w=600&q=80";
            }
            
            int defaultImage = planta.getNombre().toLowerCase().contains("helecho") ? R.drawable.helecho : R.drawable.suculenta;

            if (!url.isEmpty()) {
                com.bumptech.glide.Glide.with(context)
                        .load(url)
                        .placeholder(defaultImage)
                        .error(defaultImage)
                        .centerCrop()
                        .into(holder.ivPlant);
            } else {
                holder.ivPlant.setImageResource(defaultImage);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlantaClick(planta);
            } else {
                Intent intent = new Intent(context, PlantaDetalleActivity.class);
                intent.putExtra("planta_id", planta.getIdPlanta());
                context.startActivity(intent);
            }
        });

        if (holder.btnEditar != null) {
            holder.btnEditar.setOnClickListener(v -> {
                if (listener != null) listener.onEditarClick(planta);
            });
        }

        if (holder.btnEliminar != null) {
            holder.btnEliminar.setOnClickListener(v -> {
                if (listener != null) listener.onEliminarClick(planta);
            });
        }
    }

    @Override
    public int getItemCount() {
        return plantaList.size();
    }

    public static class PlantaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEspecie, tvRiego;
        ImageView ivPlant, btnEditar, btnEliminar;

        public PlantaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePlantaItem);
            tvEspecie = itemView.findViewById(R.id.tvEspeciePlantaItem);
            tvRiego = itemView.findViewById(R.id.tvRiegoPlantaItem);
            ivPlant = itemView.findViewById(R.id.ivPlantaItem);
            btnEditar = itemView.findViewById(R.id.btnEditarPlanta);
            btnEliminar = itemView.findViewById(R.id.btnEliminarPlanta);
        }
    }
}
