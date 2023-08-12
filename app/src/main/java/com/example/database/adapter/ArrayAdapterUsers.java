package com.example.database.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.database.R;
import com.example.database.models.User;

import java.util.List;

//класс который адаптирует данные под список в Android
public class ArrayAdapterUsers extends ArrayAdapter<User> {

    private LayoutInflater inflater;
    private int layout;
    private List<User> users;

    //конструктор, нужен чтобы получить данные из другого класса
    public ArrayAdapterUsers(Context context, int resource, List<User> users) {
        super(context,resource,users);
        this.layout = resource;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    //метод для присвоения данных каждой строке списка
    public View getView(int position, View convertView, ViewGroup parent) {
        //здесь идёт раздувание контейнера, это означает что строка списка превращается в
        //кастомный элемент, то есть в this.layout идёт присвоение item_user
        View view=inflater.inflate(this.layout, parent, false);

        //присвоение нужных виджетов
        TextView textView_name = view.findViewById(R.id.textView_name);
        TextView textView_age = view.findViewById(R.id.textView_age);
        TextView textView_description = view.findViewById(R.id.textView_description);
        //получить экземпляр модели данных по позиции
        User user = users.get(position);

        //присвоить виджетам нужные данные
        textView_name.setText(user.getName());
        textView_age.setText(String.valueOf(user.getAge()));
        textView_description.setText(user.getDescription());

        return view;
    }
}
