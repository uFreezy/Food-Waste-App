package com.f83260.foodwaste.data;

import android.content.Context;
import android.database.Cursor;

import com.f83260.foodwaste.common.DBManager;
import com.f83260.foodwaste.data.model.Opportunity;
import com.f83260.foodwaste.data.model.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreRepository {

    private static volatile StoreRepository instance;
    private final Context context;

    StoreRepository(Context context) {
        this.context = context;
    }

    public static StoreRepository getInstance(Context context) {
        if (instance == null) {
            instance = new StoreRepository(context);
        }
        return instance;
    }

    public List<Store> getStores() {
        String query = "SELECT id, name, long, lat FROM stores;";

        DBManager mng = new DBManager(this.context);
        Cursor cursor = mng.getReadableDatabase().rawQuery(query, null);

        List<Store> stores = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            Double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("long"));
            Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("lat"));

            stores.add(new Store(name, lng, lat,getOpportunityByStore(id)));
        }
        cursor.close();
        return stores;
    }

    public List<Opportunity> getOpportunityByStore(Integer id){
        String query = "SELECT * FROM opportunities WHERE store_id="+id+";";

        DBManager mng = new DBManager(this.context);
        Cursor cursor = mng.getReadableDatabase().rawQuery(query, null);

        List<Opportunity> opportunities = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
            boolean available = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow("is_available")));
            int sId = cursor.getInt(cursor.getColumnIndexOrThrow("store_id"));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));

            opportunities.add(new Opportunity(name, available, sId, createdAt));
        }
        cursor.close();
        return opportunities;
    }
}
