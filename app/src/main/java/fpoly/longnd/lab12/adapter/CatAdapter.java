package fpoly.longnd.lab12.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.longnd.lab12.R;
import fpoly.longnd.lab12.dto.CatDTO;

public class CatAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<CatDTO> list;

    public CatAdapter(Context context, ArrayList<CatDTO> list) {
        this.context = context;
        this.list = list;
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
        View view = inflater.inflate(R.layout.item_category, parent, false);

        TextView tv_id = view.findViewById(R.id.tv_id);
        TextView tv_name = view.findViewById(R.id.tv_name);
        ImageView img_edit = view.findViewById(R.id.img_edit);
        ImageView img_delete = view.findViewById(R.id.img_delete);

        CatDTO catDTO = list.get(position);
        tv_id.setText(String.valueOf(catDTO.getId()));
        tv_name.setText(catDTO.getName());

        return view;
    }
}
