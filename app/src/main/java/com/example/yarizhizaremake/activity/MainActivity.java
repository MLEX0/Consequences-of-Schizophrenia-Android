package com.example.yarizhizaremake.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yarizhizaremake.R;
import com.example.yarizhizaremake.model.LoreClass;
import com.example.yarizhizaremake.temp.TempData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Массив с лором
    private ArrayList<LoreClass> LoreList;
    //Layout, на котром меняем background
    private ConstraintLayout MainLayout;

    //Отстальные лаяуты
    private ConstraintLayout NavigateLayout;
    private ConstraintLayout SceneLayout;
    private ConstraintLayout MenuLayout;

    private ConstraintLayout NextNavLayout;

    //Кнопки включения/выключения звуков
    private ImageView soundOff;
    private ImageView soundOn;

    //Кнопка финала
    private Button fin;

    //Лайаут финала
    private ConstraintLayout TheEnd;

    //Поток, управляющий побуквенным выводом
    private Thread threadTextOut;

    //Текстовые поля
    private TextView textDialog;
    private TextView textPersonage;

    //Картинки персонажей
    private ImageView John;
    private ImageView Iuda;
    private ImageView Eric;
    private ImageView Chase;
    private ImageView Biden;
    private ImageView Kaera;
    private ImageView Koen;
    private ImageView Pushnoy;
    private ImageView Sinon;
    private ImageView Stugach;
    private ImageView Terua;
    private ImageView Zorgan;

    //MediaPlayer
    private MediaPlayer mp;

    //Переменная, отслеживающая состояние пропущенного диалога
    private boolean skipThisDialog = false;

    //Счётчик слайдов
    private int PlayerPage;

    //Счётчик игры
    private int counterAuthorClick;

    //Поля для концовок
    private int endingCount;
    private int dayCounter;
    private int endingChoise;

    //Лаяауты для концовок
    private ConstraintLayout WakeUpChoiseLayout;
    private ConstraintLayout EndingChoiseLayout;
    private boolean CanPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        if(TempData.isMusicOn){startPlayMusic();}
        if((!mp.isPlaying()) && TempData.isMusicOn){startPlayMusic();}
    }

    private void Init() {

        PlayerPage = 0;
        counterAuthorClick = 0;
        LoreList = new ArrayList<LoreClass>();

        //Заполнение массива данными из текстового файла
        InputStream is = this.getResources().openRawResource(R.raw.lore);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        String[] words;
        try {
            while ((readLine = br.readLine()) != null) {
                words = readLine.split("@");
                LoreList.add(new LoreClass(words[0],Integer.parseInt(words[1]), words[2], words[3]){
                });
            }

            is.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Инициализация Layout-ов, т.к вся игра в одном окне
        MainLayout = findViewById(R.id.BackGround);
        NavigateLayout = findViewById(R.id.ConstraintLayoutNavigate);
        SceneLayout = findViewById(R.id.ConstraintLayoutScene);
        MenuLayout = findViewById(R.id.ConstraintLayoutMainMenu);
        TheEnd = findViewById(R.id.ConstraintLayoutEnding);


        //Текстовое поле персонажа
        textPersonage = findViewById(R.id.textViewPersonageName);

        //Кнопка конца игры
        fin = findViewById(R.id.ButtonEnd);

        //Картинки персонажей

        John = findViewById(R.id.imageViewJohn);
        Iuda = findViewById(R.id.imageViewIuda);
        Eric = findViewById(R.id.imageViewEric);
        Chase = findViewById(R.id.imageViewChase);
        Biden = findViewById(R.id.imageViewBiden);
        Kaera = findViewById(R.id.imageViewKaera);
        Koen = findViewById(R.id.imageViewKoen);
        Pushnoy = findViewById(R.id.imageViewPushnoy);
        Sinon = findViewById(R.id.imageViewSinon);
        Stugach = findViewById(R.id.imageViewStugach);
        Terua = findViewById(R.id.imageViewTerua);
        Zorgan = findViewById(R.id.imageViewZorgan);


        //Переменная, которая не даёт уйти на другой слайд, во время диалога
        TempData.CanNext = true;

        TempData.CanSkip = true;

        CanPress = true;

        //Кнопки включения/выключения звука
        soundOff = findViewById(R.id.imageViewSoundOff);
        soundOn = findViewById(R.id.imageViewSoundOn);

        //Поля для концовок
        endingCount = 0;
        dayCounter = 0;
        endingChoise = 0;

        //Лаяауты для концовок
        WakeUpChoiseLayout = findViewById(R.id.ConstraintLayoutWakeChoise);
        EndingChoiseLayout = findViewById(R.id.ConstraintLayoutEndingChoise);
    }

    //Главный метод игры, двигает её вперёд и завершает
    private void Next(){

        //Toast.makeText(this, String.valueOf(PlayerPage), Toast.LENGTH_SHORT).show();
        if((!mp.isPlaying()) && TempData.isMusicOn){mp.start();}


        if(TempData.CanSkip)
        {
            skipThisDialog = true;
        }

        fin.setVisibility(View.GONE);

        if(TempData.CanNext) {

            TempData.CanNext = false;
            skipThisDialog = false;


            if (LoreList.get(PlayerPage).PageNumber == 317) {

                MenuLayout.setVisibility(View.VISIBLE);
                SceneLayout.setVisibility(View.GONE);
                NavigateLayout.setVisibility(View.GONE);
                TheEnd.setVisibility(View.GONE);
                fin.setVisibility(View.GONE);
            } else{

                PlayerPage++;
                //Toast.makeText(this, String.valueOf(PlayerPage), Toast.LENGTH_SHORT).show();
                if (LoreList.get(PlayerPage).PageNumber == 301 && endingChoise == 0)
                {
                    fin.setVisibility(View.VISIBLE);
                    TheEnd.setVisibility(View.VISIBLE);
                    NavigateLayout.setVisibility(View.GONE);
                    TempData.CanNext = false;

                }
                if (LoreList.get(PlayerPage).PageNumber == 307 && endingChoise == 1)
                {
                    fin.setVisibility(View.VISIBLE);
                    TheEnd.setVisibility(View.VISIBLE);
                    NavigateLayout.setVisibility(View.GONE);
                    TempData.CanNext = false;
                }

                if (LoreList.get(PlayerPage).PageNumber == 307 && endingChoise == 2)
                {
                    fin.setVisibility(View.VISIBLE);
                    TheEnd.setVisibility(View.VISIBLE);
                    NavigateLayout.setVisibility(View.GONE);
                    TempData.CanNext = false;
                }

                if (LoreList.get(PlayerPage).PageNumber == 8)
                {
                    NavigateLayout.setVisibility(View.GONE);
                    WakeUpChoiseLayout.setVisibility(View.VISIBLE);
                    skipThisDialog = true;
                }
                else if (LoreList.get(PlayerPage).PageNumber == 144 || (dayCounter < 6 && dayCounter != 0))
                {
                    NavigateLayout.setVisibility(View.GONE);
                    EndingChoiseLayout.setVisibility(View.VISIBLE);
                    TempData.CanSkip = false;
                }
                else if(LoreList.get(PlayerPage).PageNumber == 316)
                {
                    NavigateLayout.setVisibility(View.GONE);
                }
                else if(LoreList.get(PlayerPage).PageNumber == 317)
                {
                    NavigateLayout.setVisibility(View.GONE);
                }
                else
                {
                    NavigateLayout.setVisibility(View.VISIBLE);
                }
                if (LoreList.get(PlayerPage).PageNumber == 282)
                {
                    if (endingChoise == 0)
                    {
                        PlayerPage = 282;
                    }
                    if (endingChoise == 1)
                    {
                        PlayerPage = 301;
                    }
                    if (endingChoise == 2)
                    {
                        PlayerPage = 308;
                    }
                }

                if(PlayerPage > 0){
                    WriteSave(String.valueOf(PlayerPage)+"@"+String.valueOf(endingChoise));
                }
                SelectBackground();
                SelectPersonage();

                if(!LoreList.get(PlayerPage).TextDialog.equals("NOTEXT")){
                    SceneLayout.setVisibility(View.VISIBLE);
                    setTextDialog();

                }
                else{
                    //SceneLayout.setVisibility(View.GONE);
                    setTextDialog();
                    TempData.CanNext = true;
                }

            }
        }
    }

    //Метод для выбора фоновой картинки
    private void SelectBackground(){
        switch(LoreList.get(PlayerPage).getPagePath()){
            case "Comix1":
                MainLayout.setBackground(getDrawable(R.drawable.comix1));
                break;
            case "Comix2":
                MainLayout.setBackground(getDrawable(R.drawable.comix2));
                break;

            case "Comix3":
                MainLayout.setBackground(getDrawable(R.drawable.comix3));
                break;

            case "Comix4":
                MainLayout.setBackground(getDrawable(R.drawable.comix4));
                break;

            case "Logo":
                MainLayout.setBackground(getDrawable(R.drawable.logo));
                break;

            case "1":
                MainLayout.setBackground(getDrawable(R.drawable.mainhome));
                break;

            case "2C":
                MainLayout.setBackground(getDrawable(R.drawable.street));
                break;

            case "3":
                MainLayout.setBackground(getDrawable(R.drawable.praktika));
                break;

            case "4":
                MainLayout.setBackground(getDrawable(R.drawable.praktika));
                break;

            case "5":
                MainLayout.setBackground(getDrawable(R.drawable.doprosnaya));
                break;

            case "6":
                MainLayout.setBackground(getDrawable(R.drawable.base));
                break;

            case "7":
                MainLayout.setBackground(getDrawable(R.drawable.pc));
                break;

            case "8":
                MainLayout.setBackground(getDrawable(R.drawable.black));
                break;

            case "9":
                MainLayout.setBackground(getDrawable(R.drawable.black));
                break;

            case "10":
                MainLayout.setBackground(getDrawable(R.drawable.mountains));
                break;

            case "11":
                MainLayout.setBackground(getDrawable(R.drawable.mountains));
                break;

            case "12":
                MainLayout.setBackground(getDrawable(R.drawable.mountains));
                break;

            case "13":
                MainLayout.setBackground(getDrawable(R.drawable.ship_dark));
                break;

            case "14":
                MainLayout.setBackground(getDrawable(R.drawable.ship2));
                break;

            case "15":
                MainLayout.setBackground(getDrawable(R.drawable.ship3));
                break;

            case "16":
                MainLayout.setBackground(getDrawable(R.drawable.ship3));
                break;

            case "17":
                MainLayout.setBackground(getDrawable(R.drawable.ship3));
                break;

            case "18":
                MainLayout.setBackground(getDrawable(R.drawable.field));
                break;

            case "19":
                MainLayout.setBackground(getDrawable(R.drawable.mainhome));
                break;

            case "20":
                MainLayout.setBackground(getDrawable(R.drawable.black));
                break;

            default:
                break;

        }
    }

    //Метод для выбора картинки и тектса персонажа, запуска его анимации
    private void SelectPersonage(){

        //Анимация персонажей
        TranslateAnimation animationLeft = new TranslateAnimation(-400, 0, 50, 50);
        animationLeft.setDuration(200);
        animationLeft.setFillAfter(true);

        //Анимация Вовы
        TranslateAnimation animationRight = new TranslateAnimation(400, 0, 50, 50);
        animationRight.setDuration(200);
        animationRight.setFillAfter(true);


        switch (LoreList.get(PlayerPage).PersonageName){
            case "Джон":
                textPersonage.setText("Джон");

                John.setVisibility(View.VISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.startAnimation(animationRight);
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();



                break;

            case "Эрик":
                textPersonage.setText("Эрик");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric.setVisibility(View.VISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric.startAnimation(animationLeft);
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Чейз":
                textPersonage.setText("Чейз");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.VISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.startAnimation(animationRight);
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Джон и Иуда Гай":
                textPersonage.setText("Джон и Иуда Гай");

                John.setVisibility(View.VISIBLE);
                Iuda.setVisibility(View.VISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.startAnimation(animationRight);
                Iuda.startAnimation(animationLeft);
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Каэра Суханова":
                textPersonage.setText("Каэра Суханова");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric.setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.VISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.startAnimation(animationLeft);
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Доктор Коэн":
                textPersonage.setText("Доктор Коэн");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.startAnimation(animationLeft);
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Пушной":
                textPersonage.setText("Пушной");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.VISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.startAnimation(animationRight);
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Голос с неба":
                textPersonage.setText("Голос с неба");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;

            case "Теруя":
                textPersonage.setText("Теруя");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.VISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.startAnimation(animationLeft);
                Zorgan.clearAnimation();

                break;



            case "Синон":
                textPersonage.setText("Синон");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.VISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.startAnimation(animationRight);
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;


            case "Джон и Пушной":
                textPersonage.setText("Джон и Пушной");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;


            case "Зорган":
                textPersonage.setText("Джон");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.VISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.startAnimation(animationRight);

                break;

            case "Стугач":
                textPersonage.setText("Стугач");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.VISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.startAnimation(animationRight);
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;





            case "Иуда Гай":
                textPersonage.setText("Иуда Гай");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.VISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.startAnimation(animationLeft);
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();



                break;

            default:

                textPersonage.setText("...");

                John.setVisibility(View.INVISIBLE);
                Iuda.setVisibility(View.INVISIBLE);
                Eric .setVisibility(View.INVISIBLE);
                Chase.setVisibility(View.INVISIBLE);
                Biden.setVisibility(View.INVISIBLE);
                Kaera.setVisibility(View.INVISIBLE);
                Koen.setVisibility(View.INVISIBLE);
                Pushnoy.setVisibility(View.INVISIBLE);
                Sinon.setVisibility(View.INVISIBLE);
                Stugach.setVisibility(View.INVISIBLE);
                Terua.setVisibility(View.INVISIBLE);
                Zorgan.setVisibility(View.INVISIBLE);


                John.clearAnimation();
                Iuda.clearAnimation();
                Eric .clearAnimation();
                Chase.clearAnimation();
                Biden.clearAnimation();
                Kaera.clearAnimation();
                Koen.clearAnimation();
                Pushnoy.clearAnimation();
                Sinon.clearAnimation();
                Stugach.clearAnimation();
                Terua.clearAnimation();
                Zorgan.clearAnimation();

                break;
        }

    }


    //Метод продолжеия игры по клику
    public void ResumeGameOnClick(View view){

        MenuLayout.setVisibility(View.GONE);
        SceneLayout.setVisibility(View.VISIBLE);
        NavigateLayout.setVisibility(View.VISIBLE);
        MenuLayout.setVisibility(View.GONE);
        Init();
        //Загрузка сохранения
        String save = ReadSave();
        if(!save.equals("")){

            String[] words = save.split("@");
            PlayerPage = Integer.parseInt(words[0]) - 1;
            endingChoise = Integer.parseInt(words[1]);
        }
        Next();
    }

    //Метод начала новой игры по клику
    public void NewGameOnClick(View view){

        String title = "Начать новую игру?";
        String message = "Если вы начнёте новую игру, " +
                "то все ваши предыдущие сохранения будут удалены";
        String button1String = "Да";
        String button2String = "Нет";

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MenuLayout.setVisibility(View.GONE);
                SceneLayout.setVisibility(View.VISIBLE);
                NavigateLayout.setVisibility(View.VISIBLE);
                MenuLayout.setVisibility(View.GONE);
                Init();
                //Очистка сохранения
                WriteSave(String.valueOf(1)+"@0");
                Next();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        builder.show();

    }

    //Метод сохранения номера страницы в файл
    public void WriteSave(String text) {

        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = openFileOutput("saves.txt", MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Метод чтения номера страницы из файла
    public String ReadSave() {
        FileInputStream fileInputStream = null;
        String text = "";
        try {
            fileInputStream = openFileInput("saves.txt");
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            text = new String(bytes);
            return text;

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    public void NextOnClick(View view)
    {
        Next();
    }

    //Метод для побуквенного воспроизведения текста
    private void setTextDialog(){
        threadTextOut = new Thread() {
            public void run() {
                try {

                    textDialog = findViewById(R.id.textViewTextDialog);
                    //textDialog.setMovementMethod(new ScrollingMovementMethod());
                    String text = LoreList.get(PlayerPage).TextDialog;
                    if(!text.equals("NOTEXT")){
                        char[] trimmedText = text.toCharArray();
                        String outText = "";
                        for(int i = 0; i < trimmedText.length; i++){
                            if(!skipThisDialog){
                                outText += trimmedText[i];
                                textDialog.setText(outText);
                                TimeUnit.MILLISECONDS.sleep(50);
                            }
                            else{
                                textDialog.setText(text);
                                TempData.CanNext = true;
                                break;
                            }
                        }
                        CanPress = true;
                        TempData.CanNext = true;
                    }
                    else
                    {
                        textDialog.setText("");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };threadTextOut.start();
    }



    private void startPlayMusic(){
        mp = MediaPlayer.create(this, R.raw.background_music);
        if(TempData.isMusicOn){
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.stop();
                }
            });
        }
    }

    //При закрытии приложения музыка останавливается
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()){stopPlayMusic();}
    }

    //Метод остановки музыки
    private void stopPlayMusic() {
        mp.stop();
        try {
            mp.prepare();
            mp.seekTo(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Методы, для того, чтобы музыка останавливалась при паузе и запускалась при входе
    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.pause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if((!mp.isPlaying()) && TempData.isMusicOn){mp.start();}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if((!mp.isPlaying()) && TempData.isMusicOn){mp.start();}
    }

    //Отключение звука в настройке
    public void SoundsOff(View view){

        TempData.isMusicOn = false;
        mp.pause();
        soundOff.setVisibility(View.GONE);
        soundOn.setVisibility(View.VISIBLE);
    }

    //Включение звука в настройке
    public void SoundsOn(View view){
        TempData.isMusicOn = true;
        mp.start();
        soundOff.setVisibility(View.VISIBLE);
        soundOn.setVisibility(View.GONE);
    }

    //Обработчики событий кнопок концовок
    public void WakeUpChoise(View view)
    {
        Next();
        WakeUpChoiseLayout.setVisibility(View.GONE);
        NavigateLayout.setVisibility(View.VISIBLE);
    }

    public void NotWakeUpChoise(View view)
    {
        endingCount++;
        if(endingCount > 5)
        {
            String title = "Конец!";
            String message = "Вы не проснулись, это был правильный выбор, до новых встреч!";
            String button1String = "Да";

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);  // заголовок
            builder.setMessage(message); // сообщение
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    PlayerPage = 315;
                    Next();
                    WakeUpChoiseLayout.setVisibility(View.GONE);
                }
            });
            builder.show();
        }
    }

    public void ChoiseLearning(View view)
    {
            if(CanPress)
            {
            if(dayCounter >= 6)
            {
                CanPress = false;
                EndingChoiseLayout.setVisibility(View.GONE);
                Next();
            }
            if(dayCounter == 2)
            {
                CanPress = false;
                dayCounter++;
                endingChoise = 0;
                Next();
            }
            else
            {
                CanPress = false;
                dayCounter++;
                Next();
            }
        }
    }

    public void ChoiseDvach(View view)
    {
        if(CanPress)
        {
            if(dayCounter >= 6)
            {
                CanPress = false;
                TempData.CanSkip = true;
                EndingChoiseLayout.setVisibility(View.GONE);
                Next();
            }
            if(dayCounter == 2)
            {
                CanPress = false;
                dayCounter++;
                endingChoise = 1;
                Next();
            }
            else
            {
                CanPress = false;
                dayCounter++;
                Next();
            }
        }
    }

    public void ChoiseSites(View view)
    {
        if(CanPress)
        {
            if(dayCounter == 6)
            {
                CanPress = false;
                TempData.CanSkip = true;
                EndingChoiseLayout.setVisibility(View.GONE);

                Next();
            }
            if(dayCounter == 2)
            {
                CanPress = false;
                dayCounter++;
                endingChoise = 3;
                Next();
            }
            else
            {
                CanPress = false;
                dayCounter++;
                Next();
            }
        }
    }

    public void FinishOnClick(View view)
    {
        MenuLayout.setVisibility(View.VISIBLE);
        SceneLayout.setVisibility(View.GONE);
        NavigateLayout.setVisibility(View.GONE);
        TheEnd.setVisibility(View.GONE);
        fin.setVisibility(View.GONE);
    }

}