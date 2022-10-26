package com.example.controla_peso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_peso.dao.daoCadastroBovino;
import com.example.controla_peso.dao.daoDados;
import com.example.controla_peso.entidade.EntidadeCadastroBovino;
import com.example.controla_peso.entidade.EntidadeDados;
import com.example.controla_peso.foto.AlbumStorageDirFactory;
import com.example.controla_peso.foto.BaseAlbumDirFactory;
import com.example.controla_peso.foto.FroyoAlbumDirFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CadastroBovinoActivit extends AppCompatActivity {

    private Button BtSalvar, BtTirarFoto, BtSalvarImagem;
    private EditText EtCodigoBrinco;
    private EditText EtData;
    private EditText EtRaca;
    private EditText EtObservacao;
    private TextView TvVoltar;
    private ImageView Imagem;
    private final int  TIRAR_FOTO = 1;
    private Bitmap imageBitmap;
    private byte[] outImage;
    private String chamada;
    private Switch  SwitchVendido;

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private VideoView mVideoView;
    private Uri mVideoUri;
    private  int id = 0;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    EntidadeCadastroBovino entidade = new EntidadeCadastroBovino();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_bovino);

        SwitchVendido = (Switch)findViewById(R.id.switchVendido);
        mImageView = (ImageView)findViewById(R.id.imageView);
        BtTirarFoto = (Button)findViewById(R.id.button_capturar);
        EtCodigoBrinco =(EditText)findViewById(R.id.editTextText_codigo_cadastro);
        EtData =(EditText)findViewById(R.id.editText_date_cadastro);

        EtRaca =(EditText)findViewById(R.id.editTextText_raca);
        EtObservacao = (EditText)findViewById(R.id.editTextText_observacao_cadastro);
        BtSalvar =(Button)findViewById(R.id.button_salvar_cadastro);

        Intent it = getIntent();
        chamada = it.getStringExtra("chamada");
        String param = it.getStringExtra("id");
        if(param !=null){
            id = Integer.parseInt(param);
        }
        preencheCamposEdicao(id, chamada);


        //Foto
        BtTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);

            }
        });

        BtSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chamada!= null){
                    if(chamada.equals("editar")){
                        update(); 
                    }
                }else{
                   Salvar();
                }

            }
        });
        /*
         Habilita botão voltar da actionBar
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }

    /**
     * chama Activit que exibe imagem em tela cheia
     * (descontinuado)
     */
    public void exibirImagemTelacheia(int id){
        if(imageBitmap != null){
            Intent intent = new Intent(this, ImagemTelaCheiaActivit.class);
            intent.putExtra("imagem", converteImagem());
            intent.putExtra("id", id);
            startActivity(intent);

        }else {
            if (outImage != null) {
                Intent intent = new Intent(this, ImagemTelaCheiaActivit.class);
               // intent.putExtra("imagem", outImage);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        }
    }
   /**
     * Preenche campos para Edicao
     * @param id
     * @param chamada
     */
    public void preencheCamposEdicao(int id, String chamada){
        daoCadastroBovino dao = new daoCadastroBovino(this);
        if(chamada != null || id != 0){
           entidade =  dao.buscaCadastroId(id);
           EtCodigoBrinco.setText( entidade.getCadCodigoBrinco());
           EtData.setText(entidade.getCadData());
           EtData.setEnabled(false);
           EtRaca.setText(entidade.getRaca());
           EtObservacao.setText(entidade.getObservacao());
           if(entidade.isVendido()){
               SwitchVendido.setChecked(true);
           }else{
               SwitchVendido.setChecked(false);
           }
            outImage= entidade.getFoto();
            if(outImage != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                mImageView.setImageBitmap(imageBitmap);
                mImageView.setRotation(90);
            }

        }else {
            CarregaData(EtData);
           // EtData.setEnabled(false);
        }
    }
    /*
      controla o evento de voltar do teclado fisico
     */
    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        ChamaPesquisaCadastroBovino();
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_bovino, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_pesquisa_cadastro) {
            // return true;
            ChamaPesquisaCadastroBovino();
        }
        if (id == R.id.menu_pesquisa_pesagem) {
            // return true;
            ChamaPesquisaCadastroPeso();
        }
        if(id == R.id.menu_cadastrar_bovino_sair){
            ChamaPesquisaCadastroPeso();
        }
        if(id == android.R.id.home){
            //ChamaPesquisaCadastroPeso();
            ChamaPesquisaCadastroBovino();
        }


        return super.onOptionsItemSelected(item);
    }

    private void update(){
        try {
                entidade.setCadCodigoBrinco(EtCodigoBrinco.getText().toString());
                entidade.setCadData(EtData.getText().toString());
                entidade.setRaca(EtRaca.getText().toString());
                entidade.setObservacao(EtObservacao.getText().toString());
                if(outImage != null) {
                    entidade.setFoto(outImage);
                }else {
                    entidade.setFoto(converteImagem());
                }
                entidade.setVendido(SwitchVendido.isChecked());

                daoCadastroBovino bd = new daoCadastroBovino(this);
                bd.update(entidade);
                Toast.makeText(this, "DADOS ATUALIZADOS COM SUCESSO", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"ERRO AO ATUALIZAR O DADOS",Toast.LENGTH_SHORT).show();
        }
    }

    private void Salvar(){
        try {
            if(Validar(EtCodigoBrinco)) {
                entidade.setCadCodigoBrinco(EtCodigoBrinco.getText().toString());
                entidade.setCadData(EtData.getText().toString());
                entidade.setRaca(EtRaca.getText().toString());
                entidade.setObservacao(EtObservacao.getText().toString());
                entidade.setFoto(converteImagem());
                entidade.setVendido(SwitchVendido.isChecked());
                daoCadastroBovino bd = new daoCadastroBovino(this);
                bd.inserir(entidade);
                Toast.makeText(this, "DADOS salvos com sucesso", Toast.LENGTH_SHORT).show();

                ChamaPesquisaCadastroBovino();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"ERRO AO SALVAR O DADOS"+e,Toast.LENGTH_SHORT).show();
        }
    }

    public void CarregaData(EditText editTextData){
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        editTextData.setText(dataFormatada.toString());
    }
    public boolean Validar(EditText codigo){
        daoCadastroBovino dao = new daoCadastroBovino(this);
        if(codigo.getText().toString() == null || codigo.getText().toString().equals("")){
            Toast.makeText(this,"CAMPO CODIGO DEVE SER PREENCHIDO",Toast.LENGTH_SHORT).show();
            return  false;
        }if(!dao.VerificaExiste(codigo.getText().toString())){
            Toast.makeText(this,"CODIGO DO BRINCO JA CADASTRADO",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void ChamaPesquisaCadastroBovino(){
        Intent intent = new Intent(this, PesquisaCadBovinoActivity.class);
        startActivity(intent);
    }

    public void ChamaPesquisaCadastroPeso(){
        Intent intent = new Intent(this, PesquisaActivit.class);
        startActivity(intent);
    }
    public void tirarFoto(View view){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    /**
     * convete a imagem em um array de Byte
     * @return
     */
   public  byte[] converteImagemOld (){
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       if(imageBitmap != null) {
           imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
           byte imagemBytes[] = stream.toByteArray();
           return imagemBytes;
       }else{
           return null;
       }
   }

   //------------------------CONVERTER IMAGEM ----------------------//

    public  byte[] converteImagem (){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(mImageBitmap != null) {
            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imagemBytes[] = stream.toByteArray();
            outImage = imagemBytes;
            entidade.setFoto(outImage);
            //SalvarImagem(entidade);
            return imagemBytes;
        }else{
            return null;
        }
    }


   private String getAlbumName() {
       return getString(R.string.album_name);
   }

   private File getAlbumDir() {
       File storageDir = null;

       if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

           storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

           if (storageDir != null) {
               if (! storageDir.mkdirs()) {
                   if (! storageDir.exists()){
                       Log.d("CameraSample", "failed to create directory");
                       return null;
                   }
               }
           }

       } else {
           Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
       }

       return storageDir;
   }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
       // int targetW = mImageView.getWidth();
       // int targetH = mImageView.getHeight();
        int targetW = 700;
        int targetH = 700;
        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageBitmap = bitmap;
        /* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setRotation(90);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;

            converteImagem();
        }

    }

    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
        mVideoView.setVideoURI(mVideoUri);
        mImageBitmap = null;
        mVideoView.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.VISIBLE);;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S

            case ACTION_TAKE_VIDEO: {
                if (resultCode == RESULT_OK) {
                    handleCameraVideo(data);
                }
                break;
            } // ACTION_TAKE_VIDEO
        } // switch
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(
                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }




}
