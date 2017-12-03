package rs.ac.bg.etf.certificate509.x509;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportExportActivity extends AppCompatActivity {

    Button importButton, exportButton;
    Button buttonUp, chooseDirectory;
    TextView textFolder;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int EXPORT_DIALOG_ID = 0;
    static final int IMPORT_DIALOG_ID = 1;
    ListView dialogListView;

    File root;
    File curFolder;
    String path;

    private List<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);

        importButton = ( Button ) findViewById( R.id.importButton );
        exportButton = ( Button ) findViewById( R.id.exportButton );

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(EXPORT_DIALOG_ID);
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
        path = root.getPath();

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(IMPORT_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        Dialog dialog = null;

        switch (id)
        {
            case EXPORT_DIALOG_ID:
                dialog = new Dialog(ImportExportActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("File explorer");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                chooseDirectory = (Button) dialog.findViewById(R.id.savePath);
                chooseDirectory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExportDialogFragment fragment = new ExportDialogFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString("path", path);
                        fragment.setArguments(arguments);
                        fragment.show(getFragmentManager(), "tag");
                        dismissDialog(id);
                    }
                });

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });
                dialogListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if(selected.isDirectory())
                        {
                            path = selected.getPath();
                            ListDir(selected);
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Can't select file, once you choose directory press choose button", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case IMPORT_DIALOG_ID:
                dialog = new Dialog(ImportExportActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("File explorer");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                chooseDirectory = (Button) dialog.findViewById(R.id.savePath);
                chooseDirectory.setVisibility(View.INVISIBLE);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });
                dialogListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if(selected.isDirectory())
                        {
                            path = selected.getPath();
                            ListDir(selected);
                        }
                        else
                        {
                            if(!selected.getPath().contains(".p12"))
                            {
                                Toast.makeText(getBaseContext(), "Can't select this file, please select .p12 file", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                AlertDialog.Builder alert = new AlertDialog.Builder(ImportExportActivity.this);
                                LayoutInflater inflater= getLayoutInflater();
                                //this is what I did to added the layout to the alert dialog
                                View layout=inflater.inflate(R.layout.password_dialog,null);
                                alert.setView(layout);
                                final EditText passwordInput=(EditText)layout.findViewById(R.id.password_alert);
                                alert.setMessage("Enter password which protects the file")
                                        .setPositiveButton("Import", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO import logic
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        }
                    }
                });
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id)
        {
            case EXPORT_DIALOG_ID:
            case IMPORT_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }



    void ListDir(File f)
    {
        if(f.equals(root))
        {
            buttonUp.setEnabled(false);
        }
        else
        {
            buttonUp.setEnabled(true);
        }
        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file: files)
        {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);

        dialogListView.setAdapter(directoryList);
    }
}
