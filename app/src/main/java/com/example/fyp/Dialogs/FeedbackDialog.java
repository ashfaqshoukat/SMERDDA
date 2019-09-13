package com.example.fyp.Dialogs;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;


import com.example.fyp.R;

import java.util.zip.Inflater;


public class FeedbackDialog extends DialogFragment {

    RadioGroup quality,deliveytime,responsetime,happy;
    TextView mTextView2,mButton;
    ImageView mCrossBtn;
    String[][] testData = { {"AVERAGE","NORMAL","LATE","NO"}};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_activity, container, false);
        init(view);
        allListener();
        return view;
    }

    private void init(View view) {

        quality=view.findViewById(R.id.productQuality);
        deliveytime=view.findViewById(R.id.deliverytime);
        responsetime=view.findViewById(R.id.responsetime);
        happy=view.findViewById(R.id.happy);
        mTextView2 = view.findViewById(R.id.text2);
        mButton = view.findViewById(R.id.sendbtn);
        mCrossBtn = view.findViewById(R.id.crossbtn);
    }

    private void allListener() {

        mCrossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[][] trainingData = {
                        {"GOOD","FAST","QUICKLY","YES","RECOMENDED"},
                        {"BAD","SLOW","QUICKLY","YES","NOT RECOMENDED"},
                        {"AVERAGE","SLOW","LATE","YES","NOT RECOMENDED"},
                        {"GOOD","NORMAL","QUICKLY","NO","RECOMENDED"}
                };
                ID3 classifier = new ID3();
                classifier.train(trainingData);
                classifier.printTree();
                classifier.classify(testData);
//                Toast.makeText(getContext(),testData.toString(),Toast.LENGTH_LONG).show();;
            }
        });
        quality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.good:
                        testData[0][0]="GOOD";
                        break;
                    case R.id.bad:
                        testData[0][0]="BAD";
                        break;
                    case R.id.average:
                        testData[0][0]="AVERAGE";
                        break;


                }
            }
        });

        deliveytime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.fast:
                        testData[0][1]="FAST";
                        break;
                    case R.id.slow:
                        testData[0][1]="SLOW";
                        break;
                    case R.id.normal:
                        testData[0][1]="NORMAL";
                        break;


                }
            }
        });
        responsetime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.quickly:
                        testData[0][2]="QUICKLY";
                        break;
                    case R.id.late:
                        testData[0][2]="LATE";
                        break;



                }
            }
        });
        happy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.yes:
                        testData[0][3]="YES";
                        break;
                    case R.id.no:
                        testData[0][3]="NO";
                        break;



                }
            }
        });

    }
}
