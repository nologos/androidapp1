package com.m4n0.myapplication.a2023app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
// ^^ boilerplate

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Random;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //    my definitions
    private TextView problemText;
    private TextView answerWindow;
    private int goodAnswer;

    // my functions
    private int presentequasion() {
        // random number between 2 and 9
        Random rand = new Random();
        int num1 = rand.nextInt(10) + 2;
        int num2 = rand.nextInt(8) + 2;

        // convert to string {1st number} x {2nd number} =

        String problem = num1 + " x " + num2 + " = ";
        problemText.setText(problem);
        // returns num1 * num2
        int answer = num1 * num2;
        return answer;
    }


    //        \/\/ boilerplate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ^^ boilerplate
// below is main
        problemText = findViewById(R.id.problemwindow);
        answerWindow = findViewById(R.id.answerwindow);
        goodAnswer = presentequasion();

        GridLayout numberPad = findViewById(R.id.number_pad);
        int childCount = numberPad.getChildCount();


        for (int i = 0; i < childCount; i++) {
            View view = numberPad.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                String number = button.getText().toString();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentAnswer = answerWindow.getText().toString();
                        answerWindow.setText(currentAnswer + number);
                    }
                });
            }
        }

        Button btnBack = findViewById(R.id.btnBack);
        Button btnEnter = findViewById(R.id.btnEnter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentAnswer = answerWindow.getText().toString();
                if (currentAnswer.length() > 0) {
                    answerWindow.setText(currentAnswer.substring(0, currentAnswer.length() - 1));
                }
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if currentAnswer is equal to goodAnswer
                String currentAnswer = answerWindow.getText().toString();
                if (currentAnswer.length() > 0) {
                    int currentAnswerInt = Integer.parseInt(currentAnswer);
                    if (currentAnswerInt == goodAnswer) {
                        Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                        goodAnswer = presentequasion();
                        answerWindow.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                        answerWindow.setText("");
                    }
                }


            }
        });
    }
}