package com.staqu.healthmonitoring

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.staqu.healthmonitoring.custom.FadingSnackbar
import com.staqu.healthmonitoring.network.ApiClient
import com.staqu.healthmonitoring.network.ApiService
import com.staqu.healthmonitoring.network.Const
import com.staqu.healthmonitoring.network.model.Check
import com.staqu.healthmonitoring.network.model.CheckResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), RecyclerViewActionListener {

    lateinit var contentProgressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var fadingSnackbar: FadingSnackbar
    lateinit var errorView: LinearLayout
    lateinit var refreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""

        fadingSnackbar = findViewById(R.id.snackbar)
        contentProgressBar = findViewById(R.id.contentProgressBar)
        recyclerView = findViewById(R.id.recyclerView)
        errorView = findViewById(R.id.errorView)
        refreshLayout = findViewById(R.id.refresh)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16))
        refreshLayout.setOnRefreshListener {

            getAllChecks(true)

        }

        findViewById<MaterialButton>(R.id.retryButton).setOnClickListener{
            contentProgressBar.visibility = View.VISIBLE
            errorView.visibility = View.GONE
            getAllChecks(false)
        }

        getAllChecks(false)
    }

    private fun getAllChecks(refresh: Boolean) {

        val apiService = ApiClient.getClient(applicationContext)!!
                .create(ApiService::class.java)

        // Fetching all checks
        apiService.listAllChecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckResponse>() {
                    override fun onSuccess(checks: CheckResponse) {
                        // Received all checks
                        Log.d(Const.TAG, "All checks: " + checks.checks.toString())
                        contentProgressBar.visibility = View.GONE
                        errorView.visibility = View.GONE
                        if (refresh) refreshLayout.isRefreshing = false


                        setAdapter(checks.checks)
                    }

                    override fun onError(e: Throwable) {
                        // Network error
                        contentProgressBar.visibility = View.GONE

                        if (refresh) refreshLayout.isRefreshing = false

                        if (!refresh)
                            errorView.visibility = View.VISIBLE
                        else
                            showFadingSnackbar(getString(R.string.error_text_general))
                        Log.d(Const.TAG, "Get checks error: " + e.localizedMessage)
                    }
                })
    }

    private fun setAdapter(checks: List<Check>) {
        var checksAdapter = ChecksAdapter(this, checks)
        checksAdapter.actionListener = this
        recyclerView.adapter = checksAdapter
    }


    private fun showFadingSnackbar(message: String) {

        fadingSnackbar.show(
                messageText = message,
                //actionId = message.actionId,
                longDuration = true,
                actionClick = {
                    //actionClickListener()
                    fadingSnackbar.dismiss()
                },
                // When the snackbar is dismissed, ping the snackbar message manager in case there
                // are pending messages.
                dismissListener = {
                    //snackbarMessageManager.loadNextMessage()
                }
        )
    }

    override fun pingUrlCopied(message: String) {
        //showFadingSnackbar(message)
    }

}

interface RecyclerViewActionListener {
    fun pingUrlCopied(message: String)
}