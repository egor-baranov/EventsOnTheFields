package com.usualteam.eventsonthefields

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import java.lang.ClassCastException


class DashboardFragment : Fragment() {

    interface DashboardInteractionListener

    var mListener: DashboardInteractionListener? = null
    var dashboardLayout: LinearLayout? = null
    var properties: HashMap<String, View?> = hashMapOf("radiation" to null)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as DashboardInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " должен реализовывать интерфейс DashboardInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dashboard, container, false)
        dashboardLayout = view.findViewById<LinearLayout>(R.id.dashbord_layout)
        return view
    }

    public fun addProperty(id: String, progress: Int) {
        if (dashboardLayout == null || id !in properties) return
        if (properties[id] == null) {
            val newView = LayoutInflater.from(this.context).inflate(R.layout.property, null, false)
            dashboardLayout?.addView(newView)
            newView.findViewWithTag<ProgressBar>("progressBar").progress = progress
            properties[id]= newView
        } else {
            properties[id]?.findViewWithTag<ProgressBar>("progressBar")?.progress = progress
        }
    }
}