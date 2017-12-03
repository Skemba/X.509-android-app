package rs.ac.bg.etf.certificate509.x509;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.asn1.x509.X509CertificateStructure;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.X509v3CertificateBuilder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.util.PrivateKeyFactory;
import org.spongycastle.jcajce.provider.asymmetric.X509;
import org.spongycastle.jce.X509Principal;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.spongycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.bc.BcRSAContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        X509Certificate certificate = null;
        try {
            certificate = new JcaX509CertificateConverter().setProvider("BC")
                    .getCertificate(X509Application.holderArrayList.get(0));
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        X500Principal subjectX500Principal = certificate.getSubjectX500Principal();


        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                subjectX500Principal, X509Application.getHash(X509Application.latestCertificate).getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA1withRSA");
        ContentSigner signer = null;
        try {
            signer = csBuilder.build(X509Application.getHash(X509Application.latestCertificate).getPrivate());
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
                new X500Name("CN=issuer"), new BigInteger("1"), new Date(
                System.currentTimeMillis()), new Date(
                System.currentTimeMillis() + 30 * 365 * 24 * 60 * 60
                        * 1000), csr.getSubject(), X509Application.getArray(X509Application.getHash(X509Application.latestCertificate)).getSubjectPublicKeyInfo());


        AsymmetricKeyParameter foo = null;
        try {
            foo = PrivateKeyFactory.createKey(X509Application.getKP().getPrivate()
                    .getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder()
                .find("SHA1withRSA");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder()
                .find(sigAlgId);


        ContentSigner sigGen = null;
        try {
            sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                    .build(foo);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }

        X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
        org.spongycastle.asn1.x509.Certificate eeX509CertificateStructure = holder.toASN1Structure();
        //in newer version of BC such as 1.51, this is
        //org.spongycastle.asn1.x509.Certificate eeX509CertificateStructure = holder.toASN1Structure();

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509", "BC");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        Provider pr = cf.getProvider();


        // Read Certificate
        InputStream is1 = null;
        try {
            is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
        X509Certificate theCert = null;
        try {
           theCert = (X509Certificate) cf.generateCertificate(is1);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            is1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getBaseContext(), "Certificate signed!", Toast.LENGTH_LONG).show();
        try {
            X509Application.addToHolderArrayList(new X509CertificateHolder(theCert.getEncoded()));
            X509Application.keyPairNameList.add(X509Application.latestCertificate + "Signed");
            X509Application.addHash(X509Application.latestCertificate + "Signed", X509Application.getKP());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }

    }
}
