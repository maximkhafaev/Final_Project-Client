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

public class AdapterFin extends RecyclerView.Adapter<AdapterFin.ViewHolder> {

    private final Interface_RV rv_fin_interface;
    private List<Test_Info> data;

    public AdapterFin(Interface_RV rv_fin_interface, List<Test_Info> data){
        this.rv_fin_interface = rv_fin_interface;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_model_fin, parent, false);
        return new ViewHolder(view, rv_fin_interface, data);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String fin_testname = data.get(position).getName();
        holder.text_testname.setText(fin_testname);

        int user_result = data.get(position).getResult();
        holder.userResult.setText(String.valueOf(user_result));

        int max_result = data.get(position).getMaxRes();
        holder.maxResult.setText(String.valueOf(max_result));

        int required_result = data.get(position).getReqRes();

        if (user_result >= required_result) {
            holder.isSuccessful.setTextColor(0xff669900);
            holder.isSuccessful.setText("Успешно");
        } else {
            holder.isSuccessful.setTextColor(0xffcc0000);
            holder.isSuccessful.setText("Неуспешно");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_testname;
        TextView isSuccessful;
        TextView userResult;
        TextView maxResult;

        public ViewHolder(@NonNull View itemView, Interface_RV rv_fin_interface, List<Test_Info> data) {
            super(itemView);
            text_testname = itemView.findViewById(R.id.finRV_testname);
            isSuccessful = itemView.findViewById(R.id.finRV_testStatus);
            userResult = itemView.findViewById(R.id.finRV_user_res_num);
            maxResult = itemView.findViewById(R.id.finRV_max_res);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rv_fin_interface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            rv_fin_interface.onItemClickFINISHED(data, pos);
                        }
                    }
                }
            });
        }
    }
}
