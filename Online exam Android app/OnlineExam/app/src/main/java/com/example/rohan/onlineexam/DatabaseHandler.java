package com.example.rohan.onlineexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;

/**
 * Created by Rohan on 27-03-2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {

    static final String DATABASE_NAME="MyDatabase.db";
    public static final String TABLE_NAME="question";
    static final String COL_1="ID";
    static int id=1;
    static final String COL_2="Question";
    static final String COL_3="a";
    static final String COL_4="b";
    static final String COL_5="c";
    static final String COL_6="d";
    static final String COL_7="Answer";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" ("+COL_1+" INTEGER PRIMARY KEY,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT,"+COL_6+" TEXT,"+COL_7+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        id=1;
    }
    public boolean insertData(String question,String a,String b,String c,String d,String ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id++);
        contentValues.put(COL_2,question);
        contentValues.put(COL_3,a);
        contentValues.put(COL_4,b);
        contentValues.put(COL_5,c);
        contentValues.put(COL_6,d);
        contentValues.put(COL_7,ans);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }


    public String view(String Table)
    {
        StringBuilder sbuf=new StringBuilder("");
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from "+Table,null);

        int col=cur.getColumnCount();
        while(cur.moveToNext())
        {
            for(int i=0;i<7;i++)
                sbuf.append(cur.getString(i)).append(" ");
            sbuf.append("\n");
        }
        cur.close();
        return sbuf.toString();
    }
    public SQLiteDatabase getDatabase(){
        return this.getWritableDatabase();
    }
    public Cursor getQuestion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where ID="+(id),null);
        return res;
    }
}

