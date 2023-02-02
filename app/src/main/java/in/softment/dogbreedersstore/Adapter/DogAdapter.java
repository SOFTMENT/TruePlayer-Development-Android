package in.softment.dogbreedersstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.softment.dogbreedersstore.Model.DogModel;
import in.softment.dogbreedersstore.R;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DogModel> dogModels;

    public DogAdapter(Context context,ArrayList<DogModel> dogModels) {
        this.context = context;
        this.dogModels = dogModels;
    }

    @NonNull
    @Override
    public DogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_layout_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DogAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dogModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
