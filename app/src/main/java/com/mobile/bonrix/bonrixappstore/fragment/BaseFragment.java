package com.mobile.bonrix.bonrixappstore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mobile.bonrix.bonrixappstore.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseFragment extends Fragment {
    public abstract void setToolbarForFragment();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarForFragment();
    }

    public void addFragment(BaseFragment fragment, boolean isReplace) {
        hideKeyboard();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.popBackStackImmediate(fragment.getClass().getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    void replaceFragment(Fragment mFragment, int id, String tag) {
        hideKeyboard();
        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        mTransaction.replace(id, mFragment);
        mTransaction.addToBackStack(mFragment.toString());
        mTransaction.commit();

    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
