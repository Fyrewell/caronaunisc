package br.unisc.pdm.caronauniscapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import br.unisc.pdm.caronauniscapp.chat.SalaActivity;
import br.unisc.pdm.caronauniscapp.webservice.RotaWebDao;

/**
 * Created by Diego on 11/11/2015.
 */
public class ListCaronas extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> imgs = new ArrayList<String>();
    private ArrayList<Integer> mats = new ArrayList<Integer>();
    private ArrayList<Integer> dist = new ArrayList<Integer>();
    private ArrayList<Integer> ag_recebe = new ArrayList<Integer>();
    private ArrayList<Integer> ag_fornece = new ArrayList<Integer>();
    private ArrayList<Integer> status = new ArrayList<Integer>();
    private ArrayList<String> dias = new ArrayList<String>();
    private String matricula;
    private String nome;
    private int tipo; // 1 = recebe 2 = fornece
    private Context context;

    public ListCaronas(){
    }

    public ListCaronas(ArrayList<String> imgs, ArrayList<String> list, ArrayList<Integer> mats, ArrayList<Integer> dist,
                       ArrayList<Integer> ag_recebe, ArrayList<Integer> ag_fornece, ArrayList<Integer> status,
                       ArrayList<String> dias, int tipo,
                       String mat, String nome, Context context) {
        this.list = list;
        this.imgs = imgs;
        this.mats = mats;
        this.dist = dist;
        this.ag_recebe = ag_recebe;
        this.ag_fornece = ag_fornece;
        this.status = status;
        this.dias = dias;
        this.tipo = tipo;
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
            if (tipo==1) {
                view = inflater.inflate(R.layout.list_caronas, null);
            }else{
                view = inflater.inflate(R.layout.list_caronas1, null);
            }
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));
        Log.d("list", list.toString());
        ImageView ImgPessoa = (ImageView)view.findViewById(R.id.imageView_pessoa);
        ImgPessoa.setImageBitmap(decodeBase64(imgs.get(position)));

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        if (tipo==1) {
            final Button btnEsta = (Button) view.findViewById(R.id.btnCaronaAceitar);
            btnEsta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnEsta.setVisibility(View.GONE);
                    new RotaWebDao((CaronasActivity) v.getContext()).caronasEscolher(ag_fornece.get(position), ag_recebe.get(position), dist.get(position));
                    //((CaronasActivity) v.getContext()).finish();
                }
            });
            switch(status.get(position)){
                case -1:
                    tvStatus.setText("Status: Cancelado");
                    btnEsta.setVisibility(View.GONE);
                    break;
                case 0:
                    tvStatus.setText("Status: Escolher");
                    btnEsta.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvStatus.setText("Status: Aceitar");
                    btnEsta.setVisibility(View.GONE);
                    break;
                case 2:
                    tvStatus.setText("Status: Aprovado");
                    btnEsta.setVisibility(View.GONE);
                    break;
            }
            final Button btnCaronaMapa = (Button) view.findViewById(R.id.btnCaronaMapa);
            btnCaronaMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapAct = new Intent(context,MapsSearchActivity.class);
                    mapAct.putExtra("matricula", matricula);
                    mapAct.putExtra("target_usr_mat", mats.get(position).toString());
                    context.startActivity(mapAct);
                    notifyDataSetChanged();
                }
            });
        }else{
            final Button btnAceitar = (Button) view.findViewById(R.id.btnCaronaAceitar);
            final Button btnRecusar = (Button) view.findViewById(R.id.btnCaronaRecusar);
            btnAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnAceitar.setVisibility(View.GONE);
                    new RotaWebDao((CaronasActivity) v.getContext()).caronasAceitar(ag_fornece.get(position), ag_recebe.get(position), dist.get(position));
                    //((CaronasActivity) v.getContext()).finish();
                }
            });
            btnRecusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnRecusar.setVisibility(View.GONE);
                    new RotaWebDao((CaronasActivity) v.getContext()).caronasRecusar(ag_fornece.get(position), ag_recebe.get(position), dist.get(position));
                    //((CaronasActivity) v.getContext()).finish();
                }
            });
            switch(status.get(position)){
                case -1:
                    tvStatus.setText("Status: Cancelado");
                    btnAceitar.setVisibility(View.GONE);
                    btnRecusar.setVisibility(View.GONE);
                    break;
                case 0:
                    tvStatus.setText("Status: Escolher");
                    btnAceitar.setVisibility(View.VISIBLE);
                    btnRecusar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvStatus.setText("Status: Aceitar");
                    btnAceitar.setVisibility(View.VISIBLE);
                    btnRecusar.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvStatus.setText("Status: Aprovado");
                    btnAceitar.setVisibility(View.GONE);
                    btnRecusar.setVisibility(View.GONE);
                    break;
            }
            final Button btnCaronaMapa = (Button) view.findViewById(R.id.btnCaronaMapa);
            btnCaronaMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapAct = new Intent(context,MapsSearchActivity.class);
                    mapAct.putExtra("matricula", matricula);
                    mapAct.putExtra("target_usr_mat", mats.get(position).toString());
                    context.startActivity(mapAct);
                    notifyDataSetChanged();
                }
            });
        }

        Button btnChat = (Button) view.findViewById(R.id.btnCaronaChat);

        TextView tvDist = (TextView) view.findViewById(R.id.tvDistancia);
        TextView tvDia = (TextView) view.findViewById(R.id.tvDia);

        tvDist.setText(dist.get(position)+"m");
        tvDia.setText(dias.get(position));

        btnChat.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
