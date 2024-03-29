package com.f83260.foodwaste.data;

import android.content.Context;
import android.database.Cursor;

import com.f83260.foodwaste.common.DBManager;
import com.f83260.foodwaste.data.model.Opportunity;
import com.f83260.foodwaste.data.model.Store;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static volatile DataRepository instance;

    private final DBManager dbManager;
    DataRepository(Context context) {
        this.dbManager = new DBManager(context);
        this.dbManager.getWritableDatabase();
    }

    public static DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context);
        }
        return instance;
    }

    private Store getStoreByAttribute(String attributeName, Object attributeValue){
        String query = "SELECT id, name, long, lat FROM stores WHERE "+attributeName+" = '"+attributeValue+"';";

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

    private List<Opportunity> getOpportunitiesByAttribute(String attributeName, Object attributeValue){
        String query = "SELECT * FROM opportunities WHERE "+attributeName+" = '"+attributeValue+"';";

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
       return getStoreByAttribute("name", storeName);
    }

    public Store getStoreById(int storeId){
        return getStoreByAttribute("id ", storeId);
    }


    public List<Opportunity> getOpportunityByStore(Integer id){
       return getOpportunitiesByAttribute("store_id", id);
    }

    public List<Opportunity> getOpportunitiesForUser(String userId){
       return getOpportunitiesByAttribute("user_id", userId);
    }

    public void reserveOpportunity(Opportunity opp, String userId){
        String query = "UPDATE opportunities SET user_id = '" + userId+"' WHERE id = " + opp.getId();

        this.dbManager.getWritableDatabase().execSQL(query);

        opp.setUserClaimedId(userId);
    }

    public void removeReservation(Opportunity opp){
        String query = "UPDATE opportunities SET user_id = null WHERE id = " + opp.getId();

        this.dbManager.getWritableDatabase().execSQL(query);

        opp.setUserClaimedId(null);

    }
}
