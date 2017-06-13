package id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Adapter.SavedAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl525.moviehd.Model.Saved;

/**
 * Created by PC on 6/13/2017.
 */

public class SavedActivity extends AppCompatActivity {
    private RecyclerView rv;
    private DatabaseHelper db;
    private SavedAdapter savedAdapter;
    private ArrayList<Saved> saved = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved);

        db = new DatabaseHelper(this);
        rv = (RecyclerView) findViewById(R.id.recyclerViewSaved);
        rv.setLayoutManager(new LinearLayoutManager(this));
        savedAdapter = new SavedAdapter(this, saved);
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        saved.clear();
        Cursor c = db.getAllMovie();
        while (c.moveToNext()) {
            String title = c.getString(1);
            String desc = c.getString(2);

            Saved sve = new Saved(title, desc);
            saved.add(sve);

            if (!(saved.size() < 1)) {
                rv.setAdapter(savedAdapter);
            } else {
                rv.setVisibility(View.GONE);
            }
        }
    }
}

