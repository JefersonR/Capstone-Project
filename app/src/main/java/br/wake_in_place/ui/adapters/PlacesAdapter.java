package br.wake_in_place.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.models.PlaceItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.CardChangesViewHolder> {

    private List<PlaceItem> cardViewListItems;
    private Context context;
    private PlaceListener placeListener;


    public PlacesAdapter(List<PlaceItem> palettes, PlaceListener placeListener) {
        this.cardViewListItems = new ArrayList<PlaceItem>();
        this.cardViewListItems.addAll(palettes);
        this.placeListener = placeListener;

    }

    @Override
    public CardChangesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_places, viewGroup, false);
        context = viewGroup.getContext();
        return new CardChangesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardChangesViewHolder holder, int i) {

        if (cardViewListItems!= null && cardViewListItems.get(holder.getAdapterPosition()) != null) {
            final PlaceItem item = cardViewListItems.get(holder.getAdapterPosition());
            holder.tvAddress.setText(item.getAddress());
            Picasso.with(context).load(item.getImgUrl()).into(holder.imgMaps);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeListener.onItemClick(view, item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (cardViewListItems != null)
            return cardViewListItems.size();
        else return 0;

    }

    public interface PlaceListener {
        public void onItemClick(View view, PlaceItem placeItem);
    }


    class CardChangesViewHolder extends RecyclerView.ViewHolder {

         @BindView(R.id.tv_address)
         TextView tvAddress;
         @BindView(R.id.img_maps)
         ImageView imgMaps;
         @BindView(R.id.card)
         CardView card;

         private CardChangesViewHolder(View rootView) {
             super(rootView);
             ButterKnife.bind(this, rootView);
         }
    }

}