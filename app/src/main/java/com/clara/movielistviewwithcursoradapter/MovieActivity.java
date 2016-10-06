package com.clara.movielistviewwithcursoradapter;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

public class MovieActivity extends AppCompatActivity implements MovieCursorAdapter.RatingChangedListener {

	private static final String TAG = "MOVIE ACTIVITY";
	DatabaseManager dbManager;
	MovieCursorAdapter cursorListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);

		dbManager = new DatabaseManager(this);

		final EditText newMovieNameET = (EditText) findViewById(R.id.add_movie_name);
		final RatingBar newMovieRB = (RatingBar) findViewById(R.id.add_movie_rating_bar);
		Button addNew = (Button) findViewById(R.id.add_movie_button);
		addNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = newMovieNameET.getText().toString();
				float rating = newMovieRB.getRating();
				dbManager.addMovie(name, rating); // TODO: detect errors and handle appropriately
			}
		});


		final ListView movieList = (ListView) findViewById(R.id.movie_list_view);

		//create cursor
		Cursor cursor = dbManager.getAllMovies();
		//create CursorAdapter
		cursorListAdapter = new MovieCursorAdapter(this, cursor, true);
		//Configure ListView to use this adapter
		movieList.setAdapter(cursorListAdapter);

		addNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = newMovieNameET.getText().toString();
				float rating = newMovieRB.getRating();
				dbManager.addMovie(name, rating);
				cursorListAdapter.changeCursor(dbManager.getAllMovies());

				//TODO Add this movie to the database
				//TODO Update list

			}
		});

	}

	public void notifyRatingChanged(int movieID, float rating) {

		//Update DB, and then update the cursor for the ListView if necessary.
		dbManager.updateRating(movieID, rating);

		//makes sure the list and db are in sync
		cursorListAdapter.changeCursor(dbManager.getAllMovies());
	}


	//Don't forget these! Close and re-open DB as Activity pauses/resumes.

	@Override
	protected void onPause(){
		super.onPause();
		dbManager.close();
	}

	@Override
	protected void onResume(){
		super.onResume();
		dbManager = new DatabaseManager(this);
	}
}
