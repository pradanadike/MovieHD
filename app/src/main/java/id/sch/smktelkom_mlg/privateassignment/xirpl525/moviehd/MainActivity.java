package id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Adapter.GridViewAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Model.Movie;
import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Service.API;
import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Service.GsonGetRequest;
import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Service.VolleyController;

public class MainActivity extends AppCompatActivity {

    GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Popular Movies",
                        "Highest Rate Movies",
                        "Now Playing Movies",
                        "Upcoming Movies",
                        "Saved",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    FetchMovies fetchMovies = new FetchMovies();
                    String url = "http://api.themoviedb.org/3/movie/popular";
                    fetchMovies.execute(url);
                } else if (position == 1) {
                    FetchMovies fetchMovies = new FetchMovies();
                    String url = "http://api.themoviedb.org/3/movie/top_rated";
                    fetchMovies.execute(url);
                } else if (position == 2) {
                    FetchMovies fetchMovies = new FetchMovies();
                    String url = "http://api.themoviedb.org/3/movie/now_playing";
                    fetchMovies.execute(url);
                } else if (position == 3) {
                    FetchMovies fetchMovies = new FetchMovies();
                    String url = "http://api.themoviedb.org/3/movie/upcoming";
                    fetchMovies.execute(url);
                }else if (position == 4) {
                    //hehe
                    startActivity(new Intent(MainActivity.this, SavedActivity.class));
                }

                getSupportFragmentManager().beginTransaction()
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        gv = (GridView) findViewById(R.id.movieitem_list);

        FetchMovies fetchMovies = new FetchMovies();
        String url = "http://api.themoviedb.org/3/movie/popular";
        fetchMovies.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        @Override
        protected void onPreExecute() {

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading movies...");
            progressDialog.show();
            super.onPreExecute();
        }

        protected String FetchMovies(String... params) {
            String url = "http://api.themoviedb.org/3/movie/popular";
            GsonGetRequest<Movie> myRequest = new GsonGetRequest<Movie>
                    (url, Movie.class, null, new Response.Listener<Movie>() {

                        @Override
                        public void onResponse(Movie response) {
                            Log.d("FLOW", "onResponse: " + (new Gson().toJson(response)));
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("FLOW", "onErrorResponse: ", error);
                        }
                    });
            VolleyController.getInstance(this).addToRequestQueue(myRequest);

            String sortingCriteria = params[0];
            Uri builtUri = Uri.parse(API.API_URL).buildUpon()
                    .appendQueryParameter("sort_by", sortingCriteria + ".desc")
                    .appendQueryParameter("api_key", API.API_KEY)
                    .build();
            String response;
            try {
                response = getJSON(builtUri);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public String getJSON(Uri builtUri) {
            InputStream inputStream;
            StringBuffer buffer;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJson = null;

            try {
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJson = buffer.toString();
            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }
            return moviesJson;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> data = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResult = "";
            try {
                final String BASE_URL = params[0];

                final String API_KEY = "?api_key=0de0302cadd562654ea7d9f0598e2df5";
                Uri builtUri = Uri.parse(BASE_URL + API_KEY).buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonResult = buffer.toString();
                JSONObject result = new JSONObject(jsonResult);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    JSONArray data_json = result.getJSONArray("results");
                    for (int i = 0; i < data_json.length(); i++) {
                        Movie movie = new Movie();
                        JSONObject object = data_json.getJSONObject(i);

                        movie.setId(object.getInt("id"));
                        movie.setPoster_path("http://image.tmdb.org/t/p/w185" + object.getString("poster_path"));
                        movie.setPoster_path("http://image.tmdb.org/t/p/w185" + object.getString("poster_path"));
                        movie.setBackdrop_path("http://image.tmdb.org/t/p/w780" + object.getString("backdrop_path"));
                        movie.setRelease_date(object.getString("release_date"));
                        movie.setTitle(object.getString("title"));
                        movie.setOverview(object.getString("overview"));
                        movie.setVote_average(object.getLong("vote_average"));
                        data.add(movie);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(final List<Movie> movies) {
            super.onPostExecute(movies);
            progressDialog.hide();
            GridViewAdapter gridViewAdapter = new GridViewAdapter(getApplicationContext(), movies);
            gv.setAdapter(gridViewAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = movies.get(position);
                    Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                    intent.putExtra("poster_path", movie.getPoster_path());
                    intent.putExtra("backdrop_path", movie.getBackdrop_path());
                    intent.putExtra("year", movie.getRelease_date());
                    intent.putExtra("release", movie.getRelease_date());
                    intent.putExtra("sinopsis", movie.getOverview());
                    intent.putExtra("title", movie.getTitle());
                    intent.putExtra("duration", movie.getVote_average());
                    intent.putExtra("id", movie.getId());

                    startActivity(intent);
                }
            });
        }
    }
}
