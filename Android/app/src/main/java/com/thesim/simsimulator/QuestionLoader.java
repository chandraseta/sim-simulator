package com.thesim.simsimulator;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.thesim.simsimulator.utils.Helper;

/**
 * AsyncTaskLoader class to fetch questions from SIM Simulator server.
 */
class QuestionLoader extends AsyncTaskLoader<String> {

  private int size;

  QuestionLoader(Context context, int size) {
    super(context);
    this.size = size;
  }

  @Override
  protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }

  @Override
  public String loadInBackground() {
    return Helper.getQuestions(size);
  }
}
