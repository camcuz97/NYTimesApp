package com.example.camcuz97.nytimessearch;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;

/**
 * Created by camcuz97 on 6/20/16.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    //min num of items to have before needing more
    private int visibleThreshold = 5;
    //the current offset page of data loaded
    private int currentPage = 0;
    //the total num of items in dataset after load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    public EndlessScrollListener(){

    }
    public EndlessScrollListener(int visibleThreshold){
        this.visibleThreshold = visibleThreshold;
    }
    public EndlessScrollListener(int visibleThreshold, int startPage){
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        // Sets the  footerViewType
        int footerViewType = getFooterViewType();

        ArrayAdapter<Article> adapter = (ArrayAdapter<Article>) view.getAdapter();
        int lastViewType = adapter.getItemViewType(adapter.getCount() - 1);

        // check the lastview is footview
        boolean isFootView = lastViewType == footerViewType;

        if (loading && (totalItemCount > previousTotalItemCount)) {
            if (!isFootView) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
        }
        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }
    }
    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract boolean onLoadMore(int page, int totalItemsCount);

    // set FooterView type
    // if don't use footview loadmore  set: -1
    public abstract int getFooterViewType();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
