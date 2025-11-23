package fpoly.longnd.lab12.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.longnd.lab12.dbHelper.MyDbHelper;
import fpoly.longnd.lab12.dto.CatDTO;

public class CatDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase db;

    public CatDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int addCat(CatDTO catDTO) {
        ContentValues values = new ContentValues();
        values.put("name", catDTO.getName());
        return (int) db.insert("tb_cat", null, values);
    }

    public ArrayList<CatDTO> getAll() {
        ArrayList<CatDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_cat";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        CatDTO catDTO = new CatDTO();
                        catDTO.setId(cursor.getInt(0));
                        catDTO.setName(cursor.getString(1));
                        list.add(catDTO);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public CatDTO getCatById(int id) {
        CatDTO catDTO = null;
        String[] params = {String.valueOf(id)};
        String sql = "SELECT * FROM tb_cat WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, params);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    catDTO = new CatDTO();
                    catDTO.setId(cursor.getInt(0));
                    catDTO.setName(cursor.getString(1));
                }
            } finally {
                cursor.close();
            }
        }
        return catDTO;
    }

    public boolean updateRow(CatDTO catDTO) {
        ContentValues values = new ContentValues();
        values.put("name", catDTO.getName());
        String[] params = {String.valueOf(catDTO.getId())};
        return db.update("tb_cat", values, "id = ?", params) > 0;
    }

    public boolean deleteRow(int id) {
        String[] params = {String.valueOf(id)};
        return db.delete("tb_cat", "id = ?", params) > 0;
    }
}
