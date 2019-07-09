package com.onica.booksearch;

import com.onica.booksearch.controller.MenuController;


public class bookSearchMain {
    
    
    public static void main (String [] args) {
        MenuController menuController = new MenuController();
        menuController.startUserMenuChoices();
    }
}
