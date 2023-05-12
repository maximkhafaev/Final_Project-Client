package com.maximkhafaev.pilotquiz.Recycle_Views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Info;
import com.maximkhafaev.pilotquiz.R;

import java.util.List;

public class AdapterUnfin extends RecyclerView.Adapter<AdapterUnfin.ViewHolder> {

    private final Interface_RV rv_unfin_interface;
    private List<Test_Info> data;

    public AdapterUnfin(Interface_RV rv_unfin_interface, List<Test_Info> data) {
        this.rv_unfin_interface = rv_unfin_interface;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_model_unfin, parent, false);
        return new ViewHolder(view, rv_unfin_interface, data);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String testname = data.get(position).getName();
        holder.text_testname.setText(testname);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_testname;
        public ViewHolder(@NonNull View itemView, Interface_RV rv_unfin_interface, List<Test_Info> data) {
            super(itemView);

            text_testname = itemView.findViewById(R.id.unfinRV_testname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rv_unfin_interface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos !=  RecyclerView.NO_POSITION) {
                            rv_unfin_interface.onItemClick_UNFINISHED(data, pos);
                        }
                    }
                }
            });
        }
    }
}
