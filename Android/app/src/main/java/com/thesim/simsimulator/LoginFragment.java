package com.thesim.simsimulator;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.thesim.simsimulator.utils.Helper;

import static com.thesim.simsimulator.AuthActivity.firebaseAuth;
import static com.thesim.simsimulator.AuthActivity.firebaseUser;

/**
 * LoginFragment handles user sign in authentication.
 */
public class LoginFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();

  private View view;
  private ImageView ivLogo;
  private EditText etEmail;
  private EditText etPassword;
  private Button btnLogin;
  private TextView tvSignUp;
  private TextView tvForget;
  private FragmentManager fragmentManager;

  public LoginFragment() {

  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_login, container, false);

    ivLogo = (ImageView) view.findViewById(R.id.login_logo);
    etEmail = (EditText) view.findViewById(R.id.login_email);
    etPassword = (EditText) view.findViewById(R.id.login_password);
    btnLogin = (Button) view.findViewById(R.id.button_login);
    tvSignUp = (TextView) view.findViewById(R.id.link_signup);
    tvForget = (TextView) view.findViewById(R.id.link_forget);
    fragmentManager = getActivity().getSupportFragmentManager();

    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        login();
      }
    });

    tvSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        replaceFragment("SignUpFragment");
      }
    });

    tvForget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        replaceFragment("ForgetFragment");
      }
    });

    return view;
  }

  protected void login() {
    if (!validateData()) {
      onLoginFailed();
      return;
    }

    btnLogin.setEnabled(false);

    final String email = etEmail.getText().toString();
    final String password = etPassword.getText().toString();

    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          // Account creation successful, user is signed in with new account.
          firebaseUser = firebaseAuth.getCurrentUser();
          onLoginSuccess();
        } else {
          onLoginFailed();
        }
      }
    });
  }

  protected void onLoginFailed() {
    Log.d(TAG, "login failed");
    if (Helper.isNetworkAvailable(getActivity().getBaseContext())) {
      Toast.makeText(getActivity().getBaseContext(), "The entered email and/or password is incorrect", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(getActivity().getBaseContext(), "Failed to connect to network", Toast.LENGTH_LONG).show();
    }
    btnLogin.setEnabled(true);
  }

  protected void onLoginSuccess() {
    Log.d(TAG, "login completed successfully");
    Toast.makeText(getActivity().getBaseContext(), "Welcome, " + firebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
    btnLogin.setEnabled(true);

    Intent intent = new Intent(getActivity(), MainActivity.class);
    startActivity(intent);
    getActivity().finish();
  }

  protected boolean validateData() {
    boolean valid = true;

    String email = etEmail.getText().toString();
    String password = etPassword.getText().toString();

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
      case "SignUpFragment":
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_enter, R.anim.left_exit)
            .replace(R.id.frameContainer, new SignUpFragment(), "SignUpFragment")
            .commit();
        break;

      case "ForgetFragment":
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.left_enter, R.anim.right_exit)
            .replace(R.id.frameContainer, new ForgetFragment(), "ForgetFragment")
            .commit();
        break;
    }
  }
}
