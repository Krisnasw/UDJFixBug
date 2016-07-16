package intivestudio.web.id.udjseamolec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends ActionBarActivity {

    private static final String TAG = Profile.class.getSimpleName();
    private TextView quizQuestion;
    private ImageView gambarSoal;
    private RadioGroup radioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private RadioButton optionFour;
    private TextView result;
    private int currentQuizQuestion;
    private int quizCount;
    private int ScoreString = 0;
    private int selected[] = null;
    private int corAns[] = null;
    private int checked = 0;
    private int jwb = 0;

    private SessionManager session;
    private SQLiteFunction func;
    private SQLiteHandler db;

    private static String url = "http://192.168.0.10/droid/images/";
    private static final String TAG_DAFTAR = "quiz_questions";
    private static final String TAG_ID = "id";
    private static final String TAG_SOAL = "question";
    private static final String TAG_A = "pila";
    private static final String TAG_B = "pilb";
    private static final String TAG_C = "pilc";
    private static final String TAG_D = "pild";
    private static final String TAG_JWB = "correct_answer";
    private static final String TAG_GAMBAR = "image";

    // public ImageLoader imageLoader;
    Toolbar toolbar;

    private QuizWrapper firstQuestion;

    private List<QuizWrapper> parsedObject;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        quizQuestion = (TextView)findViewById(R.id.quiz_question);
        gambarSoal = (ImageView)findViewById(R.id.imageSoal);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        optionOne = (RadioButton)findViewById(R.id.radio0);
        optionTwo = (RadioButton)findViewById(R.id.radio1);
        optionThree = (RadioButton)findViewById(R.id.radio2);
        optionFour = (RadioButton)findViewById(R.id.radio3);
        ImageView previousButton = (ImageView) findViewById(R.id.previousquiz);
        ImageView nextButton = (ImageView) findViewById(R.id.nextquiz);
        //imageLoader = new ImageLoader(getApplicationContext());
        result = (TextView) findViewById(R.id.resultView);

        func = new SQLiteFunction(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ujian Dalam Jaringan");

        AsyncJsonObject asyncObject = new AsyncJsonObject();

        asyncObject.execute("");

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);
                int correctAnswerForQuestion = firstQuestion.getCorrectAnswer();
                selected[currentQuizQuestion] = userSelection;
                if(userSelection == correctAnswerForQuestion){
                    // correct answer
                    ScoreString += 1;
                    Toast.makeText(QuizActivity.this, "Jawaban Anda Benar", Toast.LENGTH_LONG).show();

                    currentQuizQuestion++;

                    //finish
                    if(currentQuizQuestion >= quizCount){
                        for (int i =0; i < quizCount; i++)
                        {
                            if (corAns[i] == selected[i]){
                                jwb++;
                            }
                        }
                        AlertDialog tampilKotakAlert;
                        tampilKotakAlert = new AlertDialog.Builder(QuizActivity.this)
                                .create();
                        tampilKotakAlert.setTitle("Hasil Ujian");
                        tampilKotakAlert.setIcon(R.mipmap.ic_launcher);
                        tampilKotakAlert.setMessage("Nilai : " + (jwb * 100 / quizCount));

                        tampilKotakAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Keluar",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent q = new Intent(QuizActivity.this, Profile.class);
                                        finish();
                                        startActivity(q);
                                    }
                                });

                        tampilKotakAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HashMap<String, String> user = db.getUserDetails();

                                        String name = user.get("name");
                                        String Score = jwb * 100 / quizCount +"";

                                        InputNilai(name, Score);
                                    }
                                });

                        tampilKotakAlert.show();
                        Toast.makeText(QuizActivity.this, "End of the Quiz Questions", Toast.LENGTH_LONG).show();

                        return;

                    }
                    //end finish
                    else{

                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        Picasso.with(QuizActivity.this).load(url+firstQuestion.getImage()).into(gambarSoal);
                        uncheckedRadioButton();
                        optionOne.setText(firstQuestion.getPila());
                        optionTwo.setText(firstQuestion.getPilb());
                        optionThree.setText(firstQuestion.getPilc());
                        optionFour.setText(firstQuestion.getPild());

                    }

                }

                else{

                    // failed question
                    ScoreString += 0;
                    currentQuizQuestion++;
                    Toast.makeText(QuizActivity.this, "You chose the wrong answer", Toast.LENGTH_LONG).show();

                    if(currentQuizQuestion >= quizCount){
                        AlertDialog tampilKotakAlert;
                        tampilKotakAlert = new AlertDialog.Builder(QuizActivity.this)
                                .create();
                        tampilKotakAlert.setTitle("Hasil Ujian");
                        tampilKotakAlert.setIcon(R.mipmap.ic_launcher);
                        tampilKotakAlert.setMessage("Nilai : " + (ScoreString * 100 / quizCount));

                        tampilKotakAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Keluar",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent q = new Intent(QuizActivity.this, Profile.class);
                                        finish();
                                        startActivity(q);
                                    }
                                });

                        tampilKotakAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HashMap<String, String> user = db.getUserDetails();

                                        String name = user.get("name");
                                        String Score = ScoreString +"";

                                        InputNilai(name, Score);
                                    }
                                });

                        tampilKotakAlert.show();
                        Toast.makeText(QuizActivity.this, "End of the Quiz Questions", Toast.LENGTH_LONG).show();

                        return;

                    }

                    else{

                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        Picasso.with(QuizActivity.this).load(url+firstQuestion.getImage()).into(gambarSoal);
                        uncheckedRadioButton();
                        optionOne.setText(firstQuestion.getPila());
                        optionTwo.setText(firstQuestion.getPilb());
                        optionThree.setText(firstQuestion.getPilc());
                        optionFour.setText(firstQuestion.getPild());

                    }

                    return;

                }

            }

        });

        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                currentQuizQuestion--;
                Log.d("asshole",String.valueOf(selected[currentQuizQuestion]));
                if(currentQuizQuestion < 0){

                    return;

                }

                //uncheckedRadioButton();

                firstQuestion = parsedObject.get(currentQuizQuestion);
                quizQuestion.setText(firstQuestion.getQuestion());
                Picasso.with(QuizActivity.this).load(url+firstQuestion.getImage()).into(gambarSoal);
                optionOne.setText(firstQuestion.getPila());
                optionTwo.setText(firstQuestion.getPilb());
                optionThree.setText(firstQuestion.getPilc());
                optionFour.setText(firstQuestion.getPild());
                radioGroup.check(-1);
                if(selected[currentQuizQuestion] == 1){
                    radioGroup.check(R.id.radio0);
                }
                if(selected[currentQuizQuestion] == 2){
                    radioGroup.check(R.id.radio1);
                }
                if(selected[currentQuizQuestion] == 3){
                    radioGroup.check(R.id.radio2);
                }
                if(selected[currentQuizQuestion] == 4){
                    radioGroup.check(R.id.radio3);
                }

            }

        });

    }

    private class AsyncJsonObject extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override

        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httpPost = new HttpPost("http://192.168.0.10/droid/quiz.php");

            String jsonResult = "";

            try {

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return jsonResult;

        }

        @Override

        protected void onPreExecute() {

// TODO Auto-generated method stub

            super.onPreExecute();

            progressDialog = ProgressDialog.show(QuizActivity.this, "Loading Quiz","Wait....", true);

        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();

            System.out.println("Resulted Value: " + result);

            parsedObject = returnParsedJsonObject(result);

            if(parsedObject == null){

                return;

            }

            quizCount = parsedObject.size();
            selected = new int[quizCount];
            corAns = new int[quizCount];
            for (int i = 0; i < quizCount; i++)
            {
                firstQuestion = parsedObject.get(i);
                corAns[i] = firstQuestion.getCorrectAnswer();
            }
            firstQuestion = parsedObject.get(0);
            quizQuestion.setText(firstQuestion.getQuestion());
            Picasso.with(QuizActivity.this).load(url+firstQuestion.getImage()).into(gambarSoal);
            optionOne.setText(firstQuestion.getPila());
            optionTwo.setText(firstQuestion.getPilb());
            optionThree.setText(firstQuestion.getPilc());
            optionFour.setText(firstQuestion.getPild());

        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }

    private List<QuizWrapper> returnParsedJsonObject(String result){

        List<QuizWrapper> jsonObject = new ArrayList<QuizWrapper>();

        JSONObject resultObject = null;

        JSONArray jsonArray = null;

        QuizWrapper newItemObject = null;

        try {

            resultObject = new JSONObject(result);

            System.out.println("Testing the water " + resultObject.toString());

            jsonArray = resultObject.optJSONArray("quiz_questions");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject jsonChildNode = null;

            try {
                jsonChildNode = jsonArray.getJSONObject(i);
                int id = jsonChildNode.getInt(TAG_ID);
                String question = jsonChildNode.getString(TAG_SOAL);
                String pila = jsonChildNode.getString(TAG_A);
                String pilb = jsonChildNode.getString(TAG_B);
                String pilc = jsonChildNode.getString(TAG_C);
                String pild = jsonChildNode.getString(TAG_D);
                int correctAnswer = jsonChildNode.getInt(TAG_JWB);
                String image = jsonChildNode.getString(TAG_GAMBAR);
                System.out.println("Gambar e Cok : "+image);
                newItemObject = new QuizWrapper(id, question, pila, pilb, pilc, pild, correctAnswer, image);
                jsonObject.add(newItemObject);
            } catch (JSONException e) {

                e.printStackTrace();

            }

        }

        return jsonObject;

    }

    private int getSelectedAnswer(int radioSelected){

        int answerSelected = 0;

        if(radioSelected == R.id.radio0){

            answerSelected = 1;

        }

        if(radioSelected == R.id.radio1){

            answerSelected = 2;

        }

        if(radioSelected == R.id.radio2){

            answerSelected = 3;

        }

        if(radioSelected == R.id.radio3){

            answerSelected = 4;

        }

        return answerSelected;

    }

    private void uncheckedRadioButton(){

        optionOne.setChecked(false);

        optionTwo.setChecked(false);

        optionThree.setChecked(false);

        optionFour.setChecked(false);

    }

    private void InputNilai(final String name,final String Score) {

        String tag_string_req = "req_input";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_HASIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Input Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        String uid = jObj.getString("id_users");

                        JSONObject user = jObj.getJSONObject("hasil");
                        String name = user.getString("nama");
                        String nilai = user.getString("nilai");

                        // Inserting row in users table
                        func.addNilai(name, nilai);

                        // Launch main activity
                        Intent intent = new Intent(QuizActivity.this,
                                Profile.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Input Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", name);
                params.put("nilai", Score);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuizActivity.this,Mapel.class);
        startActivity(intent);
        finish();
    }
}