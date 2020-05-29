package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

private const val REGULAR_CRIME_ITEM_TYPE = 0
private const val SERIOUS_CRIME_ITEM_TYPE = 1

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    //To assoicate it with CrimeListViewModel (to use functions)
    private val crimeListViewModel : CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    //Save the size of the amount of crimes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        //LayoutManager positions every item and defines how scrolling works (ex. grid or line)

        updateUI()

        return view
    }

    //Connects the Adapter to RecyclerView to display UI
    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    /*
    Abstract class to have CrimeHolder and SeriousCrimeHolder has its children to use the same bind
    method. Similar bind methods but different executions.
    Necessary in order to run in the adapter depending on the crime.
     */
    private abstract inner class AbstractCrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(crime: Crime)
    }
    /*
    CrimeHolder stores a reference to an item's view (sometimes references a specific widgets
    within that vieww.
    */
    private inner class CrimeHolder(view: View)
        : AbstractCrimeHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        //stores the title and the date into the title and date widgets (changes to current crime)
        override fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if(crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        //Makes the holder of the crime (view) clickable in order to display the toast
        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class SeriousCrimeHolder(view: View) : AbstractCrimeHolder(view) {

        private lateinit var crime: Crime

        val contactPoliceButton: Button = itemView.findViewById(R.id.contact_police_button)

        init {
            contactPoliceButton.setOnClickListener {
                val callingMessage = getString(R.string.contact_police_message, crime.title)
                Toast.makeText(context, callingMessage, Toast.LENGTH_SHORT).show()
            }
        }

        override fun bind(crime: Crime) {
            this.crime = crime
        }
    }
    private inner class CrimeAdapter (var crimes: List<Crime>) : RecyclerView.Adapter<AbstractCrimeHolder>() {

        //Responsible for creating the view holder (CrimeHolder), no data is put into CrimeHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractCrimeHolder {
            Log.d(TAG, "Creating a view holder")
            if (viewType == SERIOUS_CRIME_ITEM_TYPE) {
                val view = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false)
                return SeriousCrimeHolder(view)
            } else {
                val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                return CrimeHolder(view)
            }
        }

        //Says how many items we have in the list
        override fun getItemCount() = crimes.size

        //Seting the data on the ViewHolder (CrimeHolder), sets data into CrimeHolder
        override fun onBindViewHolder(holder: AbstractCrimeHolder, position: Int) {
            Log.d(TAG, "Binding a view holder")
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemViewType(position: Int) =
            if (crimes[position].requiresPolice) { SERIOUS_CRIME_ITEM_TYPE } else{ REGULAR_CRIME_ITEM_TYPE }
    }


    //to call to get an instance of your fragment
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}