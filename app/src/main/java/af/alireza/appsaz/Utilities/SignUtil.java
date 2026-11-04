package af.alireza.appsaz.Utilities;

import android.content.Context;
import android.content.res.AssetManager;
import com.android.apksig.ApkSigner;
//import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import sun1.security.pkcs.PKCS8Key;

public class SignUtil {
	public static PrivateKey privateKey1;
	public static X509Certificate certificate1;

	public static void loadKey(AssetManager assets, Context context, LoadKeyCallback callback)
			throws IOException, InvalidKeyException, CertificateException {
		loadKey(assets);
		SignUtil st = new SignUtil();
		st.privateKey = st.privateKey1;
		st.certificate = st.certificate1;
		callback.call(st);
	}

	private static void loadKey(AssetManager assets) throws IOException, InvalidKeyException, CertificateException {
		loadPrivateKey(assets);
		loadCert(assets);
	}

	private static void loadCert(AssetManager assets) throws IOException, CertificateException {
		InputStream cert = assets.open("key/testkey.x509.pem");
		certificate1 = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(cert);
		cert.close();
	}

	private static void loadPrivateKey(AssetManager assets) throws IOException, InvalidKeyException {
		InputStream key = assets.open("key/testkey.pk8");
		PKCS8Key pkcs8 = new PKCS8Key();
		pkcs8.decode(key);
		privateKey1 = pkcs8;
		key.close();
	}

	private static void loadKey(SignUtil.LoadKeyCallback callback, KeyStore ks, String keyPath, String alias,
			char[] storePass, char[] keyPass) throws Exception {
		InputStream i = new FileInputStream(keyPath);
		ks.load(i, storePass);
		if (alias.isEmpty())
			alias = ks.aliases().nextElement();
		PrivateKey prk = (PrivateKey) ks.getKey(alias, keyPass);
		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		SignUtil st = new SignUtil();
		st.privateKey = prk;
		st.certificate = cert;
		callback.call(st);
	}

	private PrivateKey privateKey;
	private X509Certificate certificate;

	private SignUtil() {
	}

	public boolean sign(File in, File out, int minSdk) {
		ApkSigner.SignerConfig.Builder builder = new ApkSigner.SignerConfig.Builder("CERT", privateKey,
				Collections.singletonList(certificate));
		ApkSigner.SignerConfig signerConfig = builder.build();
		ApkSigner.Builder builder1 = new ApkSigner.Builder(Collections.singletonList(signerConfig));
		builder1.setInputApk(in);
		builder1.setOutputApk(out);
		builder1.setCreatedBy("Appsaz");
		builder1.setMinSdkVersion(minSdk);
		builder1.setV1SigningEnabled(true);
		builder1.setV2SigningEnabled(true);
		builder1.setV3SigningEnabled(true);
      //  builder1.setV4SigningEnabled(true);
		ApkSigner signer = builder1.build();
		try {
			signer.sign();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public interface LoadKeyCallback {
		void call(SignUtil signTool);
	}
public static boolean signWithJKS(Context c, File in, File out, int minSdk, File jksFile, String alias, String storePassword, String keyPassword) {
    try {
        // بارگذاری KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(jksFile)) {
            keyStore.load(fis, storePassword.toCharArray());
        }

        // استخراج کلید خصوصی و گواهی
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
        X509Certificate certificate =  (X509Certificate)  keyStore.getCertificate(alias);

        if (privateKey == null || certificate == null) {
            throw new Exception("Invalid keystore.");
        }

        // تنظیمات امضا
        ApkSigner.SignerConfig.Builder builder = new ApkSigner.SignerConfig.Builder("CERT", privateKey,
                Collections.singletonList(certificate));
        ApkSigner.SignerConfig signerConfig = builder.build();
        ApkSigner.Builder apkSignerBuilder = new ApkSigner.Builder(Collections.singletonList(signerConfig));
        apkSignerBuilder.setInputApk(in);
        apkSignerBuilder.setOutputApk(out);
        apkSignerBuilder.setCreatedBy("Appsaz");
        apkSignerBuilder.setMinSdkVersion(minSdk);
        apkSignerBuilder.setV1SigningEnabled(true);
        apkSignerBuilder.setV2SigningEnabled(true);
        apkSignerBuilder.setV3SigningEnabled(true);
        //apkSignerBuilder.setV4SigningEnabled(true);
		//apkSignerBuilder.setV4SignatureOutputFile(new File("/storage/emulated/0/ABC/output.apk.idsig"));
        // اجرای امضا
        ApkSigner signer = apkSignerBuilder.build();
        signer.sign();
        return true;
    } catch (Exception e) {
		  // new MaterialAlertDialogBuilder(c).setTitle(c.getString(R.string.a_error)).setMessage(e.getMessage().toString())
         // setPositiveButton(c.getString(R.string.a_ok), null).setCancelable(false).show();
        return false;
    }
}
}