package com.xll.common.utils.base;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class SSLConnetionUtils {
    private static final String upopHost = "query.unionpaysecure.com";
    private static final int HTTPS_PORT = 443;
    private static final String certFileAlias = "unionpaysecure";
    private static final String trustStorePassword = "changeit";
    private static final String JAVA_HOME = "C:\\Program Files\\Java\\jdk1.6.0_21\\jre";
    private static final String JAVA_HOME_SYS = System.getProperty("java.home");
    private static final String trustStoreFileRelativePath = "/lib/security/cacerts";

    public SSLConnetionUtils() {
    }

    public static void importCertificate(String jksPath, String jksPassword) {
        try {
            Certificate certificate = getUpopCertificate();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load((InputStream)null, jksPassword.toCharArray());
            setRemainCertificates(ks, jksPath, jksPassword);
            ks.setCertificateEntry("unionpaysecure", certificate);
            ks.store(new FileOutputStream(jksPath), jksPassword.toCharArray());
            System.out.println("Import Certificate unionpaysecure Successful!");
        } catch (Exception var4) {
            throw new RuntimeException("Exception while importing certificate to Java Key Store for unionpaysecure certificate.", var4);
        }
    }

    public static void importCertificate() {
        try {
            String jksPath = JAVA_HOME_SYS + "/lib/security/cacerts";
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load((InputStream)null, "changeit".toCharArray());
            setRemainCertificates(ks, jksPath, "changeit");
            Certificate certificate = getUpopCertificate();
            ks.setCertificateEntry("unionpaysecure", certificate);
            ks.store(new FileOutputStream(jksPath), "changeit".toCharArray());
            System.out.println("Import Certificate unionpaysecure Successful!");
        } catch (Exception var3) {
            throw new RuntimeException("Exception while importing certificate to Java Key Store for unionpaysecure certificate.", var3);
        }
    }

    private static void setRemainCertificates(KeyStore ks, String jksPath, String jksPassword) {
        try {
            FileInputStream in = new FileInputStream(jksPath);
            KeyStore ks4RemainCerts = KeyStore.getInstance("JKS");
            Certificate c = null;
            ks4RemainCerts.load(in, jksPassword.toCharArray());
            Enumeration e = ks4RemainCerts.aliases();

            while(e.hasMoreElements()) {
                String alias = (String)e.nextElement();
                c = ks4RemainCerts.getCertificate(alias);
                ks.setCertificateEntry(alias, c);
            }

        } catch (Exception var8) {
            throw new RuntimeException("Exception while setting remain certificates to Java Key Store for unionpaysecure certificate.", var8);
        }
    }

    public static void deleteCertificate(String alias, String jksPath, String jksPassword) {
        try {
            FileInputStream in = new FileInputStream(jksPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(in, jksPassword.toCharArray());
            if (ks.containsAlias(alias)) {
                ks.deleteEntry(alias);
                ks.store(new FileOutputStream(jksPath), jksPassword.toCharArray());
            }

        } catch (Exception var5) {
            throw new RuntimeException("Exception while deleting certificate from Java Key Store.", var5);
        }
    }

    public static void deleteCertificate(String alias) {
        try {
            String jksPath = JAVA_HOME_SYS + "/lib/security/cacerts";
            FileInputStream in = new FileInputStream(jksPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(in, "changeit".toCharArray());
            if (ks.containsAlias(alias)) {
                ks.deleteEntry(alias);
                ks.store(new FileOutputStream(jksPath), "changeit".toCharArray());
            }

        } catch (Exception var4) {
            throw new RuntimeException("Exception while deleting certificate from Java Key Store.", var4);
        }
    }

    public static void printCertificateInfo(String jksPath, String jksPassword) {
        try {
            FileInputStream in = new FileInputStream(jksPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(in, jksPassword.toCharArray());
            Enumeration e = ks.aliases();

            while(e.hasMoreElements()) {
                String alias = (String)e.nextElement();
                if ("unionpaysecure".equals(alias)) {
                    System.out.println("Your imported certificate " + alias);
                    Certificate c = ks.getCertificate(alias);
                    System.out.println("Content: " + c.toString());
                }
            }

        } catch (Exception var7) {
            throw new RuntimeException("Exception while printing certificates in Java Key Store.", var7);
        }
    }

    public static void printCertificateInfo() {
        try {
            String jksPath = JAVA_HOME_SYS + "/lib/security/cacerts";
            FileInputStream in = new FileInputStream(jksPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(in, "changeit".toCharArray());
            Enumeration e = ks.aliases();

            while(e.hasMoreElements()) {
                String alias = (String)e.nextElement();
                if ("unionpaysecure".equals(alias)) {
                    System.out.println("Your imported certificate " + alias);
                    Certificate c = ks.getCertificate(alias);
                    System.out.println("Content: " + c.toString());
                }
            }

        } catch (Exception var6) {
            throw new RuntimeException("Exception while printing certificates in Java Key Store.", var6);
        }
    }

    public static Certificate getUpopCertificate() {
        try {
            TrustManager trm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init((KeyManager[])null, new TrustManager[]{trm}, (SecureRandom)null);
            SSLSocketFactory factory = sc.getSocketFactory();
            SSLSocket socket = (SSLSocket)factory.createSocket("query.unionpaysecure.com", 443);
            socket.startHandshake();
            SSLSession session = socket.getSession();
            Certificate[] servercerts = session.getPeerCertificates();
            socket.close();
            return servercerts[0];
        } catch (Exception var6) {
            throw new RuntimeException("Exception while get upop certificate.", var6);
        }
    }

    public static void main(String[] args) throws Exception {
        deleteCertificate("unionpaysecure");
        importCertificate();
        printCertificateInfo();
    }
}