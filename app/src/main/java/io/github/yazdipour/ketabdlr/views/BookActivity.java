package io.github.yazdipour.ketabdlr.views;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.utils.StringUtils;
import io.github.yazdipour.ketabdlr.viewmodels.BookViewModel;

public class BookActivity extends AppCompatActivity {
    private BookViewModel vm;

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
        Book book = new Gson().fromJson(data, Book.class);
        vm = new BookViewModel(book);
        setupUI(book);
        TextView tv_time = findViewById(R.id.tv_time);
        WebView wv = findViewById(R.id.webView);
        vm.loadWebView(ApiHandler.getApi(this), wv, tv_time);
    }

    private void setupUI(Book book) {
        findViewById(R.id.btn_dl).setOnClickListener(v -> vm.download(this, (CircularProgressButton) v, book));
        ((TextView) findViewById(R.id.tv_title)).setText(book.getName());
        ((TextView) findViewById(R.id.tv_author)).setText(book.getAuthor());
        if (!StringUtils.isNullOrEmpty(book.getYear())) {
            TextView tv_year = findViewById(R.id.tv_year);
            tv_year.setVisibility(View.VISIBLE);
            tv_year.setText(book.getYear());
        }
        vm.loadImageCovers(findViewById(R.id.im_cover),findViewById(R.id.im_bg));
    }
}
