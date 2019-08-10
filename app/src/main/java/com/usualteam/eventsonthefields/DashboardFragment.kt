package com.usualteam.eventsonthefields

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ClassCastException


class DashboardFragment : Fragment() {

    interface DashboardInteractionListener

    var mListener: DashboardInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            mListener = context as DashboardInteractionListener
        } catch (e: ClassCastException){
            throw ClassCastException(context.toString() + " должен реализовывать интерфейс DashboardInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard, container, false)
    }

}