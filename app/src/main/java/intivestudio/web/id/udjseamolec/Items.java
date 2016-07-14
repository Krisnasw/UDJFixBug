package intivestudio.web.id.udjseamolec;

/**
 * Created by Krisnasw on 5/17/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Items extends Activity {
    // Declare Variables
    String nama;
    String nilai;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.list_row);

        Intent i = getIntent();
        // Get the result of rank
        nama = i.getStringExtra("nama");
        // Get the result of country
        nilai = i.getStringExtra("nilai");

        // Locate the TextViews in singleitemview.xml
        TextView txtrank = (TextView) findViewById(R.id.name);
        TextView txtcountry = (TextView) findViewById(R.id.score);

        // Set results to the TextViews
        txtrank.setText(nama);
        txtcountry.setText(nilai);
    }
}
