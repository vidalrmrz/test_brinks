package co.com.brinks.test.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import co.com.brinks.test.activities.ParentActivity;
import co.com.brinks.test.network.VolleyService;

/**
 * Created by vidalramirez on 1/09/18.
 */

public class ParentFragments extends Fragment
{
    ParentActivity parentActivity;
    protected View rootView;

    public ParentFragments()
    {
        super();
    }

    public ParentActivity getParentActivity()
    {
        if(this.parentActivity==null)
        {
            this.parentActivity = (ParentActivity) this.getActivity();
        }
        return this.parentActivity;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof ParentActivity){
            parentActivity =(ParentActivity) context;
        }

    }

    public ProgressDialog getProcessDialog()
    {
        if (getParentActivity()==null)
        {
            return null;
        }
        return getParentActivity().getProcessDialog();
    }

    @Override
    public Context getContext()
    {
        if (isAdded() && !isDetached())
        {
            return  getActivity();
        }
        else
        {
            return super.getContext();
        }
    }

    public void showLongToast(String text)
    {
        if (isAdded() && isVisible() && !isDetached())
        {
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    protected VolleyService getVolleyService()
    {
        return VolleyService.getInstance();
    }
}
