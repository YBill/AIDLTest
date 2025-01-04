package com.bill.aidlserver;

import android.util.Log;

/**
 * 拷贝的 自动生成的 IBookManager.java 类，自己写的号打印日志
 */
public interface IBookManagerManual extends android.os.IInterface {
    /**
     * Default implementation for IBookManager.
     */
    public static class Default implements IBookManagerManual {
        @Override
        public java.util.List<Book> getBookList() throws android.os.RemoteException {
            return null;
        }

        @Override
        public void addBook(Book book) throws android.os.RemoteException {
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements IBookManagerManual {
        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.bill.aidlserver.IBookManager interface,
         * generating a proxy if needed.
         */
        public static IBookManagerManual asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            android.util.Log.e("YBill", "obj:" + obj);
            android.util.Log.e("YBill", "iin:" + iin);
            if (((iin != null) && (iin instanceof IBookManagerManual))) {
                return ((IBookManagerManual) iin);
            }
            return new Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            String descriptor = DESCRIPTOR;
            if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
                data.enforceInterface(descriptor);
            }
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
            }
            switch (code) {
                case TRANSACTION_getBookList: {
                    Log.e("YBill", "Stub getBookList");
                    java.util.List<Book> _result = this.getBookList();
                    reply.writeNoException();
                    _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    break;
                }
                case TRANSACTION_addBook: {
                    Log.e("YBill", "Stub addBook");
                    Book _arg0;
                    _arg0 = _Parcel.readTypedObject(data, Book.CREATOR);
                    this.addBook(_arg0);
                    reply.writeNoException();
                    break;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }

        private static class Proxy implements IBookManagerManual {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public java.util.List<Book> getBookList() throws android.os.RemoteException {
                Log.e("YBill", "Proxy getBookList mRemote = " + mRemote);
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void addBook(Book book) throws android.os.RemoteException {
                Log.e("YBill", "Proxy addBook mRemote = " + mRemote);
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _Parcel.writeTypedObject(_data, book, 0);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }

    public static final String DESCRIPTOR = "com.bill.aidlserver.IBookManager";

    public java.util.List<Book> getBookList() throws android.os.RemoteException;

    public void addBook(Book book) throws android.os.RemoteException;

    /**
     * @hide
     */
    static class _Parcel {
        static private <T> T readTypedObject(
                android.os.Parcel parcel,
                android.os.Parcelable.Creator<T> c) {
            if (parcel.readInt() != 0) {
                return c.createFromParcel(parcel);
            } else {
                return null;
            }
        }

        static private <T extends android.os.Parcelable> void writeTypedObject(
                android.os.Parcel parcel, T value, int parcelableFlags) {
            if (value != null) {
                parcel.writeInt(1);
                value.writeToParcel(parcel, parcelableFlags);
            } else {
                parcel.writeInt(0);
            }
        }

        static private <T extends android.os.Parcelable> void writeTypedList(
                android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
            if (value == null) {
                parcel.writeInt(-1);
            } else {
                int N = value.size();
                int i = 0;
                parcel.writeInt(N);
                while (i < N) {
                    writeTypedObject(parcel, value.get(i), parcelableFlags);
                    i++;
                }
            }
        }
    }
}
