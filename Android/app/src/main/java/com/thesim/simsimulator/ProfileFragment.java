package com.thesim.simsimulator;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.thesim.simsimulator.AuthActivity.firebaseUser;


/**
 * ProfileFragment handles user profile changes such as email or password change.
 */
public class ProfileFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();
  private View view;

  private ImageView ivLogo;
  private TextView tvNamePrompt;
  private TextView tvEmailPrompt;
  private TextView tvPasswordPrompt;
  private EditText etName;
  private EditText etEmail;
  private EditText etPassword;
  private Button btnName;
  private Button btnEmail;
  private Button btnPassword;

  boolean darkThemeActivated;

  public ProfileFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_profile, container, false);

    ivLogo = (ImageView) view.findViewById(R.id.edit_logo);
    tvNamePrompt = (TextView) view.findViewById(R.id.edit_name_prompt);
    tvEmailPrompt = (TextView) view.findViewById(R.id.edit_email_prompt);
    tvPasswordPrompt = (TextView) view.findViewById(R.id.edit_password_prompt);
    etName = (EditText) view.findViewById(R.id.edit_name);
    etEmail = (EditText) view.findViewById(R.id.edit_email);
    etPassword = (EditText) view.findViewById(R.id.edit_password);

    btnName = (Button) view.findViewById(R.id.button_profile_name);
    btnName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String name = etName.getText().toString();
        if (name.isEmpty()) {
          etName.setError("enter a name");
        } else {
          UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
          try {
            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  Log.d(TAG, "updateProfile completed successfully");
                  Toast.makeText(getActivity().getBaseContext(), "Name changed to " + name, Toast.LENGTH_LONG).show();
                } else {
                  Toast.makeText(getActivity().getBaseContext(), "Failed to change name, check your internet connection", Toast.LENGTH_LONG).show();
                }
              }
            });
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    });

    btnEmail = (Button) view.findViewById(R.id.button_profile_email);
    btnEmail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String email = etEmail.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          etEmail.setError("enter a valid email address");
        } else {
          firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                Toast.makeText(getActivity().getBaseContext(), "Email changed to " + email, Toast.LENGTH_LONG).show();
              } else {
                Toast.makeText(getActivity().getBaseContext(), "Failed to change email, check your internet connection", Toast.LENGTH_LONG).show();
              }
            }
          });

        }
      }
    });

    btnPassword = (Button) view.findViewById(R.id.button_profile_password);
    btnPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String password = etPassword.getText().toString();
        if (password.isEmpty() || password.length() < 8 || password.length() > 40) {
          etPassword.setError("must be between 8 and 40 alphanumeric characters");
        } else {
          firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                Toast.makeText(getActivity().getBaseContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
              } else {
                Toast.makeText(getActivity().getBaseContext(), "Failed to change password, check your internet connection", Toast.LENGTH_LONG).show();
              }
            }
          });
        }
      }
    });

    ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (darkThemeActivated) {
          darkThemeActivated = false;
          view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
          tvNamePrompt.setTextColor(getResources().getColor(R.color.fontDark));
          tvPasswordPrompt.setTextColor(getResources().getColor(R.color.fontDark));
          tvEmailPrompt.setTextColor(getResources().getColor(R.color.fontDark));
          etName.setTextColor(getResources().getColor(R.color.fontLessDark));
          etEmail.setTextColor(getResources().getColor(R.color.fontLessDark));
          etPassword.setTextColor(getResources().getColor(R.color.fontLessDark));
          Toast.makeText(getActivity().getBaseContext(), "Normal theme activated", Toast.LENGTH_SHORT).show();
        } else {
          darkThemeActivated = true;
          view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
          tvNamePrompt.setTextColor(getResources().getColor(R.color.fontLight));
          tvPasswordPrompt.setTextColor(getResources().getColor(R.color.fontLight));
          tvEmailPrompt.setTextColor(getResources().getColor(R.color.fontLight));
          etName.setTextColor(getResources().getColor(R.color.fontLight));
          etEmail.setTextColor(getResources().getColor(R.color.fontLight));
          etPassword.setTextColor(getResources().getColor(R.color.fontLight));
          Toast.makeText(getActivity().getBaseContext(), "Dark mode for edit profile activated", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

    return view;
  }

}
