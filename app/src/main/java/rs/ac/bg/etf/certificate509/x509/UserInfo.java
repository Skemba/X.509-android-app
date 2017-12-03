package rs.ac.bg.etf.certificate509.x509;

import android.widget.CheckBox;

import java.math.BigInteger;
import java.security.KeyPair;

/**
 * Created by Aleksa on 28.05.2016..
 */
public class UserInfo
{
    private String keyPairName = "";
    private int keySize = 13;
    private BigInteger serialNumber = new BigInteger("1");
    private String version = "v3";
    private String expirationDate;
    private String cn="";
    private String ou="";
    private String o="";
    private String e="";
    private String st="";
    private String l="";
    private String c="";
    private boolean isCA = false;
    private KeyPair keyPair;
    private int pathLen = 1;
    private String email ="", dns="", ipAddress="", uri="";
    private boolean basicConstrainsCritical = false;
    private boolean issuerAlternativeNameCritical = false, IssuerAlternativeNameAdded = false;
    private boolean keyUsageCritical = false, keyUsageAdded = false;
    boolean digitalSignature = false, nonRepudiation = false, keyEncipherment = false, dataEncipherment = false;
    boolean keyAggrement = false, encipherOnly = false, crlIssuer = false, keyCertSigned = false, decipherOnly = false;

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean issuerAlternativeNameAdded() {
        return IssuerAlternativeNameAdded;
    }

    public void setIssuerAlternativeNameAdded(boolean issuerAlternativeNameAdded) {
        IssuerAlternativeNameAdded = issuerAlternativeNameAdded;
    }

    public boolean isKeyUsageAdded() {
        return keyUsageAdded;
    }

    public void setKeyUsageAdded(boolean keyUsageAdded) {
        this.keyUsageAdded = keyUsageAdded;
    }

    public boolean isCA() {
        return isCA;
    }

    public void setIsCA(boolean isCA) {

        this.isCA = isCA;
    }

    public int getPathLen() {
        return pathLen;
    }

    public void setPathLen(int pathLen) {
        this.pathLen = pathLen;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getExpirationDate() {
        if ( expirationDate.contains("month") )
        {
            return Integer.parseInt(expirationDate.substring(0, 1));
        }
        else
        {
            return 12;
        }
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public boolean isBasicConstrainsCritical() {
        return basicConstrainsCritical;
    }

    public void setBasicConstrainsCritical(boolean basicConstrainsCritical) {
        this.basicConstrainsCritical = basicConstrainsCritical;
    }

    public boolean issuerAlternativeNameCritical() {
        return issuerAlternativeNameCritical;
    }

    public void setIssuerAlternativeNameCritical(boolean issuerAlternativeNameCritical) {
        this.issuerAlternativeNameCritical = issuerAlternativeNameCritical;
    }

    public boolean isKeyUsageCritical() {
        return keyUsageCritical;
    }

    public void setKeyUsageCritical(boolean keyUsageCritical) {
        this.keyUsageCritical = keyUsageCritical;
    }

    public boolean isDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public boolean isNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public boolean isKeyEncipherment() {
        return keyEncipherment;
    }

    public void setKeyEncipherment(boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public boolean isDataEncipherment() {
        return dataEncipherment;
    }

    public void setDataEncipherment(boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public boolean isKeyAggrement() {
        return keyAggrement;
    }

    public void setKeyAggrement(boolean keyAggrement) {
        this.keyAggrement = keyAggrement;
    }

    public boolean isEncipherOnly() {
        return encipherOnly;
    }

    public void setEncipherOnly(boolean encipherOnly) {
        this.encipherOnly = encipherOnly;
    }

    public boolean isCrlIssuer() {
        return crlIssuer;
    }

    public void setCrlIssuer(boolean crlIssuer) {
        this.crlIssuer = crlIssuer;
    }

    public boolean isKeyCertSigned() {
        return keyCertSigned;
    }

    public void setKeyCertSigned(boolean keyCertSigned) {
        this.keyCertSigned = keyCertSigned;
    }

    public boolean isDecipherOnly() {
        return decipherOnly;
    }

    public void setDecipherOnly(boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }
}
