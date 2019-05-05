package io.github.yazdipour.ketabdlr.viewmodels;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.io.File;

import androidx.core.app.NotificationCompat;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import io.github.yazdipour.ketabdlr.R;
import io.github.yazdipour.ketabdlr.models.Book;
import io.github.yazdipour.ketabdlr.services.Api;
import io.github.yazdipour.ketabdlr.services.KetabParser;
import io.github.yazdipour.ketabdlr.utils.FileUtils;
import io.github.yazdipour.ketabdlr.utils.ImageUtils;
import io.github.yazdipour.ketabdlr.utils.PermissionUtils;
import io.github.yazdipour.ketabdlr.utils.StringUtils;

public class BookViewModel {
    public Book book;

    public BookViewModel(Book book) {
        this.book = book;
    }

    public void loadWebView(Api api, WebView wv, TextView tv_time) {
        api.request(book.getUrl(), book.getCookie(),
                (e, result) -> {
                    if (e != null) e.printStackTrace();
                    else {
                        try {
                            KetabParser.BookPageToBook(book, result.getResult());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        if (!StringUtils.isNullOrEmpty(book.getDetails()))
                            wv.loadData(book.getDetails(), "text/html; charset=utf-8", "UTF-8");
                        if (StringUtils.isNullOrEmpty(book.getTimeToRead())) return;
                        tv_time.setText(book.getTimeToRead());
                        tv_time.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void loadImageCovers(ImageView imageView, ImageView imageView1) {
        Ion.with(imageView)
                .placeholder(R.drawable.logo_g)
                .error(R.drawable.logo_r)
                .load(book.getCover());
        Ion.with(imageView1)
                .fadeIn(true)
                .load(book.getCover());
    }

    public void download(Activity activity, CircularProgressButton btn, Book book) {
        if (!PermissionUtils.verifyStoragePermissions(activity)) return;
        File folder = FileUtils.getFolderCreateIfNotExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Ketab");
        btn.startMorphAnimation();
        Ion.with(activity)
                .load(book.getDownloadUrl())
                .setHeader("Cookie", book.getCookie())
                .progress((downloaded, total) ->
                        Log.d("Download>>>", "Progress: " + downloaded / total))
                .write(new File(folder + "/" + book.getFileName()))
                .setCallback((e, file) -> {
                    try {
                        if (e != null) throw new NetworkErrorException();
                        Toast.makeText(activity, activity.getString(R.string.download_successfully), Toast.LENGTH_SHORT).show();
                        btn.doneLoadingAnimation(Color.parseColor("#A3CB38"), ImageUtils.drawableToBitmap(activity.getDrawable(R.drawable.ic_check_black_24dp)));
                        book.setFilePath(file.getAbsolutePath());
                        book.setSha1(FileUtils.getFileSha1(file));
                        Intent pdfIntent = FileUtils.getPdfIntent(file);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "Download")
                                .setContentTitle(activity.getString(R.string.download_successfully))
                                .setContentText(file.getCanonicalPath())
                                .setSmallIcon(R.drawable.logo_o)
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(activity, 0, pdfIntent, PendingIntent.FLAG_ONE_SHOT));
                        ((NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE))
                                .notify(book.getId(), builder.build());
                        activity.startActivity(pdfIntent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        if (e instanceof NetworkErrorException) {
                            btn.startMorphRevertAnimation();
                            Toast.makeText(activity, activity.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
