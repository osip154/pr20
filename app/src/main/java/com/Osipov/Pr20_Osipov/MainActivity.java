package com.Osipov.Pr20_Osipov;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUserId, edName, edSName, edMail, edPhone, edCity, edAge;
    private Button btnSave, btnRead, btnUpdate, btnDelete;

    private DatabaseReference myDataBase;
    private final String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edUserId = findViewById(R.id.edUserId);
        edName   = findViewById(R.id.edName);
        edSName  = findViewById(R.id.edSName);
        edMail   = findViewById(R.id.edMail);
        edPhone  = findViewById(R.id.edPhone);
        edCity   = findViewById(R.id.edCity);
        edAge    = findViewById(R.id.edAge);

        btnSave   = findViewById(R.id.btnSave);
        btnRead   = findViewById(R.id.btnRead);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSave) {
            saveData();
        } else if (id == R.id.btnRead) {
            readDataById();
        } else if (id == R.id.btnUpdate) {
            updateData();
        } else if (id == R.id.btnDelete) {
            deleteData();
        }
    }

    private void saveData() {
        String name  = edName.getText().toString().trim();
        String sName = edSName.getText().toString().trim();

        if (name.isEmpty() || sName.isEmpty()) {
            Toast.makeText(this, "Заполните хотя бы Имя и Фамилию", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(
                name, sName,
                edMail.getText().toString().trim(),
                edPhone.getText().toString().trim(),
                edCity.getText().toString().trim(),
                edAge.getText().toString().trim()
        );

        String generatedId = myDataBase.push().getKey();
        if (generatedId == null) {
            Toast.makeText(this, "Ошибка генерации ID", Toast.LENGTH_SHORT).show();
            return;
        }

        myDataBase.child(generatedId).setValue(newUser)
                .addOnSuccessListener(unused -> {
                    edUserId.setText(generatedId);
                    Toast.makeText(MainActivity.this,
                            "Сохранено! ID: " + generatedId, Toast.LENGTH_LONG).show();
                    clearFields(false);
                });
    }

    private void readDataById() {
        String userId = edUserId.getText().toString().trim();

        if (userId.isEmpty()) {
            Toast.makeText(this, "Введите ID пользователя", Toast.LENGTH_SHORT).show();
            return;
        }


        myDataBase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        edName.setText(user.name);
                        edSName.setText(user.sName);
                        edMail.setText(user.mail);
                        edPhone.setText(user.phone);
                        edCity.setText(user.city);
                        edAge.setText(user.age);
                        Toast.makeText(MainActivity.this,
                                "Пользователь загружен", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Пользователь с таким ID не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,
                        "Ошибка чтения: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData() {
        String userId = edUserId.getText().toString().trim();

        if (userId.isEmpty()) {
            Toast.makeText(this, "Введите ID пользователя для обновления", Toast.LENGTH_SHORT).show();
            return;
        }

        String name  = edName.getText().toString().trim();
        String sName = edSName.getText().toString().trim();

        if (name.isEmpty() || sName.isEmpty()) {
            Toast.makeText(this, "Заполните хотя бы Имя и Фамилию", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(
                name, sName,
                edMail.getText().toString().trim(),
                edPhone.getText().toString().trim(),
                edCity.getText().toString().trim(),
                edAge.getText().toString().trim()
        );

        myDataBase.child(userId).setValue(updatedUser)
                .addOnSuccessListener(unused ->
                        Toast.makeText(MainActivity.this,
                                "Данные обновлены!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this,
                                "Ошибка обновления: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteData() {
        String userId = edUserId.getText().toString().trim();

        if (userId.isEmpty()) {
            Toast.makeText(this, "Введите ID пользователя для удаления", Toast.LENGTH_SHORT).show();
            return;
        }

        myDataBase.child(userId).removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(MainActivity.this,
                            "Пользователь удалён", Toast.LENGTH_SHORT).show();
                    clearFields(true);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this,
                                "Ошибка удаления: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearFields(boolean clearId) {
        if (clearId) edUserId.setText("");
        edName.setText("");
        edSName.setText("");
        edMail.setText("");
        edPhone.setText("");
        edCity.setText("");
        edAge.setText("");
    }
}
