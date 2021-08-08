package com.example.pulseoximeter2021.DataLayer.Room;

import androidx.annotation.NonNull;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyFirebaseDatabase {

    private com.google.firebase.database.FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDbReference;
    private DatabaseReference recordsDbReference;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Record> records = new ArrayList<>();

    public MyFirebaseDatabase() {
        firebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        usersDbReference = firebaseDatabase.getReference("Users");
        recordsDbReference = firebaseDatabase.getReference("Records");
    }

    public interface DataStatus
    {
        void userDataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException;
        void userDataIsInserted();
        void userDataIsUpdated();
        void userDataIsDeleted();

        void recordDataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException;
        void recordDataIsInserted();
        void recordDataIsUpdated();
        void recordDataIsDeleted();
    }

    public void addUser(String uid, User user,final DataStatus dataStatus)
    {
        try {
            usersDbReference.child("Users").child(uid).setValue(user).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    String aa = "a";
                }
            });
            dataStatus.userDataIsInserted();

//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    dataStatus.userDataIsInserted();
//                }
//            });
        } catch (Exception e)
        {
            String rx = e.getMessage();
            int a = 4;
        }

    }

    public void addRecord(String uid, Record record,final DataStatus dataStatus)
    {
        String key = recordsDbReference.child("Records").child(uid).push().getKey();

        recordsDbReference.child("Records").child(uid).child(key).setValue(record).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.recordDataIsInserted();
            }
        });
    }

    public void readUsers(final DataStatus dataStatus)
    {
        usersDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                ArrayList<String> keys = new ArrayList<String>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());

                    User user = keyNode.getValue(User.class);

                    if(!users.contains(user))
                        users.add(user);
                }

                try {
                    dataStatus.userDataIsLoaded(users,keys);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readRecords(final DataStatus dataStatus)
    {
        recordsDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                records.clear();

                ArrayList<String> keys = new ArrayList<String>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());

                    Record record = keyNode.getValue(Record.class);

                    if(!records.contains(record))
                        records.add(record);
                }

                try {
                    dataStatus.recordDataIsLoaded(records,keys);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
