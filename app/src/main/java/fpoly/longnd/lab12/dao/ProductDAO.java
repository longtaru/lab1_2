package fpoly.longnd.lab12.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fpoly.longnd.lab12.dbHelper.MyDbHelper;
import fpoly.longnd.lab12.dto.ProductDTO;

public class ProductDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase db;

    public ProductDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int addProduct(ProductDTO productDTO) {
        ContentValues values = new ContentValues();
        values.put("name", productDTO.getName());
        values.put("price", productDTO.getPrice());
        values.put("id_cat", productDTO.getId_cat());
        return (int) db.insert("tb_product", null, values);
    }

    public ArrayList<ProductDTO> getAllProduct() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_product";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        ProductDTO productDTO = new ProductDTO();
                        productDTO.setId(cursor.getInt(0));
                        productDTO.setName(cursor.getString(1));
                        productDTO.setPrice(cursor.getDouble(2));
                        productDTO.setId_cat(cursor.getInt(3));
                        list.add(productDTO);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public ProductDTO getProductById(int id) {
        ProductDTO productDTO = null;
        String[] params = {String.valueOf(id)};
        String sql = "SELECT * FROM tb_product WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, params);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    productDTO = new ProductDTO();
                    productDTO.setId(cursor.getInt(0));
                    productDTO.setName(cursor.getString(1));
                    productDTO.setPrice(cursor.getDouble(2));
                    productDTO.setId_cat(cursor.getInt(3));
                }
            } finally {
                cursor.close();
            }
        }
        return productDTO;
    }

    public boolean updateRow(ProductDTO productDTO) {
        ContentValues values = new ContentValues();
        values.put("name", productDTO.getName());
        values.put("price", productDTO.getPrice());
        values.put("id_cat", productDTO.getId_cat());
        String[] params = {String.valueOf(productDTO.getId())};
        return db.update("tb_product", values, "id = ?", params) > 0;
    }

    public boolean deleteRow(int id) {
        String[] params = {String.valueOf(id)};
        return db.delete("tb_product", "id = ?", params) > 0;
    }
}
