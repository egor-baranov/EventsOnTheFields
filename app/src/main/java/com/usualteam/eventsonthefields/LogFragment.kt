package com.usualteam.eventsonthefields

import android.support.v4.app.INotificationSideChannel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import java.lang.Math.abs

class LogFragment : Fragment() {
    interface LogInteractionListener {

    }

    var mListener: LogInteractionListener? = null
    var logLayout: LinearLayout? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as LogInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " должен реализовывать интерфейс LogInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.log, container, false)
        logLayout = view.findViewById(R.id.logLayout)
        return view
    }

    public fun addOneTimeEvent(text: String, damage: Int) {
        if (logLayout == null) return
        val newView = LayoutInflater.from(this.context).inflate(R.layout.one_time_event, null, false)
        logLayout?.addView(newView)
        logLayout?.findViewWithTag<TextView>("text")?.text = text
        logLayout?.findViewWithTag<TextView>("damage")?.text = "${if(damage >= 0) "+" else "-"}${abs(damage)}HP"
        // logLayout?.findViewWithTag<ImageView>("image")?.
    }
}