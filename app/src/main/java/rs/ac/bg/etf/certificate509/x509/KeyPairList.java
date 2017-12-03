package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class KeyPairList extends AppCompatActivity {

    private ListView lv;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_key_pair_list);

        lv = (ListView) findViewById(R.id.keyPairNameList);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_row, R.id.keyPairNameText,
                X509Application.keyPairNameList );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (lv.getItemAtPosition(position)).toString();
                Intent intent = new Intent(getBaseContext(), ShowDetailsActivity.class);
                intent.putExtra("selected", selectedFromList);
                startActivity(intent);
            }
        });
    }

}
