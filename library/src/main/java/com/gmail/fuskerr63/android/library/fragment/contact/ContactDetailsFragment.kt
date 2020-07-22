package com.gmail.fuskerr63.android.library.fragment.contact

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.gmail.fuskerr63.android.library.delegate.contact.ContactDetailsDelegate
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter
import com.gmail.fuskerr63.android.library.view.ContactDetailsView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.library.R
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class ContactDetailsFragment : MvpAppCompatFragment(), ContactDetailsView {
    private lateinit var clickListener: OnMenuItemClickDetails
    private lateinit var contactDetailsDelegate: ContactDetailsDelegate

    private lateinit var name: String
    private lateinit var cancelString: String
    private lateinit var sendString: String

    @InjectPresenter
    lateinit var detailsPresenter: ContactDetailsPresenter

    @Inject
    lateinit var detailsPresenterProvider: Provider<ContactDetailsPresenter>

    @ProvidePresenter
    fun providePresenter() : ContactDetailsPresenter = detailsPresenterProvider.get()

    override fun updateDetails(contact: Contact?) {
        if (contact != null) {
            name = contact.contactInfo.name
            contactDetailsDelegate.showDetails(contact)
            if (view != null
                    && contact.birthday.get(Calendar.YEAR) != 1) {
                val button = view?.findViewById<Button>(R.id.birthday_button)
                button?.visibility = View.VISIBLE
                button?.setOnClickListener { _ -> detailsPresenter.onClickBirthday(
                        contact,
                        cancelString,
                        sendString
                ) }
            }
        }

    }

    override fun loadingStatus(show: Boolean) {
        val status = if (show) View.VISIBLE else View.GONE
        view?.findViewById<ProgressBar>(R.id.progress_bar_details)?.visibility = status
    }

    override fun setTextButton(text: String?) {
        view?.findViewById<Button>(R.id.birthday_button)?.text = text
    }

    override fun showMessageToast(text: CharSequence?) =
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()

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
            clickListener.onMenuItemClickDetails(arguments!!.getInt("ID"), name)
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
        }
        cancelString = context.getString(R.string.cancel_notification)
        sendString = context.getString(R.string.send_notification)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)
        val title = activity?.findViewById<TextView>(R.id.title)
        title?.setText(R.string.contact_details_title)
        contactDetailsDelegate = ContactDetailsDelegate(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsPresenter.showDetails(arguments!!.getInt("ID"), cancelString, sendString)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) : ContactDetailsFragment {
            val contactDetailsFragment = ContactDetailsFragment()
            val bundle = Bundle()
            bundle.putInt("ID", id)
            contactDetailsFragment.arguments = bundle
            return contactDetailsFragment
        }
    }
}