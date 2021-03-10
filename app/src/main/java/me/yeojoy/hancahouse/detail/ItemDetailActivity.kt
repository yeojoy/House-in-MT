package me.yeojoy.hancahouse.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.WebActivity
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.model.Item
import me.yeojoy.hancahouse.model.ItemDetail

class ItemDetailActivity : AppCompatActivity(), ItemDetailContract.View<Item> {

    companion object {
        private val TAG = ItemDetailActivity::class.java.simpleName
    }

    private lateinit var textViewDescription: TextView
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var linearLayoutImages: LinearLayout

    private lateinit var presenter : ItemDetailContract.Presenter<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)
        setPresenter(ItemDetailPresenter(this))
        presenter.onViewCreated()

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent == null) {
            finish()
            return
        }

        val item = intent.getParcelableExtra(Constants.KEY_ITEM) as Item?
        Log.d(TAG, "DETAIL ACTIVITY >>> $item")
        item.let {
            presenter.setItem(it!!)
        }

        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.height = resources.displayMetrics.widthPixels
        appBarLayout.layoutParams = params
        val imageViewThumbnail = findViewById<ImageView>(R.id.image_view_thumbnail)
        val textViewTitle = findViewById<TextView>(R.id.text_view_title)
        textViewDescription = findViewById(R.id.text_view_description)
        progressBarLoading = findViewById(R.id.progressBarLoading)
        linearLayoutImages = findViewById(R.id.linear_layout_images)
        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance())

        if (!TextUtils.isEmpty(item?.thumbnailUrl) &&
                item?.thumbnailUrl != Constants.NO_IMAGE) {
            val size = "-120x90"
            val imageUrl = item?.thumbnailUrl?.replace(size, "")
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageViewThumbnail)
        }
        textViewTitle.text = item?.title
        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbarLayout.title = item?.author

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.go_web_page -> {
                presenter.goWebClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGetHouseDetail(itemDetail: ItemDetail) {
        Log.i(TAG, "onGetHouseDetail()")

        progressBarLoading.visibility = View.GONE

        linearLayoutImages.removeAllViews()

        for (url in itemDetail.imageUrls) {

            val imageUrl = Constants.HOST + url
            val imageView = ImageView(this)
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView)

            linearLayoutImages.addView(imageView, imageLayoutParams);
        }
    }

    private val imageLayoutParams: LinearLayout.LayoutParams
        get() {
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_bottom_detail_image)
            return params
        }

    override fun onGetWholeContents(contents: SpannableStringBuilder) {
        textViewDescription.text = contents
    }

    override fun onGetEmailAddress(emailAddress: String) {
        Log.d(TAG, "emailAddress : $emailAddress")
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        Toast.makeText(this@ItemDetailActivity, "email", Toast.LENGTH_SHORT).show()
    }

    override fun onGetTelephoneNumber(telephoneNumber: String) {
        Log.d(TAG, "telephoneNumber : $telephoneNumber")
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $telephoneNumber")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        Toast.makeText(this@ItemDetailActivity, "phone", Toast.LENGTH_SHORT).show()
    }


    override fun setPresenter(presenter: ItemDetailContract.Presenter<Item>) {
        this.presenter = presenter
    }

    override fun onGetWebUrl(url: String) {
        Log.d(TAG, "url : $url")
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, R.string.toast_no_detail_url, Toast.LENGTH_SHORT)
                    .show()
            return
        }
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(Constants.KEY_INTENT_URL, url)
        startActivity(intent)
    }
}