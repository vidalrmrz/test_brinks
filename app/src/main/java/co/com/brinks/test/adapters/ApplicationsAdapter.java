package co.com.brinks.test.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.brinks.test.R;
import co.com.brinks.test.models.Application;
import co.com.brinks.test.network.VolleyResult;
import co.com.brinks.test.network.VolleyService;

/**
 * Created by vidalramirez on 1/09/18.
 */

public class ApplicationsAdapter extends ArrayAdapter<Application>
{
    private String category;

    private List<Application> objects;
    private List<Application> filterObjects;

    public ApplicationsAdapter(Context context, List<Application> items)
    {
        super(context, R.layout.item_app, items);
        objects = items;
        filterObjects = items;
    }

    @Nullable
    @Override
    public Application getItem(int position)
    {
        return filterObjects.get(position);
    }

    @Override
    public int getCount()
    {
        return filterObjects==null ? 0 : filterObjects.size();
    }

    @NonNull
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                List<Application> tempList= new ArrayList<>();

                if ((constraint== null || constraint.length()==0) && category==null)
                {
                    tempList = objects;
                }
                else if(objects!=null)
                {
                    for (Application app : objects)
                    {
                        boolean isCategory = category==null || category.matches(app.getCategory().getAttributes().getLabel());
                        boolean contains = (constraint == null || constraint.length() == 0) || app.getName().getLabel().toUpperCase().contains(constraint.toString().toUpperCase());
                        if (isCategory && contains)
                        {
                            tempList.add(app);
                        }
                    }
                }

                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                //noinspection unchecked
                if(results.values==null)
                {
                    return;
                }

                filterObjects = (List<Application>) results.values;
                if (results.count > 0)
                {
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        AppRowHolder holder;
        View view;
        if (convertView==null || convertView.getTag()==null)
        {
            view = View.inflate(getContext(), R.layout.item_app, null);
            holder = new AppRowHolder(view);
            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (AppRowHolder) view.getTag();
        }

        final Application item = filterObjects.get(position);

        holder.nameTextView.setText(item.getName().getLabel());
        holder.dateTextView.setText(item.getReleaseDate().getAttributes().getLabel());

        VolleyService.getInstance().downloadImage(item.getImages()[0].getLabel(), holder.imageView, 0, new VolleyResult<Bitmap>()
        {
            @Override
            public void onSuccess(Bitmap response) throws Exception
            {

            }

            @Override
            public void onError(VolleyError error, Bitmap response)
            {

            }
        });

        return view;
    }

    class AppRowHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.nameTextView)
        TextView nameTextView;

        @BindView(R.id.dateTextView)
        TextView dateTextView;

        AppRowHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }
}
