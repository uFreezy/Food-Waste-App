package com.f83260.foodwaste.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.f83260.foodwaste.common.DBManager;
import com.f83260.foodwaste.data.model.Opportunity;
import com.f83260.foodwaste.data.model.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreRepository {

    private static volatile StoreRepository instance;

    private DBManager dbManager;
    StoreRepository(Context context) {
        this.dbManager = new DBManager(context);
        this.dbManager.getWritableDatabase();
    }

    public static StoreRepository getInstance(Context context) {
        if (instance == null) {
            instance = new StoreRepository(context);
        }
        return instance;
    }

    public List<Store> getStores() {
        String query = "SELECT id, name, long, lat FROM stores;";

        Cursor cursor = this.dbManager.getReadableDatabase().rawQuery(query, null);

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

    public Store getStoreByName(String storeName){
        String query = "SELECT id, name, long, lat FROM stores WHERE name = '"+storeName+"';";

        Cursor cursor = this.dbManager.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            Double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("long"));
            Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("lat"));

            return new Store(name, lng, lat,getOpportunityByStore(id));
        }

        cursor.close();

        return null;
    }

    public Store getStoreById(int storeId){
        String query = "SELECT id, name, long, lat FROM stores WHERE id = '"+storeId+"';";

        Cursor cursor = this.dbManager.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            Double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("long"));
            Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("lat"));

            return new Store(name, lng, lat,getOpportunityByStore(id));
        }

        cursor.close();

        return null;
    }


    public List<Opportunity> getOpportunityByStore(Integer id){
        String query = "SELECT * FROM opportunities WHERE store_id="+id+";";

        Cursor cursor = this.dbManager.getReadableDatabase().rawQuery(query, null);

        List<Opportunity> opportunities = new ArrayList<>();

        while (cursor.moveToNext()) {
            int oppId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
            boolean available = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow("is_available")));
            int sId = cursor.getInt(cursor.getColumnIndexOrThrow("store_id"));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

            opportunities.add(new Opportunity(oppId, name, available, sId, createdAt, userId));
        }
        cursor.close();
        return opportunities;
    }

    public List<Opportunity> getOpportunitiesForUser(String userId){
        String query = "SELECT * FROM opportunities WHERE user_id='"+userId+"';";

        Cursor cursor = this.dbManager.getReadableDatabase().rawQuery(query, null);

        List<Opportunity> opportunities = new ArrayList<>();

        while (cursor.moveToNext()) {
            int oppId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
            boolean available = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow("is_available")));
            int sId = cursor.getInt(cursor.getColumnIndexOrThrow("store_id"));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));

            opportunities.add(new Opportunity(oppId, name, available, sId, createdAt,userId));
        }
        cursor.close();
        return opportunities;
    }

    public Opportunity reserveOpportunity(Opportunity opp, String userId){
        String query = "UPDATE opportunities SET user_id = '" + userId+"' WHERE id = " + opp.getId();

        this.dbManager.getWritableDatabase().execSQL(query);

        opp.setUserClaimedId(userId);

        return opp;
    }

    public Opportunity removeReservation(Opportunity opp){
        String query = "UPDATE opportunities SET user_id = null WHERE id = " + opp.getId();

        this.dbManager.getWritableDatabase().execSQL(query);

        opp.setUserClaimedId(null);

        return opp;
    }
}
