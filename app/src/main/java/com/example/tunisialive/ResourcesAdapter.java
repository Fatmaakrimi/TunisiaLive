package com.example.tunisialive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder> {

    private List<Resource> resources;
    private Context context;

    public ResourcesAdapter(Context context, List<Resource> resources, PreferencesManager preferencesManager) {
        this.context = context;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.name.setText(resource.getName());

        // Set logo based on URL
        String source = resource.getUrl().toLowerCase();
        if (source.contains("mosaiquefm")) {
            holder.logo.setImageResource(R.drawable.mosaique_logo);
        } else if (source.contains("kapitalis")) {
            holder.logo.setImageResource(R.drawable.kapitalis_logo);
        } else if (source.contains("alchourouk")) {
            holder.logo.setImageResource(R.drawable.alchourouk_logo);
        } else if (source.contains("hakaekonline")) {
            holder.logo.setImageResource(R.drawable.hakaekonline_logo);
        } else if (source.contains("lapresse.tn")) {
            holder.logo.setImageResource(R.drawable.lapresse_tn_logo);
        } else if (source.contains("tuniscope")) {
            holder.logo.setImageResource(R.drawable.tuniscope_logo);
        } else if (source.contains("business")) {
            holder.logo.setImageResource(R.drawable.businessnews_logo);
        } else if (source.contains("tunisienumerique")) {
            holder.logo.setImageResource(R.drawable.tunisienumerique_logo);
        } else if (source.contains("leaders")) {
            holder.logo.setImageResource(R.drawable.leader_logo);
        } else if (source.contains("babnet")) {
            holder.logo.setImageResource(R.drawable.babnet_logo);
        } else if (source.contains("africanmanager")) {
            holder.logo.setImageResource(R.drawable.africanmanager_logo);
        } else if (source.contains("jawharafm")) {
            holder.logo.setImageResource(R.drawable.jawharafm_logo);
        } else if (source.contains("radioexpressfm")) {
            holder.logo.setImageResource(R.drawable.expressfm_logo);
        } else if (source.contains("arabesque")) {
            holder.logo.setImageResource(R.drawable.arabesque_logo);
        }
        // Open resource articles activity on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ResourceArticlesActivity.class);
            intent.putExtra("RESOURCE_URL", resource.getUrl());
            intent.putExtra("RESOURCE_NAME", resource.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.resource_name);
            logo = itemView.findViewById(R.id.resource_logo);
        }
    }
}
