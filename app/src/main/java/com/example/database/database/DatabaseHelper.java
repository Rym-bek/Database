package com.example.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.database.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//класс для работы с базой данных
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "users.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 9;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    //открыть и получить базу данных по стандартному пути приложения в телефоне
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    //обновить базу данных
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    //прооверить имеется ли база данных с таким названием
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    //копирование базы данных
    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //метод байтового переноса базы данных
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //открыть базу данных
    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    //закрыть базу данных
    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //проверка версии, в случае если новая версия больше старой происходит обновление
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    //специальные методы
    //добавить пользователя
    public void addUser(String name, int age, String description)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("description", description);
        mDataBase.insert("users", null, values);
    }

    //получить информацию о пользователе
    public List getUsersInfo(){
        List<User> userList = new ArrayList<>();
        String query ="SELECT * FROM users";
        Cursor cursor = mDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            String description = cursor.getString(3);
            userList.add(new User(id,name,age,description));
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    //удалить пользователя
    public void deleteUser(int currentUserId){
        mDataBase.delete("users","id"+ " = ?",new String[]{Long.toString(currentUserId)});
    }

    //обновить пользователя
    public void updateUserInfo(int id, String name, int age, String description){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("age", age);
        values.put("description", description);
        mDataBase.update("users", values, "id = ?", new String[]{String.valueOf(id)});
    }



}
