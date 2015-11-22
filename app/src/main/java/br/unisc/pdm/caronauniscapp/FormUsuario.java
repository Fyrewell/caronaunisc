package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import br.unisc.pdm.caronauniscapp.database.Usuario;
import br.unisc.pdm.caronauniscapp.database.UsuarioDAO;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioTela;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioWebDao;

/**
 * Recebe informacoes da tela de informacoes de usuario para a criacao ou edicao de um perfil.
 *
 * Created by Diego on 05/10/2015.
 */
public class FormUsuario extends ActionBarActivity implements UsuarioTela {

    private UsuarioDAO dao;
    private String matricula = "";

    ImageView pickImage;
    Bitmap selectedImage;

    private final int SELECT_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pessoa);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            matricula = extras.getString("matricula");
        }

        dao = new UsuarioDAO(this);
        dao.open();

        if (!matricula.equals("")) {
            UsuarioWebDao webservice = new UsuarioWebDao(this);
            webservice.getUsuarioByMat(matricula);
        }

        pickImage = (ImageView) findViewById(R.id.imageView3);
        pickImage.setImageResource(R.drawable.ic_person);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();
                        try {
                            ExifInterface exif = new ExifInterface(filePath);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int rotate = 0;
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotate = 270;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotate = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotate = 90;
                                    break;
                            }
                            pickImage.setImageBitmap(selectedImage);
                            pickImage.setRotation(rotate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public static String encodeTobase64(Bitmap image) {
        if(image==null) return "";
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void populaTela(Usuario p) {
        EditText edit_matricula = (EditText) findViewById(R.id.edit_matricula);
        EditText edit_nome = (EditText) findViewById(R.id.edit_nome);
        EditText edit_senha = (EditText) findViewById(R.id.edit_senha);
        RadioButton rb_masc = (RadioButton) findViewById(R.id.radio_masculino);
        RadioButton rb_fem = (RadioButton) findViewById(R.id.radio_feminino);
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_ambos = (RadioButton) findViewById(R.id.radio_ambos);
        edit_matricula.setText(String.valueOf(p.getMatricula()));
        edit_nome.setText(p.getNome());
        edit_senha.setText(p.getSenha());
        if(p.getSexo().equals("Masculino"))
            rb_masc.setChecked(true);
        if(p.getSexo().equals("Feminino"))
            rb_fem.setChecked(true);
        if(p.getCadastroTipo()==1)
            rb_dar.setChecked(true);
        if(p.getCadastroTipo()==2)
            rb_receber.setChecked(true);
        if(p.getCadastroTipo()==3)
            rb_ambos.setChecked(true);

        pickImage = (ImageView) findViewById(R.id.imageView3);
        pickImage.setImageBitmap(decodeBase64(p.getFoto()));
    }

    /**
     * Captura os dados informados pelo usuario e edita seu perfil caso usuario ja exista,
     * ou realiza seu cadastro caso seja novo usuario.
     */
    public void insertOrEditPerson() {
        //Buscando dados de entrada digitados pelo usuario
        EditText edit_id = (EditText) findViewById(R.id.edit_id);
        EditText edit_matricula = (EditText) findViewById(R.id.edit_matricula);
        EditText edit_nome = (EditText) findViewById(R.id.edit_nome);
        EditText edit_senha = (EditText) findViewById(R.id.edit_senha);
        RadioButton rb_masc = (RadioButton) findViewById(R.id.radio_masculino);
        RadioButton rb_fem = (RadioButton) findViewById(R.id.radio_feminino);
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_ambos = (RadioButton) findViewById(R.id.radio_ambos);
        String sexo = null;
        if(rb_masc.isChecked()) sexo = "Masculino";
        if(rb_fem.isChecked()) sexo = "Feminino";
        int ctipo = 0;
        if(rb_dar.isChecked()) ctipo = 1;
        if(rb_receber.isChecked()) ctipo = 2;
        if(rb_ambos.isChecked()) ctipo = 3;

        if (edit_id.equals("")||edit_matricula.equals("")||edit_nome.equals("")||edit_senha.equals("")||rb_masc.equals("")||ctipo==0){
            Toast.makeText(this,"Entrar com todos os dados!",Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario person = new Usuario();

        if(edit_id.getText().toString().length() > 0)
            person.setId(Integer.parseInt(edit_id.getText().toString()));

        person.setNome(edit_nome.getText().toString());
        person.setMatricula(Integer.parseInt(edit_matricula.getText().toString()));
        person.setSenha(edit_senha.getText().toString());
        person.setSexo(sexo);
        person.setCadastroTipo(ctipo);

        selectedImage = ((BitmapDrawable)pickImage.getDrawable()).getBitmap();
        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), pickImage.getMatrix(), false);
        person.setFoto(encodeTobase64(selectedImage));

        UsuarioWebDao webservice = new UsuarioWebDao(this);
        if(!matricula.equals("")){
            webservice.editUsuario(person);
        }else{
            webservice.insertUsuario(person);
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_accept_pessoa){
            insertOrEditPerson();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void popularView(List<Usuario> values) {
        populaTela(values.get(0));
    }

    public void cadastrar(View v)
    {
        insertOrEditPerson();
    }
}