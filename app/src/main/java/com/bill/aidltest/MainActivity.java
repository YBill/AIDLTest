package com.bill.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bill.aidlserver.IBookManagerManual;

import java.util.List;

import com.bill.aidlserver.Book;
import com.bill.aidlserver.IOnNewBookArrivedListener;

public class MainActivity extends AppCompatActivity {

    private IBookManagerManual mIBookManager;


    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManagerManual.Stub.asInterface(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 服务挂了会走这里，可以处理重连
            // 注意这里是运行在主进程的主线程中，即在UI线程中
        }
    };

    // Binder 的死亡监听
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            // Binder 死亡会回调这里，可以处理重连
            // 注意这里是运行在主进程的Binder线程池中 （这是和onServiceDisconnected的唯一区别）
        }
    };

    private final IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            // 注意这个回调是在主进程中，但是不在主线程，而是在一个新的线程中（Binder线程池）
            Log.e("YBill", "onNewBookArrived book = " + newBook);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("YBill", "main process id:" + Process.myTid());
        Intent intent = new Intent("com.bill.aidlserver.BookService");
        intent.setPackage("com.bill.aidltest");
//        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    public void handleGet(View view) {
        Log.e("YBill", "handleGet");
        try {
            List<Book> list = mIBookManager.getBookList();
            Log.e("YBill", "query book list, list type:" + list.getClass().getCanonicalName());
            Log.e("YBill", "query book list:" + list.toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAdd(View view) {
        Log.e("YBill", "handleAdd");
        Book book = new Book(100, "Python");
        try {
            mIBookManager.addBook(book);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRegister(View view) {
        Log.e("YBill", "handleRegister listener = " + mOnNewBookArrivedListener);
        try {
            mIBookManager.registerListener(mOnNewBookArrivedListener);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleUnregister(View view) {
        Log.e("YBill", "handleUnregister listener = " + mOnNewBookArrivedListener);
        try {
            mIBookManager.unregisterListener(mOnNewBookArrivedListener);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}