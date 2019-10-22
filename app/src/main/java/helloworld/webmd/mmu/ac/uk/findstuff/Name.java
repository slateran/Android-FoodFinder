package helloworld.webmd.mmu.ac.uk.findstuff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class Name extends Activity {

    public void homeButton(View view) {
        Intent H = new Intent(Name.this, MainActivity.class);
        startActivity(H);
    }



    //declare variables


    public String lons;
    public String lati; //this is where they are stored when parsed into strings

    public String name = "Name: "; // Name of establishment
    public String address = "Address: "; // address of establishment
    public String spaces =  "                "; //
    public String distance = "Distance(Km): "; //Distance to establishment
    public String rating = "Rating: " ;//rating of establishment
    public String score =  "/5"; //

    public String estName;
    public String space;
    public String ratingD = "Date Rated: ";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); //enables strict mode which keeps operations away from the main thread
    }

    public void SearchClickName(View view){


        EditText textInput = (EditText)findViewById(R.id.textInput);
        estName = textInput.getText().toString();
        space = (estName.replace(" ", "%20"));



        ArrayList<String> displayAddress = new ArrayList<String>();

        try{
            URL kris = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=s_name&name="+space);
            HttpURLConnection conn = (HttpURLConnection) kris.openConnection();
            InputStreamReader input = new InputStreamReader(conn.getInputStream());
            BufferedReader buff = new BufferedReader(input);

            String est;
            while ((est = buff.readLine()) !=null) {
                JSONArray ja = new JSONArray(est);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    displayAddress.add(name+jo.getString("BusinessName"));


                    if (!jo.getString("AddressLine1").equals("")) ;{
                        displayAddress.add(address+jo.getString("AddressLine1"));}
                    if (! jo.getString("AddressLine2").equals("")) {
                        displayAddress.add(spaces+jo.getString("AddressLine2"));
                    }
                        if (! jo.getString("AddressLine3").equals("")){
                            displayAddress.add(spaces+jo.getString("AddressLine3"));
                    }
                    displayAddress.add(spaces+jo.getString("PostCode"));


                    if (jo.getString("RatingValue").equals("-1")){
                        displayAddress.add(rating+"Exempt");
                    }else displayAddress.add(rating + jo.getString("RatingValue")+score);
                    displayAddress.add(ratingD + jo.get("RatingDate"));

                    displayAddress.add("");

                }

            }
        }
        catch (MalformedInputException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();  }


        TableLayout displayAddressTable = (TableLayout) findViewById(R.id.displayAddressTable);
        displayAddressTable.removeAllViews();

        int i =0;


            for (String entry : displayAddress) {
                TextView tv = new TextView(getApplicationContext());
                TableRow tr = new TableRow(getApplicationContext());
                tv.setText(entry);
                tv.setTextColor(Color.BLACK);
                tr.addView(tv);
                displayAddressTable.addView(tr);i++;
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
