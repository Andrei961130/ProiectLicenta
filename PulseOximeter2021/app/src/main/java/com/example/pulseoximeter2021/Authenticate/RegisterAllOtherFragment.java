package com.example.pulseoximeter2021.Authenticate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.DataLayer.Room.MyFirebaseDatabase;
import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class RegisterAllOtherFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    EditText etFirstName;
    EditText etLastName;
    EditText etPhoneNumber;
    RadioGroup rgGender;
    EditText etBirthDay;
    Button btnContinue;

    String genderStr = "Male";
    String dateStr = "";

    public RegisterAllOtherFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_all_other, container, false);

        etFirstName = view.findViewById(R.id.fragment_register_et_first_name);
        etLastName = view.findViewById(R.id.fragment_register_et_last_name);
        etPhoneNumber = view.findViewById(R.id.fragment_register_et_phone_number);
        rgGender = view.findViewById(R.id.fragment_register_radio_group);
        etBirthDay = view.findViewById(R.id.fragment_register_et_date);
        btnContinue = view.findViewById(R.id.fragment_register_all_btn_continue);

        btnContinue.setOnClickListener(this::finalizeRegistration);
        rgGender.setOnCheckedChangeListener(this::rgGenderOnCheckedChange);

        etBirthDay.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();

            new DatePickerDialog(getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateStr = new StringBuilder().append(dayOfMonth)
                        .append("/").append(monthOfYear)
                        .append("/").append(year).toString();

                etBirthDay.setText(String.format(" %s", dateStr));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();

        });

        return view;
    }

    private void finalizeRegistration(View view)
    {
        String firstNameStr = etFirstName.getText().toString();
        String lastNameStr = etLastName.getText().toString();
        String phoneStr = etPhoneNumber.getText().toString();

        if(firstNameStr.isEmpty())
        {
            etFirstName.setError("Please insert your first name");
            etFirstName.requestFocus();
        }
        else if(lastNameStr.isEmpty())
        {
            etLastName.setError("Please insert your last name");
            etLastName.requestFocus();
        }
        else if(phoneStr.isEmpty())
        {
            etPhoneNumber.setError("Please insert your phone number");
            etPhoneNumber.requestFocus();
        }
        else if(!PhoneNumberUtils.isGlobalPhoneNumber(phoneStr))
        {
            etPhoneNumber.setError("Please insert a valid phone number");
            etPhoneNumber.requestFocus();
        }
        else if(genderStr.isEmpty())
        {
            Toast.makeText(getContext(), "Please pick one option", Toast.LENGTH_LONG).show();
            rgGender.requestFocus();
        }
        else if(dateStr.isEmpty())
        {
            etBirthDay.setError("Please pick a date");
            etBirthDay.requestFocus();
        }
        else
        {
            String uid = firebaseAuth.getUid();

            Bundle bundle = getArguments();
            User user = new User(uid, bundle.getString("email"),
                    firstNameStr, lastNameStr, dateStr, genderStr, false);

            databaseReference.child("Users").child(uid).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),"Saved",Toast.LENGTH_LONG).show();
                            firebaseAuth.getCurrentUser().updateProfile( new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstNameStr.concat(" ").concat(lastNameStr))
                                    //.setPhotoUri(Uri.parse("Doctor"))
                                    .build()
                            );
                            requireActivity().finish();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Try again",Toast.LENGTH_SHORT).show();
                        }
                    });

//            AddUserInFireBase addUserInFireBase = new AddUserInFireBase();
//            addUserInFireBase.execute(user);


//            requireActivity().finish();
        }
    }

    private void rgGenderOnCheckedChange(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.fragment_register_rb_male:
                genderStr = "Male";
                break;
            case R.id.fragment_register_rb_female:
                genderStr = "Female";
                break;
        }
    }

    private class AddUserInFireBase extends AsyncTask<User,Void,Void>
    {
        @Override
        protected Void doInBackground(User... users) {
            new MyFirebaseDatabase().addUser(users[0].getUid(), users[0], new MyFirebaseDatabase.DataStatus() {
                @Override
                public void userDataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {

                }

                @Override
                public void userDataIsInserted() {

                }

                @Override
                public void userDataIsUpdated() {

                }

                @Override
                public void userDataIsDeleted() {

                }

                @Override
                public void recordDataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException {

                }

                @Override
                public void recordDataIsInserted() {

                }

                @Override
                public void recordDataIsUpdated() {

                }

                @Override
                public void recordDataIsDeleted() {

                }
            });
            return null;
        }
    }
}