package io.github.yazdipour.ketabdlr.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;
import io.github.yazdipour.ketabdlr.utils.StringUtils;

public class SearchActivity extends AppCompatActivity {
    private List<Book> books = new ArrayList<>();
    private ArrayAdapter<Book> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        swipeRefreshLayout = findViewById(R.id.sr);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
        SearchView searchView = findViewById(R.id.sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!StringUtils.isNullOrEmpty(query.trim())) search(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //setup Recycler view
        ListView recyclerView = findViewById(R.id.rv);
        adapter = new ArrayAdapter<Book>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, books) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text2.setMaxLines(3);
                text2.setTextColor(Color.parseColor("#666666"));
                try {
                    text1.setText(books.get(position).getName());
                    text2.setText(books.get(position).getAuthor());
                } catch (Exception ignored) {
                }
                return view;
            }
        };
        recyclerView.setOnItemClickListener((parent, view, position, id) -> openBookActivity(position));
        recyclerView.setAdapter(adapter);
        //setup image view
        ImageView imageView = findViewById(R.id.imageView);
        Drawable[] array = new Drawable[]{getDrawable(R.drawable.logo_g), getDrawable(R.drawable.logo_o), getDrawable(R.drawable.logo_r), getDrawable(R.drawable.logo_b)};
        TransitionDrawable transitionDrawable = new TransitionDrawable(array);
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(5000);
    }

    private void search(String query) {
        books.clear();
        swipeRefreshLayout.setRefreshing(true);
        ApiHandler.getApi(this).getSearch(query, 1, (e, result) -> {
            try {
                if (e != null) throw e;
                List<Book> newBooks = KetabParser.BookListElementToBooks(result.getResult(),
                        result.getRequest().getHeaders().get("Cookie"));
                books.addAll(newBooks);
                adapter.notifyDataSetChanged();
            } catch (Exception e1) {
                Toast.makeText(SearchActivity.this, "Error", Toast.LENGTH_SHORT).show();
            } finally {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void openBookActivity(int position) {
        Intent i = new Intent(this, BookActivity.class);
        String data = new Gson().toJson(books.get(position));
        i.putExtra("data", data);
        startActivity(i);
    }
}
