package fpoly.longnd.lab12.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.longnd.lab12.R;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dao.ProductDAO;
import fpoly.longnd.lab12.dto.CatDTO;
import fpoly.longnd.lab12.dto.ProductDTO;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ProductDTO> list;
    private final ProductDAO productDAO;
    private final CatDAO catDAO;
    private final OnDataChangedListener mListener;

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    public ProductAdapter(Context context, ArrayList<ProductDTO> list, OnDataChangedListener listener) {
        this.context = context;
        this.list = list;
        this.productDAO = new ProductDAO(context);
        this.catDAO = new CatDAO(context);
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView tv_id;
        TextView tv_name;
        TextView tv_price;
        ImageView img_edit;
        ImageView img_delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder();
            holder.tv_id = convertView.findViewById(R.id.tv_id_prod);
            holder.tv_name = convertView.findViewById(R.id.tv_name_prod);
            holder.tv_price = convertView.findViewById(R.id.tv_price_prod);
            holder.img_edit = convertView.findViewById(R.id.img_edit_prod);
            holder.img_delete = convertView.findViewById(R.id.img_delete_prod);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProductDTO productDTO = list.get(position);
        holder.tv_id.setText(String.valueOf(productDTO.getId()));
        holder.tv_name.setText(productDTO.getName());
        holder.tv_price.setText(String.valueOf(productDTO.getPrice()));

        holder.img_edit.setOnClickListener(v -> showEditProductDialog(productDTO));

        holder.img_delete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(productDTO);
        });

        return convertView;
    }

    private void showEditProductDialog(final ProductDTO productDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        final EditText ed_name_prod_dialog = view.findViewById(R.id.ed_name_prod_dialog);
        final EditText ed_price_prod_dialog = view.findViewById(R.id.ed_price_prod_dialog);
        final Spinner spinner_cat_dialog = view.findViewById(R.id.spinner_cat_dialog);

        ed_name_prod_dialog.setText(productDTO.getName());
        ed_price_prod_dialog.setText(String.valueOf(productDTO.getPrice()));

        ArrayList<CatDTO> listCat = catDAO.getAll();
        SpinnerCatAdapter spinnerCatAdapter = new SpinnerCatAdapter(context, listCat);
        spinner_cat_dialog.setAdapter(spinnerCatAdapter);

        for (int i = 0; i < listCat.size(); i++) {
            if (listCat.get(i).getId() == productDTO.getId_cat()) {
                spinner_cat_dialog.setSelection(i);
                break;
            }
        }

        builder.setTitle("Sửa sản phẩm");
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = ed_name_prod_dialog.getText().toString().trim();
            String priceStr = ed_price_prod_dialog.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(context, "Tên và giá không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            CatDTO selectedCat = (CatDTO) spinner_cat_dialog.getSelectedItem();

            productDTO.setName(name);
            productDTO.setPrice(price);
            productDTO.setId_cat(selectedCat.getId());

            if (productDAO.updateRow(productDTO)) {
                Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onDataChanged();
                }
            } else {
                Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(final ProductDTO productDTO) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm '" + productDTO.getName() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (productDAO.deleteRow(productDTO.getId())) {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            mListener.onDataChanged();
                        }
                    } else {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
