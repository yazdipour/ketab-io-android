package io.github.yazdipour.ketabdlr.activity;

import android.os.Bundle;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.Api;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;

public class MainActivity extends AppCompatActivity {
    private List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seUpUI();
        ApiHandler.getApi(this).request(Api.BASE_URL, new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) e.printStackTrace();
                else {
                    books.clear();
                    books.addAll(KetabParser.BookListElementToBooks(result));
                    fillListView();
                }
            }
        });
    }

    private void fillListView() {

    }

    private void seUpUI() {

    }
}
