package rs.ac.bg.etf.certificate509.x509;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.x500.AttributeTypeAndValue;
import org.spongycastle.asn1.x500.RDN;
import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameStyle;
import org.spongycastle.asn1.x500.style.RFC4519Style;
import org.spongycastle.asn1.x509.AuthorityKeyIdentifier;
import org.spongycastle.asn1.x509.BasicConstraints;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.asn1.x509.Extensions;
import org.spongycastle.asn1.x509.KeyUsage;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.util.PublicKeyFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ShowDetailsActivity extends AppCompatActivity {

    TextView certificateTextView;
    TextView dataTextView;
    TextView versionTextView;
    TextView serialNumberTextView;
    TextView sertificateAlgTextView;
    TextView issuerTextView;
    TextView validityTextView;
    TextView notBeforeTextView;
    TextView notAfterTextView;
    TextView subjectTextView;
    TextView subjectPubKeyInfoTextView;
    TextView pubKeyAlgTextView;
    TextView extentionsAllTextView;
    TextView modulsTextView;
    TextView exponentTextView;
    TextView signatureBitesTextView;
    boolean basicCritical = false;
    boolean issuerCritical = false;
    boolean usageCritical = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        serialNumberTextView = ( TextView ) findViewById(R.id.serialNumberDetails);
        issuerTextView = ( TextView ) findViewById(R.id.issuerDetails);
        notBeforeTextView = ( TextView ) findViewById(R.id.notBeforeDetails);
        notAfterTextView = ( TextView ) findViewById(R.id.notAfterDetails);
        subjectTextView = ( TextView ) findViewById(R.id.SubjectDetails);
        modulsTextView = ( TextView ) findViewById(R.id.modulsDetails);
        exponentTextView = ( TextView ) findViewById(R.id.exponentDetails);
        signatureBitesTextView = ( TextView ) findViewById(R.id.SignatureBytesDetails);
        extentionsAllTextView = (TextView) findViewById(R.id.extentionAllDetails);

        Intent intent = getIntent();
        String selectedString = intent.getStringExtra("selected");

        KeyPair keyPair = X509Application.getHash(selectedString);
        X509CertificateHolder holder = X509Application.getArray(keyPair);

        serialNumberTextView.setText("Serial number: " + holder.getSerialNumber());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        notBeforeTextView.setText("Not Before: " + df.format(holder.getNotBefore()) );
        notAfterTextView.setText("Not After: " + df.format(holder.getNotAfter()) );

        String issuerString = "Issuer: ";

        X500NameStyle x500NameStyle = RFC4519Style.INSTANCE;

        X500Name x500name = null;
        try {
            x500name = new JcaX509CertificateHolder( new JcaX509CertificateConverter().setProvider( "BC" )
                    .getCertificate( holder )).getIssuer();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        RDN[] rdns = x500name.getRDNs();
        for ( RDN rdn : rdns )
        {
            for ( AttributeTypeAndValue attribute : rdn.getTypesAndValues() )
            {
                issuerString = issuerString + x500NameStyle.oidToDisplayName( attribute.getType() )
                        + " = "+ attribute.getValue() + ",";
            }
        }
        issuerString = issuerString.substring(0,issuerString.length()-1);
        issuerTextView.setText(issuerString);

        String subjectString = "Subject: ";

        x500NameStyle = RFC4519Style.INSTANCE;

        x500name = null;
        try {
            x500name = new JcaX509CertificateHolder( new JcaX509CertificateConverter().setProvider( "BC" )
                    .getCertificate( holder )).getSubject();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        rdns = x500name.getRDNs();
        for ( RDN rdn : rdns )
        {
            for ( AttributeTypeAndValue attribute : rdn.getTypesAndValues() )
            {
                subjectString = subjectString + x500NameStyle.oidToDisplayName( attribute.getType() )
                        + " = " + attribute.getValue()+",";

            }
        }
        subjectString = subjectString.substring(0, subjectString.length()-1);
        subjectTextView.setText(subjectString);

        SubjectPublicKeyInfo keyInfo = holder.getSubjectPublicKeyInfo();
        RSAKeyParameters rsa = null;
        try {
            rsa = (RSAKeyParameters) PublicKeyFactory.createKey(keyInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BigInteger modulus = rsa.getModulus();
        BigInteger exponent = rsa.getExponent();

        modulsTextView.setText("Moduls: " + modulus);
        exponentTextView.setText("Exponent: " + exponent);

        signatureBitesTextView.setText(new String(holder.getSignature()));

        if( holder.hasExtensions() )
        {
            String extensionText = "X509v3 extensions:\n";
            Extensions extensions = holder.getExtensions();
            Extension extension = extensions.getExtension(extensions.getExtensionOIDs()[0]);
            Log.w("tag", extension.getParsedValue().toString());
            String oidBasic = Extension.basicConstraints.getId();
            String oidIssuer = Extension.issuerAlternativeName.getId();
            String oidUsage = Extension.keyUsage.getId();
            Set<String> critical = null;
            try {
                critical = new JcaX509CertificateConverter().setProvider("BC")
                        .getCertificate(holder).getCriticalExtensionOIDs();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            if(critical == null || critical.isEmpty())
            {
                byte[] extensionValue = null;
                try {
                    extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                            .getCertificate(holder).getExtensionValue(oidBasic);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
                if(extensionValue != null)
                {
                    ASN1OctetString akiOc = ASN1OctetString.getInstance(extensionValue);
                    BasicConstraints aki = BasicConstraints.getInstance(akiOc.getOctets());
                    extensionText += "X509v3 Basic Constraints: non-critical\n";
                    if(aki.isCA())
                    {
                        extensionText += "CA:true\n";
                        if(aki.getPathLenConstraint() != null)
                        {
                            extensionText += "pathLen: " + aki.getPathLenConstraint().toString() + "\n";
                        }
                    }
                }
                extensionValue = null;
                try {
                    extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                            .getCertificate(holder).getExtensionValue(oidIssuer);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
                if( extensionValue != null )
                {
                    String subjectaltnamestring="";
                    String separator = "";
                    Collection c= null;
                    try{

                    c =
                    new JcaX509CertificateConverter().setProvider("BC")
                            .getCertificate(holder).getIssuerAlternativeNames();

                    }
                    catch (CertificateException e)
                    {
                    e.printStackTrace();
                    }
                    if (c != null)
                    {
                        Iterator iter = c.iterator();
                        while (iter.hasNext()) {
                            List next = (List) iter.next();
                            int OID = ((Integer) next.get(0)).intValue();

                            switch (OID) {
                                case 0:
                                    // Already taken care of
                                    Object obj = next.get(1);
                                    if (obj != null) {
                                        subjectaltnamestring += separator + "OtherName=" + obj.toString();
                                        separator = ", ";
                                    }
                                    break;
                                case 1:
                                    subjectaltnamestring += separator + "RFC822Name="
                                            + (String) next.get(1);
                                    separator = ", ";
                                    break;
                                case 2:
                                    subjectaltnamestring += separator + "DNSName="
                                            + (String) next.get(1);
                                    separator = ", ";
                                    break;
                                case 6:
                                    if (!subjectaltnamestring.equals(""))
                                        subjectaltnamestring += ", ";
                                    subjectaltnamestring += separator + "URI="
                                            + (String) next.get(1);
                                    separator = ", ";
                                    break;
                                case 7:
                                    subjectaltnamestring += separator + "IPAddress="
                                            + (String) next.get(1);
                                    separator = ", ";
                                    break;
                            }
                            extensionText += "X509v3 Issuer Alternative Name: non-critical\n"+subjectaltnamestring+"\n";
                        }
                    }

                }
                extensionValue = null;
                try {
                    extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                            .getCertificate(holder).getExtensionValue(oidUsage);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
                if( extensionValue != null )
                {
                    boolean[] bits = null;
                    try {
                        bits = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getKeyUsage();
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    extensionText += "X509v3 Key Usage: non-critical\n";
                    if(bits != null)
                    for( int i = 0; i<9; i++ )
                    {
                        switch(i)
                        {
                            case 0:
                                if(bits[i])
                                    extensionText += "digitalSignature,";
                                break;
                            case 1:
                                if(bits[i])
                                    extensionText+="nonRepudiation,";
                                break;
                            case 2:
                                if(bits[i])
                                    extensionText+="keyEncipherment,";
                                break;
                            case 3:
                                if(bits[i])
                                    extensionText+="dataEncipherment,";
                                break;
                            case 4:
                                if(bits[i])
                                    extensionText+="keyAgreement,";
                                break;
                            case 5:
                                if(bits[i])
                                    extensionText+="keyCertSign,";
                                break;
                            case 6:
                                if(bits[i])
                                    extensionText+="crlSign,";
                                break;
                            case 7:
                                if(bits[i])
                                    extensionText+="encipherOnly,";
                                break;
                            case 8:
                                if(bits[i])
                                    extensionText+="decipherOnly,";
                                break;
                        }
                    }
                }
            }
            else
            {
                for(String oid : critical)
                {
                    if ( oidBasic.equals(oid) ) basicCritical = true;
                    if ( oidIssuer.equals(oid) ) issuerCritical = true;
                    if ( oidUsage.equals(oid) ) usageCritical = true;
                }
                if( basicCritical )
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidBasic);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if(extensionValue != null) {
                        ASN1OctetString akiOc = ASN1OctetString.getInstance(extensionValue);
                        BasicConstraints aki = BasicConstraints.getInstance(akiOc.getOctets());
                        extensionText += "X509v3 Basic Constraints: critical\n";
                        if (aki.isCA()) {
                            extensionText += "CA:true\n";
                            if (aki.getPathLenConstraint() != null) {
                                extensionText += "pathLen: " + aki.getPathLenConstraint().toString() + "\n";
                            }
                        }
                    }
                }
                else
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidBasic);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if(extensionValue != null)
                    {
                        ASN1OctetString akiOc = ASN1OctetString.getInstance(extensionValue);
                        BasicConstraints aki = BasicConstraints.getInstance(akiOc.getOctets());
                        extensionText += "X509v3 Basic Constraints: non-critical\n";
                        if(aki.isCA()) {
                            extensionText += "CA:true\n";
                            if (aki.getPathLenConstraint() != null) {
                                extensionText += "pathLen: " + aki.getPathLenConstraint().toString() + "\n";
                            }
                        }
                    }
                }
                if( issuerCritical )
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidIssuer);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if( extensionValue != null )
                    {
                        String subjectaltnamestring="";
                        String separator = "";
                        Collection c= null;
                        try{

                            c =
                                    new JcaX509CertificateConverter().setProvider("BC")
                                            .getCertificate(holder).getIssuerAlternativeNames();

                        }
                        catch (CertificateException e)
                        {
                            e.printStackTrace();
                        }
                        if (c != null)
                        {
                            Iterator iter = c.iterator();
                            while (iter.hasNext()) {
                                List next = (List) iter.next();
                                int OID = ((Integer) next.get(0)).intValue();

                                switch (OID) {
                                    case 0:
                                        // Already taken care of
                                        Object obj = next.get(1);
                                        if (obj != null) {
                                            subjectaltnamestring += separator + "OtherName=" + obj.toString();
                                            separator = ", ";
                                        }
                                        break;
                                    case 1:
                                        subjectaltnamestring += separator + "RFC822Name="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 2:
                                        subjectaltnamestring += separator + "DNSName="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 6:
                                        if (!subjectaltnamestring.equals(""))
                                            subjectaltnamestring += ", ";
                                        subjectaltnamestring += separator + "URI="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 7:
                                        subjectaltnamestring += separator + "IPAddress="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                }
                                extensionText += "X509v3 Issuer Alternative Name: critical\n"+subjectaltnamestring+"\n";
                            }
                        }

                    }
                }
                else
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidIssuer);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if( extensionValue != null )
                    {
                        String subjectaltnamestring="";
                        String separator = "";
                        Collection c= null;
                        try{

                            c =
                                    new JcaX509CertificateConverter().setProvider("BC")
                                            .getCertificate(holder).getIssuerAlternativeNames();

                        }
                        catch (CertificateException e)
                        {
                            e.printStackTrace();
                        }
                        if (c != null)
                        {
                            Iterator iter = c.iterator();
                            while (iter.hasNext()) {
                                List next = (List) iter.next();
                                int OID = ((Integer) next.get(0)).intValue();

                                switch (OID) {
                                    case 0:
                                        // Already taken care of
                                        Object obj = next.get(1);
                                        if (obj != null) {
                                            subjectaltnamestring += separator + "OtherName=" + obj.toString();
                                            separator = ", ";
                                        }
                                        break;
                                    case 1:
                                        subjectaltnamestring += separator + "RFC822Name="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 2:
                                        subjectaltnamestring += separator + "DNSName="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 6:
                                        if (!subjectaltnamestring.equals(""))
                                            subjectaltnamestring += ", ";
                                        subjectaltnamestring += separator + "URI="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                    case 7:
                                        subjectaltnamestring += separator + "IPAddress="
                                                + (String) next.get(1);
                                        separator = ", ";
                                        break;
                                }
                                extensionText += "X509v3 Issuer Alternative Name: non-critical\n"+subjectaltnamestring+"\n";
                            }
                        }

                    }
                }
                if ( usageCritical )
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidUsage);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if( extensionValue != null )
                    {
                        extensionText += "X509v3 Key Usage: critical\n";
                        boolean[] bits = null;
                        try {
                            bits = new JcaX509CertificateConverter().setProvider("BC")
                                    .getCertificate(holder).getKeyUsage();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                        if(bits != null)
                            for( int i = 0; i<9; i++ )
                            {
                                switch(i)
                                {
                                    case 0:
                                        if(bits[i])
                                            extensionText += "digitalSignature,";
                                        break;
                                    case 1:
                                        if(bits[i])
                                            extensionText+="nonRepudiation,";
                                        break;
                                    case 2:
                                        if(bits[i])
                                            extensionText+="keyEncipherment,";
                                        break;
                                    case 3:
                                        if(bits[i])
                                            extensionText+="dataEncipherment,";
                                        break;
                                    case 4:
                                        if(bits[i])
                                            extensionText+="keyAgreement,";
                                        break;
                                    case 5:
                                        if(bits[i])
                                            extensionText+="keyCertSign,";
                                        break;
                                    case 6:
                                        if(bits[i])
                                            extensionText+="crlSign,";
                                        break;
                                    case 7:
                                        if(bits[i])
                                            extensionText+="encipherOnly,";
                                        break;
                                    case 8:
                                        if(bits[i])
                                            extensionText+="decipherOnly,";
                                        break;
                                }
                            }
                    }
                }
                else
                {
                    byte[] extensionValue = null;
                    try {
                        extensionValue = new JcaX509CertificateConverter().setProvider("BC")
                                .getCertificate(holder).getExtensionValue(oidUsage);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                    if( extensionValue != null )
                    {
                        boolean[] bits = null;
                        try {
                            bits = new JcaX509CertificateConverter().setProvider("BC")
                                    .getCertificate(holder).getKeyUsage();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                        extensionText += "X509v3 Key Usage: non-critical\n";
                        if(bits != null)
                            for( int i = 0; i<9; i++ )
                            {
                                switch(i)
                                {
                                    case 0:
                                        if(bits[i])
                                            extensionText += "digitalSignature,";
                                        break;
                                    case 1:
                                        if(bits[i])
                                            extensionText+="nonRepudiation,";
                                        break;
                                    case 2:
                                        if(bits[i])
                                            extensionText+="keyEncipherment,";
                                        break;
                                    case 3:
                                        if(bits[i])
                                            extensionText+="dataEncipherment,";
                                        break;
                                    case 4:
                                        if(bits[i])
                                            extensionText+="keyAgreement,";
                                        break;
                                    case 5:
                                        if(bits[i])
                                            extensionText+="keyCertSign,";
                                        break;
                                    case 6:
                                        if(bits[i])
                                            extensionText+="crlSign,";
                                        break;
                                    case 7:
                                        if(bits[i])
                                            extensionText+="encipherOnly,";
                                        break;
                                    case 8:
                                        if(bits[i])
                                            extensionText+="decipherOnly,";
                                        break;
                                }
                            }
                    }
                }
            }
            extensionText = extensionText.substring(0, extensionText.length()-1) + "\n";
            extentionsAllTextView.setVisibility(View.VISIBLE);
            extentionsAllTextView.setText(extensionText);
        }



    }
}
