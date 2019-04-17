package io.github.yazdipour.ketabdlr.activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;

import android.os.Bundle;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ApiHandler.getApi(this).getSearch("office", 1, new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) e.printStackTrace();
                else {
                    books.clear();
                    books.addAll(KetabParser.BookListElementToBooks(result));
                }
            }
        });
    }
}
