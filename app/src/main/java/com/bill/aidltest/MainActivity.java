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

public class MainActivity extends AppCompatActivity {

    private IBookManagerManual mIBookManager;


    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManagerManual.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
        try {
            List<Book> list = mIBookManager.getBookList();
            Log.e("YBill", "query book list, list type:" + list.getClass().getCanonicalName());
            Log.e("YBill", "query book list:" + list.toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAdd(View view) {
        Book book = new Book(100, "Python");
        try {
            mIBookManager.addBook(book);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}