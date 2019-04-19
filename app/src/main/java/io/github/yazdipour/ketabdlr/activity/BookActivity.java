package io.github.yazdipour.ketabdlr.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import androidx.appcompat.app.AppCompatActivity;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;
import io.github.yazdipour.ketabdlr.utils.StringUtils;

public class BookActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        String data = getIntent().getStringExtra("data");
        if (StringUtils.isNullOrEmpty(data)) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        book = new Gson().fromJson(data, Book.class);
        setupUI(book);
        ApiHandler.getApi(this).request(book.getUrl(), (e, result) -> {
            if (e != null) e.printStackTrace();
            else {
                try {
                    book = KetabParser.BookPageToBook(book, result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (!StringUtils.isNullOrEmpty(book.getDetails()))
                    fillWebView(book);
                if (StringUtils.isNullOrEmpty(book.getTimeToRead())) return;
                TextView tv_time = findViewById(R.id.tv_time);
                tv_time.setText(book.getTimeToRead());
                tv_time.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupUI(Book book) {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_dl).setOnClickListener(v -> startDownload(v, book));
        ((TextView) findViewById(R.id.tv_title)).setText(book.getName());
        ((TextView) findViewById(R.id.tv_author)).setText(book.getAuthor());
        if (!StringUtils.isNullOrEmpty(book.getYear())) {
            TextView tv_year = findViewById(R.id.tv_year);
            tv_year.setVisibility(View.VISIBLE);
            tv_year.setText(book.getYear());
        }
        Ion.with(findViewById(R.id.im_cover))
                .placeholder(R.drawable.logo_b)
                .error(R.drawable.logo_r)
                .load(book.getCover());
    }

    private void startDownload(View v, Book book) {
        if (v instanceof br.com.simplepass.loadingbutton.customViews.CircularProgressButton) {
            br.com.simplepass.loadingbutton.customViews.CircularProgressButton btn = (br.com.simplepass.loadingbutton.customViews.CircularProgressButton) v;
            btn.startMorphAnimation();
        }
        Toast.makeText(this, "Downloading!!!" + book.getDownloadUrl(), Toast.LENGTH_SHORT).show();
    }

    private void fillWebView(Book book) {
        WebView webView = findViewById(R.id.webView);
        webView.loadData(book.getDetails(), "text/html; charset=utf-8", "UTF-8");
    }
}
