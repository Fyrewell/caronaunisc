package br.unisc.pdm.caronauniscapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.unisc.pdm.caronauniscapp.chat.ChatApplication;
import br.unisc.pdm.caronauniscapp.chat.SalaActivity;

/**
 * Created by Diego on 11/11/2015.
 */
public class ListCaronas extends BaseAdapter implements ListAdapter {

        private ArrayList<String> list = new ArrayList<String>();
        private ArrayList<String> imgs = new ArrayList<String>();
        private ArrayList<Integer> mats = new ArrayList<Integer>();
        private String matricula;
        private String nome;
        private Context context;


        public ListCaronas(ArrayList<String> imgs, ArrayList<String> list, ArrayList<Integer> mats, String mat, String nome, Context context) {
            this.list = list;
            this.imgs = imgs;
            this.mats = mats;
            this.context = context;
            this.matricula = mat;
            this.nome = nome;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_caronas, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position));
            Log.d("list", list.toString());
            ImageView ImgPessoa = (ImageView)view.findViewById(R.id.imageView_pessoa);
            ImgPessoa.setImageBitmap(decodeBase64(imgs.get(position)));

            Button escolher = (Button) view.findViewById(R.id.button8);
            escolher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SalaActivity.class);
                    intent.putExtra("chat_usr_nome", list.get(position));
                    intent.putExtra("chat_usr_mat", mats.get(position).toString());
                    intent.putExtra("matricula", matricula);
                    intent.putExtra("nome", nome);
                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            });
            /*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(Color.CYAN);
                    String val = (String) list.get(position);
                    Intent intent = new Intent(v.getContext(), MapsSearchActivity.class);
                    intent.putExtra("chat_usr_nome", list.get(position));
                    intent.putExtra("chat_usr_mat", mats.get(position).toString());
                    intent.putExtra("matricula", matricula);
                    intent.putExtra("nome", nome);
                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            });
            */
            return view;
        }

        public static Bitmap decodeBase64(String input)
        {
            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }

}
