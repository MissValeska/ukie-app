package com.ukie.ukie;

import android.provider.BaseColumns;

/**
 * Created by Valeska on 25/05/2017.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ukie";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}