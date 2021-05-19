package com.diegoalarcon.emichat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegoalarcon.emichat.R
import com.diegoalarcon.emichat.adapters.RatesAdapter
import com.diegoalarcon.emichat.dialogues.RateDialog
import com.diegoalarcon.emichat.models.NewRateEvent
import com.diegoalarcon.emichat.models.Rate
import com.diegoalarcon.emichat.toast
import com.diegoalarcon.emichat.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*
import java.util.EventListener


class RatesFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratesDBRef: CollectionReference

    private var ratesSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRatesDB()
        setUpCurrentUser()

        setUpRecyclerView()
        setUpFab()

        subscribeToRating()
        subscribeToNewRatings()

        return _view
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter
    }

    private fun setUpFab() {
        _view.fabRating.setOnClickListener { RateDialog().show(childFragmentManager, "") }
    }

    private fun setUpRatesDB() {
        ratesDBRef = store.collection("rates")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun saveRate(rate: Rate) {
        val newRating = HashMap<String, Any>()
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createdAt"] = rate.profileImgUrl

        ratesDBRef.add(newRating)
            .addOnCompleteListener {
                requireActivity().toast("Rating añadido")
            }
            .addOnFailureListener {
                requireActivity().toast("Rating error, intenta nuevamente")
            }
    }

    private fun subscribeToRating() {
        ratesSubscription = ratesDBRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    exception?.let {
                        activity!!.toast("Excepcion!")
                        return
                    }

                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        adapter.notifyDataSetChanged()
                        _view.recyclerView.smoothScrollToPosition(0)
                    }
                }
            })
    }

    private fun subscribeToNewRatings() {
        rateBusListener = RxBus.listen(NewRateEvent::class.java).subscribe({
            saveRate(it.rate)
        })
    }

    override fun onDestroyView() {
        rateBusListener.dispose()
        ratesSubscription?.remove()
        super.onDestroyView()
    }
}

