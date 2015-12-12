package akumoshop.akumo.myrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private ManageTABLE objManageTABLE;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connected Database
        objManageTABLE = new ManageTABLE(this);

        //Tester Add Value
        //testerAddValue();

        //Delete All SQLite
        deleteAllSQLite();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    } // Main Method

    private void synJSONtoSQLite() {

        //Setup my Policy
        StrictMode.ThreadPolicy myThreadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(myThreadPolicy);

        int intTable = 1;
        while (intTable <= 2) {

            InputStream objInputStream = null;
            String strJSON = null;
            String strURLuser = "http://filespace.webhop.net/php_get_data.php";
            String strURLfood = "http://filespace.webhop.net/php_get_data_food.php";
            HttpPost objHttpPost = null;

            //1. Create InputStream
            try {
                HttpClient objHttpClient = new DefaultHttpClient();
                switch (intTable) {
                    case 1:
                        objHttpPost = new HttpPost(strURLuser);
                        break;
                    case 2:
                        objHttpPost = new HttpPost(strURLfood);
                        break;
                }

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {
                Log.d("Rest", "InputStream ==" + e.toString());
            }


            //2. Create strJSON
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));

                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = objBufferedReader.readLine()) != null) {
                    objStringBuilder.append(strLine);
                }   //while
                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("Rest", "strJSON ==> " + e.toString());
            }

            //3. Update to SQLite
            try {
                JSONArray objJsonArray = new JSONArray(strJSON);
                for (int i=0;i<objJsonArray.length();i++) {
                    JSONObject object = objJsonArray.getJSONObject(i);

                    switch (intTable) {
                        case 1:
                            //For userTABLE
                            String strUser = object.getString("User");
                            String strPassword = object.getString("Password");
                            String strName = object.getString("Name");
                            objManageTABLE.addValueToUser(strUser, strPassword, strName);
                            break;
                        case 2:
                            //For foodTABLE
                            String strFood = object.getString("Food");
                            String strSource = object.getString("Source");
                            String strPrice = object.getString("Price");
                            objManageTABLE.addValueToFood(strFood, strSource, strPrice);
                            break;
                    }   //switch
                }   //for
            } catch (Exception e) {
                Log.d("Rest", "Update ==> " + e.toString());
            }

            intTable += 1;
        }   //while

    }   //synJSONtoSQLite

    private void deleteAllSQLite() {

        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);

    }

    private void testerAddValue() {

        objManageTABLE.addValueToUser("user", "pass", "ประวิทย์");
        objManageTABLE.addValueToFood("food", "source", "price");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://akumoshop.akumo.myrestaurant/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://akumoshop.akumo.myrestaurant/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
} // Main Class
