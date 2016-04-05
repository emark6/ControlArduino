package vgun.controlarduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Экземпляры классов наших кнопок
    Button forward,left,right,back,connect;

    OutputStream outStream;
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket;

    //Эта функция запускается автоматически при запуске приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //"Соединям" вид кнопки в окне приложения с реализацией
        forward = (Button) findViewById(R.id.fwd_btn);
        left = (Button) findViewById(R.id.lft_btn);
        right = (Button) findViewById(R.id.rgt_btn);
        back = (Button) findViewById(R.id.bck_btn);
        //Добавлем "слушатель нажатий" к кнопке
        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        outStream.write('W');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            outStream.write('T');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

                return false;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        outStream.write('A');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        outStream.write('T');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        outStream.write('D');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        outStream.write('T');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        outStream.write('S');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        outStream.write('T');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });


        //Включаем bluetooth. Если он уже включен, то ничего не произойдет
        connect();
        //Выводим сообщение об успешном подключении

        try {
            //Получаем выходной поток для передачи данных
            outStream = clientSocket.getOutputStream();
            //Пишем данные в выходной поток
            outStream.write('T');

        } catch (IOException e) {
            //Если есть ошибки, выводим их в лог
            Log.d("BLUETOOTH", e.getMessage());
        }
    }

    void connect() {
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);

        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        //Пытаемся проделать эти действия
        try{
            //Устройство с данным адресом - наш Bluetooth Bee
            //Адрес опредеяется следующим образом: установите соединение
            //между ПК и модулем (пин: 1234), а затем посмотрите в настройках
            //соединения адрес модуля. Скорее всего он будет аналогичным.
            BluetoothDevice device = bluetooth.getRemoteDevice("20:15:12:29:61:42");

            //Инициируем соединение с устройством
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[] {int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();

            //В случае появления любых ошибок, выводим в лог сообщение
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();
    }

    //Как раз эта функция и будет вызываться

    @Override
    public void onClick(View v) {

        //Пытаемся послать данные

    }


}
