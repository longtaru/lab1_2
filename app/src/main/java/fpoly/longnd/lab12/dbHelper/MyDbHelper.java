package fpoly.longnd.lab12.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lab12.db";
    private static final int DATABASE_VERSION = 1;


    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCat = "CREATE TABLE tb_cat (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);";
        db.execSQL(sqlCat);

        // Add sample data
        db.execSQL("INSERT INTO tb_cat (name) VALUES ('Food')");
        db.execSQL("INSERT INTO tb_cat (name) VALUES ('Drink')");
        db.execSQL("INSERT INTO tb_cat (name) VALUES ('Other')");

        String sqlProduct = "CREATE TABLE tb_product (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, price NUMBER NOT NULL DEFAULT 0, id_cat integer, CONSTRAINT fk_category FOREIGN KEY (id_cat) REFERENCES tb_cat (id))";
        db.execSQL(sqlProduct);

        // Add sample product data
        db.execSQL("INSERT INTO tb_product (name, price, id_cat) VALUES ('Pizza', 10.99, 1)");
        db.execSQL("INSERT INTO tb_product (name, price, id_cat) VALUES ('Burger', 5.99, 1)");
        db.execSQL("INSERT INTO tb_product (name, price, id_cat) VALUES ('Coca-Cola', 1.99, 2)");
        db.execSQL("INSERT INTO tb_product (name, price, id_cat) VALUES ('Water', 0.99, 2)");
        db.execSQL("INSERT INTO tb_product (name, price, id_cat) VALUES ('Napkins', 2.49, 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS tb_cat");
            db.execSQL("DROP TABLE IF EXISTS tb_product");
            onCreate(db);
        }
    }
}
