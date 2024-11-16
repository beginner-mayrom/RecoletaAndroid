package com.example.recoleta;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users != null ? users.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_model, parent, false);
        }

        // Obter o usuário atual
        User user = users.get(position);

        TextView name = convertView.findViewById(R.id.textView11);
        TextView lastName = convertView.findViewById(R.id.textView13);
        TextView typeUser = convertView.findViewById(R.id.textView15);
        TextView email = convertView.findViewById(R.id.textView17);
        ImageView redirect = convertView.findViewById(R.id.imageView10);

        name.setText(user.getFirstName());
        lastName.setText(!Objects.equals(user.getLastName(), "") ? user.getLastName() : "");
        typeUser.setText(!Objects.equals(user.getUserType(), "") ? user.getUserType() : "");
        email.setText(user.getEmail());

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("user_id", user.get_id());// Passar dados do usuário
                intent.putExtra("first_name", user.getFirstName());
                intent.putExtra("last_name", user.getLastName());
                intent.putExtra("user_type", user.getUserType());
                intent.putExtra("is_admin", user.isAdmin());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
