package fpoly.longnd.lab12.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.longnd.lab12.R;
import fpoly.longnd.lab12.dao.ProductDAO;
import fpoly.longnd.lab12.dto.ProductDTO;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ProductDTO> list;
    private final ProductDAO productDAO;

    public ProductAdapter(Context context, ArrayList<ProductDTO> list) {
        this.context = context;
        this.list = list;
        this.productDAO = new ProductDAO(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product, parent, false);

        TextView tv_id = view.findViewById(R.id.tv_id_prod);
        TextView tv_name = view.findViewById(R.id.tv_name_prod);
        TextView tv_price = view.findViewById(R.id.tv_price_prod);
        ImageView img_delete = view.findViewById(R.id.img_delete_prod);

        ProductDTO productDTO = list.get(position);
        tv_id.setText(String.valueOf(productDTO.getId()));
        tv_name.setText(productDTO.getName());
        tv_price.setText(String.valueOf(productDTO.getPrice()));

        img_delete.setOnClickListener(v -> {
            boolean success = productDAO.deleteRow(productDTO.getId());
            if (success) {
                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                list.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
