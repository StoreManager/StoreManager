package com.cottee.managerstore.adapter.order_adapter.dished_info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cottee.managerstore.R;
import com.cottee.managerstore.bean.OrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/4/7.
 */

public class HandleDishInfoAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo.IndentListNoOkBean.TrolleyListBean.ListBean> dishList;

    public HandleDishInfoAdapter(Context context, List<OrderInfo.IndentListNoOkBean.TrolleyListBean.ListBean> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    @Override
    public int getCount() {
        return dishList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_order_handle_dish_info, null);
            holder.tv_handle_dish_name = view.findViewById(R.id.tv_handle_dish_name);
            holder.tv_handle_dish_count = view.findViewById(R.id.tv_handle_dish_count);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_handle_dish_name.setText(dishList.get(i).getGoodsName());
        holder.tv_handle_dish_count.setText(String.valueOf(dishList.get(i).getDisNum()));
        return view;
    }
    protected static class ViewHolder{
        TextView tv_handle_dish_name;
        TextView tv_handle_dish_count;
    }
}
