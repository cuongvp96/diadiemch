package c.top3haui.diadiemch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VanCuong on 1/12/2016.
 */

public class Recycleview_adpater extends RecyclerView.Adapter<Recycleview_adpater.MyViewHolder> {
    Context context;
    private ArrayList<Items> arrayList;
    private ClickListener clickListener;

    public Recycleview_adpater(Context context, ArrayList<Items> arrayList, ClickListener clickListener) {
        this.arrayList = arrayList;
        this.clickListener = clickListener;
        this.context = context;
        // this.context = context;
    }

    public Recycleview_adpater(Context context, ArrayList<Items> arrayList) {
        this.arrayList = arrayList;
        //     this.clickListener=clickListener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder vh, int position) {
        Items item = arrayList.get(position);
        vh.tvTen.setText("Tên: " + item.getTen());
        vh.tvLoaigas.setText("Loại gas: " + item.getLoaigas());
        vh.tvMotagia.setText("Mô tả giá: " + item.getMotagia());
        vh.tvSdt.setText("SDT: " + item.getSdt());
        vh.tvChu.setText("Chủ CH: " + item.getChucuahang());
        vh.tvDiadiem.setText("Địa điểm: " + item.getDiadiem());
        vh.tvLatlng.setText("Latlng: " + item.getLatlng());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView tvTen, tvMotagia, tvDiadiem, tvLatlng,tvLoaigas,tvSdt,tvChu;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tvTen = (TextView) view.findViewById(R.id.tvTen);
            tvMotagia = (TextView) view.findViewById(R.id.tv_motagia);
            tvDiadiem = (TextView) view.findViewById(R.id.tv_diadiem);
            tvLatlng = (TextView) view.findViewById(R.id.tv_latlng);
            tvLoaigas = (TextView) view.findViewById(R.id.tvLoai);
            tvSdt = (TextView) view.findViewById(R.id.tv_sdt);
            tvChu = (TextView) view.findViewById(R.id.tv_chucuahang);

        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
