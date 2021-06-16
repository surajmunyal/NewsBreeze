package com.greedygame.newsbreeze.ui.list;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import com.greedygame.newsbreeze.R;
import com.greedygame.newsbreeze.base.BaseFragment;
import com.greedygame.newsbreeze.data.model.Article;
import com.greedygame.newsbreeze.ui.detail.NewsDetailFragment;
import com.greedygame.newsbreeze.ui.detail.NewsDetailsActivity;
import com.greedygame.newsbreeze.util.ViewModelFactory;

public class NewsListFragment extends BaseFragment implements ICardClickListener {

    @BindView(R.id.recyclerView) RecyclerView newsRecyclerView;
    @BindView(R.id.tv_error) TextView errorTextView;
    @BindView(R.id.loading_view) View loadingView;
    @BindView(R.id.btnSavedList) Button btnSavedList;
    @BindView(R.id.btnSort) Button btnSortList;

    @Inject ViewModelFactory viewModelFactory;
    private NewsListViewModel viewModel;
    private boolean isFromSavedList;
    private SearchView searchView;
    private NewsListAdapter newsListAdapter;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_place_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            isFromSavedList = getArguments().getBoolean("isFromSavedList");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsListViewModel.class);

        newsListAdapter = new NewsListAdapter(viewModel, this, this,isFromSavedList);
        newsRecyclerView.setAdapter(newsListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        newsRecyclerView.setLayoutManager(linearLayoutManager);

        initClickListenrs();
        observableViewModel();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager)requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(requireActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                newsListAdapter.getFilter().filter(query);
                return false;
            }
        });

        btnSortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sortNewsList();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initClickListenrs() {
        btnSavedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsListFragment newsListFragment =new NewsListFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSavedList",true);
                newsListFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.iconFrame, newsListFragment).commit();
            }
        });



        if(isFromSavedList) {
            btnSavedList.setVisibility(View.GONE);
            btnSortList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCardClick(Article articles) {

        Intent intent = new Intent(requireActivity(), NewsDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("articleDetails",articles);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void observableViewModel() {
        viewModel.getRepos().observe(this, repos -> {
            if(repos != null) newsRecyclerView.setVisibility(View.VISIBLE);
        });

        viewModel.getError().observe(this, isError -> {
            if (isError != null) if(isError) {
                errorTextView.setVisibility(View.VISIBLE);
                newsRecyclerView.setVisibility(View.GONE);
                errorTextView.setText("An Error Occurred While Loading Data!");
            }else {
                errorTextView.setVisibility(View.GONE);
                errorTextView.setText(null);
            }
        });

        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    errorTextView.setVisibility(View.GONE);
                    newsRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
