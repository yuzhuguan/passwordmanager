package com.codef1.oldcode.data;

import android.provider.BaseColumns;

/**
 * Created by yuzhu on 2017/12/15.
 */

public class PasswordContract {

    public static final String DATABASE_NAME = "password.db";
    public static final int DATABASE_VERSION = 1;

    public class PWEntry implements BaseColumns{
        public static final String TABLE = "password";
        public static final String COL_PW_NAME = "name";
        public static final String COL_PW_VALUE = "password";
    }
}
