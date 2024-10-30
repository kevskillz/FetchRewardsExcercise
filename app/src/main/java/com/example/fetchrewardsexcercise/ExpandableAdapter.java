package com.example.fetchrewardsexcercise;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableAdapter extends RecyclerView.Adapter<ExpandableAdapter.GroupViewHolder> {

    private final Map<Integer, List<Item>> groupedItems;
    private final Map<Integer, Boolean> expandedGroups = new HashMap<>();

    public ExpandableAdapter(Map<Integer, List<Item>> groupedItems) {
        this.groupedItems = groupedItems;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_id, parent, false);
        return new GroupViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Integer listId = (Integer) groupedItems.keySet().toArray()[position];
        List<Item> items = groupedItems.get(listId);
        holder.listIdTextView.setText("List ID: " + listId);

        holder.childContainer.removeAllViews();
        if (expandedGroups.getOrDefault(listId, false)) {
            for (Item item : items) {
                View childView = LayoutInflater.from(holder.childContainer.getContext())
                        .inflate(R.layout.item_entry, holder.childContainer, false);

                TextView itemIdTextView = childView.findViewById(R.id.itemIdTextView);
                TextView itemNameTextView = childView.findViewById(R.id.itemNameTextView);

                // Set ID and Name
                itemIdTextView.setText(String.valueOf(item.getId())); // Display ID
                itemNameTextView.setText(item.getName()); // Display Name

                holder.childContainer.addView(childView);
            }
            holder.childContainer.setVisibility(View.VISIBLE);
            holder.headerContainer.setVisibility(View.VISIBLE);
        } else {
            holder.childContainer.setVisibility(View.GONE);
            holder.headerContainer.setVisibility(View.GONE);

        }

        holder.listIdTextView.setOnClickListener(v -> {
            boolean isExpanded = expandedGroups.getOrDefault(listId, false);
            expandedGroups.put(listId, !isExpanded);
            notifyItemChanged(position);
        });
    }



    @Override
    public int getItemCount() {
        return groupedItems.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView listIdTextView;
        private final ViewGroup childContainer;
        private final ViewGroup headerContainer;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            listIdTextView = itemView.findViewById(R.id.listIdTextView);
            childContainer = itemView.findViewById(R.id.childContainer);
            headerContainer = itemView.findViewById(R.id.headerContainer);
        }
    }

}
