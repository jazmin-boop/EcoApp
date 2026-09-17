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

import com.bumptech.glide.Glide;
import com.example.ecoapp.LibraryPlantDetailActivity;
import com.example.ecoapp.R;
import com.example.ecoapp.model.LibraryPlant;

import java.util.List;

public class LibraryPlantAdapter extends RecyclerView.Adapter<LibraryPlantAdapter.LibViewHolder> {

    private final Context context;
    private final List<LibraryPlant> plantList;

    public LibraryPlantAdapter(Context context, List<LibraryPlant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public LibViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_plant, parent, false);
        return new LibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibViewHolder holder, int position) {
        LibraryPlant plant = plantList.get(position);
        holder.tvName.setText(plant.getNombre());
        holder.tvSpecies.setText(plant.getEspecie());

        // Cargar imagen con Glide desde URL o recurso local
        if (plant.getImageUrl() != null && !plant.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(plant.getImageUrl())
                    .placeholder(plant.getImageResId())
                    .error(plant.getImageResId())
                    .centerCrop()
                    .into(holder.ivPlant);
        } else {
            holder.ivPlant.setImageResource(plant.getImageResId());
        }

        holder.chip1.setText(plant.getTag1());
        holder.chip2.setText(plant.getTag2());
        
        if (plant.getTag3() != null && !plant.getTag3().isEmpty()) {
            holder.chip3.setVisibility(View.VISIBLE);
            holder.chip3.setText(plant.getTag3());
        } else {
            holder.chip3.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LibraryPlantDetailActivity.class);
            intent.putExtra("nombre", plant.getNombre());
            intent.putExtra("especie", plant.getEspecie());
            intent.putExtra("descripcion", plant.getDescripcion());
            intent.putExtra("origen", plant.getOrigen());
            intent.putExtra("riego", plant.getRiegoInfo());
            intent.putExtra("imagen", plant.getImageResId());
            intent.putExtra("imageUrl", plant.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class LibViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpecies, chip1, chip2, chip3;
        ImageView ivPlant;

        public LibViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvLibName);
            tvSpecies = itemView.findViewById(R.id.tvLibSpecies);
            chip1 = itemView.findViewById(R.id.chip1);
            chip2 = itemView.findViewById(R.id.chip2);
            chip3 = itemView.findViewById(R.id.chip3);
            ivPlant = itemView.findViewById(R.id.ivLibPlant);
        }
    }
}
