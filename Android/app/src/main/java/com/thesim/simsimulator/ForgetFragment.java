package com.thesim.simsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import static com.thesim.simsimulator.AuthActivity.firebaseAuth;

/**
 * ForgetFragment handles user's password reset in case the old password is forgotten.
 */
public class ForgetFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();

  private View view;
  private EditText etEmail;
  private Button btnForget;
  private TextView tvLogin;
  private FragmentManager fragmentManager;

  public ForgetFragment() {

  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_forget, container, false);

    etEmail = (EditText) view.findViewById(R.id.forget_email);
    btnForget = (Button) view.findViewById(R.id.button_forget);
    tvLogin = (TextView) view.findViewById(R.id.link_relogin);
    fragmentManager = getActivity().getSupportFragmentManager();

    btnForget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetPassword();
      }
    });

    tvLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        replaceFragment("LoginFragment");
      }
    });

    return view;
  }

  protected void resetPassword() {
    if (!validateData()) {
      onForgetFailed();
      return;
    }

    btnForget.setEnabled(false);

    String email = etEmail.getText().toString();
    firebaseAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              onForgetSuccess();
            } else {
              onForgetFailed();
            }
          }
        });
  }

  protected void onForgetFailed() {
    Toast.makeText(getActivity().getBaseContext(), "Email sent", Toast.LENGTH_LONG).show();
    btnForget.setEnabled(true);
    replaceFragment("LoginFragment");
  }

  protected void onForgetSuccess() {
    Toast.makeText(getActivity().getBaseContext(), "Email sent", Toast.LENGTH_LONG).show();
    btnForget.setEnabled(true);
    replaceFragment("LoginFragment");
  }

  protected boolean validateData() {
    boolean valid = true;

    String email = etEmail.getText().toString();

    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      etEmail.setError("enter a valid email address");
      valid = false;
    } else {
      etEmail.setError(null);
    }

    return valid;
  }

  protected void replaceFragment(String fragmentName) {
    switch (fragmentName) {
      case "LoginFragment":
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_enter, R.anim.left_exit)
            .replace(R.id.frameContainer, new LoginFragment(), "LoginFragment")
            .commit();
        break;

      case "SignUpFragment":
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.left_enter, R.anim.right_exit)
            .replace(R.id.frameContainer, new SignUpFragment(), "SignUpFragment")
            .commit();
        break;
    }
  }
}
