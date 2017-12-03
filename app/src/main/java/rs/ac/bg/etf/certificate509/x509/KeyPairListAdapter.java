package rs.ac.bg.etf.certificate509.x509;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Aleksa on 19.06.2016..
 */
public class KeyPairListAdapter extends ArrayAdapter<String>
{
    Context context;
    int layoutResourceId;
    String data[] = null;

    public KeyPairListAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StringHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new StringHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.keyPairNameText);

            row.setTag(holder);
        }
        else
        {
            holder = (StringHolder)row.getTag();
        }

        String weather = data[position];
        holder.txtTitle.setText(weather);

        return row;
    }

    static class StringHolder
    {
        TextView txtTitle;
    }
}
