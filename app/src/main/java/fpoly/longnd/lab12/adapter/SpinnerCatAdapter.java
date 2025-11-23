package fpoly.longnd.lab12.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.longnd.lab12.R;
import fpoly.longnd.lab12.dto.CatDTO;

public class SpinnerCatAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<CatDTO> list;

    public SpinnerCatAdapter(Context context, ArrayList<CatDTO> list) {
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

    private static class ViewHolder {
        TextView tv_id;
        TextView tv_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_spinner_cat, parent, false);
            holder = new ViewHolder();
            holder.tv_id = convertView.findViewById(R.id.tv_id_spinner);
            holder.tv_name = convertView.findViewById(R.id.tv_name_spinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CatDTO catDTO = list.get(position);
        holder.tv_id.setText(String.valueOf(catDTO.getId()));
        holder.tv_name.setText(catDTO.getName());

        return convertView;
    }
}
