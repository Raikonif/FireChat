package com.diegoalarcon.emichat.dialogues



import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.diegoalarcon.emichat.R
import com.diegoalarcon.emichat.models.NewRateEvent
import com.diegoalarcon.emichat.models.Rate
import com.diegoalarcon.emichat.toast
import com.diegoalarcon.emichat.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_rate, null)
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_tittle))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                requireActivity().toast("Presiona Ok")
                val textRate = view.editTextRateFeedback.text.toString()
                if(textRate.isNotEmpty()) {
                    val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString()
                        ?: kotlin.run { "" }
                    val rate = Rate(textRate, view.ratingBarFeedback.rating, Date(), imgURL)
                    RxBus.publish(NewRateEvent(rate))
                }
            }

            .setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
                requireActivity().toast("Presiona Cancelar")
            }
            .create()


        return super.onCreateDialog(savedInstanceState)
    }
}