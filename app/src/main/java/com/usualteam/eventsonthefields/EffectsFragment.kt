package com.usualteam.eventsonthefields

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class EffectsFragment : Fragment() {

    interface EffectsInteractionListener

    var mListener: EffectsInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as EffectsInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " должен реализовывать интерфейс EffectsInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.effects, container, false)
        return view
    }
}