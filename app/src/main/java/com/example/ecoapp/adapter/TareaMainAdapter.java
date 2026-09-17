package com.example.ecoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
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

public class TareaMainAdapter extends RecyclerView.Adapter<TareaMainAdapter.TareaMainViewHolder> {

    public interface OnTareaCompletadaListener {
        void onTareaCompletada(Planta tarea, int position);
        void onEstablecerAlarma(Planta tarea);
    }

    private final Context context;
    private final List<Planta> tareasList;
    private final OnTareaCompletadaListener listener;

    public TareaMainAdapter(Context context, List<Planta> tareasList, OnTareaCompletadaListener listener) {
        this.context = context;
        this.tareasList = tareasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea_calendario, parent, false);
        return new TareaMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaMainViewHolder holder, int position) {
        Planta tarea = tareasList.get(position);

        holder.tvNombre.setText(tarea.getNombre());
        holder.tvTipo.setText("📍 " + tarea.getUbicacion());

        // Asignar icono avatar según especie
        if (tarea.getEspecie().toLowerCase().contains("monstera") || tarea.getEspecie().toLowerCase().contains("helecho")) {
            holder.ivIcono.setImageResource(R.drawable.helecho);
        } else {
            holder.ivIcono.setImageResource(R.drawable.suculenta);
        }

        // Tachar texto cuando se marque el checkbox y ocultar
        holder.cbHecho.setOnCheckedChangeListener(null); // Reset
        holder.cbHecho.setChecked(false);
        holder.tvNombre.setPaintFlags(holder.tvNombre.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.tvNombre.setTextColor(Color.parseColor("#2E7D32"));

        holder.cbHecho.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.tvNombre.setPaintFlags(holder.tvNombre.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvNombre.setTextColor(Color.parseColor("#888888"));
                
                // Retraso de 1 segundo para mostrar el tachado antes de eliminar de la lista y actualizar BD
                new Handler().postDelayed(() -> {
                    if (listener != null) {
                        listener.onTareaCompletada(tarea, holder.getAdapterPosition());
                    }
                }, 800);
            }
        });

        // Configurar reloj / alarma
        holder.tvReloj.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEstablecerAlarma(tarea);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareasList.size();
    }

    public static class TareaMainViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcono;
        TextView tvNombre, tvTipo, tvReloj;
        CheckBox cbHecho;

        public TareaMainViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcono = itemView.findViewById(R.id.ivIconoTarea);
            tvNombre = itemView.findViewById(R.id.tvNombrePlantaCal);
            tvTipo = itemView.findViewById(R.id.tvTipoTareaCal);
            tvReloj = itemView.findViewById(R.id.btnRelojAlarma);
            cbHecho = itemView.findViewById(R.id.cbHecho);
        }
    }
}
