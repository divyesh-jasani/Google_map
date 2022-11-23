package com.project.mapgoogle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.project.Constants;
import com.project.model.PlacesModel;

import java.util.ArrayList;

public class PlacesRVAdapter extends ListAdapter<PlacesModel, PlacesRVAdapter.ViewHolder> {

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<PlacesModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlacesModel>() {
        @Override
        public boolean areItemsTheSame(PlacesModel oldItem, PlacesModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(PlacesModel oldItem, PlacesModel newItem) {
            return oldItem.getPlaceName().equals(newItem.getPlaceName()) &&
                    oldItem.getPlaceAddress().equals(newItem.getPlaceAddress())
                    ;
        }
    };
    private OnItemClickListener listener;

    PlacesRVAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_rv, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacesModel model = getCourseAt(position);
        holder.courseNameTV.setText(model.getPlaceName());
        holder.courseDurationTV.setText(model.getPlaceAddress());
    }

    public PlacesModel getCourseAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(PlacesModel model, String clickType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTV, courseDurationTV;
        ImageView ivEdit, ivDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing each view of our recycler view.
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
//            courseDescTV = itemView.findViewById(R.id.idTVCourseDescription);
            courseDurationTV = itemView.findViewById(R.id.idTVCourseDuration);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position), Constants.EDIT);
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position), Constants.DELETE);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position), Constants.VIEW_CLICK);
                    }
                }
            });
        }
    }
}