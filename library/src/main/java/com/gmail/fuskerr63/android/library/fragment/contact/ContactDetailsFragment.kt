package com.gmail.fuskerr63.android.library.fragment.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gmail.fuskerr63.android.library.delegate.contact.ContactDetailsDelegate
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentContainer
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentFactory
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.android.library.viewmodel.factory.ContactViewModelFactory
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.interactor.NotificationStatus
import com.gmail.fuskerr63.library.R
import kotlinx.android.synthetic.main.fragment_contact_details.*
import kotlinx.coroutines.FlowPreview
import java.util.Calendar
import javax.inject.Inject

class ContactDetailsFragment : Fragment() {
    private lateinit var clickListener: OnMenuItemClickDetails
    private lateinit var contactDetailsDelegate: ContactDetailsDelegate
    private lateinit var contactViewModel: ContactViewModel

    private var name: String? = null

    lateinit var viewModel: ContactViewModel

    fun updateDetails(contact: Contact?) {
        if (contact != null) {
            name = contact.contactInfo.name
            contactDetailsDelegate.showDetails(contact)
            if (view != null && contact.birthday.get(Calendar.YEAR) != 1) {
                with(birthday_button) {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        contactViewModel.onClickBirthday(contact)
                    }
                }
            }
        }
    }

    fun loadingStatus(show: Boolean) {
        val status = if (show) View.VISIBLE else View.GONE
        progress_bar_details.visibility = status
    }

    fun setTextButton(status: NotificationStatus?) {
        if (status != null) {
            birthday_button.text =
                if (status.isAlarmUp) getString(R.string.cancel_notification)
                else getString(R.string.send_notification)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.app_bar_map_details) {
            clickListener.onMenuItemClickDetails(arguments?.getInt("ID") ?: -1, name)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMenuItemClickDetails) {
            clickListener = context
        }
        val app = activity?.application
        if (app is ContactApplicationContainer) {
            val appContainer = app.appComponent
            val contactComponent = appContainer.plusContactComponent()
            contactComponent.inject(this)
            viewModel = appContainer.plusViewModelComponentFactory().create(this, 1).getViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)
        activity?.setTitle(R.string.contact_list_title)
        contactDetailsDelegate = ContactDetailsDelegate(view)
        return view
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel.getContact(requireArguments().getInt("ID"))
            .observe(
                viewLifecycleOwner,
                Observer<Contact> { contact ->
                    updateDetails(contact)
                }
            )
        contactViewModel.getBirthdayStatus().observe(
            viewLifecycleOwner,
            Observer<NotificationStatus> { status ->
                setTextButton(status)
            }
        )
        contactViewModel.getLoadingStatus().observe(
            viewLifecycleOwner,
            Observer<Boolean> { status ->
                loadingStatus(status)
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(
                "ID" to id
            )
        }
    }
}
