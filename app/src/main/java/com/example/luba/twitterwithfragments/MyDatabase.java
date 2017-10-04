package com.example.luba.twitterwithfragments;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by luba on 10/3/17.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)

public class MyDatabase {

    public static final String NAME = "RestClientDatabase";

    public static final int VERSION = 1;
}