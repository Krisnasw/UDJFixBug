package intivestudio.web.id.udjseamolec;

/**
 * Created by Krisnasw on 5/27/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class  Mapel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private Toolbar tbar;
    private SQLiteHandler db;
    private SessionManager session;
    ListView list;
    TextView napel;
    TextView create;
    TextView paket;


    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://192.168.1.66/droid/g.php/";

    //JSON Node Names
    private static final String TAG_OS = "ngHasil";
    private static final String TAG_VER = "napel";
    private static final String TAG_CREATE = "created";
    private static final String TAG_PAKET = "nama_paket";

    JSONArray result = null;
    Toolbar toolbar;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        tbar = (Toolbar) findViewById(R.id.tbar);
        list = (ListView) findViewById(R.id.list);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("Paket Soal");

        View header = navigationView.getHeaderView(0);
        TextView txt_jeneng = (TextView)header.findViewById(R.id.jeneng);


        list.setClickable(true);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        Log.e("name",name);

        txt_jeneng.setText(name);

        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            napel = (TextView)findViewById(R.id.napel);
            create = (TextView)findViewById(R.id.create);
            paket = (TextView)findViewById(R.id.paket);
            pDialog = new ProgressDialog(Mapel.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }



        @Override
        protected JSONObject doInBackground(String... args) {

            JSONFunctions jParser = new JSONFunctions();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONfromURL(url);
            try {
                // Getting JSON Array from URL
                result = json.getJSONArray(TAG_OS);
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String ver = c.getString(TAG_VER);
                    String create = c.getString(TAG_CREATE);
                    String paket = c.getString(TAG_PAKET);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_VER, ver);
                    map.put(TAG_CREATE,create);
                    map.put(TAG_PAKET,paket);

                    oslist.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return json;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }

            ListAdapter adapter = new SimpleAdapter(Mapel.this,oslist,R.layout.custom_list_view,new String[]{TAG_VER,TAG_CREATE,TAG_PAKET},new int[]{R.id.napel,R.id.create,R.id.paket});
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Mapel.this,QuizActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_menu1){
            Intent intent = new Intent(Mapel.this,Profile.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_menu2){
            Intent intent = new Intent(Mapel.this,Score.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.nav_log_out){
            logoutUser();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(Gravity.LEFT);
        }
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Mapel.this, Login.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}