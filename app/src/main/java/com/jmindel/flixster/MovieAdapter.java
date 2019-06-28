package com.jmindel.flixster;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jmindel.flixster.models.ImgConfig;
import com.jmindel.flixster.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPosterImage, ivBackdropImage;
        TextView tvTitle, tvOverview;
        LinearLayout llMovieItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            llMovieItem = itemView.findViewById(R.id.llMovieItem);
        }
    }

    ArrayList<Movie> movies;
    Context context;
    ImgConfig config;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    /**
     * Creates and inflates a new view.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the context and the inflater
        // LEARN: How are contexts used, and what do they signify?
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the movie view into the parent => create the view based on the layout file
        // LEARN: Which root would we be attaching to? The XML root, or something else? And when/why would we want to do that?
        // LEARN: How is a context related to a ViewGroup? Is everything a ViewGroup?
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // Return a new ViewHolder that contains the new view
        /* LEARN: What's the point of the ViewHolder? It sounds like it's just for accessing different parts of the view easily,
         *  but why is it standardized in the polymorphic hierarchy? How could it be a pattern if there doesn't seem like there's
         *  much else to store other than what the immediate consumer wants, and no business logic behind it?
         */
        return new ViewHolder(movieView);
    }

    /**
     * Binds an inflated view to a new item.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Fetch the selected movie
        Movie movie = movies.get(position);

        // Load data into ViewHolder
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // Based on current orientation, choose image to load and its container
        // LEARN: Likely bad practice--other better patterns here if we can't use an else to ensure type safety?
        String imageUrl = null;
        ImageView iv = null;
        int placeholderImg = 0;
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
            iv = holder.ivPosterImage;
            placeholderImg = R.drawable.flicks_movie_placeholder;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
            iv = holder.ivBackdropImage;
            placeholderImg = R.drawable.flicks_backdrop_placeholder;
        }

        // Insert image using Glide
        RequestOptions glideOptions = new RequestOptions()
                .transforms(new RoundedCorners(8))
                .placeholder(placeholderImg)
                .error(placeholderImg);
        Glide.with(context)
             .load(imageUrl)
             .apply(glideOptions)
             .into(iv);

        // Set up click events
        holder.llMovieItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                // Create a new intent
                // Add extras for movie info
                // Launch the intent
            }
        });
    }

    /**
     * Returns the total number of items in the list.
     * @return
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setConfig(ImgConfig config) {
        this.config = config;
    }
}
