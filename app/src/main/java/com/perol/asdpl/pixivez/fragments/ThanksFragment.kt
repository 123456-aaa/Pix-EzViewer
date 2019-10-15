package com.perol.asdpl.pixivez.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.dialog.ThanksDialog


class ThanksFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pre_thanks)
        findPreference<PreferenceCategory>("huonaicai")?.isVisible = !BuildConfig.ISGOOGLEPLAY
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "support" -> {

            }
            "thanks"->{
                val thanksDialog = ThanksDialog()
                thanksDialog.show(childFragmentManager)
            }
            "wepay" -> {
                val view = activity!!.layoutInflater.inflate(R.layout.wepayimage, null)
                view.findViewById<ImageView>(R.id.imageview).setImageResource(R.drawable.weixinqr)
                AlertDialog.Builder(activity).setView(view).setPositiveButton(android.R.string.ok){_,_->

                }.show()

            }

        }
        return super.onPreferenceTreeClick(preference)
    }
}