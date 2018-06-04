package br.wake_in_place.ui.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.models.AlarmItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jeferson on 07/08/2016.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.CardChangesViewHolder> {

    private List<AlarmItem> cardViewListItems;
    private AlarmListener alarmListener;


    public AlarmAdapter(List<AlarmItem> palettes, AlarmListener alarmListener) {
        this.cardViewListItems = new ArrayList<AlarmItem>();
        this.cardViewListItems.addAll(palettes);
        this.alarmListener = alarmListener;
    }

    @Override
    public CardChangesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_alarm, viewGroup, false);
        return new CardChangesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardChangesViewHolder holder, int i) {

        if (cardViewListItems.get(holder.getAdapterPosition()) != null) {
            final AlarmItem item = cardViewListItems.get(holder.getAdapterPosition());

            try {
                holder.constraintItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarmListener.onItemClick(view);
                    }
                });

                holder.constraintItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        alarmListener.onLongItemClick(view);
                        return false;
                    }
                });
                holder.switch1.setChecked(item.isActive());
                holder.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.setActive(isChecked);
                    }
                });

                holder.txtHour.setText(item.getHour());
                holder.txtDays.setText(item.getRepeatDays());
                holder.txtAddress.setText(item.getAddress());

            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cardViewListItems != null)
            return cardViewListItems.size();
        else return 0;

    }

    public interface AlarmListener {
        public void onItemClick(View view);

        public void onLongItemClick(View view);

    }

    protected class CardChangesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_hour)
        TextView txtHour;
        @BindView(R.id.txt_days)
        TextView txtDays;
        @BindView(R.id.switch1)
        Switch switch1;
        @BindView(R.id.txt_address)
        TextView txtAddress;
        @BindView(R.id.constraint_item)
        ConstraintLayout constraintItem;

        private CardChangesViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

}