package io.github.yazdipour.ketabdlr.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;
import io.github.yazdipour.ketabdlr.views.BookActivity;

public class SearchViewModel {
    private Activity activity;
    public List<Book> books = new ArrayList<>();

    public SearchViewModel(Activity activity) {
        this.activity = activity;
    }

    public void openBookActivity(int position) {
        Intent i = new Intent(activity, BookActivity.class);
        i.putExtra("data", new Gson().toJson(books.get(position)));
        activity.startActivity(i);
    }

    public void search(String query, ArrayAdapter<Book> adapter, SwipeRefreshLayout swipeRefreshLayout) throws UnsupportedEncodingException {
        query = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        books.clear();
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        ApiHandler.getApi(activity).getSearch(query, 1, (e, result) -> {
            try {
                if (e != null) throw e;
                List<Book> newBooks = KetabParser.BookListElementToBooks(result.getResult(),
                        result.getRequest().getHeaders().get("Cookie"));
                books.addAll(newBooks);
                adapter.notifyDataSetChanged();
            } catch (Exception e1) {
                Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
            } finally {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
