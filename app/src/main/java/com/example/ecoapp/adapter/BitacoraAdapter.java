package com.example.ecoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoapp.R;
import com.example.ecoapp.model.BitacoraRiego;

import java.util.List;

public class BitacoraAdapter extends RecyclerView.Adapter<BitacoraAdapter.BitacoraViewHolder> {

    private List<BitacoraRiego> bitacoraList;

    public BitacoraAdapter(List<BitacoraRiego> bitacoraList) {
        this.bitacoraList = bitacoraList;
    }

    @NonNull
    @Override
    public BitacoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bitacora, parent, false);
        return new BitacoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BitacoraViewHolder holder, int position) {
        BitacoraRiego riego = bitacoraList.get(position);
        holder.tvFecha.setText(riego.getFechaHora());
        String lluviaStr = riego.getLlovio() == 1 ? "Llovió 🌧️" : "Sin lluvia ☀️";
        holder.tvClima.setText("Temp: " + riego.getTemperaturaMomento() + "°C • " + lluviaStr);
    }

    @Override
    public int getItemCount() {
        return bitacoraList.size();
    }

    public static class BitacoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvClima;

        public BitacoraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFechaRiego);
            tvClima = itemView.findViewById(R.id.tvDetalleClima);
        }
    }
}
