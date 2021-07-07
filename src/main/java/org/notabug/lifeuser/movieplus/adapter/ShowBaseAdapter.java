package org.notabug.lifeuser.movieplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.notabug.lifeuser.movieplus.MovieDatabaseHelper;
import org.notabug.lifeuser.movieplus.R;
import org.notabug.lifeuser.movieplus.activity.DetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowBaseAdapter extends RecyclerView.Adapter<ShowBaseAdapter.ShowItemViewHolder> {
    private ArrayList<JSONObject> mShowArrayList;
    private HashMap<String, String> mGenreHashMap;

    private Context context;

    private String genreType;

    private boolean mGridView;

    // API key names
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "backdrop_path";
    public static final String KEY_POSTER = "poster_path";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "overview";
    public static final String KEY_RATING = "vote_average";
    public static final String KEY_GENRES = "genre_ids";
    public static final String KEY_RELEASE_DATE = "release_date";
    public static final String KEY_CATEGORIES = MovieDatabaseHelper.COLUMN_CATEGORIES;

    /**
     * Sets up the adapter with the necessary configurations.
     * @param showList the list of shows that will be shown.
     * @param genreList the mapping of ids to genres.
     * @param gridView if the user wants the shows displayed in a grid or a list.
     */
    public ShowBaseAdapter(ArrayList<JSONObject> showList,
                           HashMap<String, String> genreList, boolean gridView) {
        // Get the right "type" of genres.
        if (genreType == "2" || genreType == null) {
            this.genreType = null;
        } else if (genreType == "1") {
            this.genreType = "movie";
        } else {
            this.genreType = "tv";
        }

        mShowArrayList = showList;
        mGenreHashMap = genreList;

        mGridView = gridView;
    }

    /**
     * The View of every item that is displayed in the grid/list.
     */
    public static class ShowItemViewHolder extends RecyclerView.ViewHolder {
        CardView showView;
        TextView showTitle;
        ImageView showImage;
        TextView showDescription;
        TextView showGenre;
        RatingBar showRating;
        View categoryColorView;

        ShowItemViewHolder(View itemView) {
            super(itemView);
            showView = (CardView) itemView.findViewById(R.id.cardView);
            showTitle = (TextView) itemView.findViewById(R.id.title);
            showImage = (ImageView) itemView.findViewById(R.id.image);
            categoryColorView = itemView.findViewById(R.id.categoryColor);

            // Only used if presented in a list.
            showDescription = (TextView) itemView.findViewById(R.id.description);
            showGenre = (TextView) itemView.findViewById(R.id.genre);
            showRating = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }

    @Override
    public int getItemCount() {
        // Return the amount of items in the list.
        return mShowArrayList.size();
    }

    @Override
    public ShowItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new CardItem when needed.
        View view;
        if(mGridView) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.show_grid_card, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.show_card, parent, false);
        }
        ShowItemViewHolder showItemViewHolder = new ShowItemViewHolder(view);
        return showItemViewHolder;
    }

    @Override
    public void onBindViewHolder(ShowItemViewHolder holder, int position) {
        // Fill the views with the needed data.
        final JSONObject showData = mShowArrayList.get(position);

        Context context = holder.showView.getContext();

        // Fills the views with show details.
        try {
            // Load the thumbnail with Picasso.
            if(showData.getString(KEY_POSTER).equals("null")) {
                holder.showImage.setImageDrawable
                        (context.getResources().getDrawable(R.drawable.image_broken_variant));
            } else {
                Picasso.with(context).load("https://image.tmdb.org/t/p/w342"
                        + showData.getString(KEY_POSTER)).into(holder.showImage);
            }

            // Check if the object has "title" if not,
            // it is a series and "name" is used.
            String name = (showData.has(KEY_TITLE)) ?
                    showData.getString(KEY_TITLE) : showData.getString(KEY_NAME);

            // Set the title and description.
            holder.showTitle.setText(name);

            // Set the right category color if available.
            if(showData.has(KEY_CATEGORIES)) {
                int green, blue, purple, yellow, red;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    green = context.getColor(R.color.colorGreen);
                    blue = context.getColor(R.color.colorBlue);
                    purple = context.getColor(R.color.colorPurple);
                    yellow = context.getColor(R.color.colorYellow);
                    red = context.getColor(R.color.colorRed);
                } else {
                    green = context.getResources().getColor(R.color.colorGreen);
                    blue = context.getResources().getColor(R.color.colorBlue);
                    purple = context.getResources().getColor(R.color.colorPurple);
                    yellow = context.getResources().getColor(R.color.colorYellow);
                    red = context.getResources().getColor(R.color.colorRed);
                }

                switch (showData.getInt(KEY_CATEGORIES)) {
                    case 0:
                        // This is the plan to watch category.
                        holder.categoryColorView.setBackgroundColor(purple);
                        break;
                    case 1:
                        // This is the watched category.
                        holder.categoryColorView.setBackgroundColor(blue);
                        break;
                    case 2:
                        // This is the watching category.
                        holder.categoryColorView.setBackgroundColor(green);
                        break;
                    case 3:
                        // This is the on hold category.
                        holder.categoryColorView.setBackgroundColor(yellow);
                        break;
                    case 4:
                        // This is the dropped category.
                        holder.categoryColorView.setBackgroundColor(red);
                        break;
                    default:
                        holder.categoryColorView.setBackgroundColor(green);
                        break;
                }
                holder.categoryColorView.setVisibility(View.VISIBLE);
            }

            // Only if the shows are presented in a list.
            if(!mGridView) {
                holder.showDescription.setText(showData.getString(KEY_DESCRIPTION));

                // Divide the rating in two so it fits in the five stars.
                holder.showRating.setRating(Float.parseFloat(showData.getString(KEY_RATING)) / 2);

                // Remove the [ and ] from the String
                String genreIds = showData.getString(KEY_GENRES)
                        .substring(1, showData.getString(KEY_GENRES)
                                .length() - 1);

                // Split the String with ids and set them into an array.
                String[] genreArray = genreIds.split(",");

                // Get sharedPreferences in case the genreHashMap doesn't have the value
                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        "GenreList", Context.MODE_PRIVATE);

                // Add all the genres in one String.
                String genreNames = "";
                for (int i = 0; i < genreArray.length; i++) {
                    if (mGenreHashMap.get(genreArray[i]) != null) {
                        genreNames += ", " + mGenreHashMap.get(genreArray[i]);
                    } else {
                        genreNames += ", " + sharedPreferences.getString(genreArray[i], "");
                    }
                }

                // Remove the first ", " from the string and set the text.
                holder.showGenre.setText(genreNames.substring(2));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        // Send the movie data and the user to DetailActivity when clicking on a card.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("movieObject", showData.toString());
                if(showData.has(KEY_NAME)) {
                    intent.putExtra("isMovie", false);
                }
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        // The id is the same as the position,
        // therefore returning the position is enough.
        return position;
    }
}
