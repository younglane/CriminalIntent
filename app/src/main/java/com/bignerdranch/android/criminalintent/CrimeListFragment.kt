package com.bignerdranch.android.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    /*
    Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var addCrimeLayout: LinearLayout
    private lateinit var addCrimeButton: Button
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    //To assoicate it with CrimeListViewModel (to use functions)
    private val crimeListViewModel : CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    //Activity instance hosting the fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    //Tell the fragmentManager that your fragment should receive a call to Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        addCrimeLayout = view.findViewById(R.id.crime_layout) as LinearLayout
        addCrimeButton = view.findViewById(R.id.add_crime) as Button

        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        //LayoutManager positions every item and defines how scrolling works (ex. grid or line)

        crimeRecyclerView.visibility = View.GONE
        addCrimeLayout.visibility = View.GONE

        addCrimeButton.setOnClickListener {
            addCrime()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer {crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
    }

    //Set variable to null, afterward you cannot access the activity
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    //Creates the menu/inflates and responding to the selection of an action item
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    //Respond to MenuItem selection by creating a new crime, saving it to the database, and then
    //notifying the parent activity that the new crime has been selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                addCrime()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addCrime() {
        val crime = Crime()
        crimeListViewModel.addCrime(crime)
        callbacks?.onCrimeSelected(crime.id)
    }

    //Connects the Adapter to RecyclerView to display UI
    private fun updateUI(crimes: List<Crime>) {
        if(crimes.size == 0) {
            crimeRecyclerView.visibility = View.GONE
            addCrimeLayout.visibility = View.VISIBLE
        } else {
            crimeRecyclerView.visibility = View.VISIBLE
            addCrimeLayout.visibility = View.GONE

            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        }
    }


    /*
    CrimeHolder stores a reference to an item's view (sometimes references a specific widgets
    within that vieww.
    */
    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        //stores the title and the date into the title and date widgets (changes to current crime)
        fun bind(crime: Crime) {
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
            callbacks?.onCrimeSelected(crime.id)
        }

    }

    private inner class CrimeAdapter (var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        //Responsible for creating the view holder (CrimeHolder), no data is put into CrimeHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            Log.d(TAG, "Creating a view holder")
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        //Says how many items we have in the list
        override fun getItemCount() = crimes.size

        //Seting the data on the ViewHolder (CrimeHolder), sets data into CrimeHolder
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            Log.d(TAG, "Binding a view holder")
            val crime = crimes[position]
            holder.bind(crime)
        }
    }


    //to call to get an instance of your fragment
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}