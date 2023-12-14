package com.himatifunpad.imazine.ui.screen.settings

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.data.local.APP_THEME_DARK
import com.himatifunpad.imazine.core.data.local.APP_THEME_LIGHT
import com.himatifunpad.imazine.core.data.local.APP_THEME_SYSTEM
import com.himatifunpad.imazine.core.data.local.Keys
import com.himatifunpad.imazine.core.di.entrypoint.PrefsEntryPoint
import com.himatifunpad.imazine.databinding.FragmentSettingsBinding
import com.himatifunpad.imazine.ui.ext.dsl.defaultValue
import com.himatifunpad.imazine.ui.ext.dsl.intListPreference
import com.himatifunpad.imazine.ui.ext.dsl.onChange
import com.himatifunpad.imazine.ui.ext.dsl.onClick
import com.himatifunpad.imazine.ui.ext.dsl.preference
import com.himatifunpad.imazine.ui.ext.dsl.switchPreference
import com.himatifunpad.imazine.ui.ext.dsl.titleRes
import com.himatifunpad.imazine.ui.ext.toggleAppTheme
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.util.base.BaseFragment
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
  private val binding by viewBinding<FragmentSettingsBinding>()

  private val container get() = binding.container
  private val toolbar get() = binding.toolbar

  override fun setupView() {
    toolbar.title = getString(R.string.settings)
    toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    childFragmentManager.commit {
      replace(container.id, SettingsContainer())
    }
  }

  class SettingsContainer : PreferenceFragmentCompat() {
    private val mActivity get() = requireActivity()
    private val prefs
      get() = EntryPointAccessors.fromApplication(
        requireContext(),
        PrefsEntryPoint::class.java
      ).prefs()

    override fun onCreatePreferences(
      savedInstanceState: Bundle?,
      rootKey: String?
    ) {
      val screen = preferenceManager.createPreferenceScreen(mActivity)
      preferenceScreen = screen
      setupPreferenceScreen(screen)
    }

    private fun setupPreferenceScreen(screen: PreferenceScreen) = with(screen) {
      setTitle(R.string.settings)

      // app theme
      intListPreference(activity) {
        key = Keys.APP_THEME
        titleRes = R.string.prefs_app_theme
        entriesRes = listOf(
          R.string.prefs_app_theme_light,
          R.string.prefs_app_theme_dark,
          R.string.prefs_app_theme_system
        )
        entryValues = listOf(
          APP_THEME_LIGHT,
          APP_THEME_DARK,
          APP_THEME_SYSTEM
        )
        defaultValue = APP_THEME_SYSTEM

        onChange {
          toggleAppTheme(it as Int)
          mActivity.recreate()
          true
        }
      }

      // notify new post
      switchPreference {
        key = Keys.NOTIFY_NEW_POST
        titleRes = R.string.prefs_notify_new_post
        summaryOn = getString(R.string.prefs_notify_new_post_on)
        summaryOff = getString(R.string.prefs_notify_new_post_off)
        defaultValue = true
        onChange { value ->
          if (value as Boolean) {
            Firebase.messaging.subscribeToTopic("newpost")
          } else {
            Firebase.messaging.unsubscribeFromTopic("newpost")
          }
          true
        }
      }

      // logout
      preference {
        titleRes = R.string.prefs_logout
        onClick {
          launchLogoutDialog()
        }
      }
    }

    private fun launchLogoutDialog() {
      MaterialDialog(requireContext()).show {
        title(res = R.string.dialog_logout)
        message(res = R.string.dialog_message_logout)

        positiveButton(R.string.dialog_yes) { dialog ->
          onLogout()
          dialog.dismiss()
        }
        negativeButton(R.string.dialog_no)
      }
    }

    private fun onLogout() {
      viewLifecycleOwner.lifecycleScope.launch {
        Firebase.messaging.unsubscribeFromTopic("newpost")
        prefs.clearUser()
        findNavController().navigate(
          SettingsFragmentDirections.actionSettingsToAuth()
        )
      }
    }
  }
}
