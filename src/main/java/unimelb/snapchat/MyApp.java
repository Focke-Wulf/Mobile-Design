package unimelb.snapchat;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Junwen on 10/11/2016.
 */
public class MyApp extends ContentProvider {


    @Override
    public boolean onCreate() {
        SQLiteDatabase db = this.getContext().openOrCreateDatabase("Myquery",
                Context.MODE_PRIVATE, null);

        db.execSQL("create table tab(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NUll)" );
        ContentValues values = new ContentValues();
        values.put("name", "test");
        db.insert("tab", "_id", values);
        db.close();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
