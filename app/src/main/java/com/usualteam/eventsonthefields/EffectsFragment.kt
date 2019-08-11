package com.usualteam.eventsonthefields

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class EffectsFragment : Fragment() {

    interface EffectsInteractionListener
    var effectsGrid: GridLayout? = null
    var mListener: EffectsInteractionListener? = null
    private val IconFromType: HashMap<String, Int> = hashMapOf("heal" to  R.drawable.ic_health_care, "radiation" to
            R.drawable.ic_radiation_sign
    )

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
        effectsGrid = view.findViewById(R.id.effects_grid)
        return view
    }

    public fun addContinuousEvent(text: String, type: String){
        if(effectsGrid == null || this.context == null) return
        val newView = LayoutInflater.from(this.context).inflate(R.layout.continuous_event, null, false)
        effectsGrid?.addView(newView)
        newView?.findViewWithTag<TextView>("text")?.text = text
        if(type in IconFromType) newView?.findViewWithTag<ImageView>("image")?.setImageResource(IconFromType[type]!!)
        Log.d("DEBUG", (type in IconFromType).toString())
        Log.d("DEBUG", type)
    }

    public fun clearGrid(){
        if(effectsGrid == null) return
        effectsGrid?.removeAllViews()
    }
}