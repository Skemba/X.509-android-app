package rs.ac.bg.etf.certificate509.x509;

import android.app.Application;

import org.spongycastle.cert.X509CertificateHolder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Aleksa on 28.05.2016..
 */
public class X509Application extends Application
{

    UserInfo userInfo;
    CertificateGenerator certGen;
    public static String latestCertificate;
    static HashMap<String, KeyPair> keyPairHashMap = new HashMap<>();
    public static ArrayList<X509CertificateHolder> holderArrayList = new ArrayList<>();
    public static ArrayList<String> keyPairNameList = new ArrayList<>();
    public static KeyPair kp = generateCAKeyPair();

    public void setUserInfo (UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }
    public void setCertGen( CertificateGenerator certGen )
    {
        this.certGen = certGen;
    }
    public static void addHash(String key, KeyPair keyPair)
    {
        keyPairHashMap.put(key, keyPair);
        latestCertificate = key;
    }

    public static KeyPair getKP()
    {
        return  kp;
    }

    public static KeyPair generateCAKeyPair()
    {
        PrivateKey privKey;
        PublicKey pubKey;
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
        if( keyGen != null ) {
            keyGen.initialize(1024, sr);

            KeyPair keypair = keyGen.generateKeyPair();
            privKey = keypair.getPrivate();
            pubKey = keypair.getPublic();
            return keypair;
        }
        return null;
    }
    public static KeyPair getHash(String key)
    {
        return keyPairHashMap.get(key);
    }
    public static X509CertificateHolder getArray(KeyPair keyPair)
    {
        Iterator<X509CertificateHolder> iterator = holderArrayList.iterator();
        while (iterator.hasNext())
        {
            X509CertificateHolder holder = iterator.next();
            PublicKey publicKey= null;

            try
            {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(holder.getSubjectPublicKeyInfo().getEncoded()));
            }
            catch (InvalidKeySpecException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if((publicKey.hashCode() == (keyPair.getPublic()).hashCode()))
                return holder;

        }
        return null;
    }

    public static void addToHolderArrayList(X509CertificateHolder holder)
    {
        holderArrayList.add(holder);
    }

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public CertificateGenerator getCertGen()
    {
        return certGen;
    }

}
