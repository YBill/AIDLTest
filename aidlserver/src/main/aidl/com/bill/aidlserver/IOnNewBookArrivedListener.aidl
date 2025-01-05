// IOnNewBookArrivedListener.aidl
package com.bill.aidlserver;

import com.bill.aidlserver.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}