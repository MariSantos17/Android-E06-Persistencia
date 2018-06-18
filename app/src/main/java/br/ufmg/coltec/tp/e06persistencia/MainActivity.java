package br.ufmg.coltec.tp.e06persistencia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static int FOTO_CODE = 1;
    private static final String filename= "foto.png";
    private static final String APP_PREF_ID = "PrefID1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = this.getSharedPreferences(APP_PREF_ID, 0);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        String hour = format.format(new Date());

        String hourAnt = pref.getString("ultimo_acesso", "none");
        if(!hourAnt.equals("none")) Toast.makeText(this,"Ultimo acesso: " + hourAnt,Toast.LENGTH_SHORT).show();
        else Log.i("main", "Registro: primeiro registro de acesso");

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ultimo_acesso", hour);
        editor.commit();


        Button tirarFoto = findViewById(R.id.foto);
        Button salvarFoto = findViewById(R.id.salvar);
        final ImageView foto = findViewById(R.id.img_foto);


        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fotointent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(fotointent, FOTO_CODE);
            }
        });

        final File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM), filename);

        salvarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm=((BitmapDrawable)foto.getDrawable()).getBitmap();



                FileOutputStream out = null;
                try {

                    out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getBaseContext(),"Foto salva", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = findViewById(R.id.img_foto);
        if (requestCode == FOTO_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}
