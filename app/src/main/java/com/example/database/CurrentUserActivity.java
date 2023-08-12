package com.example.database;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.database.DatabaseHelper;

import java.io.IOException;

public class CurrentUserActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    int id, age;
    String name,description;

    private EditText editText_name_CurrentUser,editText_age_CurrentUser,editText_description_CurrentUser;
    private Button button_update, button_delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_user_activity);

        //получить виджеты
        editText_name_CurrentUser=findViewById(R.id.editText_name_CurrentUser);
        editText_age_CurrentUser=findViewById(R.id.editText_age_CurrentUser);
        editText_description_CurrentUser=findViewById(R.id.editText_description_CurrentUser);

        button_update=findViewById(R.id.button_update);
        button_delete=findViewById(R.id.button_delete);

        //получить данные из прошлой активности, чтобы они вывелись в виджетах
        id=getIntent().getIntExtra("id",0);
        name=getIntent().getStringExtra("name");
        age=getIntent().getIntExtra("age",0);
        description=getIntent().getStringExtra("description");

        //присвоить эти данные
        editText_name_CurrentUser.setText(name);
        editText_age_CurrentUser.setText(String.valueOf(age));
        editText_description_CurrentUser.setText(description);

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

        //кнопка обновления данных
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //метод для обновления данных, он расположен внутри DatabaseHelper
                mDBHelper.updateUserInfo(id,editText_name_CurrentUser.getText().toString(),
                        Integer.parseInt(String.valueOf(editText_age_CurrentUser.getText())),editText_description_CurrentUser.getText().toString());
                //всплывающее сообщение
                Toast.makeText(getApplicationContext(), "Пользователь изменён", Toast.LENGTH_LONG).show();
                //запуск другого окна
                Intent intent = new Intent(CurrentUserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //кнопка удаления
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //метод удаления пользователя
                mDBHelper.deleteUser(id);
                //всплывающее сообщение
                Toast.makeText(getApplicationContext(), "Пользователь удалён", Toast.LENGTH_LONG).show();
                //запуск другого окна
                Intent intent = new Intent(CurrentUserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
    }
}
