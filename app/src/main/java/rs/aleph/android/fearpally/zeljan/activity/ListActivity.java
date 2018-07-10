package rs.aleph.android.fearpally.zeljan.activity;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import rs.aleph.android.fearpally.R;
import rs.aleph.android.fearpally.zeljan.db.ORMLightHelper;
import rs.aleph.android.fearpally.zeljan.db.model.Prijava;
import rs.aleph.android.fearpally.zeljan.dilog.AboutDialog;
import rs.aleph.android.fearpally.zeljan.preferences.Prefererences;

public class ListActivity extends AppCompatActivity {


    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;

    public static String ACTOR_KEY = "ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.prijava_list);

        try {
            List<Prijava> list = getDatabaseHelper().getPrijavaDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Prijava p = (Prijava) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, Detail.class);
                    intent.putExtra(ACTOR_KEY, p.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.prijava_list);

        if (listview != null){
            ArrayAdapter<Prijava> adapter = (ArrayAdapter<Prijava>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Prijava> list = getDatabaseHelper().getPrijavaDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Prijava Prekrsaja");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.priprema_add_new_prijava:
                final Dialog dialogAdd = new Dialog(this);
                dialogAdd.setContentView(R.layout.add_prijava_layout);



                Button add =(Button)dialogAdd.findViewById(R.id.add_prijava_add_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edNaslov=(EditText)dialogAdd.findViewById(R.id.prijava_name_add);
                        EditText edOpis=(EditText)dialogAdd.findViewById(R.id.prijava_kratki_opis_add);
                        EditText edStatus=(EditText)dialogAdd.findViewById(R.id.prijava_status_add);
                        EditText edDatum=(EditText)dialogAdd.findViewById(R.id.prijava_datum_add);

                        Prijava p =new Prijava();
                        p.setmName(edNaslov.getText().toString());
                        p.setmOpis(edOpis.getText().toString());
                        p.setmStatus(edStatus.getText().toString());
                        p.setmDatum(edDatum.getText().toString());

                        try {
                            getDatabaseHelper().getPrijavaDao().create(p);


                            //provera podesenja
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast){
                                Toast.makeText(ListActivity.this, "Dodata nova prijava", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Dodata nova prijava");
                            }

                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialogAdd.dismiss();
                    }
                });
                Button cancel = (Button) dialogAdd.findViewById(R.id.cancel_prijava_add_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd.dismiss();
                    }
                });

                dialogAdd.show();

                break;
            case R.id.priprema_about:
                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;

            case R.id.priprema_preferences:
                startActivity(new Intent(ListActivity.this, Prefererences.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
