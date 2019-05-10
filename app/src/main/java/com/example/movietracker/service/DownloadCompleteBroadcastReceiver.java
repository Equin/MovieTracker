package com.example.movietracker.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.movietracker.view.contract.DownloadListenerView;

public class DownloadCompleteBroadcastReceiver extends BroadcastReceiver {

    private Long downloadId;
    private DownloadListenerView downloadListenerView;

    public DownloadCompleteBroadcastReceiver(Long downloadId, DownloadListenerView downloadListenerView) {
        this.downloadId = downloadId;
        this.downloadListenerView = downloadListenerView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        if (downloadId != id) {
            return;
        }

        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL && downloadListenerView != null)
                    {
                        //  String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        downloadListenerView.onDownloadCompleted();

                    } else if (status == DownloadManager.STATUS_FAILED && downloadListenerView != null) {
                        downloadListenerView.onDownloadFailed();
                    }
                }
            }
            cursor.close();
        }
        context.unregisterReceiver(this);
    }
}
