package com.thesim.simsimulator;

import java.io.Serializable;

/**
 * Question class contains a question for the TestActivity.
 */
public class Question implements Serializable {
  private int id = -1;
  private String body;
  private String imageId;
  private String answer;
  private String answerAlt1;
  private String answerAlt2;
  private String answerAlt3;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getAnswerAlt1() {
    return answerAlt1;
  }

  public void setAnswerAlt1(String answerAlt1) {
    this.answerAlt1 = answerAlt1;
  }

  public String getAnswerAlt2() {
    return answerAlt2;
  }

  public void setAnswerAlt2(String answerAlt2) {
    this.answerAlt2 = answerAlt2;
  }

  public String getAnswerAlt3() {
    return answerAlt3;
  }

  public void setAnswerAlt3(String answerAlt3) {
    this.answerAlt3 = answerAlt3;
  }

  public boolean isDataComplete() {
    return (id != -1 && body != null && answer != null &&
        answerAlt1 != null && answerAlt2 != null && answerAlt3 != null);
  }
}