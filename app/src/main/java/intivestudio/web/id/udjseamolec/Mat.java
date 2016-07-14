package intivestudio.web.id.udjseamolec;

/**
 * Created by Krisnasw on 5/27/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Mat extends AppCompatActivity {
    ListView list;
    TextView ver;
    TextView name;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://192.168.1.66/droid/mat.php/";

    //JSON Node Names
    private static final String TAG_OS = "hasil";
    private static final String TAG_VER = "nama_paket";

    JSONArray android = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Paket Soal");

        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView)findViewById(R.id.napel);
            pDialog = new ProgressDialog(Mat.this);
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
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String ver = c.getString(TAG_VER);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_VER, ver);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(Mat.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_VER }, new int[] {
                            R.id.napel});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                                Toast.makeText(Mat.this, "Anda Memilih " + oslist.get(+position).get("nama_paket"), Toast.LENGTH_SHORT).show();
                                Intent q = new Intent(Mat.this, QuizActivity.class);
                                startActivity(q);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}