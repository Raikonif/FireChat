package com.diegoalarcon.emichat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diegoalarcon.emichat.R
import com.diegoalarcon.emichat.models.TotalMessagesEvent
import com.diegoalarcon.emichat.toast
import com.diegoalarcon.emichat.utils.CircleTransform
import com.diegoalarcon.emichat.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.util.EventListener

class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    private lateinit var infoBusListener: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_info, container, false)
        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        //Firebase Style Total Messages
        //suscribeToTotalMessageFirebaseStyle()

        //Total Messages Event Bus + reactive style
        suscribeToTotalMessageEventBusReactiveStyle()
        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName }
            ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        }
    }

    private fun suscribeToTotalMessageFirebaseStyle() {
        chatDBRef.addSnapshotListener(object : EventListener,
            com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(
                querySnapshot: QuerySnapshot?,
                exception: FirebaseFirestoreException?
            ) {
                exception?.let {
                    activity!!.toast("Exception")
                    return
                }
                querySnapshot?.let { _view.textViewInfoTotalMessages.text = "${it.size()}" }
            }
        })
    }

    private fun suscribeToTotalMessageEventBusReactiveStyle(){
        infoBusListener = RxBus.listen(TotalMessagesEvent::class.java).subscribe({
            _view.textViewInfoTotalMessages.text = "${it.total}"
        })
    }

    override fun onDestroyView() {
        infoBusListener.dispose()
        chatSubscription?.remove()
        super.onDestroyView()
    }
}
