package me.yeojoy.hancahouse;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import me.yeojoy.hancahouse.app.adapter.HouseAdapter
import me.yeojoy.hancahouse.detail.DetailHouseActivity
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.util.AppPreferences

class MainActivity : AppCompatActivity(), MainContract.View {
    companion object {
        val TAG = MainActivity::class.simpleName
    }

    private lateinit var presenter: MainContract.Presenter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPresenter(MainPresenter(this))

        setSupportActionBar(toolbar)

        presenter.onViewCreated()

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = HouseAdapter(presenter)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val floatActionButton = findViewById<FloatingActionButton>(R.id.floating_action_button_refresh)
        floatActionButton.setOnClickListener {
            // TODO load list
        }

        presenter.retrieveHouses()
        // TODO if this is first launch, load page 2 and 3.

    }

    override fun onDestroy() {
        presenter.onViewDestroyed()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.go_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGetHouses(hasError: Boolean) {
        if (hasError) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            textViewEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            textViewEmptyList.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(house: House) {
        // TODO set image transition animation
        val intent = Intent(this, DetailHouseActivity::class.java)
        intent.putExtra(House::class.simpleName, house)
//        startActionMode(intent /* , activitonOptions.toBundle()*/)
        startActivity(intent)
    }

    override fun refreshResult(houses: List<House>?) {

    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }
}
