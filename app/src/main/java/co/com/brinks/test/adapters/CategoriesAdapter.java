package co.com.brinks.test.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.brinks.test.R;
import co.com.brinks.test.models.Attribute;

/**
 * Created by vidalramirez on 1/09/18.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.RowHolder>
{

    private List<Attribute> itemList;

    private Activity mActivity;
    private CategoryRowListener mListener;

    private int[] colors;

    public CategoriesAdapter(Activity activity, List<Attribute> itemList, CategoryRowListener listener)
    {
        this.itemList = itemList;
        this.mActivity = activity;
        this.mListener = listener;

        colors = new int[] {R.color.color1, R.color.color2, R.color.color3};
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup viewGroup, final int position)
    {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        return new RowHolder(v);
    }

    @Override
    public void onBindViewHolder(RowHolder rowHolder, final int position)
    {
        final Attribute item = itemList.get(position);

        rowHolder.nameTextView.setText(item.getLabel());

        int color = colors[position % colors.length];
        color = mActivity.getResources().getColor(color);

        rowHolder.contentView.setBackgroundColor(color);
        rowHolder.nameTextView.setTextColor(color);

        rowHolder.itemView.setOnClickListener(view ->
        {
            if (mListener != null)
            {
                mListener.onClickItem(view, itemList.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return null != itemList ? itemList.size() : 0;
    }

    public interface CategoryRowListener
    {
        void onClickItem(View view, Attribute attribute);
    }

    class RowHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.nameTextView)
        TextView nameTextView;

        @BindView(R.id.contentView)
        View contentView;

        RowHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
