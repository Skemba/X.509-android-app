package rs.ac.bg.etf.certificate509.x509;

import android.text.TextUtils;
import android.util.Log;

import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x509.BasicConstraints;
import org.spongycastle.asn1.x509.GeneralName;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.asn1.x509.X509Extension;
import org.spongycastle.asn1.x509.X509Extensions;
import org.spongycastle.cert.CertIOException;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.X509v3CertificateBuilder;
import org.spongycastle.jce.X509KeyUsage;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;


public class CertificateGenerator
{


    PrivateKey privKey;
    PublicKey pubKey;

    public void generateKeys(int keySize, UserInfo userInfo)
    {
        SecureRandom sr = new SecureRandom();

        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if( keyGen != null )
        {
            keyGen.initialize(keySize, sr);

            KeyPair keypair = keyGen.generateKeyPair();
            privKey = keypair.getPrivate();
            pubKey = keypair.getPublic();

            userInfo.setKeyPair(keypair);
        }
    }

    public X509CertificateHolder generateCertificate(UserInfo userInfo)
    {
        X500Name dnName = new X500Name("C="+userInfo.getC()+
                ", ST="+userInfo.getSt()+
                ", L="+userInfo.getL()+
                ", O="+userInfo.getO()+
                ", OU="+userInfo.getOu()+
                ", CN="+userInfo.getCn());

        Date currentDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MONTH, userInfo.getExpirationDate());
        Date exp = cal.getTime();

        byte[] encoded = pubKey.getEncoded();
        SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(
                ASN1Sequence.getInstance(encoded));
        Log.w("tag", "private key is: "+privKey+" and public is: "+pubKey);

        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(new X500Name("cn=etf"), userInfo.getSerialNumber(), currentDate,
                exp, dnName, subjectPublicKeyInfo  );

        if ( userInfo.isCA() )
        {
            try {
                builder.addExtension(X509Extensions.BasicConstraints, userInfo.isBasicConstrainsCritical(), new BasicConstraints(userInfo.getPathLen()));
            } catch (CertIOException e) {
                e.printStackTrace();
            }
        }

        if( userInfo.issuerAlternativeNameAdded() )
        {
            GeneralName email= null;
            GeneralName dns = null;
            GeneralName ipAddress = null;
            GeneralName uri = null ;
            int iNum = 0;
            if (!TextUtils.isEmpty(userInfo.getEmail()))
            {
                email = new GeneralName(GeneralName.rfc822Name, userInfo.getEmail());
                iNum++;
            }
            if(!TextUtils.isEmpty(userInfo.getDns()))
            {
                dns = new GeneralName(GeneralName.dNSName, userInfo.getDns());
                iNum++;
            }

            if(!TextUtils.isEmpty(userInfo.getIpAddress()))
            {
                try {
                    ipAddress = new GeneralName(GeneralName.iPAddress, userInfo.getIpAddress());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                iNum++;
            }

            if(!TextUtils.isEmpty(userInfo.getUri()))
            {
                uri = new GeneralName(GeneralName.uniformResourceIdentifier, userInfo.getUri());
                iNum++;
            }
            ASN1Encodable[] issuerAltName = new ASN1Encodable[iNum];
            int j = 0;
            for ( int i = 0; j < iNum && i < 4; i++ )
            {
                switch (i)
                {
                    case 0:
                        if(email != null)
                        {
                            issuerAltName[j] = email;
                            j++;
                        }
                        break;
                    case 1:
                        if(dns != null)
                        {
                            issuerAltName[j] = dns;
                            j++;
                        }
                        else
                        break;
                    case 2:
                        if(ipAddress != null)
                        {
                            issuerAltName[j] = ipAddress;
                            j++;
                        }
                        break;
                    case 3:
                        if(uri != null)
                        {
                            issuerAltName[j] = uri;
                            j++;
                        }
                        break;
                }
            }

            try {
                builder.addExtension(
                        X509Extensions.IssuerAlternativeName, userInfo.issuerAlternativeNameCritical(), new DERSequence(issuerAltName));
            } catch (CertIOException e) {
                e.printStackTrace();
            }
        }
        if(userInfo.isKeyUsageAdded())
        {
            int digitalSignature = 0x00, nonRepudiation = 0x00, keyEncipherment = 0x00, dataEncipherment = 0x00;
            int keyAggrement = 0x00, encipherOnly = 0x00, crlIssuer = 0x00, keyCertSigned = 0x00, decipherOnly = 0x00;
            if(userInfo.digitalSignature) digitalSignature = X509KeyUsage.digitalSignature;
            if(userInfo.nonRepudiation) nonRepudiation = X509KeyUsage.nonRepudiation;
            if(userInfo.keyEncipherment) keyEncipherment = X509KeyUsage.keyEncipherment;
            if(userInfo.dataEncipherment) dataEncipherment = X509KeyUsage.dataEncipherment;
            if(userInfo.keyAggrement) keyAggrement = X509KeyUsage.keyAgreement;
            if(userInfo.encipherOnly) encipherOnly = X509KeyUsage.encipherOnly;
            if(userInfo.crlIssuer) crlIssuer = X509KeyUsage.cRLSign;
            if(userInfo.keyCertSigned) keyCertSigned = X509KeyUsage.keyCertSign;
            if(userInfo.decipherOnly) decipherOnly = X509KeyUsage.decipherOnly;

            try {
                builder.addExtension(X509Extension.keyUsage, userInfo.isKeyUsageCritical(), new X509KeyUsage(
                        digitalSignature|nonRepudiation|keyEncipherment|dataEncipherment|keyAggrement|
                encipherOnly|crlIssuer|keyCertSigned|decipherOnly));
            } catch (CertIOException e) {
                e.printStackTrace();
            }
        }

        Log.w("tag", builder.toString());

        ContentSigner sigGen = null;

        try {
            sigGen = new JcaContentSignerBuilder("SHA1withRSA").build(privKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }

        X509CertificateHolder holder = builder.build(sigGen);
        Log.w("tag", holder.toString());

        return holder;
    }

}
