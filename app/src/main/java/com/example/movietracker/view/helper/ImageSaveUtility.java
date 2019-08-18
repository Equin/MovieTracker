package com.example.movietracker.view.helper;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.service.DownloadCompleteBroadcastReceiver;
import com.example.movietracker.view.contract.DownloadListenerView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageSaveUtility  {

    private ImageSaveUtility() {}

    public static void saveImageToDisk(Context context, String imageSourcePath, String movieName, DownloadListenerView downloadListenerView) {
        saveWithDownloadManager(context, imageSourcePath, movieName, downloadListenerView);
    }

    private static void saveWithDownloadManager(Context context, String imageSourcePath, String movieName, DownloadListenerView downloadListenerView) {

        if(downloadListenerView != null) {
            downloadListenerView.onDownloadStarted();
        }

        Uri uri= Uri.parse(NetConstant.IMAGE_HIGHT_RES_URL + imageSourcePath);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("Data Download");
        request.setDescription("Android Data download using DownloadManager.");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"MovieTracker/" + movieName);
        request.setMimeType("image/jpeg");

        context.registerReceiver(
                new DownloadCompleteBroadcastReceiver(
                        downloadManager.enqueue(request),
                        downloadListenerView),
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private static void saveWithGlide(Context context, String imageSourcePath, String movieName) {
        Handler mainHandler = new Handler(context.getMainLooper());

        Glide
                .with(context)
                .asFile()
                .load(NetConstant.IMAGE_HIGHT_RES_URL + imageSourcePath)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {

                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Image wasn`t loaded", Toast.LENGTH_LONG).show();
                            }
                        };
                        mainHandler.post(myRunnable);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        if (saveFile(resource, movieName)) {
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Image loaded", Toast.LENGTH_LONG).show();
                                }
                            };
                            mainHandler.post(myRunnable);
                            return true;
                        } else {
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Image wasn`t loaded", Toast.LENGTH_LONG).show();
                                }
                            };
                            mainHandler.post(myRunnable);
                            return false;
                        }
                    }
                }).submit();
    }

    private static boolean saveFile(File file, String fileName) {
        try {

            File newFile = createDirectory(fileName);

            try (InputStream in = new BufferedInputStream(new FileInputStream(file));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

        return  true;
    }

    private static File createDirectory(String fileName) throws IOException {
        String rootDirPath =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/MovieTracker";

        File rootDir = new File(rootDirPath);
        if (!rootDir.exists()) rootDir.mkdirs();

        File dstFile = new File(rootDir, fileName + ".jpg");
        if (!dstFile.exists()) {
            dstFile.createNewFile();
        }
        return dstFile;
    }
}
