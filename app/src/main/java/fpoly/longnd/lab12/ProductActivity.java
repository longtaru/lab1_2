package fpoly.longnd.lab12;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fpoly.longnd.lab12.adapter.ProductAdapter;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dao.ProductDAO;
import fpoly.longnd.lab12.dto.CatDTO;
import fpoly.longnd.lab12.dto.ProductDTO;

public class ProductActivity extends AppCompatActivity {
    private EditText edProdName, edProdPrice;
    private Spinner spnCat;
    private Button btnAddProd, btnUpdateProd;
    private ListView lvProduct;

    private ProductDAO productDAO;
    private CatDAO catDAO;

    private ProductAdapter productAdapter;
    private ArrayList<ProductDTO> listProduct;
    private ArrayList<CatDTO> listCategory;

    private int selectedProductPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Initialize DAOs
        productDAO = new ProductDAO(this);
        catDAO = new CatDAO(this);

        // Find views
        edProdName = findViewById(R.id.ed_prod_name);
        edProdPrice = findViewById(R.id.ed_prod_price);
        spnCat = findViewById(R.id.spn_cat);
        btnAddProd = findViewById(R.id.btn_add_prod);
        btnUpdateProd = findViewById(R.id.btn_update_prod);
        lvProduct = findViewById(R.id.lv_product);

        // Load data
        listProduct = productDAO.getAllProduct();
        listCategory = catDAO.getAll();

        // Setup adapter
        productAdapter = new ProductAdapter(this, listProduct);
        lvProduct.setAdapter(productAdapter);

        // Setup spinner
        List<String> categoryNames = new ArrayList<>();
        for (CatDTO cat : listCategory) {
            categoryNames.add(cat.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCat.setAdapter(spinnerAdapter);

        // Add button listener
        btnAddProd.setOnClickListener(v -> addProduct());

        // Update button listener
        btnUpdateProd.setOnClickListener(v -> updateProduct());

        // ListView item click listener
        lvProduct.setOnItemClickListener((parent, view, position, id) -> {
            selectedProductPosition = position;
            ProductDTO product = listProduct.get(position);
            edProdName.setText(product.getName());
            edProdPrice.setText(String.valueOf(product.getPrice()));

            // Set spinner selection
            for (int i = 0; i < listCategory.size(); i++) {
                if (listCategory.get(i).getId() == product.getId_cat()) {
                    spnCat.setSelection(i);
                    break;
                }
            }
        });
    }

    private void addProduct() {
        String name = edProdName.getText().toString();
        String priceStr = edProdPrice.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int catPosition = spnCat.getSelectedItemPosition();
        int catId = listCategory.get(catPosition).getId();

        ProductDTO newProduct = new ProductDTO();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setId_cat(catId);

        long result = productDAO.addProduct(newProduct);
        if (result > 0) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            listProduct.clear();
            listProduct.addAll(productDAO.getAllProduct());
            productAdapter.notifyDataSetChanged();
            resetFields();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProduct() {
        if (selectedProductPosition == -1) {
            Toast.makeText(this, "Please select a product to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edProdName.getText().toString();
        String priceStr = edProdPrice.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int catPosition = spnCat.getSelectedItemPosition();
        int catId = listCategory.get(catPosition).getId();

        ProductDTO updatedProduct = listProduct.get(selectedProductPosition);
        updatedProduct.setName(name);
        updatedProduct.setPrice(price);
        updatedProduct.setId_cat(catId);

        boolean success = productDAO.updateRow(updatedProduct);
        if (success) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            productAdapter.notifyDataSetChanged();
            resetFields();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFields() {
        edProdName.setText("");
        edProdPrice.setText("");
        spnCat.setSelection(0);
        selectedProductPosition = -1;
    }
}
