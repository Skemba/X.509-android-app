package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdditionalUserInput extends AppCompatActivity
{

    UserInfo userInfo;
    EditText cn;
    EditText ou;
    EditText o;
    EditText l;
    EditText st;
    EditText c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_user_input);

        userInfo = ((X509Application) getApplication()).getUserInfo();

        cn = ( EditText ) findViewById(R.id.cnEdit);
        ou = ( EditText ) findViewById(R.id.ouEdit);
        o = ( EditText ) findViewById(R.id.oEdit);
        l = ( EditText ) findViewById(R.id.lEdit);
        st = ( EditText ) findViewById(R.id.stEdit);
        c = ( EditText ) findViewById(R.id.cEdit);

        Button nextPage = (Button) findViewById(R.id.nextPageButton);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setCn(cn.getText().toString());
                userInfo.setOu(ou.getText().toString());
                userInfo.setO(o.getText().toString());
                userInfo.setL(l.getText().toString());
                userInfo.setSt(st.getText().toString());
                userInfo.setC(c.getText().toString());
                Intent i = new Intent(getBaseContext(), ExtensionUserInput.class);
                startActivity(i);
            }
        });





    }

}
