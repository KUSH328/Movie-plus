package org.notabug.lifeuser.movieplus.adapter;

import android.content.Intent;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.notabug.lifeuser.movieplus.R;
import org.notabug.lifeuser.movieplus.activity.DetailActivity;

import java.util.ArrayList;

/*
* This class is quite similar to the MovieBaseAdapter.
* The main difference is the orientation, which needs the RecyclerView.
* This class is primarily used for the "Similar Movies" list in DetailActivity.
*/

// TODO: Currently, this class is almost the same as ShowBaseAdapter, it would be nice if they could be merged.
public class SimilarMovieBaseAdapter extends RecyclerView.Adapter<SimilarMovieBaseAdapter.MovieItemViewHolder> {

	ArrayList<JSONObject> similarMovieList;
	public Context context;
	
	// Create the adapter with the list of similar shows and the context.
	public SimilarMovieBaseAdapter(ArrayList<JSONObject> similarMovieList,
			Context context) {
		this.similarMovieList = similarMovieList;
		this.context = context;
	}

	// Views that each CardItem will contain.
	public static class MovieItemViewHolder extends RecyclerView.ViewHolder {
		CardView cardView;
		TextView movieTitle;
		ImageView movieImage;

		MovieItemViewHolder(View itemView) {
			super(itemView);
			cardView = (CardView) itemView.findViewById(R.id.cardView);
			movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
			movieImage = (ImageView) itemView.findViewById(R.id.movieImage);
		}
	}

	@Override
	public int getItemCount() {
		// Return the amount of items in the list.
		return similarMovieList.size();
	}

	@Override
	public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// Create a new CardItem when needed.
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.movie_card, parent, false);
		MovieItemViewHolder movieItemViewHolder = new MovieItemViewHolder(view);
		return movieItemViewHolder;
	}

	@Override
	public void onBindViewHolder(MovieItemViewHolder holder, int position) {
		// Fill the views with the needed data.
		final JSONObject movieData = similarMovieList.get(position);

		try {
			// Depending if it is a movie or a series, it needs to get the title or name.
			String title;
			if(movieData.has("title")) {
				title = "title";
			} else {
				title = "name";
			}

			holder.movieTitle.setText(movieData.getString(title));

			// Either show the poster or an icon indicating that the poster is not available.
			if(movieData.getString("poster_path") == null) {
				holder.movieImage.setImageDrawable
						(context.getResources().getDrawable(R.drawable.image_broken_variant));
			} else {
				Picasso.with(context).load("https://image.tmdb.org/t/p/w300" +
						movieData.getString("poster_path"))
						.into(holder.movieImage);
			}

			// Quickly fade in the poster when loaded.
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.fade_in_fast);
			holder.movieImage.startAnimation(animation);
		} catch(JSONException je) {
			je.printStackTrace();
		}

		// Send the movie data and the user to DetailActivity when clicking on a card.
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), DetailActivity.class);
				intent.putExtra("movieObject", movieData.toString());
				if(movieData.has("name")) {
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

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}
}
