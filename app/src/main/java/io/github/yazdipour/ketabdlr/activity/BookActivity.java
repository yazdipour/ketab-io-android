package io.github.yazdipour.ketabdlr.activity;

import android.os.Bundle;

import com.koushikdutta.async.future.FutureCallback;

import androidx.appcompat.app.AppCompatActivity;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;

public class BookActivity extends AppCompatActivity {
    Book book = new Book();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        seUpUI();
        ApiHandler.getApi(this).request(book.getUrl(), new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) e.printStackTrace();
                else {
                    book = KetabParser.BookPageToBook(book, result);
                    seUpUI();
                }
            }
        });
    }

    private void seUpUI() {

    }
}
