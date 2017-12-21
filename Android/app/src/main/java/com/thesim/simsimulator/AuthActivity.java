package com.thesim.simsimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * AuthActivity handles user account creation and user authentication using Google Firebase.
 */

public class AuthActivity extends AppCompatActivity {

  private FragmentManager fragmentManager;

  public static FirebaseAuth firebaseAuth;
  public static FirebaseUser firebaseUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);
    fragmentManager = this.getSupportFragmentManager();

    if (savedInstanceState == null) {
      fragmentManager.beginTransaction()
          .replace(R.id.frameContainer, new LoginFragment(), "LoginFragment")
          .commit();

    }
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}
