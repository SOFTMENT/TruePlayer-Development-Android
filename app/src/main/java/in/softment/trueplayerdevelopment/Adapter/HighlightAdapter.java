package in.softment.trueplayerdevelopment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.softment.trueplayerdevelopment.Fragment.HomeFragment;
import in.softment.trueplayerdevelopment.Model.HighlightModel;
import in.softment.trueplayerdevelopment.PlayLandscapeVideoActivity;
import in.softment.trueplayerdevelopment.R;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.ViewHolder> {
    private HomeFragment homeFragment;
    private final ArrayList<HighlightModel> highlightModels;
    private Context context;

    public HighlightAdapter(Context context,HomeFragment homeFragment, ArrayList<HighlightModel> highlightModels){
        this.context = context;
        this.homeFragment = homeFragment;
        this.highlightModels = highlightModels;
    }

    @NonNull
    @Override
    public HighlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hightlight_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightAdapter.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            HighlightModel highlightModel = highlightModels.get(position);
            if (!highlightModel.thumbnail.isEmpty()) {
                Glide.with(homeFragment.getContext()).load(highlightModel.thumbnail).placeholder(R.drawable.placeholder).into(holder.highlightImage);

            }
            holder.highlightBy.setText("By \""+highlightModel.getUserName()+"\"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayLandscapeVideoActivity.class);
                    intent.putExtra("link",highlightModel.getVideoUrl());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        if (highlightModels.size() > 0) {
            homeFragment.no_highlights_available.setVisibility(View.GONE);
        }
        else {
            homeFragment.no_highlights_available.setVisibility(View.VISIBLE);
        }

        return highlightModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView highlightImage;
        private TextView  highlightBy;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            this.highlightBy = itemView.findViewById(R.id.highlightby);
            this.highlightImage = itemView.findViewById(R.id.highlightimage);
        }
    }
}
