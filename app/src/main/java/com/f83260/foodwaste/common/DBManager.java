package com.f83260.foodwaste.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";

    private static final String[] TABLE_DEFINITIONS = {"CREATE TABLE stores (\n" +
            "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            "  name TEXT NOT NULL, long NUMERIC NOT NULL, \n" +
            "  lat NUMERIC NOT NULL\n" +
            ");\n",
            "CREATE TABLE opportunities (\n" +
                    "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
                    "  product_name TEXT NOT NULL, quantity NUMERIC NOT NULL, \n" +
                    "  is_available INTEGER NOT NULL, created_at STRING NOT NULL, \n" +
                    "  store_id INTEGER NOT NULL,\n" +
                    "  user_id TEXT \n " +
                    ");\n"};

    private static final String[] SQL_DELETE_ENTRIES = {
            "DROP TABLE IF EXISTS stores; \n",
            "DROP TABLE IF EXISTS opportunities;"};

    private final SQLiteDatabase db = getWritableDatabase();

    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_DEFINITIONS[0]);
        db.execSQL(TABLE_DEFINITIONS[1]);

        List<String> storeSqls = new ArrayList<>();
        storeSqls.add("INSERT INTO stores (name, long, lat) VALUES('Umbrella Bar', 23.3487565, 42.6516595);");
        storeSqls.add("INSERT INTO stores (name, long, lat) VALUES('Bun Brothers', 23.3403464, 42.6526856);");
        storeSqls.add("INSERT INTO stores (name, long, lat) VALUES('Механа \"Роден Край\"', 23.3414904, 42.6487331);");

        List<String> oppSqls = new ArrayList<>();

        String[] oppNames = {"tomatoes", "potatoes", "pizza", "cucumbers"};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Random randGen = new Random();

        for (int i = 0; i < storeSqls.size(); i++) {
            int quant = randGen.nextInt(10);
            String createdAt = dateFormat.format(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(randGen.nextInt(10))));

            String oppSql = "";
            for (int j = 0; j < 3; j++) {
                oppSql = String.format("INSERT INTO opportunities (product_name, quantity, is_available, created_at, store_id) VALUES('%s',%s, 1,'%s', %s);", oppNames[randGen.nextInt(oppNames.length)], quant, createdAt, i + 1);
                oppSqls.add(oppSql);
            }
        }

        for (String storeSql : storeSqls) {
            db.execSQL(storeSql);
        }

        for (String oppSql : oppSqls) {
            db.execSQL(oppSql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES[0]);
        db.execSQL(SQL_DELETE_ENTRIES[1]);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES[0]);
        db.execSQL(SQL_DELETE_ENTRIES[1]);
        this.onCreate(db);
    }
}
