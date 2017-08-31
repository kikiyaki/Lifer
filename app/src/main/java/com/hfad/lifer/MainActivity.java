package com.hfad.lifer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    int cash = 2222;
    int health = 50;
    int respect = 50;
    int danger = 50;
    double old = 18;

    int lplusCash;
    int lplusHealth;
    int lplusRespect;
    int lplusDanger;

    int rplusCash;
    int rplusHealth;
    int rplusRespect;
    int rplusDanger;

    String group;
    int record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setCard();

        // Выставление шрифта
        Button rb = (Button) findViewById(R.id.RightButton);
        rb.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        Button lb = (Button) findViewById(R.id.LeftButton);
        lb.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t1 = (TextView) findViewById(R.id.cash);
        t1.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t2 = (TextView) findViewById(R.id.health);
        t2.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t3 = (TextView) findViewById(R.id.respect);
        t3.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t4 = (TextView) findViewById(R.id.danger);
        t4.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t5 = (TextView) findViewById(R.id.ActivityText);
        t5.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
        TextView t6 = (TextView) findViewById(R.id.TextOld);
        t6.setTypeface(Typeface.createFromAsset(getAssets(), "Bulgaria.ttf"));
    }

    public void setParam(int Param, TextView View) {
        String StringParam = String.valueOf(Param + "%");
        View.setText(StringParam);
    }

    public void setView() {
        if (cash < 0) {
            cash = 0;
        }

        health = alignParam(health);
        respect = alignParam(respect);
        danger = alignParam(danger);


        TextView CashView = (TextView) findViewById(R.id.cash);
        TextView HealthView = (TextView) findViewById(R.id.health);
        TextView RespectView = (TextView) findViewById(R.id.respect);
        TextView DangerView = (TextView) findViewById(R.id.danger);
        TextView OldView = (TextView) findViewById(R.id.TextOld);

        String CashText = String.valueOf(cash);
        CashView.setText(CashText);
        setParam(health, HealthView);
        setParam(respect, RespectView);
        setParam(danger, DangerView);
        String StringOld = String.valueOf((int) old) + " Лет";
        OldView.setText(StringOld);
    }

    public void setCard() {

        // Выставление номера группы в формате ХХХХ
        // для выбора карточек соответствующих четырем параметрам
        if (cash <10000) {
            group = "1";
        } else {
            if (cash <100000) {
                group = "2";
            } else {
                group = "3";
            }
        }
        if (health <50) {
            group+="1";
        } else {
            group+="2";
        }
        if (respect <50) {
            group+="1";
        } else {
            group+="2";
        }
        if (danger <50) {
            group+="1";
        } else {
            group+="2";
        }

        // Создание номера группы при каком-либо параметре,
        // соответствующем концу игры
        if (cash == 0) {
            group = "0111";
        }
        if (health == 0) {
            group = "1011";
        }
        if (respect == 0) {
            group = "1101";
        }
        if (danger == 0) {
            group = "1110";
        }


        // Здесь группе присваивается особый номер, который будет использоваться
        // только для показа первых карточек с правилами игры
        if (cash == 2222) {
            group = "1";
        }
        if (cash == 2223) {
            group = "2";
        }
        if (cash == 2224) {
            group = "3";
        }


        SQLiteOpenHelper sqLiteOpenHelper = new DBHelper(this);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("CARDS",
                new String[]{"TEXT", "IMAGE_RESOURCE_ID", "LEFT_BTN_TXT", "RIGHT_BTN_TXT",
                        "LEFT_CASH", "LEFT_HEALTH", "LEFT_RESPECT", "LEFT_DANGER",
                        "RIGHT_CASH", "RIGHT_HEALTH", "RIGHT_RESPECT", "RIGHT_DANGER"},
                "NUMBER = ?", new String[]{group + (String.valueOf((int) (Math.random() * 2) + 1))},
                null, null, null);

        if (cursor.moveToFirst()) {
            String activityText = cursor.getString(0);
            int activityImageId = cursor.getInt(1);
            String leftBtnText = cursor.getString(2);
            String rightBtnText = cursor.getString(3);
            lplusCash = cursor.getInt(4);
            lplusHealth = cursor.getInt(5);
            lplusRespect = cursor.getInt(6);
            lplusDanger = cursor.getInt(7);

            rplusCash = cursor.getInt(8);
            rplusHealth = cursor.getInt(9);
            rplusRespect = cursor.getInt(10);
            rplusDanger = cursor.getInt(11);


            TextView activityTextView = (TextView) findViewById(R.id.ActivityText);
            if (cash==0|health==0|respect==0|danger==0) {
                Cursor cursor1 = db.query("CARDS", new String[] {"RIGHT_DANGER"}, "NUMBER = ?",
                        new String[] {"0000"}, null, null, null);

                if (cursor1.moveToFirst()) {
                    record = cursor1.getInt(0);
                }
                if (record < old) {
                    activityTextView.setText(activityText + "\nНовый рекорд " + (int) old + " Лет");
                    ContentValues cardValues = new ContentValues();
                    cardValues.put("RIGHT_DANGER", old);
                    db.update("CARDS",
                            cardValues,
                            "NUMBER = ?",
                            new String[] {"0000"});
                } else {
                    activityTextView.setText(activityText + "\nВам было " + (int) old + " Лет");
                }

                cash = 2000;
                health = 50;
                respect = 50;
                danger = 50;
                old = 18;

            } else {
                activityTextView.setText(activityText);
            }

            ImageView activityImageView = (ImageView) findViewById(R.id.ActivityImage);
            activityImageView.setImageResource(activityImageId);
            Button leftBtn = (Button) findViewById(R.id.LeftButton);
            leftBtn.setText(leftBtnText);
            Button rightBtn = (Button) findViewById(R.id.RightButton);
            rightBtn.setText(rightBtnText);

        }
    }

    public void onLeftBtnClicked(View view) {
        cash += lplusCash;
        health += lplusHealth;
        respect += lplusRespect;
        danger += lplusDanger;
        old += 0.5;
        setView();
        setCard();
    }

    public void onRightBtnClicked(View view) {
        cash += rplusCash;
        health += rplusHealth;
        respect += rplusRespect;
        danger += rplusDanger;
        old += 0.5;
        setView();
        setCard();
    }

    public int alignParam(int Param) {
        if (Param > 100) {
            Param = 100;
        }

        if (Param < 0) {
            Param = 0;
        }
        return Param;
    }

}