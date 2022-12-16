package com.example.move_t;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInfrater;
    private Context context;
    public boolean shown = true;

    final ListAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
      void onItemClick(ListElement item);
    }

    public ListAdapter(List<ListElement> itemList, Context context, ListAdapter.OnItemClickListener listener){
        this.mInfrater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInfrater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }
    public void setItems(List<ListElement> items){mData = items;}

    public List<Integer> getSelecteds(){
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked()){
                list.add(mData.get(i).id);
            }
        }
        return list;
    }

    public void cleanSelecteds(){
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked()){
                mData.get(i).setChecked(false);
            }
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView name,extra;
        Switch checkSw;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            name = itemView.findViewById(R.id.nameTextView);
            extra = itemView.findViewById(R.id.extraTextView);
            checkSw = itemView.findViewById(R.id.checkSwitch);

        }

        void bindData(final ListElement item){
            iconImage.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            name.setText(item.getName());
            extra.setText(item.getDesc());
            checkSw.setChecked(item.isChecked());
            checkSw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setChecked(checkSw.isChecked());
                }
            });
            if(!shown)
            {
                checkSw.setVisibility(View.INVISIBLE);
                checkSw.setClickable(false);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                listener.onItemClick(item);
                //this is the new line
                checkSw.setChecked(item.isChecked());
              }
            });
        }
    }


}
