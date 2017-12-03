package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button keyPair, importExport, showDetails, signCertificate, exportSigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        keyPair = (Button) findViewById(R.id.keyPair);

        keyPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getBaseContext(), UserInput.class );
                startActivity( intent );
            }
        });

        importExport = (Button) findViewById(R.id.importExport);

        importExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ImportExportActivity.class);
                startActivity(intent);
            }
        });

        showDetails = ( Button ) findViewById(R.id.showDetails);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), KeyPairList.class);
                startActivity(intent);
            }
        });

        signCertificate = ( Button ) findViewById(R.id.signCertificate);

        signCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignActivity.class);
                startActivity(intent);
            }
        });


    }
}
