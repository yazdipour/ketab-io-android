package io.github.yazdipour.ketabdlr.views;

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

import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.utils.StringUtils;
import io.github.yazdipour.ketabdlr.viewmodels.SearchViewModel;

public class SearchActivity extends AppCompatActivity {
    private ArrayAdapter<Book> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        swipeRefreshLayout = findViewById(R.id.sr);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
        vm = new SearchViewModel(this);
        ((SearchView) findViewById(R.id.sv)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (!StringUtils.isNullOrEmpty(query.trim()))
                        vm.search(query.trim(), adapter, swipeRefreshLayout);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
                android.R.layout.simple_list_item_2, android.R.id.text1, vm.books) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text2.setMaxLines(3);
                text2.setTextColor(Color.parseColor("#666666"));
                try {
                    text1.setText(vm.books.get(position).getName());
                    text2.setText(vm.books.get(position).getAuthor());
                } catch (Exception ignored) {
                }
                return view;
            }
        };
        recyclerView.setOnItemClickListener((parent, view, position, id) -> vm.openBookActivity(position));
        recyclerView.setAdapter(adapter);
        //setup image view
        ImageView imageView = findViewById(R.id.imageView);
        Drawable[] array = new Drawable[]{getDrawable(R.drawable.logo_g), getDrawable(R.drawable.logo_o), getDrawable(R.drawable.logo_r), getDrawable(R.drawable.logo_b)};
        TransitionDrawable transitionDrawable = new TransitionDrawable(array);
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(5000);
    }
}
