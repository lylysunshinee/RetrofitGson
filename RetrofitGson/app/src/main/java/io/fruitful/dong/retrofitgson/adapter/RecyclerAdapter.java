package io.fruitful.dong.retrofitgson.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import io.fruitful.dong.retrofitgson.R;
import io.fruitful.dong.retrofitgson.model.User;

/**
 * Created by Ares on 8/22/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<User> itemList;
    private Context mContext;
    private ClickListener clickListener;


    public RecyclerAdapter(Context mContext, List<User> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_row_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.mAppname.setText(itemList.get(position).getName());
        Picasso.with(mContext)
                .load(itemList.get(position).getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mIcon);


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mAppname;
        public ImageView mIcon;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mAppname = (TextView) itemView.findViewById(R.id.tvName);
            mIcon = (ImageView) itemView.findViewById(R.id.imIcon);

            mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });

            mIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    clickListener.onItemLongClick(getAdapterPosition(), view);
                    return false;
                }
            });
        }
    }


    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
