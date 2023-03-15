package com.m4n0.myapplication.a2023app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
// ^^ boilerplate

import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //    my definitions
    private TextView problemText;
    private TextView answerWindow;
    private int goodAnswer;


    private GridView mGridView;             // textlist
    private List<String> mWords;            // textlist

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

//        adding textview list
            mGridView = findViewById(R.id.gridView);
            mWords = new ArrayList<>();

            // Add some sample words to the list
            mWords.add("apple");
            mWords.add("banana");
            mWords.add("cherry");
            mWords.add("date");
            mWords.add("elderberry");

            // Create an adapter for the grid view and set it
            WordAdapter adapter = new WordAdapter(this, mWords);
            mGridView.setAdapter(adapter);

//        adding textview list close


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
                        if (currentAnswer.length() < 6){
                            answerWindow.setText(currentAnswer + number);
                            }else{
                                answerWindow.setText(currentAnswer);
                            }
                        
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
                        Toast toast = Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 100);
                        toast.show();
                        goodAnswer = presentequasion();
                        answerWindow.setText("");
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, "Incorrect!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 100);
                        toast.show();
                        answerWindow.setText("");
                    }
                }


            }
        });
    }
}