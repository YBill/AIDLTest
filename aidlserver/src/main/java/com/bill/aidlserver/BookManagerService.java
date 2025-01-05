package com.bill.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author ywb
 * date 2025/1/4
 * desc AIDL Server 端
 * 注意：BookManagerService 是在一个新的进程中；和 mBinder 是一个进程；
 * 但是新进程并不代表是子线程，比如 onCreate 方法是在新进程的主线程中，如果执行耗时操作还需要起线程的；
 * 不过，mBinder 是在 Binder 的线程池中，是子线程；
 */
public class BookManagerService extends Service {

    private final CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    // 注意如果想要根据一个对象注销只能使用RemoteCallbackList
    private final RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();
    private final AtomicBoolean mIsServerDestroy = new AtomicBoolean(false);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("YBill", "server process id:" + Process.myTid());
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "iOS"));

        // 模拟5s向客户端发送一个添加书籍的回调
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsServerDestroy.get()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Book newBook = new Book(1000, "New Book");
//                    Log.e("YBill", "send:" + mListenerList.size());
                    int N = mListenerList.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
                        try {
                            listener.onNewBookArrived(newBook);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    mListenerList.finishBroadcast();
                }
            }
        }).start();
    }

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

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.e("YBill", "BookManagerService registerListener listener = " + listener);
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.e("YBill", "BookManagerService unregisterListener");
            mListenerList.unregister(listener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServerDestroy.set(true);
        super.onDestroy();
    }
}
