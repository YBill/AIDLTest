package com.bill.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author ywb
 * date 2025/1/4
 * desc AIDL Server 端
 */
public class BookManagerService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("YBill", "server process id:" + Process.myTid());
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "iOS"));
    }

    private final CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private final IBookManagerManual.Stub mBinder = new IBookManagerManual.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e("YBill", "BookManagerService execute getBookList, size = " + mBookList.size());
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.e("YBill", "BookManagerService execute addBook, book = " + book);
            mBookList.add(book);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
