package com.example.franciscovictor.vendaslinguicao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapts NewsEntry objects onto views for lists
 */
public final class NewsEntryAdapter extends ArrayAdapter<VendedorLista> {

    private final int newsItemLayoutResource;

    public NewsEntryAdapter(final Context context, final int newsItemLayoutResource) {
        super(context, 0);
        this.newsItemLayoutResource = newsItemLayoutResource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final VendedorLista entry = getItem(position);

        // Setting the title view is straightforward
        viewHolder.tvNomeVendedor.setText(entry.getNome());

        viewHolder.tvFraseVendedor.setText(entry.getFrase());

        viewHolder.tvDistVendedor.setText(entry.getDistancia());

        return view;
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView = null;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(newsItemLayoutResource, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;


        if(null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.tvNomeVendedor = (TextView) workingView.findViewById(R.id.tvNomeVendedor);
            viewHolder.tvFraseVendedor = (TextView) workingView.findViewById(R.id.tvFraseVendedor);
            viewHolder.tvDistVendedor = (TextView) workingView.findViewById(R.id.tvDistVendedor);


            workingView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    /**
     * ViewHolder allows us to avoid re-looking up view references
     * Since views are recycled, these references will never change
     */
    private static class ViewHolder {
        public TextView tvNomeVendedor;
        public TextView tvFraseVendedor;
        public TextView tvDistVendedor;
    }


}