package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.spongycastle.cert.X509CertificateHolder;

public class ExtensionUserInput extends AppCompatActivity {

    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3;

    Switch isCACertificate, isCriticalBasicContstraints, isCriticalIssuerName,isCriticalKeyUsage;
    EditText pathLen, issuerEmail, dnsName, ipAddress, uri;
    TextView pathLenText;
    TextView issuerEmailText, issuerDnsNameText, issuerIpAddressText, issuerURIText;
    CheckBox issuerAltName, useKeyUsage;
    CheckBox digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment;
    CheckBox keyAggrement, encipherOnly, crlIssuer, keyCertSigned, decipherOnly;
    Button generate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_user_input);

        final UserInfo userInfo = ((X509Application) getApplication()).getUserInfo();

        issuerEmailText = (TextView) findViewById(R.id.emailText);
        issuerDnsNameText = (TextView) findViewById(R.id.dnsNameText);
        issuerIpAddressText = (TextView) findViewById(R.id.ipAddressText);
        issuerURIText = (TextView) findViewById(R.id.uriText);

        issuerEmail = ( EditText ) findViewById(R.id.email);
        dnsName = ( EditText ) findViewById(R.id.dns);
        ipAddress = ( EditText ) findViewById(R.id.ipAddress);
        uri = ( EditText ) findViewById(R.id.uri);



        isCriticalBasicContstraints = (Switch) findViewById(R.id.isCriticalBasicConstrains);
        isCACertificate = (Switch) findViewById(R.id.isCA);
        isCriticalIssuerName = (Switch) findViewById(R.id.isCriticalIssuerAltName);
        isCriticalKeyUsage = (Switch) findViewById(R.id.isCriticalKeyUsage);

        digitalSignature = ( CheckBox ) findViewById( R.id.digSign );
        digitalSignature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setDigitalSignature(isChecked);
            }
        });
        nonRepudiation = ( CheckBox ) findViewById( R.id.nonRepudiation );
        nonRepudiation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setNonRepudiation(isChecked);
            }
        });
        keyEncipherment = ( CheckBox ) findViewById( R.id.keyEncipherment );
        keyEncipherment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setKeyEncipherment(isChecked);
            }
        });
        dataEncipherment = ( CheckBox ) findViewById( R.id.dataEncipherment );
        dataEncipherment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 userInfo.setDataEncipherment(isChecked);
            }
        });
        keyAggrement = ( CheckBox ) findViewById( R.id.keyAgreement );
        keyAggrement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setKeyAggrement(isChecked);
            }
        });
        encipherOnly = ( CheckBox ) findViewById( R.id.encipherOnly );
        encipherOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setEncipherOnly(isChecked);
            }
        });
        crlIssuer = ( CheckBox ) findViewById( R.id.cRLSign );
        crlIssuer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setCrlIssuer(isChecked);
            }
        });
        keyCertSigned = ( CheckBox ) findViewById( R.id.keyCertSign );
        keyCertSigned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setKeyCertSigned(isChecked);
            }
        });
        decipherOnly = ( CheckBox ) findViewById( R.id.decipherOnly );
        decipherOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setDecipherOnly(isChecked);
            }
        });

        pathLen = (EditText) findViewById(R.id.pathLen);
        pathLenText = (TextView) findViewById(R.id.pathLenText);

        isCriticalBasicContstraints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    userInfo.setBasicConstrainsCritical(isChecked);
            }
        });

        isCACertificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked )
                {
                    userInfo.setIsCA(true);
                    pathLenText.setVisibility(View.VISIBLE);
                    pathLen.setVisibility(View.VISIBLE);
                }
                else
                {
                    userInfo.setIsCA(false);
                    pathLenText.setVisibility(View.INVISIBLE);
                    pathLen.setVisibility(View.INVISIBLE);
                }
            }
        });

        generate = ( Button ) findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(issuerEmail.getText().toString())) userInfo.setEmail(issuerEmail.getText().toString());
                if(!TextUtils.isEmpty(dnsName.getText().toString())) userInfo.setDns(dnsName.getText().toString());
                if(!TextUtils.isEmpty(ipAddress.getText().toString())) userInfo.setIpAddress(ipAddress.getText().toString());
                if(!TextUtils.isEmpty(uri.getText().toString())) userInfo.setUri(uri.getText().toString());
                if(!TextUtils.isEmpty(pathLen.getText().toString())) userInfo.setPathLen(Integer.parseInt((pathLen.getText().toString())));
                ((X509Application) getApplication()).setUserInfo(userInfo);
                CertificateGenerator certGen = new CertificateGenerator();
                ((X509Application) getApplication()).setCertGen(certGen);
                certGen.generateKeys(userInfo.getKeySize(), userInfo);
                ((X509Application) getApplication()).addHash(userInfo.getKeyPairName(), userInfo.getKeyPair());
                X509Application.addToHolderArrayList(certGen.generateCertificate(userInfo));
                //For testing only
                X509CertificateHolder holder = X509Application.getArray(userInfo.getKeyPair());
                //
                X509Application.keyPairNameList.add(userInfo.getKeyPairName());
                Intent intent = new Intent(getBaseContext(), MainMenu.class);
                startActivity(intent);
            }
        });

        useKeyUsage = (CheckBox) findViewById(R.id.useKeyUsage);
        useKeyUsage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setKeyUsageAdded(isChecked);
                if (isChecked) {
                    isCriticalKeyUsage.setVisibility(View.VISIBLE);
                    digitalSignature.setVisibility(View.VISIBLE);
                    nonRepudiation.setVisibility(View.VISIBLE);
                    keyEncipherment.setVisibility(View.VISIBLE);
                    dataEncipherment.setVisibility(View.VISIBLE);
                    encipherOnly.setVisibility(View.VISIBLE);
                    crlIssuer.setVisibility(View.VISIBLE);
                    keyCertSigned.setVisibility(View.VISIBLE);
                    decipherOnly.setVisibility(View.VISIBLE);
                    keyAggrement.setVisibility(View.VISIBLE);
                } else {
                    isCriticalKeyUsage.setVisibility(View.INVISIBLE);
                    digitalSignature.setVisibility(View.INVISIBLE);
                    nonRepudiation.setVisibility(View.INVISIBLE);
                    keyEncipherment.setVisibility(View.INVISIBLE);
                    dataEncipherment.setVisibility(View.INVISIBLE);
                    encipherOnly.setVisibility(View.INVISIBLE);
                    crlIssuer.setVisibility(View.INVISIBLE);
                    keyCertSigned.setVisibility(View.INVISIBLE);
                    decipherOnly.setVisibility(View.INVISIBLE);
                    keyAggrement.setVisibility(View.INVISIBLE);
                }
            }
        });

        issuerAltName = (CheckBox) findViewById(R.id.useIssuerAltName);
        issuerAltName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setIssuerAlternativeNameAdded(isChecked);
                if (isChecked) {
                    isCriticalIssuerName.setVisibility(View.VISIBLE);
                    issuerEmail.setVisibility(View.VISIBLE);
                    dnsName.setVisibility(View.VISIBLE);
                    ipAddress.setVisibility(View.VISIBLE);
                    uri.setVisibility(View.VISIBLE);
                    issuerEmailText.setVisibility(View.VISIBLE);
                    issuerDnsNameText.setVisibility(View.VISIBLE);
                    issuerIpAddressText.setVisibility(View.VISIBLE);
                    issuerURIText.setVisibility(View.VISIBLE);
                } else {
                    isCriticalIssuerName.setVisibility(View.INVISIBLE);
                    issuerEmail.setVisibility(View.INVISIBLE);
                    dnsName.setVisibility(View.INVISIBLE);
                    ipAddress.setVisibility(View.INVISIBLE);
                    uri.setVisibility(View.INVISIBLE);
                    issuerEmailText.setVisibility(View.INVISIBLE);
                    issuerDnsNameText.setVisibility(View.INVISIBLE);
                    issuerIpAddressText.setVisibility(View.INVISIBLE);
                    issuerURIText.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }


}
