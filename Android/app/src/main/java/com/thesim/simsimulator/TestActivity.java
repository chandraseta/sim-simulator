package com.thesim.simsimulator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<String> {

  private static final String LOG_TAG = TestActivity.class.getSimpleName();
  private RecyclerView mRecyclerView;
  private QuestionListAdapter mAdapter;
  private SharedPreferences prefs;
  private Button btnFinish;
  private int size;
  private ProgressDialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    mRecyclerView.setNestedScrollingEnabled(false);
    mAdapter = new QuestionListAdapter(this);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    btnFinish = (Button) findViewById(R.id.button_finish_test);
    btnFinish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        double percentage;
        try {
          percentage = mAdapter.getCorrectPercentage();
        } catch (Exception e) {
          e.printStackTrace();
          percentage = 0;
        }
        String percentageString = String.valueOf(percentage);
        Intent intent = new Intent(view.getContext(), ResultActivity.class);
        intent.putExtra("correctPercentage", percentageString);
        startActivity(intent);
      }
    });

    prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
    String qAmtPref = prefs.getString("QuestionAmount", "30");
    size = Integer.parseInt(qAmtPref);
    mAdapter.setSize(size);


    if (getSupportLoaderManager().getLoader(0) != null) {
      getSupportLoaderManager().initLoader(0, null, this);
    }

    if (savedInstanceState != null) {
      if (savedInstanceState.getBoolean("questions_fetched", false)) {
        mAdapter.setQuestions((ArrayList<Question>)
            savedInstanceState.getSerializable("questions"));
        mAdapter.setQuestionsShuffling((ArrayList<ArrayList<Integer>>)
            savedInstanceState.getSerializable("questions_shuffling"));
        mAdapter.setUserAnswers(
            savedInstanceState.getIntegerArrayList("user_answer"));
        mAdapter.notifyDataSetChanged();
      }
    } else {
      btnFinish.setVisibility(View.INVISIBLE);
      dialog = new ProgressDialog(this);
      dialog.setIndeterminate(true);
      dialog.setMessage("Loading questions…");
      dialog.show();
      loadQuestions();
    }
  }

  public void loadQuestions() {
    ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      Bundle queryBundle = new Bundle();
      queryBundle.putInt("size", size);
      getSupportLoaderManager().restartLoader(0, queryBundle, this);
    } else {
      Toast.makeText(this, "No internet connection, please reconnect to continue using SIM Simulator", Toast.LENGTH_LONG).show();
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }

  @Override
  public Loader<String> onCreateLoader(int id, Bundle args) {
    return new QuestionLoader(this, args.getInt("size"));
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String data) {
    mAdapter.parseJson(data);
    mAdapter.generateNewConfig();
    if (dialog.isShowing()) {
      dialog.dismiss();
    }
    btnFinish.setVisibility(View.VISIBLE);
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (mAdapter != null) {
      outState.putBoolean("question_fetched", true);
      outState.putSerializable("questions", mAdapter.getQuestions());
      outState.putSerializable("questions_shuffling", mAdapter.getQuestionsShuffling());
      outState.putIntegerArrayList("user_answer", mAdapter.getUserAnswers());
    }
  }
}
