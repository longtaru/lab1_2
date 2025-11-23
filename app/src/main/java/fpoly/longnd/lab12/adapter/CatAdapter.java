package fpoly.longnd.lab12.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.longnd.lab12.R;
import fpoly.longnd.lab12.dao.CatDAO;
import fpoly.longnd.lab12.dto.CatDTO;

public class CatAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<CatDTO> list;
    private final CatDAO catDAO;
    private final OnDataChangedListener mListener;

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    public CatAdapter(Context context, ArrayList<CatDTO> list, OnDataChangedListener listener) {
        this.context = context;
        this.list = list;
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
        ImageView img_edit;
        ImageView img_delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder();
            holder.tv_id = convertView.findViewById(R.id.tv_id);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.img_edit = convertView.findViewById(R.id.img_edit);
            holder.img_delete = convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CatDTO catDTO = list.get(position);
        holder.tv_id.setText(String.valueOf(catDTO.getId()));
        holder.tv_name.setText(catDTO.getName());

        holder.img_delete.setOnClickListener(v -> showDeleteDialog(catDTO));
        holder.img_edit.setOnClickListener(v -> showEditDialog(catDTO));

        return convertView;
    }

    private void showDeleteDialog(final CatDTO catDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (catDAO.deleteRow(catDTO.getId())) {
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

    private void showEditDialog(final CatDTO catDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_edit, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        EditText ed_name = view.findViewById(R.id.ed_name);
        Button btn_save = view.findViewById(R.id.btn_save);

        ed_name.setText(catDTO.getName());

        btn_save.setOnClickListener(v -> {
            String newName = ed_name.getText().toString().trim();
            if (!newName.isEmpty()) {
                catDTO.setName(newName);
                if (catDAO.updateRow(catDTO)) {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (mListener != null) {
                        mListener.onDataChanged();
                    }
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
