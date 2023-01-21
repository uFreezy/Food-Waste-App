package com.f83260.foodwaste.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.f83260.foodwaste.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";
    private static final int STORE_COUNT = 50;
    private static final int OPPORTUNITIES_PER_STORE = 10;
    private final Context context;
    private final Random rand = new Random();

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create database
        this.executeFromResource(db, R.raw.db);

        // Seed stores
        this.executeFromResource(db, R.raw.stores);

        // Seed opportunities
        String[] oppNames = {"tomatoes", "potatoes", "pizza", "cucumbers"};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        for (int i = 0; i < STORE_COUNT; i++) {
            for (int j = 0; j < OPPORTUNITIES_PER_STORE; j++) {
                int quant = rand.nextInt(10);
                String createdAt = dateFormat.format(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(rand.nextInt(10))));
                this.executeFromResource(db, R.raw.opps, oppNames[rand.nextInt(oppNames.length)], Integer.toString(quant), createdAt, Integer.toString(i + 1));
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        this.onCreate(db);
    }

    public void executeFromResource(SQLiteDatabase db, int resourceId, @Nullable String... params) {
        try (InputStream is = context.getResources().openRawResource(resourceId)) {
            Scanner sc = new Scanner(is);
            while (sc.hasNextLine()) {
                String sql;
                if (params != null)
                    sql = String.format(sc.nextLine(), (Object) params);
                else
                    sql = sc.nextLine();

                db.execSQL(sql);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
