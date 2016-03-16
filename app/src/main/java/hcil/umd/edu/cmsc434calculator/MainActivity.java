package hcil.umd.edu.cmsc434calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button = (Button)findViewById(R.id.buttonKey7);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Java Hookup, button clicked: " + ((Button) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayoutCalculator);
        int count = gridLayout.getChildCount();
        ButtonClickHandler buttonClickHandler = new ButtonClickHandler();
        for(int i=0; i<count; i++){
            View v = gridLayout.getChildAt(i);
            if(v instanceof Button){
                v.setOnClickListener(buttonClickHandler);
            }
        }
    }

    private class ButtonClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getApplicationContext(), "Java Hookup, button clicked: " + ((Button) v).getText(), Toast.LENGTH_SHORT).show();

            TextView textViewOutputScreen = (TextView)findViewById(R.id.textViewOutputScreen);

            // A little defensive programming here--we expect that all clicked things are buttons
            // but just in case, we have this check here.
            if(v instanceof Button){
                Button buttonClicked = (Button)v;

                // Check for the clear button. If clear button pressed, reset calculator screen to zero
                if(v.getId() == R.id.buttonKeyC){
                    textViewOutputScreen.setText("0");
                }
                // Check for the equals sign. If pressed, then calculate our result
                else if(v.getId() == R.id.buttonKeyEquals){
                    try {
                        double calcResult = CalcUtils.evaluate(textViewOutputScreen.getText().toString());
                        textViewOutputScreen.setText(Double.toString(calcResult));
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        textViewOutputScreen.setText("0");
                    }
                }
                // Check if two math operators pressed in a row. If so, show an error message using Toast
                else if(textViewOutputScreen.getText().length() > 0 &&
                        CalcUtils.isOperator(textViewOutputScreen.getText().charAt(textViewOutputScreen.getText().length() - 1)) &&
                        CalcUtils.isOperator(buttonClicked.getText().charAt(0)))
                {
                    String errorMessage = "You cannot enter two math operators in a row. You entered " + textViewOutputScreen.getText().charAt(textViewOutputScreen.getText().length() - 1) + " and " + buttonClicked.getText() + ".";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
                // Else, concatenate current button text to calculator screen
                else {
                    if (textViewOutputScreen.getText().equals("0") &&
                            !CalcUtils.isOperator(buttonClicked.getText().charAt(0))) {
                        textViewOutputScreen.setText("");
                    }

                    textViewOutputScreen.setText(textViewOutputScreen.getText().toString() + buttonClicked.getText());
                }
            }

        }
    }
}
