package io.github.yazdipour.ketabdlr.activity;

import android.accounts.NetworkErrorException;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.liulishuo.okdownload.DownloadTask;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.ApiHandler;
import io.github.yazdipour.ketabdlr.services.KetabParser;
import io.github.yazdipour.ketabdlr.utils.FileUtils;
import io.github.yazdipour.ketabdlr.utils.ImageUtils;
import io.github.yazdipour.ketabdlr.utils.NotificationSampleListener;
import io.github.yazdipour.ketabdlr.utils.NotificationUtils;
import io.github.yazdipour.ketabdlr.utils.PermissionUtils;
import io.github.yazdipour.ketabdlr.utils.StringUtils;

public class BookActivity extends AppCompatActivity {
    private Book book;
    private DownloadTask task;
    private NotificationSampleListener listener;

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
        ApiHandler.getApi(this).request(
                book.getUrl(),
                book.getCookie(),
                (e, result) -> {
                    if (e != null) e.printStackTrace();
                    else {
                        try {
                            book = KetabParser.BookPageToBook(book, result.getResult());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        if (!StringUtils.isNullOrEmpty(book.getDetails()))
                            ((WebView) findViewById(R.id.webView)).loadData(book.getDetails(), "text/html; charset=utf-8", "UTF-8");
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

//        initListener();
//        initTask();
    }

    private void startDownload(View v, Book book) {
        if (v instanceof CircularProgressButton) {
            CircularProgressButton btn = (CircularProgressButton) v;
            btn.setProgressType(ProgressType.DETERMINATE);
            btn.startMorphAnimation();
            if (!PermissionUtils.verifyStoragePermissions(this)) return;
            File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/" + book.getFileName());
            Ion.with(this)
                    .load(book.getDownloadUrl())
                    .setHeader("Cookie", book.getCookie())
                    .progress((downloaded, total) ->
                            runOnUiThread(() -> btn.setPaddingProgress((float) 100 * downloaded / total)))
                    .write(file1)
                    .setCallback((e, file) -> {
                        try {
                            if (e != null) throw new NetworkErrorException();
                            Toast.makeText(BookActivity.this, getString(R.string.download_successfully), Toast.LENGTH_SHORT).show();
                            btn.doneLoadingAnimation(Color.parseColor("#A3CB38"),
                                    ImageUtils.drawableToBitmap(getDrawable(R.drawable.ic_check_black_24dp)));
                            FileUtils.openPdf(BookActivity.this, file);
                            book.setSha1(FileUtils.getFileSha1(file));
                            NotificationUtils.build(this,
                                    BookActivity.class,
                                    getString(R.string.download_successfully),
                                    file.getParentFile().toString(),
                                    (int) System.currentTimeMillis());
                        } catch (Exception e1) {
                            if (e instanceof NetworkErrorException) {
                                btn.startMorphRevertAnimation();
                                Toast.makeText(BookActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initTask() {
        File xfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + book.getFileName());
        Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.put("Cookie", null);
        task = new DownloadTask
                .Builder(book.getDownloadUrl(), xfile)
                .setFilename(book.getFileName())
                .setPassIfAlreadyCompleted(false)
                .setMinIntervalMillisCallbackProcess(80)
                .setAutoCallbackToUIThread(false)
                .setHeaderMapFields(headerMap)
                .build();
    }

    private void initListener() {
        listener = new NotificationSampleListener(this);
        listener.attachTaskEndRunnable(() -> {
//                actionTv.setText(R.string.start);
//                actionView.setTag(null);
        });

        final Intent intent = new Intent(CancelReceiver.ACTION);
        final PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        listener.setAction(new NotificationCompat.Action(0, "Cancel", cancelPendingIntent));
        listener.initNotification();
    }

    static class CancelReceiver extends BroadcastReceiver {
        static final String ACTION = "cancelOkdownload";

        private DownloadTask task;

        CancelReceiver(@NonNull DownloadTask task) {
            this.task = task;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.task.cancel();
        }
    }
}
