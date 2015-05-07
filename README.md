# UniversalLoadingView
An universal loading view for help you 
to show a loading progressbar and label
when you do a  background task. .

It is really sample, and it has handle all issues such as,retry load , load falied... etc for you.

How to Use

    <com.sw.library.widget.library.UniversalLoadingView
        android:id="@+id/loadingView"
        app:bGradient="false"
        app:radius="50dp"
        app:bg_transparent="false"
        app:circleColor="@android:color/holo_green_dark"
        android:background="@android:color/white"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></com.sw.library.widget.library.UniversalLoadingView>

Put UniversalLoadingView in your xml files. you can custom the progress bar radius and color, and you can
also set the background is transparent or not when really start loading.

        mLoadingView = (UniversalLoadingView) findViewById(R.id.loadingView);
        mLoadingView.setOnReloadListener(new UniversalLoadingView.ReloadListner() {
            @Override
            public void reload() {
                loadImageView();
            }
        });

You can set one reload listener to UniversalLoadingView, so that it can retry for you when users click the screen to
reload.
