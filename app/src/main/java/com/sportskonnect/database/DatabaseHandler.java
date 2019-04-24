package com.sportskonnect.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sportskonnect.modal.Gameinfo;
import com.sportskonnect.modal.SelectedGameModal;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "rackonnect.db";

    // Table Name
    private static final String TABLE_SELECTED_GAME = "selecte_games";



    // Table Columns names TABLE_CART
    private static final String COL_SELECTED_GAME_ID = "COL_SELECTED_GAME_ID";
    private static final String COL_PLAYER_LEVEL_ID = "COL_PLAYER_LEVEL_ID";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = getWritableDatabase();

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase d) {



        createTableSelectedgame(d);
    }




    private void createTableSelectedgame(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_SELECTED_GAME + " ("
                + COL_SELECTED_GAME_ID + " INTEGER PRIMARY KEY, "
                + COL_PLAYER_LEVEL_ID + " INTEGER "
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB ", "onUpgrade " + oldVersion + " to " + newVersion);
    }

    public void truncateDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_GAME);
        // Create tables again
        onCreate(db);
    }



    private ContentValues getSelectedgameValue(SelectedGameModal model) {
        ContentValues values = new ContentValues();
        values.put(COL_SELECTED_GAME_ID, model.catId);
        values.put(COL_PLAYER_LEVEL_ID, model.levelId);
        return values;
    }

    private ContentValues getSelectedServergameValue(Gameinfo model) {
        ContentValues values = new ContentValues();
        values.put(COL_SELECTED_GAME_ID, model.getCatid());
        values.put(COL_PLAYER_LEVEL_ID, model.getLevelid());
        return values;
    }


    public SelectedGameModal getSelectedGame(long selectedgameid) {
        SelectedGameModal obj = null;
        String query = "SELECT * FROM " + TABLE_SELECTED_GAME + " c WHERE  c." + COL_SELECTED_GAME_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{selectedgameid + ""});
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            obj = getSelectedgameByCursor(cursor);
        }
        return obj;
    }



    private SelectedGameModal getSelectedgameByCursor(Cursor cur) {
        SelectedGameModal obj = new SelectedGameModal();
        obj.catId = cur.getInt(cur.getColumnIndex(COL_SELECTED_GAME_ID));
        obj.levelId = cur.getInt(cur.getColumnIndex(COL_PLAYER_LEVEL_ID));
        return obj;
    }




//    public List<SelectedGameModal> getActiveSelectedGameList() {
//        List<SelectedGameModal> items = new ArrayList<>();
//        StringBuilder sb = new StringBuilder();
//        sb.append(" SELECT * FROM " + TABLE_SELECTED_GAME + " c WHERE c." + COL_SELECTED_GAME_ID + "=" + (-1) + " ORDER BY c." + COL_CART_CREATED_AT + " DESC ");
//        Cursor cursor = db.rawQuery(sb.toString(), null);
//        if (cursor.moveToFirst()) {
//            items = getListCartByCursor(cursor);
//        }
//        return items;
//    }

    private List<SelectedGameModal> getListCartByCursor(Cursor cur) {
        List<SelectedGameModal> items = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
                items.add(getSelectedgameByCursor(cur));
            } while (cur.moveToNext());
        }
        return items;
    }



    public void saveSelectedgames(SelectedGameModal selectedGameModal) {
        ContentValues values = getSelectedgameValue(selectedGameModal);
        // Inserting or Update Row

        db.insertWithOnConflict(TABLE_SELECTED_GAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public void saveServerSelectedgames(Gameinfo gameinfo) {
        ContentValues values = getSelectedServergameValue(gameinfo);
        // Inserting or Update Row

        db.insertWithOnConflict(TABLE_SELECTED_GAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }




    public void deleteActiveSelectedgames(int product_id) {
        db.delete(TABLE_SELECTED_GAME, COL_SELECTED_GAME_ID + " = ?", new String[]{String.valueOf(product_id)});

//        db.delete(TABLE_SELECTED_GAME, COL_SELECTED_GAME_ID + " = ? AND " + COL_SELECTED_GAME_ID + " = ?", new String[]{"-1", String.valueOf(product_id)});

    }

    // delete all records
    public void deleteActiveCart() {
        db.execSQL("DELETE FROM " + TABLE_SELECTED_GAME);
    }

    public int getselectedgamessize() {
        int count = (int) DatabaseUtils.queryNumEntries(db, TABLE_SELECTED_GAME);
        return count;
    }

    public int getActiveSelectegameSize() {
        String countQuery = "SELECT c." + COL_SELECTED_GAME_ID + " FROM " + TABLE_SELECTED_GAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public List<SelectedGameModal> getActiveCartList() {
        List<SelectedGameModal> items = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM " + TABLE_SELECTED_GAME );
        Cursor cursor = db.rawQuery(sb.toString(), null);
        if (cursor.moveToFirst()) {
            items = getListCartByCursor(cursor);
        }
        return items;
    }

}
