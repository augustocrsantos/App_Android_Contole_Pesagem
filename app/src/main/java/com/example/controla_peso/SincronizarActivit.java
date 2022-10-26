package com.example.controla_peso;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SincronizarActivit extends AppCompatActivity {
    //private FirebaseFirestore db;
    private Button btEnviar, btImportar;
    private static final String FILE_NAME = "example.txt";
    private EditText mEditText;
    private String state;
    private List<EntidadeCadastroBovino> buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizar);
try {
    String state = Environment.getExternalStorageState();
    if (state.equals(Environment.MEDIA_MOUNTED)) {
        File docs = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "YourAppDirectory");
// Make the directory if it does not yet exist
        docs.getParentFile().mkdirs();
    }
    if (state.equals(Environment.MEDIA_MOUNTED) ||
            state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

        File docs = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "YourAppDirectory");
// Make the directory if it does not yet exist
        docs.getParentFile().mkdirs();
    }
}catch (Exception erro){
    Log.e("erro", String.valueOf(erro));
}

        mEditText = (EditText)findViewById(R.id.editTextTextMultiLine);

        btImportar = (Button)findViewById(R.id.button_importar);
        btImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });

        btEnviar = (Button)findViewById(R.id.button_exportar);
        btEnviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try{

                    //salvar();
                    PesquisaLista();
                }catch (Exception e){
                    Log.e("erro", String.valueOf(e));
                }

            }
        });
    }

    private void PesquisaLista(){
 daoCadastroBovino dao = new daoCadastroBovino(this);

    }

   public void gravar(){
       String baseFolder;
// check if external storage is available
       if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
           baseFolder = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
       }
// revert to using internal storage (not sure if there's an equivalent to the above)
       else {
           baseFolder = this.getFilesDir().getAbsolutePath();
       }

       String string = "hello world!";
       File file = new File(baseFolder + File.separator + "test.txt");
       file.getParentFile().mkdirs();
       FileOutputStream fos = null;
       try {
           fos = new FileOutputStream(file);
           fos.write(string.getBytes());
           fos.flush();
           fos.close();

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

public void salvar(){
    String text = mEditText.getText().toString();
    FileOutputStream fos = null;

    try {
        fos = openFileOutput(FILE_NAME, MODE_APPEND);
        fos.write(text.getBytes());

        mEditText.getText().clear();
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                Toast.LENGTH_LONG).show();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
 public void load(){
     FileInputStream fis = null;

     try {
         fis = openFileInput(FILE_NAME);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         StringBuilder sb = new StringBuilder();
         String text;

         while ((text = br.readLine()) != null) {
             sb.append(text).append("\n");
         }

         mEditText.setText(sb.toString());

     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         if (fis != null) {
             try {
                 fis.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
 }

}
