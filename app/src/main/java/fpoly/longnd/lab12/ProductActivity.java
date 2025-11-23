package fpoly.longnd.lab12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.longnd.lab12.adapter.ProductAdapter;
import fpoly.longnd.lab12.adapter.SpinnerCatAdapter;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dao.ProductDAO;
import fpoly.longnd.lab12.dto.CatDTO;
import fpoly.longnd.lab12.dto.ProductDTO;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnDataChangedListener {
    ListView lv_product;
    ProductDAO productDAO;
    ProductAdapter productAdapter;
    ArrayList<ProductDTO> listProd;
    Button btn_add;
    CatDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        lv_product = findViewById(R.id.lv_product);
        btn_add = findViewById(R.id.btn_add_prod);

        productDAO = new ProductDAO(this);
        listProd = new ArrayList<>();
        productAdapter = new ProductAdapter(this, listProd, this);
        lv_product.setAdapter(productAdapter);

        catDAO = new CatDAO(this);

        btn_add.setOnClickListener(v -> showAddProductDialog());
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        final EditText ed_name_prod_dialog = view.findViewById(R.id.ed_name_prod_dialog);
        final EditText ed_price_prod_dialog = view.findViewById(R.id.ed_price_prod_dialog);
        final Spinner spinner_cat_dialog = view.findViewById(R.id.spinner_cat_dialog);

        ArrayList<CatDTO> listCat = catDAO.getAll();
        SpinnerCatAdapter spinnerCatAdapter = new SpinnerCatAdapter(this, listCat);
        spinner_cat_dialog.setAdapter(spinnerCatAdapter);

        builder.setTitle("Thêm sản phẩm");
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = ed_name_prod_dialog.getText().toString().trim();
            String priceStr = ed_price_prod_dialog.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Tên và giá không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            CatDTO selectedCat = (CatDTO) spinner_cat_dialog.getSelectedItem();

            ProductDTO newProd = new ProductDTO();
            newProd.setName(name);
            newProd.setPrice(price);
            newProd.setId_cat(selectedCat.getId());

            if (productDAO.addProduct(newProd) > 0) {
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
        listProd.clear();
        listProd.addAll(productDAO.getAllProduct());
        productAdapter.notifyDataSetChanged();
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
