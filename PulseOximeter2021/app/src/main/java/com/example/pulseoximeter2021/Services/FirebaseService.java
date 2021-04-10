package com.example.pulseoximeter2021.Services;

import androidx.annotation.NonNull;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.DataLayer.Room.MyFirebaseDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FirebaseService {
    public static FirebaseService instance = new FirebaseService();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private User userDetails = null;

    private final String RECORDS = "Records";
    private final String USERS = "Users";

    private ArrayList<Record> records = new ArrayList<>();

    private FirebaseService()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        downloadUserDetails();
    }

    public interface RecordDataStatus
    {
        void DataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException;
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readUserRecords(final RecordDataStatus dataStatus)
    {
        databaseReference.child(RECORDS).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
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
                    dataStatus.DataIsLoaded(records,keys);
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

    public void addRecord(Record record, final MyFirebaseDatabase.DataStatus dataStatus)
    {
        databaseReference.child(RECORDS).child(firebaseUser.getUid()).push().setValue(record)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.recordDataIsInserted();
                    }
                });
    }

    public static FirebaseService getInstance()
    {
        return instance;
    }

    public FirebaseUser getUser()
    {
        return firebaseUser;
    }

    private void downloadUserDetails()
    {
        databaseReference.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keyNode : snapshot.getChildren()) {
                    if(keyNode.getKey().equals(firebaseUser.getUid()))
                        userDetails = keyNode.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User getUserDetails()
    {
        return userDetails;
    }

    public FirebaseUser getCurrentUser()
    {
        return firebaseUser;
    }

    public String getFullName()
    {
        return userDetails.getFirstName().concat(" ").concat(userDetails.getLastName());
    }
}
