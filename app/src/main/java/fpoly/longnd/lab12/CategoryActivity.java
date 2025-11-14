package fpoly.longnd.lab12;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.longnd.lab12.adapter.CatAdapter;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dto.CatDTO;

public class CategoryActivity extends AppCompatActivity {
    ListView lv_category;
    CatDAO catDAO;
    CatAdapter catAdapter;
    ArrayList<CatDTO> listCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lv_category = findViewById(R.id.lv_category);
        catDAO = new CatDAO(this);
        listCat = catDAO.getAll();
        catAdapter = new CatAdapter(this, listCat);
        lv_category.setAdapter(catAdapter);
    }
}
