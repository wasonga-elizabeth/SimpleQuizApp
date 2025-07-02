package com.example.simplequizapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
public class Quizviewmodel extends ViewModel {

    // Sample Questions
    private List<Question> questions = Arrays.asList(
            new Question("What is the capital of France?", Arrays.asList("Berlin", "Madrid", "Paris", "Rome"), 2),
            new Question("Which planet is known as the Red Planet?", Arrays.asList("Earth", "Mars", "Jupiter", "Saturn"), 1),
            new Question("What is the largest ocean on Earth?", Arrays.asList("Atlantic", "Indian", "Arctic", "Pacific"), 3)
            // Add more questions here
    );

    private int currentQuestionIndex = 0;
    private int score = 0;

    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQuizFinished = new MutableLiveData<>(false);
    private MutableLiveData<String> scoreText = new MutableLiveData<>();
    private MutableLiveData<String> answerFeedback = new MutableLiveData<>(); // To show correct/incorrect

    public Quizviewmodel() {
        loadQuestion();
        updateScoreText();
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean> getIsQuizFinished() {
        return isQuizFinished;
    }

    public LiveData<String> getScoreText() {
        return scoreText;
    }

    public LiveData<String> getAnswerFeedback() {
        return answerFeedback;
    }


    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            currentQuestion.setValue(questions.get(currentQuestionIndex));
            answerFeedback.setValue(null); // Reset feedback
        } else {
            isQuizFinished.setValue(true);
        }
    }

    public void submitAnswer(int selectedOptionIndex) {
        Question question = currentQuestion.getValue();
        if (question == null) return;

        if (selectedOptionIndex == question.getCorrectAnswerIndex()) {
            score++;
            answerFeedback.setValue("Correct!");
        } else {
            answerFeedback.setValue("Incorrect. The correct answer was: " + question.getOptions().get(question.getCorrectAnswerIndex()));
        }
        updateScoreText();
    }

    public void nextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }

    private void updateScoreText() {
        scoreText.setValue(String.format(Locale.getDefault(), "Score: %d/%d", score, questions.size()));
    }

    public void restartQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        isQuizFinished.setValue(false);
        loadQuestion();
        updateScoreText();
    }
}