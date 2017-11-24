package com.zhang.shaon.nerdlauncher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

class NerdLauncherActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return NerdLauncherFragment.newInstance()
    }

}
