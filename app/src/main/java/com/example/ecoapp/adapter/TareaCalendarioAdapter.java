package com.example.ecoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.R;
import com.example.ecoapp.model.Planta;

import java.util.List;

public class TareaCalendarioAdapter extends RecyclerView.Adapter<TareaCalendarioAdapter.TareaViewHolder> {

    private final Context context;
    private final List<Planta> tareasList;

    public TareaCalendarioAdapter(Context context, List<Planta> tareasList) {
        this.context = context;
        this.tareasList = tareasList;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea_calendario, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Planta tarea = tareasList.get(position);

        holder.tvNombre.setText(tarea.getNombre());
        holder.tvTipo.setText("💧 Regar planta");

        // Asignar icono con URL usando lógica dinámica
        String url = "";
        String especieLower = tarea.getEspecie().toLowerCase();
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

        int defaultImage = tarea.getEspecie().toLowerCase().contains("helecho") ? R.drawable.helecho : R.drawable.suculenta;

        if (!url.isEmpty()) {
            com.bumptech.glide.Glide.with(context)
                    .load(url)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .centerCrop()
                    .into(holder.ivIcono);
        } else {
            holder.ivIcono.setImageResource(defaultImage);
        }

        // Tachar texto cuando se marque el checkbox
        holder.cbHecho.setOnCheckedChangeListener(null); // Reset
        holder.cbHecho.setChecked(false);
        holder.tvNombre.setPaintFlags(holder.tvNombre.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.tvNombre.setTextColor(Color.parseColor("#2E7D32"));

        holder.cbHecho.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.tvNombre.setPaintFlags(holder.tvNombre.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvNombre.setTextColor(Color.parseColor("#888888"));
                Toast.makeText(context, "✅ Tarea completada", Toast.LENGTH_SHORT).show();
            } else {
                holder.tvNombre.setPaintFlags(holder.tvNombre.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.tvNombre.setTextColor(Color.parseColor("#2E7D32"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareasList.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcono;
        TextView tvNombre, tvTipo;
        CheckBox cbHecho;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcono = itemView.findViewById(R.id.ivIconoTarea);
            tvNombre = itemView.findViewById(R.id.tvNombrePlantaCal);
            tvTipo = itemView.findViewById(R.id.tvTipoTareaCal);
            cbHecho = itemView.findViewById(R.id.cbHecho);
        }
    }
}
