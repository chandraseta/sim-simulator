package com.thesim.simsimulator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

/**
 * QuestionListAdapter is an adapter for list of questions in TestActivity.
 */
public class QuestionListAdapter
    extends RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder> {

  private static final String LOG_TAG = QuestionListAdapter.class.getSimpleName();

  private ArrayList<Question> questions;
  private ArrayList<Integer> userAnswers;
  private ArrayList<ArrayList<Integer>> questionsShuffling;
  private int size;

  private LayoutInflater mInflater;

  QuestionListAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public QuestionListAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mItemView = mInflater.inflate(R.layout.fragment_question, parent, false);
    return new QuestionViewHolder(mItemView, this);
  }

  @Override
  public void onBindViewHolder(QuestionListAdapter.QuestionViewHolder holder, int position) {
    Question question = questions.get(position);
    ArrayList<Integer> map = questionsShuffling.get(position);

    holder.mIdView.setText(String.format(Locale.ENGLISH, "%d", position + 1));
    holder.mBodyView.setText(question.getBody());
    holder.mRadio.get(map.get(0)).setText(question.getAnswer());
    holder.mRadio.get(map.get(1)).setText(question.getAnswerAlt1());
    holder.mRadio.get(map.get(2)).setText(question.getAnswerAlt2());
    holder.mRadio.get(map.get(3)).setText(question.getAnswerAlt3());
  }

  @Override
  public int getItemCount() {
    if (questions != null) {
      return size;
    } else {
      return 0;
    }
  }

  public double getCorrectPercentage() {
    int totalQuestion = 0;
    int totalCorrect = 0;

    for (int i = 0; i < questionsShuffling.size(); i++) {
      totalQuestion += 1;
      if (questionsShuffling.get(i).get(0).equals(userAnswers.get(i))) {
        totalCorrect++;
      }
    }

    return (double) totalCorrect / (double) totalQuestion;
  }

  void parseJson(String jsonData) {
    questions = new ArrayList<>();

    try {
      JSONArray questionsArray = new JSONArray(jsonData);

      // Iterate through the results
      for (int i = 0; i < questionsArray.length(); i++) {
        JSONObject qJson = questionsArray.getJSONObject(i); // Get the current item
        Question question = new Question();

        try {
          question.setId(qJson.getInt("id"));
          question.setBody(qJson.getString("body"));
          question.setImageId(qJson.getString("image_id"));
          question.setAnswer(qJson.getString("answer"));
          question.setAnswerAlt1(qJson.getString("answer_alt_1"));
          question.setAnswerAlt2(qJson.getString("answer_alt_2"));
          question.setAnswerAlt3(qJson.getString("answer_alt_3"));
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (question.isDataComplete()) {
          questions.add(question);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    notifyDataSetChanged();
  }

  void generateNewConfig() {
    if (questionsShuffling == null) {
      questionsShuffling = new ArrayList<>(questions.size());
      for (int i = 0; i < questions.size(); ++i) {
        ArrayList<Integer> map = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(map);
        questionsShuffling.add(map);
      }
      for (int i = 0; i < questionsShuffling.size(); ++i) {
        Log.d("ANSWER", i + ": " + questionsShuffling.get(i));
      }
    }
    if (userAnswers == null) {
      userAnswers = new ArrayList<>(Collections.nCopies(questions.size(), -1));
    }
  }

  public ArrayList<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(ArrayList<Question> questions) {
    this.questions = questions;
  }

  public ArrayList<Integer> getUserAnswers() {
    return userAnswers;
  }

  public void setUserAnswers(ArrayList<Integer> userAnswers) {
    this.userAnswers = userAnswers;
  }

  public ArrayList<ArrayList<Integer>> getQuestionsShuffling() {
    return questionsShuffling;
  }

  public void setQuestionsShuffling(ArrayList<ArrayList<Integer>> questionsShuffling) {
    this.questionsShuffling = questionsShuffling;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  class QuestionViewHolder extends RecyclerView.ViewHolder {

    int position;
    final TextView mIdView;
    final TextView mBodyView;
    final ImageView mImageView;
    final RadioGroup mRadioGroup;
    final ArrayList<RadioButton> mRadio;
    final QuestionListAdapter mAdapter;

    QuestionViewHolder(View itemView, QuestionListAdapter adapter) {
      super(itemView);

      mIdView = (TextView) itemView.findViewById(R.id.question_number);
      mBodyView = (TextView) itemView.findViewById(R.id.question_text);
      mImageView = (ImageView) itemView.findViewById(R.id.question_image);
      mRadioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
      mAdapter = adapter;

      int count = mRadioGroup.getChildCount();
      mRadio = new ArrayList<>();
      for (int i = 0; i < count; i++) {
        View o = mRadioGroup.getChildAt(i);
        if (o instanceof RadioButton) {
          mRadio.add((RadioButton) o);
        }
      }

      position = (mRadio.get(mRadio.size() - 1).getId() / 4) % size;
      if (position == 0)
        position = size;

      int checked;
      if (position > 0) {
        position--;
        checked = userAnswers.get(position);
      } else {
        checked = -1;
      }

      if (checked != -1) {
        mRadioGroup.check(checked);
      }
      mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
          int checkedIdInGroup = (checkedId % 4) - 1;
          if (checkedIdInGroup < 0)
            checkedIdInGroup += 4;

          Log.d("LISTENER", Integer.toString(position) + ": " + Integer.toString(checkedIdInGroup));
          if (position != -1) {
            userAnswers.set(position, checkedIdInGroup);
          }
        }
      });
    }
  }
}
