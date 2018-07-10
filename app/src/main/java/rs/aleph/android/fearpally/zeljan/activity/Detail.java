package rs.aleph.android.fearpally.zeljan.activity;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import rs.aleph.android.fearpally.R;
import rs.aleph.android.fearpally.zeljan.db.ORMLightHelper;
import rs.aleph.android.fearpally.zeljan.db.model.Prijava;
import rs.aleph.android.fearpally.zeljan.db.model.Stavka;

import static rs.aleph.android.fearpally.zeljan.activity.ListActivity.NOTIF_STATUS;
import static rs.aleph.android.fearpally.zeljan.activity.ListActivity.NOTIF_TOAST;


public class Detail extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Prijava prijava;
    private Stavka stavka;

    public static String STAVKA_KEY = "STAVKA_KEY";


    private TextView tvNaslov;
    private TextView tvOpis;
    private TextView tvStatusPrijave;
    private TextView tvDatum;

    private ImageView tvSlika;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);


        try {
            prijava = getDatabaseHelper().getPrijavaDao().queryForId(key);

            tvNaslov = (TextView) findViewById(R.id.prijava_name);
            tvOpis = (TextView)findViewById(R.id.prijava_kratki_opis);
            tvStatusPrijave = (TextView) findViewById(R.id.prijava_status);
            tvDatum = (TextView) findViewById(R.id.prijava_datum);


            tvNaslov.setText(prijava.getmName());
            tvOpis.setText(prijava.getmOpis());
            tvStatusPrijave.setText(prijava.getmStatus());
            tvDatum.setText(prijava.getmDatum());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView listView = (ListView) findViewById(R.id.prijava_stavka_list);

        try {
            List<Stavka> list = getDatabaseHelper().getStavkaDao().queryBuilder()
                    .where()
                    .eq(Stavka.FIELD_NAME_USER, prijava.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Stavka m = (Stavka) listView.getItemAtPosition(position);
                    final Dialog dialog = new Dialog(Detail.this);
                    dialog.setContentView(R.layout.stavka_prikaz_dialog);

                    Button cancel = (Button) dialog.findViewById(R.id.cancel_stavka_btn_prikaz_dialog);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                     TextView tvNaslovStavka =(TextView)dialog.findViewById(R.id.stavka_naslov_prikaz_dialog);
                     TextView tvOpisDetaljniStavka=(TextView)dialog.findViewById(R.id.stavka_detaljni_opis_prikaz_dialog);
                     TextView tvNivoOzbiljnosti= (TextView)dialog.findViewById(R.id.stavka_nivo_ozbiljnosti_prikaz_dialog);
                     TextView tvKomentar= (TextView)dialog.findViewById(R.id.stavka_datum_prijave_prikaz_dialog);
                     TextView tvDatum= (TextView)dialog.findViewById(R.id.stavka_datum_prijave_prikaz_dialog);

                    prefs = PreferenceManager.getDefaultSharedPreferences(Detail.this);
                    int key = getIntent().getExtras().getInt(Detail.STAVKA_KEY);
                    try {
                        stavka = getDatabaseHelper().getStavkaDao().queryForId(key);

                        tvNaslovStavka.setText(stavka.getsNaslov());
                        tvOpisDetaljniStavka.setText(stavka.getsOpis());
                        tvNivoOzbiljnosti.setText(stavka.getsOzbiljnost());
                        tvKomentar.setText(stavka.getsKomentar());
                        tvDatum.setText(stavka.getsDatum());

//                        dialog.dismiss();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    dialog.show();
                }


            });


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.prijava_stavka_list);

        if (listview != null){
            ArrayAdapter<Stavka> adapter = (ArrayAdapter<Stavka>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Stavka> list = getDatabaseHelper().getStavkaDao().queryBuilder()
                            .where()
                            .eq(Stavka.FIELD_NAME_USER, prijava.getmId())
                            .query();

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

    private void showMessage(String message){
        //provera podesenja
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.priprema_add_stavka:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_stavka_layout);
                dialog.setCanceledOnTouchOutside(false);

                Button add =(Button)dialog.findViewById(R.id.add_stavka_add_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView tvNaslovStavka =(TextView)dialog.findViewById(R.id.stavka_naslov_add);
                        TextView tvOpisDetaljniStavka=(TextView)dialog.findViewById(R.id.stavka_detaljni_opis_add);
                        TextView tvNivoOzbiljnosti= (TextView)dialog.findViewById(R.id.stavka_nivo_ozbiljnosti_add);
                        TextView tvKomentar= (TextView)dialog.findViewById(R.id.stavka_kratki_komentar_add);
                        TextView tvDatum= (TextView)dialog.findViewById(R.id.stavka_datum_prijave_add);


                        Stavka s =new Stavka();

                        s.setsNaslov(tvNaslovStavka.getText().toString());
                        s.setsOpis(tvOpisDetaljniStavka.getText().toString());
                        s.setsOzbiljnost(tvNivoOzbiljnosti.getText().toString());
                        s.setsKomentar(tvKomentar.getText().toString());
                        s.setsDatum(tvDatum.getText().toString());


                        try {
                            getDatabaseHelper().getStavkaDao().create(s);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();

                        showMessage("Nova stavka je dodata prijavi!");

                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.priprema_edit:
                final Dialog dialogEdit = new Dialog(this);
                dialogEdit.setContentView(R.layout.edit_prijava_layout);
                dialogEdit.setCanceledOnTouchOutside(false); //ovo novo testiraj

                Button edit = (Button) dialogEdit.findViewById(R.id.add_prijava_edit_btn);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                        EditText edNaslov = (EditText) dialogEdit.findViewById(R.id.prijava_name_edit);
                        EditText edOpis = (EditText) dialogEdit.findViewById(R.id.prijava_kratki_opis_edit);
                        EditText edStatus = (EditText) dialogEdit.findViewById(R.id.prijava_status_edit);
                        EditText edDatum = (EditText) dialogEdit.findViewById(R.id.prijava_datum_edit);

                        prijava.setmName(edNaslov.getText().toString());
                        prijava.setmOpis(edOpis.getText().toString());
                        prijava.setmStatus(edStatus.getText().toString());
                        prijava.setmDatum(edDatum.getText().toString());

                       tvNaslov.setText(prijava.getmName());
                       tvOpis.setText(prijava.getmOpis());
                       tvStatusPrijave.setText(prijava.getmStatus());
                       tvDatum.setText(prijava.getmDatum());



                            getDatabaseHelper().getPrijavaDao().update(prijava);
                            refresh();
                            showMessage("Prijava je promenjena!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialogEdit.dismiss();
                    }
                });
                Button cancel = (Button) dialogEdit.findViewById(R.id.cancel_prijava_edit_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogEdit.dismiss();
                    }
                });
                dialogEdit.show();

                break;
            case R.id.priprema_remove:
                //OVTVARAMO DIALOG ZA UNOS INFORMACIJA
                final Dialog dialogRemove = new Dialog(this);
                dialogRemove.setContentView(R.layout.remove_prijava_layout);
                dialogRemove.setCanceledOnTouchOutside(false);

                TextView textView =(TextView) findViewById(R.id.text_dialog);

                Button deleteDialog = (Button) dialogRemove.findViewById(R.id.delete_prijava_btn_dialog);
                deleteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getDatabaseHelper().getPrijavaDao().delete(prijava);
                            showMessage("Prijava Deleted");
                            finish();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Button cancelDialog = (Button) dialogRemove.findViewById(R.id.cancel_prijava_btn_dialog);
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogRemove.dismiss();

                        finish();
                    }
                });
                dialogRemove.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
