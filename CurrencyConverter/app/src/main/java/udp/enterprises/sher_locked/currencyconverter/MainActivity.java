package udp.enterprises.sher_locked.currencyconverter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Spinner fromspinner;
    Spinner tospinner;
    EditText amt;
    TextView t,tv,t4,t2,t3;
    Button b;
    Double d;

    private static String paisa;
    private static String URL;
    private static String neecheWaala;
    private Views mViews;

    private Double cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.rgb(0,0,0));

        fromspinner=(Spinner)findViewById(R.id.fromCurrency);
        ArrayAdapter<CharSequence> fromadapter=ArrayAdapter.createFromResource(this,R.array.fromCurrency_array,R.layout.spinner_item);
        fromadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromspinner.setAdapter(fromadapter);
        fromspinner.setOnItemSelectedListener(this);

        tospinner=(Spinner)findViewById(R.id.toCurrency);
        ArrayAdapter<CharSequence> toadapter=ArrayAdapter.createFromResource(this,R.array.toCurrency_array,R.layout.spinner_item);
        toadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tospinner.setAdapter(toadapter);
        tospinner.setOnItemSelectedListener(this);

        mViews=new Views();
        amt=(EditText)findViewById(R.id.editText);
        t=(TextView)findViewById(R.id.textView5);
        tv=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);
        t4=(TextView)findViewById(R.id.textView4);
        t3=(TextView)findViewById(R.id.textView3);
        b=(Button)findViewById(R.id.button);
        b.setOnClickListener(this);

        Typeface tf=Typeface.createFromAsset(getAssets(), "neuropol-x-free.regular.ttf");
        t2.setTypeface(tf);
        t4.setTypeface(tf);
        t3.setTypeface(tf);
        t.setTypeface(tf);
        tv.setTypeface(tf);
        b.setTypeface(tf);

    }

    @Override
    public void onClick(View v) {
        String value=amt.getText().toString();

        try {
            d = Double.parseDouble(value);
        }
        catch (NumberFormatException e){
            d=0.0;
        }

        paisa= fromspinner.getSelectedItem().toString().substring(1,4);
        neecheWaala= tospinner.getSelectedItem().toString().substring(1,4);

        if(fromspinner.getSelectedItem().toString()==tospinner.getSelectedItem().toString()){
            amt.setText("");
            t.setText("");
            tv.setText("RESULT : ");
            Toast.makeText(MainActivity.this,"CHOOSE DIFFERENT OPTIONS !",Toast.LENGTH_LONG).show();
        }

        URL = "http://api.fixer.io/latest?base="+paisa;
        new BookDownloadTask().execute(URL);

    }

    class BookDownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            mViews.progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                java.net.URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder stringBuilder = new StringBuilder();
                String tempString;

                while ((tempString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tempString);
                    stringBuilder.append("\n");
                }

                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            mViews.progressBar.setVisibility(View.GONE);
            if (response != null) {
                parseJsonResponse(response);
            }
        }
    }

    private void parseJsonResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject ratesObject=jsonObject.getJSONObject("rates");
            cash=ratesObject.getDouble(neecheWaala);

                t.setText("" + (d * cash) + " " + neecheWaala);
                tv.setText("" + (d) + " " + paisa + " = ");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Views {
        final ProgressBar progressBar;
        public Views() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

}