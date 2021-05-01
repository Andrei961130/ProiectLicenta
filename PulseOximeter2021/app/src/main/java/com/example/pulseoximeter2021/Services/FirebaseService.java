package com.example.pulseoximeter2021.Services;

import androidx.annotation.NonNull;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Link;
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
    private final String LINKS = "Links";

    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<User> linkedUsers = new ArrayList<>();
    private ArrayList<User> searchedUsers = new ArrayList<>();
    private ArrayList<User> linkedDoctorForUser = new ArrayList<>();


    private FirebaseService()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null)
            downloadUserDetails();
    }


    public interface RecordDataStatus
    {
        void DataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException;
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface UserDataStatus
    {
        void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException;
        void DataIsInserted();
    }

    public interface NoDoctorStatus
    {
        void NoDoctor();
    }

    public void readUsersByDoctor(final UserDataStatus dataStatus)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(LINKS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linkedUsers.clear();

                ArrayList<String> keys = new ArrayList<String>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    Link link = keyNode.getValue(Link.class);

                    if(link.getDoctorUid().equals(firebaseUser.getUid())) {

                        firebaseDatabase.getReference().child(USERS).child(link.getPacientUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        User user = snapshot.getValue(User.class);

                                        if (!linkedUsers.contains(user))
                                            linkedUsers.add(user);

                                        try {
                                            dataStatus.DataIsLoaded(linkedUsers,keys);
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }) ;




//                        keys.add(keyNode.getKey());
//
//                        User user = keyNode.getValue(User.class);
//
//                        if (!users.contains(user))
//                            users.add(user);
                    }
                }

//                try {
//                    dataStatus.DataIsLoaded(users,keys);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readDoctorByUser(final String uid, final UserDataStatus dataStatus, final NoDoctorStatus noDoctorStatus)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String usedUid;
        if(uid.equals(""))
            usedUid = firebaseUser.getUid();
        else
            usedUid = uid;

        databaseReference.child(LINKS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linkedUsers.clear();

                Boolean doctorFound = false;

                ArrayList<String> keys = new ArrayList<String>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    Link link = keyNode.getValue(Link.class);

                    if(link.getPacientUid().equals(usedUid)) {

                        doctorFound = true;

                        firebaseDatabase.getReference().child(USERS).child(link.getDoctorUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        User user = snapshot.getValue(User.class);

                                        if (!linkedDoctorForUser.contains(user))
                                            linkedDoctorForUser.add(user);

                                        try {
                                            dataStatus.DataIsLoaded(linkedDoctorForUser,keys);
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }) ;




//                        keys.add(keyNode.getKey());
//
//                        User user = keyNode.getValue(User.class);
//
//                        if (!users.contains(user))
//                            users.add(user);
                    }
                }


                if(!doctorFound) {
                    noDoctorStatus.NoDoctor();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readUserRecords(final RecordDataStatus dataStatus, String uid)
    {
        databaseReference.child(RECORDS).child(uid).addValueEventListener(new ValueEventListener() {
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


    public void readUserBySearchBox(final UserDataStatus dataStatus, String searchedText) {
        databaseReference.child(USERS).orderByChild("email").startAt(searchedText).endAt(searchedText + "\uf8ff").limitToFirst(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        searchedUsers.clear();

                        ArrayList<String> keys = new ArrayList<String>();

                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            User user = keyNode.getValue(User.class);

                            if(!user.getDoctor()) {

                                keys.add(keyNode.getKey());
                                if (!searchedUsers.contains(user))
                                    searchedUsers.add(user);
                            }
                        }

                        try {
                            dataStatus.DataIsLoaded(searchedUsers, keys);
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

    public void addUserAndDoctorLink(Link link, final UserDataStatus dataStatus)
    {
        databaseReference.child(LINKS).push().setValue(link)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public static FirebaseService getInstance()
    {
        return instance;
    }

    public static FirebaseService getNewInstance()
    {
        instance = new FirebaseService();
        return instance;
    }

    public FirebaseUser getUser()
    {
        return firebaseUser;
    }

    public void downloadUserDetails()
    {
        databaseReference.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keyNode : snapshot.getChildren()) {
                    if(keyNode.getKey().equals(firebaseUser.getUid()))
                    {
                        userDetails = keyNode.getValue(User.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User getUserDetails()
    {
        return userDetails != null ? userDetails : null;
    }

    public FirebaseUser getCurrentUser()
    {
        return firebaseUser;
    }

    public String getFullName()
    {
        return firebaseUser.getDisplayName();
    }

    public String getFullNameFromDetails()
    {
        return userDetails.getFirstName().concat(" ").concat(userDetails.getLastName());
    }

    public Boolean isDoctor()
    {
        if(firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
            String isDoctor = firebaseUser.getPhotoUrl().toString();
            return isDoctor.equals("Doctor");
        }
        else
            return false;
    }
}
