package com.example.rohan.onlineexam;

import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
DatabaseHandler dh=new DatabaseHandler(this);
    TextView textView,qNo,question;
    EditText ip,port,name,branch,roll;
    private static int qNoRowCount,marks = 0,portNumber;
    private static String nam,rollNo,branchName,sex,ipAddress;
    RadioButton option1,option2,option3,option4;
    RadioGroup group,genderGroup;
    String[][] ans;
    CountDownTimer ct;
    TextView timer,markss,ansPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        textView=(TextView)findViewById(R.id.question);
        ip=(EditText)findViewById(R.id.Ip);
        port=(EditText)findViewById(R.id.Port);
        name=(EditText)findViewById(R.id.Name);
        branch=(EditText)findViewById(R.id.Branch);
        roll=(EditText)findViewById(R.id.Roll_No);
        genderGroup=(RadioGroup)findViewById(R.id.genderGroup);


    }

    public void enter(View v)
    {
        ct.cancel();
        moveToResults();
    }


    public void enterOld(View v)
    {
        try{
            Cursor res = dh.getAllData();
            if(res.getCount() == 0) {
                // show message
                showMessage("Error","Nothing found");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("id:"+ res.getString(0)+"\n");
                buffer.append("question:"+ res.getString(1)+"\n");
                buffer.append("Option 1 :"+ res.getString(2)+"\n");
                buffer.append("Option 2 :"+ res.getString(3)+"\n");
                buffer.append("Option 3 :"+ res.getString(4)+"\n");
                buffer.append("Option 4 :"+ res.getString(5)+"\n");
                buffer.append("Answer :"+ res.getString(6)+"\n\n");
            }

            // Show all data
            showMessage("Data",buffer.toString());


        }catch(Exception e ){
            e.printStackTrace();
        }
    }

    public void next(View v) {

            try {
                int id = Integer.parseInt(qNo.getText().toString());
                if(id<qNoRowCount)
                qNo.setText(String.valueOf(id + 1));
            } catch (Exception e) {
                question.setText(e.getMessage());
                e.printStackTrace();
            }

    }

    public void delete(View v)
    {try{
        Cursor r=dh.getAllData();
        while(r.moveToNext()){
            dh.deleteData(r.getString(0));}
        Toast.makeText(getApplicationContext(),"Zala Delete",Toast.LENGTH_SHORT).show();
    }catch(Exception e){
     e.printStackTrace();
    }}
    public void move(View v)
    {
        nam=name.getText().toString();
        rollNo=roll.getText().toString();
        branchName=branch.getText().toString();
        int idli=genderGroup.getCheckedRadioButtonId();
        switch (idli){
            case R.id.Female:
                sex="Female";
                break;
            default:
                sex="Male";
        }
        ipAddress=ip.getText().toString();
        portNumber=Integer.parseInt(port.getText().toString());
        setContentView(R.layout.activity_main);
        connection();
        qNoRowCount=dh.getAllData().getCount();
        mainActivityDeclarations();
        setup();
        timer=(TextView)findViewById(R.id.chronos);
        ct=new CountDownTimer(300000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                Integer seconds=(int)millisUntilFinished/1000;
                Integer min=seconds/60;
                Integer sec=seconds%60;

                timer.setText(min.toString()+" Minutes "+sec.toString()+" Seconds left...");

            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"time up...",Toast.LENGTH_SHORT).show();
                moveToResults();
            }
        };
        ct.start();
    }

    private void moveToResults() {
        setContentView(R.layout.answers);
        ansPage=(TextView)findViewById(R.id.result);
        markss=(TextView)findViewById(R.id.head);
        String pasted=computeMarks();
        ansPage.setText(pasted);
        final ArrayList back=new ArrayList();
        back.add(nam);
        back.add(rollNo);
        back.add(branchName);
        back.add(sex);
        back.add(ans);
        back.add(marks);
        back.add(qNoRowCount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket sock=new Socket(ipAddress,portNumber);
                    new ObjectInputStream(sock.getInputStream()).readObject();
                    new ObjectOutputStream(sock.getOutputStream()).writeObject(back);
                    sock.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    ansPage.setText(e.toString());
                }
            }
        }).start();

    }

    private String computeMarks() {

        String pasted = "";
        try {
            for (int i = 0; i < qNoRowCount; i++) {
                ansPage.setText("looped");
                String question = "";

                Cursor rs = dh.getQuestion(i + 1);
                while (rs.moveToNext()) {
                    question = rs.getString(1);
                }
                if (ans[0][i].equals(ans[1][i]))
                    marks++;
                pasted += "Q" + (i + 1) + ": " + question + "\n\n Actual Answer: " + ans[1][i] + "\n\n Your Answer: " + ans[0][i]+ "\n\n\n\n";
            }
            markss.setText(getString(R.string.yourMarks) + marks);
        }
        catch(Exception e){
            setContentView(R.layout.activity_main);
            question.setText(e.getLocalizedMessage());
        }
        return pasted;
    }

    private void setup() {
        Cursor res=dh.getQuestion(Integer.parseInt(qNo.getText().toString()));
        while(res.moveToNext()){
            question.setText(res.getString(1));
            option1.setText(res.getString(2));
            option2.setText(res.getString(3));
            option3.setText(res.getString(4));
            option4.setText(res.getString(5));
        }
    }

    private void mainActivityDeclarations() {

        qNo=(TextView)findViewById(R.id.questionNo);
        question=(TextView)findViewById(R.id.question);
        option1=(RadioButton)findViewById(R.id.option1);
        option2=(RadioButton)findViewById(R.id.option2);
        option3=(RadioButton)findViewById(R.id.option3);
        option4=(RadioButton)findViewById(R.id.option4);
        group=(RadioGroup)findViewById(R.id.radioGroup);
        RelativeLayout ll=(RelativeLayout) findViewById(R.id.layout);
        assert ll != null;
        ll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeRight() {
                try {
                    int id = Integer.parseInt(qNo.getText().toString());
                    if(id>1)
                        qNo.setText(String.valueOf(id - 1));
                } catch (Exception e) {
                    question.setText(e.getMessage());
                    e.printStackTrace();
                }
            }

            public void onSwipeLeft() {
                try {
                    int id = Integer.parseInt(qNo.getText().toString());
                    if(id<qNoRowCount)
                        qNo.setText(String.valueOf(id + 1));
                } catch (Exception e) {
                    question.setText(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        final SeekBar seek=(SeekBar)findViewById(R.id.seekBar);
        assert seek != null;
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Integer temp=((int)progress*(qNoRowCount/100))+1;
                if(temp>qNoRowCount)
                    qNo.setText(String.valueOf(qNoRowCount));
                else
                    qNo.setText(temp.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        qNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String c=ans[0][Integer.parseInt(String.valueOf(s))-1];
                int id=group.getCheckedRadioButtonId();
                switch(id)
                {
                    case R.id.option1:
                        c=option1.getText().toString();
                        break;
                    case R.id.option2:
                        c=option2.getText().toString();
                        break;
                    case R.id.option3:
                        c=option3.getText().toString();
                        break;
                    case R.id.option4:
                        c=option4.getText().toString();
                        break;
                    default:
                       if(ans[0][Integer.parseInt(String.valueOf(s))-1].equals("Not Attempted"))
                        c="Not Attempted";
                }
                ans[0][Integer.parseInt(String.valueOf(s))-1]=c;
                group.clearCheck();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Cursor res=dh.getQuestion(Integer.parseInt(String.valueOf(s)));
                while(res.moveToNext()){
                    question.setText(res.getString(1));
                    option1.setText(res.getString(2));
                    option2.setText(res.getString(3));
                    option3.setText(res.getString(4));
                    option4.setText(res.getString(5));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void connection() {
            System.out.println("in connection");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket client = new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));
                        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                        ArrayList<String[]> al = (ArrayList<String[]>) ois.readObject();
                        new ObjectOutputStream(client.getOutputStream()).writeObject(null);
                        client.close();
                        qNoRowCount = al.size();
                        ans = new String[2][qNoRowCount];
                        for (int i = 0; i < qNoRowCount; i++) {
                            ans[0][i] = "Not Attempted";
                            Cursor res = dh.getQuestion(i + 1);
                            while (res.moveToNext()) {
                                ans[1][i] = res.getString(6);
                            }
                        }
                        dh.onUpgrade(dh.getDatabase(), 1, 1);
                        for (String[] i : al)
                            dh.insertData(i[1], i[2], i[3], i[4], i[5], i[6]);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
    }


    private void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void prev(View v){
        try {
            int id = Integer.parseInt(qNo.getText().toString());
            if(id>1)
                qNo.setText(String.valueOf(id - 1));
        } catch (Exception e) {
            question.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    public void ans(View v){
        String temp="";
        for(int i=0;i<qNoRowCount;i++)
        {
            temp+=ans[0][i]+"\n";
        }
        showMessage("Answers",temp);
    }
}
