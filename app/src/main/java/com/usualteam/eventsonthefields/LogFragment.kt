package com.usualteam.eventsonthefields

import android.support.v4.app.INotificationSideChannel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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


    private val IconFromType: HashMap<String, Int> = hashMapOf("bomb" to  R.drawable.ic_nuclear_blast)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.log, container, false)
        logLayout = view.findViewById(R.id.logLayout)
        return view
    }

    public fun addOneTimeEvent(text: String, type: String, time: Long) {
        if (logLayout == null) return
        val t = time + 3600 * 3
        val newView = LayoutInflater.from(this.context).inflate(R.layout.one_time_event, null, false)
        logLayout?.addView(newView)
        newView?.findViewWithTag<TextView>("text")?.text = text
        // newView?.findViewWithTag<TextView>("damage")?.text = "${if(damage >= 0) "+" else "-"}${abs(damage)}HP"
        if(type in IconFromType) newView?.findViewWithTag<ImageView>("image")?.setImageResource(IconFromType[type]!!)
        newView?.findViewWithTag<TextView>("time")?.text =
                format((t % 86400 / 3600).toString())+ ":" +
                format((t % 3600 / 60).toString()) +
                ":" + format((t % 60).toString())
    }

    fun format(s: String): String{
        return if(s.length == 2) s else "0$s"
    }
}