package intivestudio.web.id.udjseamolec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import java.util.HashMap;

/**
 * Created by OWL on 28/06/2016.
 */
public class Headernav extends AppCompatActivity {
    private SessionManager session;
    private SQLiteHandler db;
    private TextView txt_jeneng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_header);
        txt_jeneng = (TextView) findViewById(R.id.jeneng);

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

        // Displaying the user details on the screen
        txt_jeneng.setText("" +name);
    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Headernav.this, Login.class);
        finish();
        startActivity(intent);
    }
}