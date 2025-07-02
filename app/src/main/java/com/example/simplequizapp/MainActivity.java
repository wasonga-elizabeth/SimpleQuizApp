package com.example.simplequizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Quizviewmodel quizViewModel;

    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private RadioButton radioButtonOption1;
    private RadioButton radioButtonOption2;
    private RadioButton radioButtonOption3;
    private RadioButton radioButtonOption4;
    private Button buttonSubmitNext;
    private TextView textViewScore;

    private List<RadioButton> radioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel
        quizViewModel = new ViewModelProvider(this).get(Quizviewmodel.class);

        // Initialize Views
        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        radioButtonOption1 = findViewById(R.id.radioButtonOption1);
        radioButtonOption2 = findViewById(R.id.radioButtonOption2);
        radioButtonOption3 = findViewById(R.id.radioButtonOption3);
        radioButtonOption4 = findViewById(R.id.radioButtonOption4);
        buttonSubmitNext = findViewById(R.id.buttonSubmitNext);
        textViewScore = findViewById(R.id.textViewScore);

        radioButtons = new ArrayList<>();
        radioButtons.add(radioButtonOption1);
        radioButtons.add(radioButtonOption2);
        radioButtons.add(radioButtonOption3);
        radioButtons.add(radioButtonOption4);

        // Observe ViewModel LiveData
        quizViewModel.getCurrentQuestion().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                if (question != null) {
                    displayQuestion(question);
                }
            }
        });

        quizViewModel.getScoreText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String score) {
                textViewScore.setText(score);
            }
        });

        quizViewModel.getIsQuizFinished().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFinished) {
                if (isFinished) {
                    showFinalScore();
                }
            }
        });

        quizViewModel.getAnswerFeedback().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String feedback) {
                if (feedback != null && !feedback.isEmpty()) {
                    Toast.makeText(MainActivity.this, feedback, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button Click Listener
        buttonSubmitNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isFinished = quizViewModel.getIsQuizFinished().getValue();
                if (isFinished != null && isFinished) {
                    quizViewModel.restartQuiz();
                    buttonSubmitNext.setText("Submit");
                    enableAnswerOptions(true);
                    radioGroupAnswers.setVisibility(View.VISIBLE); // Make sure it's visible again
                } else {
                    int selectedOptionId = radioGroupAnswers.getCheckedRadioButtonId();
                    if (selectedOptionId != -1) {
                        RadioButton selectedRadioButton = findViewById(selectedOptionId);
                        int selectedAnswerIndex = radioButtons.indexOf(selectedRadioButton);

                        quizViewModel.submitAnswer(selectedAnswerIndex);

                        Question currentQ = quizViewModel.getCurrentQuestion().getValue();
                        Boolean currentFinishedState = quizViewModel.getIsQuizFinished().getValue();

                        if (currentQ != null && (currentFinishedState != null && !currentFinishedState)) {
                            buttonSubmitNext.setText("Next");
                            enableAnswerOptions(false);
                        }

                    } else if (buttonSubmitNext.getText().toString().equals("Next")) {
                        quizViewModel.nextQuestion();
                        buttonSubmitNext.setText("Submit");
                        enableAnswerOptions(true);
                        radioGroupAnswers.clearCheck();
                    } else {
                        Toast.makeText(MainActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void displayQuestion(Question question) {
        textViewQuestion.setText(question.getText());
        List<String> options = question.getOptions();
        for (int i = 0; i < radioButtons.size(); i++) {
            if (i < options.size()) {
                radioButtons.get(i).setText(options.get(i));
                radioButtons.get(i).setVisibility(View.VISIBLE);
            } else {
                radioButtons.get(i).setVisibility(View.GONE); // Hide unused radio buttons
            }
        }
        radioGroupAnswers.clearCheck();
        enableAnswerOptions(true);
    }

    private void enableAnswerOptions(boolean enabled) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setEnabled(enabled);
        }
    }

    private void showFinalScore() {
        textViewQuestion.setText("Quiz Finished!");
        radioGroupAnswers.setVisibility(View.GONE); // Hide radio group

        String finalScoreText = quizViewModel.getScoreText().getValue();
        Toast.makeText(this, "Quiz Finished! Your final score is: " + (finalScoreText != null ? finalScoreText : ""), Toast.LENGTH_LONG).show();
        buttonSubmitNext.setText("Restart Quiz");
    }
}