package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigInteger;

public class UserInput extends AppCompatActivity
{
    private UserInfo userInfo;
    EditText keySize;
    EditText serialNumber;
    EditText keyPairName;
    Spinner versionSpinner;
    Spinner dateSpinner;
    TextView errorTextVersion;
    TextView errorTextDate;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userInfo = new UserInfo();
        setContentView(R.layout.activity_user_input);
        keySize = (EditText) findViewById(R.id.keySizeEdit);
        serialNumber = (EditText) findViewById(R.id.serialNumberEdit);
        keyPairName = ( EditText ) findViewById(R.id.keyPairNameEdit);
        versionSpinner = (Spinner) findViewById(R.id.versionSpinner);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        Button nextPageButton = (Button) findViewById(R.id.nextPageButton);
        errorTextVersion = (TextView) findViewById(R.id.versionError);
        errorTextDate = (TextView) findViewById(R.id.dateError);

        nextPageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cnt = 0;
                String keyPairNameString = keyPairName.getText().toString();
                if (!TextUtils.isEmpty(keyPairNameString))
                {
                    cnt++;
                    userInfo.setKeyPairName(keyPairNameString);
                }
                else
                {
                    keyPairName.setError("This field is empty, please enter a name");
                }
                String keySizeString = keySize.getText().toString();
                if(!TextUtils.isEmpty(keySizeString))
                {
                    if( (Integer.parseInt(keySizeString) >= 1024 ) )
                    {
                    cnt++;
                    userInfo.setKeySize(Integer.parseInt(keySizeString));
                    }
                    else
                    {
                        keySize.setError("This field is empty or key size is too small, please enter number greater than 1023");
                    }
                }
                else
                {
                    keySize.setError("This field is empty or key size is too small, please enter number greater than 1023");
                }
                String serialNumberString = serialNumber.getText().toString();
                if(!TextUtils.isEmpty(serialNumberString))
                {
                    cnt++;
                    userInfo.setSerialNumber( new BigInteger(serialNumberString) );
                }
                else
                {
                    serialNumber.setError("This field is empty, please enter a number");
                }
                String versionString = versionSpinner.getSelectedItem().toString();
                if ( versionString.equals("v3") )
                {
                    cnt++;
                    userInfo.setVersion(versionString);
                    errorTextVersion.setVisibility(View.INVISIBLE);
                }
                else
                {
                    errorTextVersion.setVisibility(View.VISIBLE);
                }
                String dateString = dateSpinner.getSelectedItem().toString();
                if( !dateString.equals(getString(R.string.date_prompt)))
                {
                    cnt++;
                    userInfo.setExpirationDate(dateString);
                    errorTextDate.setVisibility(View.INVISIBLE);
                }
                else
                {
                    errorTextDate.setVisibility(View.VISIBLE);
                }
                if( cnt == 5 )
                {
                    ((X509Application) getApplication()).setUserInfo(userInfo);
                    Intent nextPageIntet = new Intent(getBaseContext(), AdditionalUserInput.class);
                    startActivity(nextPageIntet);
                }
            }
        });


    }
}
