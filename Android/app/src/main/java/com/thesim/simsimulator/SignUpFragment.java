package com.thesim.simsimulator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.thesim.simsimulator.utils.Helper;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.thesim.simsimulator.AuthActivity.firebaseAuth;
import static com.thesim.simsimulator.AuthActivity.firebaseUser;

/**
 * SignUpFragment handles user account creation.
 */
public class SignUpFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();

  private View view;
  private EditText etName;
  private EditText etEmail;
  private EditText etPassword;
  private Button btnSignUp;
  private TextView tvLogin;
  private FragmentManager fragmentManager;

  public SignUpFragment() {

  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_signup, container, false);

    etName = (EditText) view.findViewById(R.id.signup_name);
    etEmail = (EditText) view.findViewById(R.id.signup_email);
    etPassword = (EditText) view.findViewById(R.id.signup_password);
    btnSignUp = (Button) view.findViewById(R.id.button_signup);
    tvLogin = (TextView) view.findViewById(R.id.link_login);
    fragmentManager = getActivity().getSupportFragmentManager();

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signUp();
      }
    });

    tvLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        replaceFragment("LoginFragment");
      }
    });

    return view;
  }

  protected void signUp() {
    if (!validateData()) {
      onSignUpFailed();
      return;
    }

    btnSignUp.setEnabled(false);

    final String name = etName.getText().toString();
    final String email = etEmail.getText().toString();
    final String password = etPassword.getText().toString();

    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          // Account creation successful, user is signed in with new account.
          Log.d(TAG, "createUserWithEmailAndPassword completed successfully");
          firebaseUser = firebaseAuth.getCurrentUser();
          UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
          try {
            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  Log.d(TAG, "updateProfile completed successfully");
                }
              }
            });
          } catch (Exception ex) {
            ex.printStackTrace();
          }
          onSignUpSuccess();
        } else {
          onSignUpFailed();
        }
      }
    });
  }

  protected void onSignUpFailed() {
    Log.d(TAG, "signUp failed");
    if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
      Toast.makeText(getActivity().getBaseContext(), "Failed to create new account", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(getActivity().getBaseContext(), "Failed to connect to network", Toast.LENGTH_LONG).show();
    }
    btnSignUp.setEnabled(true);
  }

  protected void onSignUpSuccess() {
    Log.d(TAG, "signUp completed successfully");
    Toast.makeText(getActivity().getBaseContext(), "Account created successfully", Toast.LENGTH_LONG).show();
    btnSignUp.setEnabled(true);

    final String name = etName.getText().toString();
    final String email = etEmail.getText().toString();
    final String password = etPassword.getText().toString();

    RegisterToServer register = new RegisterToServer(name, email, password);
    register.execute();

    Intent intent = new Intent(getActivity(), MainActivity.class);
    startActivity(intent);
    getActivity().finish();
  }

  protected boolean validateData() {
    boolean valid = true;

    String name = etName.getText().toString();
    String email = etEmail.getText().toString();
    String password = etPassword.getText().toString();

    if (name.isEmpty() || name.length() < 3) {
      etName.setError("must be at least 3 characters long");
      valid = false;
    } else {
      etName.setError(null);
    }

    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      etEmail.setError("enter a valid email address");
      valid = false;
    } else {
      etEmail.setError(null);
    }

    if (password.isEmpty() || password.length() < 8 || password.length() > 40) {
      etPassword.setError("must be between 8 and 40 alphanumeric characters");
      valid = false;
    } else {
      etPassword.setError(null);
    }

    return valid;
  }

  protected void replaceFragment(String fragmentName) {
    switch (fragmentName) {
      case "LoginFragment":
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_exit)
                .replace(R.id.frameContainer, new LoginFragment(), "LoginFragment")
                .commit();
        break;

      case "ForgetFragment":
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_exit)
                .replace(R.id.frameContainer, new ForgetFragment(), "ForgetFragment")
                .commit();
        break;
    }
  }

  private class RegisterToServer extends AsyncTask<String, HttpURLConnection, String> {

    String name;
    String email;
    String password;
    String result;

    RegisterToServer(String name, String email, String password) {
      this.name = name;
      this.email = email;
      this.password = password;
    }

    @Override
    protected String doInBackground(String... strings) {
      result =  Helper.register(name, email, password);
      return result;
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      if (result != null) {
        Log.d(TAG, result);
      }
      else {
        Log.d(TAG, "RegisterToServer returns null");
      }
    }
  }
}
