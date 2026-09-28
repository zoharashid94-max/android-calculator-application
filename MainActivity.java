package com.example.simplecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView resultTv;
    private TextView solutionTv;

    private MaterialButton buttonC;
    private MaterialButton buttonBrackOpen;
    private MaterialButton buttonBrackClose;
    private MaterialButton buttonDivide;
    private MaterialButton buttonMultiply;
    private MaterialButton buttonAddition;
    private MaterialButton buttonSubtract;
    private MaterialButton buttonEquals;

    private MaterialButton button0;
    private MaterialButton button1;
    private MaterialButton button2;
    private MaterialButton button3;
    private MaterialButton button4;
    private MaterialButton button5;
    private MaterialButton button6;
    private MaterialButton button7;
    private MaterialButton button8;
    private MaterialButton button9;

    private MaterialButton buttonAC;
    private MaterialButton buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        buttonC = findViewById(R.id.button_c);
        buttonBrackOpen = findViewById(R.id.button_open_bracket);
        buttonBrackClose = findViewById(R.id.button_close_bracket);

        buttonDivide = findViewById(R.id.button_divide);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonAddition = findViewById(R.id.button_addition);
        buttonSubtract = findViewById(R.id.button_subtract);
        buttonEquals = findViewById(R.id.button_equals);

        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);

        buttonAC = findViewById(R.id.button_ac);
        buttonDot = findViewById(R.id.button_dot);

        setClickListener(
                buttonC,
                buttonBrackOpen,
                buttonBrackClose,
                buttonDivide,
                buttonMultiply,
                buttonAddition,
                buttonSubtract,
                buttonEquals,
                button0,
                button1,
                button2,
                button3,
                button4,
                button5,
                button6,
                button7,
                button8,
                button9,
                buttonAC,
                buttonDot
        );
    }

    private void setClickListener(MaterialButton... buttons) {

        for (MaterialButton button : buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        MaterialButton button = (MaterialButton) view;

        String buttonText = button.getText().toString();

        String dataToCalculate =
                solutionTv.getText().toString();

        // AC Button
        if (buttonText.equals("AC")) {

            solutionTv.setText("");
            resultTv.setText("0");

            return;
        }

        // C Button
        if (buttonText.equals("C")) {

            if (!dataToCalculate.isEmpty()) {

                dataToCalculate =
                        dataToCalculate.substring(
                                0,
                                dataToCalculate.length() - 1
                        );
            }

            solutionTv.setText(dataToCalculate);

            String result = getResult(dataToCalculate);

            if (!result.equals("Err")) {
                resultTv.setText(result);
            }

            return;
        }

        // Equal Button
        if (buttonText.equals("=")) {

            String result = getResult(dataToCalculate);

            if (!result.equals("Err")) {

                resultTv.setText(result);
                solutionTv.setText(result);
            }

            return;
        }

        // Operators
        if (buttonText.equals("×")) {

            dataToCalculate = dataToCalculate + "*";
        }

        else if (buttonText.equals("÷")) {

            dataToCalculate = dataToCalculate + "/";
        }

        else {

            dataToCalculate = dataToCalculate + buttonText;
        }

        solutionTv.setText(dataToCalculate);

        String finalResult =
                getResult(dataToCalculate);

        if (!finalResult.equals("Err")) {

            resultTv.setText(finalResult);
        }
    }

    // Get Result
    private String getResult(String data) {

        try {

            if (data.isEmpty()) {
                return "0";
            }

            double result = calculate(data);

            if (result == (long) result) {

                return String.valueOf((long) result);
            }

            return String.valueOf(result);

        }

        catch (Exception e) {

            return "Err";
        }
    }

    // Calculator
    private double calculate(final String expression) {

        return new Object() {

            int position = -1;
            int character;

            void nextCharacter() {

                position++;

                if (position < expression.length()) {

                    character =
                            expression.charAt(position);
                }

                else {

                    character = -1;
                }
            }

            boolean eat(int characterToEat) {

                while (character == ' ') {

                    nextCharacter();
                }

                if (character == characterToEat) {

                    nextCharacter();

                    return true;
                }

                return false;
            }

            double parse() {

                nextCharacter();

                double result =
                        parseExpression();

                if (position < expression.length()) {

                    throw new RuntimeException(
                            "Unexpected character"
                    );
                }

                return result;
            }

            double parseExpression() {

                double result =
                        parseTerm();

                while (true) {

                    if (eat('+')) {

                        result =
                                result + parseTerm();
                    }

                    else if (eat('-')) {

                        result =
                                result - parseTerm();
                    }

                    else {

                        return result;
                    }
                }
            }

            double parseTerm() {

                double result =
                        parseFactor();

                while (true) {

                    if (eat('*')) {

                        result =
                                result * parseFactor();
                    }

                    else if (eat('/')) {

                        double divisor =
                                parseFactor();

                        if (divisor == 0) {

                            throw new ArithmeticException(
                                    "Cannot divide by zero"
                            );
                        }

                        result =
                                result / divisor;
                    }

                    else {

                        return result;
                    }
                }
            }

            double parseFactor() {

                if (eat('+')) {

                    return parseFactor();
                }

                if (eat('-')) {

                    return -parseFactor();
                }

                double result;

                int startPosition =
                        position;

                if (eat('(')) {

                    result =
                            parseExpression();

                    eat(')');
                }

                else if (
                        (character >= '0'
                                && character <= '9')
                                || character == '.'
                ) {

                    while (
                            (character >= '0'
                                    && character <= '9')
                                    || character == '.'
                    ) {

                        nextCharacter();
                    }

                    result =
                            Double.parseDouble(
                                    expression.substring(
                                            startPosition,
                                            position
                                    )
                            );
                }

                else {

                    throw new RuntimeException(
                            "Unexpected character"
                    );
                }

                return result;
            }

        }.parse();
    }
}