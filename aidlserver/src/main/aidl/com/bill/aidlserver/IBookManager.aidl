package com.bill.aidlserver;

import com.bill.aidlserver.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}