package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Prefs;
import af.alireza.appsaz.Utilities.SignUtil;
import af.alireza.appsaz.Utilities.Utilities;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import sun1.security.x509.AlgorithmId;
import sun1.security.x509.CertificateAlgorithmId;
import sun1.security.x509.CertificateExtensions;
import sun1.security.x509.CertificateIssuerName;
import sun1.security.x509.CertificateSerialNumber;
import sun1.security.x509.CertificateSubjectName;
import sun1.security.x509.CertificateValidity;
import sun1.security.x509.CertificateVersion;
import sun1.security.x509.CertificateX509Key;
import sun1.security.x509.KeyIdentifier;
import sun1.security.x509.PrivateKeyUsageExtension;
import sun1.security.x509.SubjectKeyIdentifierExtension;
import sun1.security.x509.X500Name;
import sun1.security.x509.X509CertImpl;
import sun1.security.x509.X509CertInfo;

public class Preferences extends PreferenceFragmentCompat {

    private static final String SELECTED_LANGUAGE_ID = "SelectedLanguageID";
    private Prefs prefs = new Prefs();
    private Utilities utils = new Utilities();
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        filePickerLauncher =
		registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
		result -> {
			if (result.getResultCode() == getActivity().RESULT_OK
				&& result.getData() != null) {
				Uri selectedKeyUri = result.getData().getData();
				if (selectedKeyUri != null) {
					String keyPath = FilesPaths.getPath(getActivity(), selectedKeyUri);
					File f = utils.copyFileToCustomDirectory(getActivity(), selectedKeyUri, BaseActivity.buildDirectory, new File(keyPath).getName().toString());
					showKeystoreDialog(getActivity(), f.getAbsolutePath(), true);
				}
			}
		});
        addPreferencesFromResource(R.xml.preferences);
        Preference theme = findPreference("prefs_night_mode");
        theme.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					getActivity().recreate();
					return true;
				}
			});
        Preference lang = findPreference("prefs_language");
        lang.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					showLanguageDialog();
					return true;
				}
			});
        Preference key = findPreference("keystore");
        key.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (utils.checkPer(getActivity())) {
						showKeystoreDialog(getActivity(), getString(R.string.key_specs), false);
					}
					return true;
				}
			});
        Preference createkey = findPreference("gen_key");
        createkey.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (utils.checkPer(getActivity())) {
						showCreateKeyDialog(getActivity());
					}
					return true;
				}
			});
        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					BaseActivity.showAboutDialog(getActivity());
					return true;
				}
			});
        Preference faq = findPreference("faq");
        faq.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					BaseActivity.showFAQDialog(getActivity());
					return true;
				}
			});
        Preference pp = findPreference("pp");
        pp.setOnPreferenceClickListener(
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					showPrivacyPolicy(getActivity());
					return true;
				}
			});
    }
    
   private void showPrivacyPolicy(Activity context) {
    // ایجاد BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
    // بارگذاری فایل XML با LayoutInflater
         View sheetView = LayoutInflater.from(context).inflate(R.layout.dialog_privacy_policy, null);
    // پیدا کردن WebView و لود کردن فایل HTML
        WebView webView = sheetView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // فعال کردن جاوا اسکریپت (اختیاری)
// تنظیم پس‌زمینه شفاف
        webView.setBackgroundColor(0x00000000); // پس‌زمینه شفاف
webView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // استفاده از رندر سخت‌افزاری // اطمینان از رندر درست
        webView.loadUrl("file:///android_asset/privacy_policy.html"); // بارگذاری فایل از assets
    // تنظیم View در BottomSheetDialog
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
}
    
    private void showLanguageDialog() {
        String[] languages = {
            getString(R.string.a_language_default),
            getString(R.string.a_language_ru),
            getString(R.string.a_language_zh),
            getString(R.string.a_language_en),
            getString(R.string.a_language_hi),
            getString(R.string.a_language_ar),
            getString(R.string.a_language_fa),
            getString(R.string.a_language_dv),
            getString(R.string.a_language_ps)
        };
        String[] languageIds = {"default", "ru", "zh", "en", "hi", "ar", "fa", "dv", "ps"};
        String selectedLanguageId = prefs.getString(getActivity(), SELECTED_LANGUAGE_ID, "default");
        int checkedItemIndex = Arrays.asList(languageIds).indexOf(selectedLanguageId);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle(getString(R.string.a_select_language))
			.setSingleChoiceItems(languages, checkedItemIndex, null)
			.setPositiveButton(
			getString(R.string.a_save),
			(dialog, which) -> {
			int selectedPosition =
				((AlertDialog) dialog).getListView().getCheckedItemPosition();
			String newSelectedLanguageId = languageIds[selectedPosition];
			prefs.putString(
				getActivity(), SELECTED_LANGUAGE_ID, newSelectedLanguageId);
			getActivity().recreate();
		})
		.setNegativeButton(
			getString(R.string.a_cancel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private AlertDialog dialog1 = null;
    public void showKeystoreDialog(Context context, String keyPath1, boolean isResult) {
        View view = LayoutInflater.from(context).inflate(R.layout.keystore, null);
        Button btn_pick = view.findViewById(R.id.key_pick);
        TextView key_name = view.findViewById((R.id.key_name));
        Spinner format = view.findViewById((R.id.format));
        EditText key_path = view.findViewById((R.id.key_path));
        TextView cert = view.findViewById((R.id.cert));
        EditText alias = view.findViewById((R.id.alias));
        LinearLayout password = view.findViewById((R.id.password));
        EditText storePass = view.findViewById((R.id.storePass));
        EditText keyPass = view.findViewById((R.id.keyPass));

        int type = prefs.getInt(context, "key_type", 0);
        format.setSelection(type);
        String keyPath = prefs.getString(context, "key_path", "");
        if (isResult) {
            key_name.setText(new File(keyPath1).getName().toString());
            key_path.setText(keyPath1);
        } else {
            key_path.setText(keyPath);
        }
        String certOrAlias = prefs.getString(context, "key_alias", "");
        alias.setText(certOrAlias);
        String storePassValue = prefs.getString(context, "key_alias_pass", "");
        storePass.setText(storePassValue);
        String keyPassValue = prefs.getString(context, "key_pass", "");
        keyPass.setText(keyPassValue);

        btn_pick.setOnClickListener(
		v -> {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.setType("*/*"); // تمام انواع فایل‌ها
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			String[] mimeTypes = {"application/octet-stream", "application/x-jks"};
			intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
			filePickerLauncher.launch(intent);
			dialog1.dismiss();
		});
        format.setOnItemSelectedListener(
			new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(
					AdapterView<?> parent, View selectedView, int position, long id) {
					if (position == 3) {
						password.setVisibility(View.GONE);
						cert.setText(context.getString(R.string.key_cert_path));
					} else {
						password.setVisibility(View.VISIBLE);
						cert.setText(context.getString(R.string.key_alias));
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		dialog1 = new MaterialAlertDialogBuilder(context)
			.setTitle(R.string.key_file) // عنوان Dialog
			.setView(view) // تنظیم View به عنوان محتوای Dialog
			.setPositiveButton(
			R.string.a_ok,
			(dialog, which) -> {
			String filesDir = getContext().getExternalFilesDir(null).getPath();
			String unsignedApk = filesDir + "/test.apk";
			String signedApk = filesDir + "/test1.apk";
			String customKeyPath = key_path.getText().toString();
			String keyAlias = alias.getText().toString();
			String keyAliasPass = storePass.getText().toString();
			String keyPassword = keyPass.getText().toString();

			try {
				utils.copyFromAssets(getContext(), "te", unsignedApk);

				boolean isSigned =
					SignUtil.signWithJKS(
					getContext(),
					new File(unsignedApk),
					new File(signedApk),
					21,
					new File(customKeyPath),
					keyAlias,
					keyAliasPass,
					keyPassword);
				if (isSigned) {
					System.out.println("keystore accepted");
					prefs.putInt(
						context, "key_type", format.getSelectedItemPosition());
					prefs.putString(
						context, "key_path", key_path.getText().toString());
					prefs.putString(
						context, "key_alias", alias.getText().toString());
					prefs.putString(
						context,
						"key_alias_pass",
						storePass.getText().toString());
					prefs.putString(
						context, "key_pass", keyPass.getText().toString());
				} else {
					System.out.println("keystore rejected");
					error("Invalid keystore");
					if (isResult && keyPath == "" && new File(keyPath1).exists()) new File(keyPath1).delete();
				}
			} catch (IOException e) {
			}
			new File(unsignedApk).delete();
			new File(signedApk).delete();
		})
		.setNeutralButton(
			R.string.a_reset,
			(dialog, which) -> {
			prefs.putInt(context, "key_type", 0);
			prefs.putString(context, "key_path", "");
			prefs.putString(context, "key_alias", "");
			prefs.putString(context, "key_alias_pass", "");
			prefs.putString(context, "key_pass", "");

			if (new File(keyPath).exists()) new File(keyPath).delete();
		})
		.setNegativeButton(R.string.a_cancel, (dialog, which) -> {
			if (isResult && keyPath == "" && new File(keyPath1).exists()) new File(keyPath1).delete();
		})
		.show();
    }

    public static class KeyParam {
        public String certOrAlias;
        public String commonName;
        public String country;
        public long days;
        public String keyPass;
        public String keyPath;
        public int keySize;
        public String localityName;
        public String organizationName;
        public String organizationUnit;
        public String stateName;
        public String storePass;
        public int type;
    }

    private EditText alias;
    private TextView cert;
    private EditText commonName;
    private EditText country;
    private Button create;
    private Button createAndUse;
    private EditText date;
    private Spinner format;
    private boolean isChanging = false;
    private EditText keyPass;
    private EditText keySize;
    private EditText localityName;
    private EditText organizationName;
    private EditText organizationUnit;
    private View password;
    private EditText path;
    private EditText stateName;
    private EditText storePass;

    public void showCreateKeyDialog(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.genkey, null);

        format = dialogView.findViewById(R.id.format);
        path = dialogView.findViewById(R.id.path);
        alias = dialogView.findViewById(R.id.alias);
        storePass = dialogView.findViewById(R.id.storePass);
        keyPass = dialogView.findViewById(R.id.keyPass);
        commonName = dialogView.findViewById(R.id.name);
        organizationUnit = dialogView.findViewById(R.id.organizationUnit);
        organizationName = dialogView.findViewById(R.id.organizationName);
        localityName = dialogView.findViewById(R.id.localityName);
        stateName = dialogView.findViewById(R.id.stateName);
        country = dialogView.findViewById(R.id.country);
        date = dialogView.findViewById(R.id.date);
        cert = dialogView.findViewById(R.id.cert);
        keySize = dialogView.findViewById(R.id.keySize);
        password = dialogView.findViewById(R.id.password);

        TextWatcher textWatcher =
			new TextWatcher() {
			@Override
			public void beforeTextChanged(
				CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				setButton();
				if (format.getSelectedItemPosition() == 3) {
					setAlias();
				}
			}
		};

        alias.addTextChangedListener(textWatcher);
        path.addTextChangedListener(textWatcher);
        storePass.addTextChangedListener(textWatcher);
        date.addTextChangedListener(textWatcher);

        format.setOnItemSelectedListener(
			new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(
					AdapterView<?> parent, View view, int position, long id) {
					switch (position) {
						case 0:
						case 1:
						case 2:
							cert.setText(R.string.key_alias);
							password.setVisibility(View.VISIBLE);
							if (alias.getText().toString().startsWith("/")) {
								alias.setText("");
							}
							break;
						case 3:
							setAlias();
							cert.setText(R.string.key_cert_path);
							password.setVisibility(View.GONE);
							keyPass.setText("");
							storePass.setText("");
							break;
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});

        MaterialAlertDialogBuilder builder =
		new MaterialAlertDialogBuilder(context)
		.setTitle(R.string.key_gen)
		.setView(dialogView)
		.setPositiveButton(
		context.getString(R.string.a_create),
		(dialog, which) -> {
			KeyParam param = save(false);
			if (param != null) {
				dialog.dismiss();
			}
		})
		.setNeutralButton(
			context.getString(R.string.key_create_and_use),
			(dialog, which) -> {
			KeyParam param = save(true);
			if (param != null) {
				dialog.dismiss();
				prefs.putInt(context, "key_type", param.type);
				prefs.putString(context, "key_path", param.keyPath);
				prefs.putString(context, "key_alias", param.certOrAlias);
				prefs.putString(context, "key_alias_pass", param.storePass);
				prefs.putString(context, "key_pass", param.keyPass);
                    
			}
		})
		.setNegativeButton(context.getString(R.string.a_cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();

        this.create = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        this.createAndUse = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        setButton();
    }

    private void setButton() {
        boolean z = true;
           if (checkEmpty(this.path)
			|| checkEmpty(this.alias)
			|| checkEmpty(this.date)
			|| (this.format.getSelectedItemPosition() != 3 && checkEmpty(this.storePass))) {
            z = false;
        }
        this.create.setEnabled(z);
        this.createAndUse.setEnabled(z);
    }

    private boolean checkEmpty(EditText editText) {
        return editText.getText().length() == 0;
    }

    private void setAlias() {
        if (!this.isChanging) {
            CharSequence charSequence;
            this.isChanging = true;
            String obj = this.path.getText().toString();
            if (obj.isEmpty()) {
                charSequence = "";
            } else if (obj.endsWith(".pk8")) {
                charSequence = obj.substring(0, obj.length() - 3) + "x509.pem";
            } else {
                charSequence = obj + ".x509.pem";
            }
            this.alias.setText(charSequence);
            this.isChanging = false;
        }
    }

    private KeyParam save(boolean isUse) {
        File key = new File(BaseActivity.buildDirectory, "Appsaz_Keystore_" + utils.getCurrentDateTime() + ".jks");
        
        int i = 0;
        KeyParam keyParam = new KeyParam();
        int selectedItemPosition = this.format.getSelectedItemPosition();
        String obj = this.keySize.getText().toString();
        if (obj.equals("")) {
            keyParam.keySize = 2048;
        } else {
            keyParam.keySize = Integer.parseInt(obj);
        }
        keyParam.type = selectedItemPosition;
       // keyParam.keyPath = this.path.getText().toString();
        keyParam.keyPath = key.getAbsolutePath();
        
        keyParam.certOrAlias = this.alias.getText().toString();
        keyParam.storePass = this.storePass.getText().toString();
        keyParam.keyPass = this.keyPass.getText().toString();
        keyParam.commonName = this.commonName.getText().toString();
        keyParam.organizationUnit = this.organizationUnit.getText().toString();
        keyParam.organizationName = this.organizationName.getText().toString();
        keyParam.localityName = this.localityName.getText().toString();
        keyParam.stateName = this.stateName.getText().toString();
        keyParam.country = this.country.getText().toString();
        keyParam.days = Long.parseLong(this.date.getText().toString()) * 365;
        try {
            generateKey(keyParam);
            if(isUse){
            copyFileToDownloads(getActivity(), key, "Appsaz", key.getName());
            File f = new File(prefs.getString(getActivity(), "key_path", ""));
            if(f.exists()) f.delete();
            }else{
            copyFileToDownloads(getActivity(), key, "Appsaz", key.getName());
            if(key.exists()) key.delete();
            }
           // Toast.makeText(getContext(), "Created", 0).show();
            return keyParam;
        } catch (Exception e) {
            StringBuilder certificateEncodingException = new StringBuilder(e.getMessage());
            StackTraceElement[] stackTrace = e.getStackTrace();
            int length = stackTrace.length;
            for (int i2 = 0; i2 < length; i2++) {
                certificateEncodingException.append('\n').append(stackTrace[i2]);
            }
            error(certificateEncodingException);
            return null;
        }
    }

    private X509Certificate generateCert(
		PrivateKey privateKey,
		PublicKey publicKey,
		X500Name x500Name,
		Date date,
		Date date2,
		CertificateExtensions certificateExtensions)
	throws CertificateEncodingException,
	CertificateException,
	IOException,
	NoSuchAlgorithmException,
	NoSuchProviderException,
	SignatureException,
	InvalidKeyException {
        String str = "SHA256withRSA";
        try {
            CertificateValidity certificateValidity = new CertificateValidity(date, date2);
            X509CertInfo x509CertInfo = new X509CertInfo();
            x509CertInfo.set("version", new CertificateVersion(2));
            x509CertInfo.set(
				"serialNumber",
				new CertificateSerialNumber(new Random().nextInt() & Integer.MAX_VALUE));
            x509CertInfo.set("algorithmID", new CertificateAlgorithmId(AlgorithmId.get(str)));
            x509CertInfo.set("subject", new CertificateSubjectName(x500Name));
            x509CertInfo.set("key", new CertificateX509Key(publicKey));
            x509CertInfo.set("validity", certificateValidity);
            x509CertInfo.set("issuer", new CertificateIssuerName(x500Name));
            if (certificateExtensions != null) {
                x509CertInfo.set("extensions", certificateExtensions);
            }
            X509CertImpl x509CertImpl = new X509CertImpl(x509CertInfo);
            x509CertImpl.sign(privateKey, str);
            return x509CertImpl;
        } catch (IOException e) {
            throw new CertificateEncodingException("getSelfCert: " + e.getMessage());
        }
    }

    private void write(PrivateKey privateKey, X509Certificate x509Certificate, KeyParam keyParam)
	throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        File keyFile = new File(keyParam.keyPath);
        if (!keyFile.getParentFile().exists()) {
            keyFile.getParentFile().mkdirs();
        }
        int i = keyParam.type;
        if (i < 3) {
            KeyStore instance =
				i == 0
				? KeyStore.getInstance("JKS")
				: i == 1 ? KeyStore.getInstance("PKCS12") : KeyStore.getInstance("BKS");
            if (keyFile.exists()) {
                instance.load(new FileInputStream(keyFile), keyParam.storePass.toCharArray());
            } else {
                instance.load(null);
            }
            instance.setKeyEntry(
				keyParam.certOrAlias,
				privateKey,
				keyParam.keyPass.toCharArray(),
				new X509Certificate[] {x509Certificate});
            instance.store(new FileOutputStream(keyFile), keyParam.storePass.toCharArray());
        } else if (i == 3) {
            FileOutputStream key = new FileOutputStream(keyFile);
            key.write(privateKey.getEncoded());
            key.close();
            FileOutputStream cery = new FileOutputStream(keyParam.certOrAlias);
            cery.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            byte[] encoded = Base64.encode(x509Certificate.getEncoded(), Base64.DEFAULT);
            cery.write(encoded);
            cery.write("\n-----END CERTIFICATE-----\n".getBytes());
            cery.flush();
        }
    }

    private void generateKey(KeyParam keyParam) throws Exception {
        KeyPairGenerator instance = KeyPairGenerator.getInstance("RSA");
        instance.initialize(keyParam.keySize, SecureRandom.getInstance("SHA1PRNG"));
        KeyPair generateKeyPair = instance.generateKeyPair();
        PublicKey publicKey = generateKeyPair.getPublic();
        PrivateKey privateKey = generateKeyPair.getPrivate();
        CertificateExtensions certificateExtensions = new CertificateExtensions();
        certificateExtensions.set(
			"SubjectKeyIdentifier",
			new SubjectKeyIdentifierExtension(new KeyIdentifier(publicKey).getIdentifier()));
        X500Name x500Name =
			new X500Name(
			keyParam.commonName,
			keyParam.organizationUnit,
			keyParam.organizationName,
			keyParam.localityName,
			keyParam.stateName,
			keyParam.country);
        Date date = new Date();
        long j = (keyParam.days * 24) * 3600000;
        Date date2 = new Date();
        date2.setTime(j + date.getTime());
        certificateExtensions.set("PrivateKeyUsage", new PrivateKeyUsageExtension(date, date2));
        write(
			privateKey,
			generateCert(privateKey, publicKey, x500Name, date, date2, certificateExtensions),
			keyParam);
    }

    protected final void error(CharSequence charSequence) {
        new MaterialAlertDialogBuilder(getActivity())
			.setTitle(getString(R.string.a_error))
			.setMessage(charSequence)
			.setPositiveButton(
			getString(R.string.a_ok),
			(dialog, which) -> {
			//    showKeystoreDialog(getActivity());
		})
		.setCancelable(false)
			.show();
    }
    public void copyFileToDownloads(Context context, File sourceFile, String folderName, String newFileName) {
    if (!sourceFile.exists()) {
      System.out.println("Source file does not exist.");
      return;
    }
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // برای اندروید 10 و بالاتر
        ContentResolver resolver = context.getContentResolver();
        Uri downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, newFileName); // نام فایل جدید
        contentValues.put(
            MediaStore.MediaColumns.MIME_TYPE, getMimeType(sourceFile.getName())); // نوع فایل
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS + "/" + folderName);
        Uri newFileUri = resolver.insert(downloadsUri, contentValues);
        if (newFileUri != null) {
          try (InputStream inputStream = new FileInputStream(sourceFile);
              OutputStream outputStream = resolver.openOutputStream(newFileUri)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
              outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            System.out.println("File successfully moved to Downloads/" + folderName);
          }
        } else {
          System.err.println("Failed to create file in Downloads/" + folderName);
        }
        String realPath = FilesPaths.getPath(context, newFileUri);
          new MaterialAlertDialogBuilder(context)
              .setTitle(getString(R.string.a_done))
              .setMessage(getString(R.string.a_saved_to) + realPath)
              .setPositiveButton(
                  getString(R.string.a_ok),
                  new DialogInterface.OnClickListener() {
                    // @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
              .show();
      } else {
        // برای اندروید 9 و پایین‌تر
        File downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File targetDir = new File(downloadsDir, folderName);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
          System.err.println("Failed to create target directory.");
          return;
        }
        File targetFile = new File(targetDir, newFileName);
        try (InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(targetFile)) {
          byte[] buffer = new byte[1024];
          int length;
          while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
          }
          outputStream.flush();
          System.out.println("File successfully moved to Downloads/" + folderName);
        }
        String realPath = targetFile.getAbsolutePath();
          new MaterialAlertDialogBuilder(context)
              .setTitle(getString(R.string.a_done))
              .setMessage(getString(R.string.a_saved_to) + realPath)
              .setPositiveButton(
                  getString(R.string.a_ok),
                  new DialogInterface.OnClickListener() {
                    // @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
              .show();
        }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error while moving file: " + e.getMessage());
    }
  }

  // متد برای استخراج نوع فایل
  private String getMimeType(String fileName) {
    String mimeType = "application/octet-stream"; // مقدار پیش‌فرض برای فایل‌های ناشناخته
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0) {
      String extension = fileName.substring(dotIndex + 1).toLowerCase();
      mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    return mimeType;
  }
}