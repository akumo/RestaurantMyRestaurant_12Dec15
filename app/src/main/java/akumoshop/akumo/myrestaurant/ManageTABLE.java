package akumoshop.akumo.myrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by akumo on 12-Dec-15.
 */
public class ManageTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public ManageTABLE(Context context) {

        //Connected Database
        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();
    }//Constructor

}// Main Class
