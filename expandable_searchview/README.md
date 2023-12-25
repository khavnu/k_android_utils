```xml
    <com.khapv.search_view.ExpandableSearchView
    android:id="@+id/expandable_searchView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginEnd="30dp"
    android:layout_marginStart="30dp"
    android:layout_marginTop="50dp"/> 
```

## Listener
```Kotlin: add button
       var imageView = ImageView(this)
        imageView.setImageResource(R.drawable.baseline_3d_rotation_24)
        imageView.setPadding(20, 20, 20, 20);
        imageView.setBackgroundResource(R.drawable.bg_ripple_circle_button)
        imageView.setOnClickListener {

        }
        expandableSearchView?.addButton(imageView)
```