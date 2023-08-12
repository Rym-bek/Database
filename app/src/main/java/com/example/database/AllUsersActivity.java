package com.example.database;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.adapter.ArrayAdapterUsers;
import com.example.database.database.DatabaseHelper;
import com.example.database.models.User;

import java.io.IOException;
import java.util.ArrayList;


public class AllUsersActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    private ArrayList<User> usersList= new ArrayList<>();
    private ListView usersListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //присвоение нужного визуального файла
        setContentView(R.layout.all_users_activity);

        //поиск необоходимого элемента управления
        usersListView=findViewById(R.id.usersListView);

        //База данных
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getWritableDatabase();
        //открытие базы данных
        mDBHelper.openDataBase();

        //метод добавления всех данных в список
        usersList.addAll(mDBHelper.getUsersInfo());
        //вызов адаптера списков
        ArrayAdapterUsers arrayAdapter = new ArrayAdapterUsers(this,R.layout.item_user,usersList);
        //присвоить адаптер
        usersListView.setAdapter(arrayAdapter);

        //метод который вызывается при нажатии на один из элементов списка
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //переход на страницу пользователя, где можно изменить или удалить его данные
                Intent intent = new Intent(AllUsersActivity.this, CurrentUserActivity.class);
                intent.putExtra("id", usersList.get(position).getId());
                intent.putExtra("name", usersList.get(position).getName());
                intent.putExtra("age", usersList.get(position).getAge());
                intent.putExtra("description", usersList.get(position).getDescription());
                startActivity(intent);
            }
        };
        usersListView.setOnItemClickListener(itemListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //закрыть базу данных при закрытии окна
        mDBHelper.close();
    }
}
