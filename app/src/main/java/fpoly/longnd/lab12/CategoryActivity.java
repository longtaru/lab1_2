package fpoly.longnd.lab12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.longnd.lab12.adapter.CatAdapter;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dto.CatDTO;

public class CategoryActivity extends AppCompatActivity implements CatAdapter.OnDataChangedListener {
    ListView lv_category;
    CatDAO catDAO;
    CatAdapter catAdapter;
    ArrayList<CatDTO> listCat;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lv_category = findViewById(R.id.lv_category);
        btn_add = findViewById(R.id.btn_add);

        catDAO = new CatDAO(this);
        listCat = new ArrayList<>();
        catAdapter = new CatAdapter(this, listCat, this);
        lv_category.setAdapter(catAdapter);

        btn_add.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(view);

        final EditText ed_name_cat_dialog = view.findViewById(R.id.ed_name_cat_dialog);

        builder.setTitle("Thêm danh mục");
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = ed_name_cat_dialog.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            CatDTO newCat = new CatDTO();
            newCat.setName(name);

            if (catDAO.addCat(newCat) > 0) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                reloadData();
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void reloadData() {
        listCat.clear();
        listCat.addAll(catDAO.getAll());
        catAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    @Override
    public void onDataChanged() {
        reloadData();
    }
}
