package com.brz.lessontime.service;

import com.brz.lessontime.presentation.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {

    private final DatabaseReference databaseReference;

    public UserService() {
        databaseReference = FirebaseDatabase.getInstance("https://lessontime-7570e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users");
    }

    public void validateUser(String email, String password, UserValidationCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                boolean userFound = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                        userFound = true;
                        break;
                    }
                }

                if (userFound) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Incorrect email or password");
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                callback.onError("Database connection error");
            }
        });
    }

    public interface UserValidationCallback {
        void onSuccess();
        void onFailure(String message);
        void onError(String message);
    }
}
