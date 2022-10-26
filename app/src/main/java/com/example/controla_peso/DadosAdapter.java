package com.example.controla_peso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.controla_peso.entidade.EntidadeDados;

import java.util.List;

public class DadosAdapter extends BaseAdapter {
    private Context context;
    private List<EntidadeDados> list;

    public DadosAdapter(Context context, List<EntidadeDados> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int auxPosition = i;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pesquisar_list_item, null);

        TextView tvCodigo = (TextView) layout.findViewById(R.id.textView_codigo);
        tvCodigo.setText(list.get(i).getCodigoBrinco());

        TextView tvData = (TextView) layout.findViewById(R.id.textView_data);
        tvData.setText(list.get(i).getData());

       // TextView tvPeso = (TextView) layout.findViewById(R.id.textView_peso);
       // tvData.setText(list.get(i).getPeso());


        return layout;
    }
}
