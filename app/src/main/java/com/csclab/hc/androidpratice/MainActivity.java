package com.csclab.hc.androidpratice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Socket socket;
    String serverIP;
    int serverPort;

    EditText addressText;
    EditText portText;
    Button btnConnect;

    /** Init Variable for Page 1 **/
    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    /** Init Variable for Page 2 **/
    TextView textResult;

    Button return_button;

    /** Init Variable **/
    String oper = "";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Func() for setup page 1 **/
        jumpToInitLayout();
    }

    public void jumpToInitLayout() {
        setContentView(R.layout.activity_init);
        addressText = (EditText) findViewById(R.id.editText);
        if (addressText != null) {
            addressText.setHint("Server IP Address");
        }
        portText = (EditText) findViewById(R.id.editText2);
        if (portText != null) {
            portText.setHint("Server Port Number");
        }
        btnConnect = (Button) findViewById(R.id.button);
        if (btnConnect != null) {
            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverIP = addressText.getText().toString();
                    serverPort = Integer.parseInt(portText.getText().toString());
                    jumpToMainLayout();
                }
            });
        }
    }

    /** Function for page 1 setup */
    public void jumpToMainLayout() {
        setContentView(R.layout.activity_main);
        //TODO: Find and bind all elements(4 buttons 2 EditTexts)
        // inputNumTxt1, inputNumTxt2
        // btnAdd, btnSub, btnMult, btnDiv
        inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        //TODO: Set 4 buttons' listener
        // HINT: myButton.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    /** Function for onclick() implement */
    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        // defines the button that has been clicked and performs the corresponding operation
        // write operation into oper, we will use it later for output
        //TODO: calculate result
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }
        // HINT:Using log.d to check your answer is correct before implement page turning
        Log.d("debug","ANS "+result);
        //TODO: Pass the result String to jumpToResultLayout() and show the result at Result view
        jumpToResultLayout(num1 + " " + oper + " " + num2 + " = " + result);
    }

    public void jumpToResultLayout(final String resultStr){
        setContentView(R.layout.result_page);

        //TODO: Bind return_button and textResult form result view
        // HINT: findViewById()
        // HINT: Remember to give type
        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] toSend;
                toSend = Arrays.copyOf(resultStr.getBytes(), resultStr.getBytes().length);
                try {
                    Log.d("Ip/Port", serverIP + " " + serverPort);
                    socket = new Socket(serverIP, serverPort);
                    socket.getOutputStream().write(toSend);
                    socket.close();
                } catch (IOException e) {
                    Log.d("EXCEPTION", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
        if (textResult != null) {
            //TODO: Set the result text
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            //TODO: prepare button listener for return button
            // HINT:
            // mybutton.setOnClickListener(new View.OnClickListener(){
            //      public void onClick(View v) {
            //          // Something to do..
            //      }
            // }
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    // TODO
                    jumpToMainLayout();
                }

            });
        }
    }
}
