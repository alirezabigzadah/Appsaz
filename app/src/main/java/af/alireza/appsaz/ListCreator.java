package af.alireza.appsaz;

import af.alireza.appsaz.Utilities.Utilities;
import af.alireza.views.BiggroupButton2;
import af.alireza.views.MagicalTextView_Master;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ListCreator extends BaseActivity {
	LinearLayout content;
	String ID;
	Utilities utils = new Utilities();
    int filesCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.files);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		final ActionBar actionBar;
		actionBar = getSupportActionBar(); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.list_title);
		content = (LinearLayout) findViewById(R.id.filesContent);

		File createDire = new File(rootAssetsPath + "lists");
		if (createDire.isDirectory() || createDire.mkdirs()) {

		}
		File createDireMenu = new File(rootAssetsPath + "menu/");
		if (createDireMenu.isDirectory() || createDireMenu.mkdirs()) {

		}

		Refresh();
		MagicalTextView_Master ADD = (MagicalTextView_Master) findViewById(R.id.add);
		ADD.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                   if (filesCount > utils.listCount) {
                        showEndTrialDialog(ListCreator.this);
                        } else {
					ID = getSomeInt();
					final AppCompatDialog dialog = new AppCompatDialog(ListCreator.this);

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.files_enter_info);
					TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
					title.setText(getString(R.string.files_add_list));
                    TextInputLayout inputLayout = dialog.findViewById(R.id.input_layout);
                    inputLayout.setHint(getString(R.string.files_list_name));
					final EditText Title = (EditText) dialog.findViewById(R.id.fileName);

					Button dialog_Cancel = dialog.findViewById(R.id.cancel);
				    Button dialog_Save = dialog.findViewById(R.id.save);
					// if thebutton is clicked, close the custom dialog
					dialog_Cancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					dialog_Save.setOnClickListener(new View.OnClickListener() {	
							@Override
							public void onClick(View v) {
								if (Title.length() == 0) {
									Title.setError(getString(R.string.a_error_name));
								} else {
									final ProgressDialog progDailog = ProgressDialog.show(ListCreator.this, "", getString(R.string.a_loading), true);
									final Handler handler = new Handler() {
										public void handleMessage(Message msg) {
											if (new File(rootAssetsPath + "list" + ID).exists()) {
												String str = utils.readFile(rootAssetsPath + "lists");
												str = str + "{List<=>" + ID + "<=>Title" + Title.getText().toString() + "<=>Listlist" + ID + "}!#";
												if (str.startsWith("null")) {
													str = str.replace("null", "");
												}
												utils.writeFile(rootAssetsPath + "lists", str);
												messageOk(ListCreator.this, getString(R.string.a_added));
												Refresh();
												return;
											}
											showErrorDialog(ListCreator.this);
										}
									};
									new Thread() {
										public void run() {
											try {
												getFile(rootAssetsPath + "list" + ID);
											} catch (IOException e) {}
											DatabaseHelper myDb;
											myDb = new DatabaseHelper(ListCreator.this, "list" + ID); 

											myDb.open();
											myDb.updateThisList(myDb.getBG(), myDb.getActionBar(), Title.getText().toString(), myDb.getActionBarColor());
											String menu = "{ThisMenu<=>BackgroundDefault}!#";
											utils.writeFile(rootAssetsPath + "menu/menulist" + ID, menu);
											myDb.updateMenu(myDb.getMenuMood(), "menulist" + ID);
											myDb.close();
											handler.sendEmptyMessage(0);
											progDailog.dismiss();

										}
									}.start();
									dialog.dismiss();
								}

							}

						});

					dialog.show();
                        }
				}
			});
	}
	public void getFile(String out) throws IOException {
        OutputStream myOutput = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
		InputStream myInput = getApplicationContext().getAssets().open("list");
        while (true) {
            int lenght = myInput.read(buffer);
            if (lenght <= 0) {
                myInput.close();
                myOutput.flush();
                myOutput.close();
                return;
            }
            myOutput.write(buffer, 0, lenght);

        }
    }
	public void Refresh() {
        TextView viewCount = findViewById(R.id.files_count);
        filesCount = utils.getListCount("list", rootAssetsPath);
        viewCount.setText(String.valueOf(filesCount));
        
		LinearLayout empty = (LinearLayout) findViewById(R.id.emptyLayout);
		ImageView emptyImg = (ImageView) findViewById(R.id.emptyImg);
		TextView emptyText = (TextView) findViewById(R.id.emptyText);
		content.removeAllViews();
		final String data = utils.readFile(rootAssetsPath + "lists");
		String str = data;
		final String Info=str;
	//	str = str.trim();
		final String[] durarray = str.split("\\!\\#");
		for (String part : durarray) {
			BiggroupButton2 button;
			String part2=part;

			if (part2.contains("{List<=>")) {
				empty.setVisibility(View.GONE);
				final String[] code = part2.replace("}", "").split("<=>");
				button = new BiggroupButton2(this);
				final String text = code[2].replace("Title", "");
				final String Img = code[3].replace("List", "");
				button.setText(text);
				button.setLeftIcon(R.drawable.ic_lists);
				button.getLeftIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorTint));
				this.content.addView(button);
				button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(ListCreator.this, ListEditor.class);
							intent.putExtra("listName", Img);
							startActivity(intent);
						}
					});
				button.getRightIcon().setImageResource(R.drawable.ic_edit_file);
				button.getRightIcon().setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View p1) {
							final AppCompatDialog dialog = new AppCompatDialog(ListCreator.this);

							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
							dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
							dialog.setContentView(R.layout.files_enter_info);
							TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
							title.setText(text);

							final EditText Title = (EditText) dialog.findViewById(R.id.fileName);
							Title.setText(text);

							TextView dialog_Cancel = (TextView) dialog.findViewById(R.id.cancel);
							TextView dialog_Delete = (TextView) dialog.findViewById(R.id.delete);
							TextView dialog_Save = (TextView) dialog.findViewById(R.id.save);
							dialog_Delete.setVisibility(View.VISIBLE);
							dialog_Cancel.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

										dialog.dismiss();
									}
								});
							dialog_Save.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (Title.length() == 0) {
											Title.setError(getString(R.string.a_error_name));
										} else {
											String str = Info.replace("{List<=>" + code[1] + "<=>Title" + text + "<=>List" + Img + "}!#", "{List<=>" + code[1] + "<=>Title" + Title.getText().toString() + "<=>List" + Img + "}!#");
											utils.writeFile(rootAssetsPath + "lists", str);
											Refresh();
											dialog.dismiss();
										}
									}

								});
							dialog_Delete.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										String str = Info.replace("{List<=>" + code[1] + "<=>Title" + text + "<=>List" + Img + "}!#", "");		
										File file = new File(rootAssetsPath + "" + Img);
										file.delete();
										File fileMenu = new File(rootAssetsPath + "menu/menu" + Img);
										fileMenu.delete();
										utils.writeFile(rootAssetsPath + "lists", str);
										Refresh();
										dialog.dismiss();

									}

								});

							dialog.show();

						}});

			} else {
				empty.setVisibility(View.VISIBLE);
				emptyImg.setImageResource(R.drawable.ic_lists);
				emptyText.setText(getString(R.string.files_empty_lists));
			}
		}}
}
