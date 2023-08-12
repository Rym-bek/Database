package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.database.DatabaseHelper;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    private EditText editText_name,editText_age,editText_description;
    private Button button_add, button_readAll;
    private String name, age, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //присвоение нужного визуального файла
        setContentView(R.layout.home_activity);

        //поиск нужных виджетов в нём
        editText_name=findViewById(R.id.editText_name);
        editText_age=findViewById(R.id.editText_age);
        editText_description=findViewById(R.id.editText_description);

        button_add=findViewById(R.id.button_add);
        button_readAll=findViewById(R.id.button_readAll);

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

        //кнопка добавления данных
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //получить данные с виджетов
                name=editText_name.getText().toString();
                age=editText_age.getText().toString();
                description=editText_description.getText().toString();

                //проверить не пустые ли они
                if(checkDataEmpty())
                {
                    //добавить пользователя
                    mDBHelper.addUser(name,Integer.parseInt(age),description);
                    //вывести сообщение
                    Toast.makeText(getApplicationContext(), "Данные добавлены", Toast.LENGTH_LONG).show();
                }
            }
        });

        //кнопка которая позволяет перейти на страницу всех пользователей
        button_readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //запуск намерения перехода на другую страницу
                Intent intent = new Intent(HomeActivity.this, AllUsersActivity.class);
                startActivity(intent);
            }
        });
    }

    //проверка на пустоту
    private boolean checkDataEmpty()
    {
        //если поле имени пустое то вернуть false
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_LONG).show();
            return false;
        }
        //то же самое для возраста
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(getApplicationContext(), "Введите возраст", Toast.LENGTH_LONG).show();
            return false;
        }
        //это же самое для описание
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(getApplicationContext(), "Введите описание", Toast.LENGTH_LONG).show();
            return false;
        }
        //если всё прошло успешно то вернуть true
        return true;
    }

    //при закрытии окна закрыть базу данных
    @Override
    protected void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
    }
}