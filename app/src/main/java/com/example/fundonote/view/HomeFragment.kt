package com.example.fundonote

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.GridView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundonote.database.DBHelper
import com.example.fundonote.databinding.FragmentSaveNoteBinding
import com.example.fundonote.view.MyAdapter
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.view.CreateNote
import com.example.fundonote.view.ProfileFragmet
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FieldValue.delete
import de.hdodenhof.circleimageview.CircleImageView
import java.nio.file.Files.delete
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private var _binding: FragmentSaveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
   // private lateinit var newNoteArrayList: ArrayList<Notes>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: MyAdapter
    private lateinit var dataBase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var noteId: String
    var gridFlag: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveNoteBinding.inflate(inflater, container, false)
        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NoteService(DBHelper(requireContext())))
        ).get(NoteViewModel::class.java)
        setHasOptionsMenu(true)
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)


        recyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        //   newNoteArrayList = arrayListOf<Notes>()

        // newNoteArrayList.addAll(noteArrayList)

        noteAdapter = MyAdapter(noteArrayList, requireContext())

        recyclerView.adapter = noteAdapter


        binding.btnfloating.setOnClickListener {
            val fragment = CreateNote()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmaintContainer, fragment)
                ?.commit()
        }


        var bundle = arguments?.getString("NoteId").toString()
        if (bundle != null) {
            noteId = bundle
        }
        removeNote()
        readNote()
        //   getNote()


        return binding.root
    }

    fun readNote() {
        noteViewModel.getNote()
        noteViewModel.getNotes.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                Log.d("SaveNoteFragment", it.noteList.size.toString())
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                recyclerView.adapter = MyAdapter(it.noteList, requireContext())
                noteArrayList = it.noteList
            }
        })
    }

    private fun removeNote() {
        noteViewModel.removeNote(noteId)
        noteViewModel.deleteNotes.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_view, menu)
        var menuItem: MenuItem = menu!!.findItem(R.id.account_Profile)
        var view: View = MenuItemCompat.getActionView(menuItem)
        var profileImage: CircleImageView = view.findViewById(R.id.toolBar_profile_image)

        Glide
            .with(this)
            .load("https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
            .into(profileImage)

        profileImage.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "profile click", Toast.LENGTH_LONG).show()
            var dialog = ProfileFragmet()
            dialog.show(childFragmentManager, "customDialog")
        })

        // search item
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        // searchView.queryHint
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.account_Profile -> {
                Toast.makeText(context, "profile click", Toast.LENGTH_LONG).show()
            }

            R.id.gridLayout -> {
                if (gridFlag == 0) {
                    switchIcon(item)
                    binding.recyclerView.layoutManager =
                        GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    gridFlag = 1
                } else {
                    switchIcon(item)
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    gridFlag = 0
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun switchIcon(item: MenuItem) {
        if (gridFlag == 0) {
            item.setIcon(resources.getDrawable(R.drawable.ic_baseline_grid_on_24))
            gridFlag = 1
        } else {
            item.setIcon(resources.getDrawable(R.drawable.ic_baseline_horizontal_split_24))
            gridFlag = 0
        }
    }

    fun filter(newText: String?) {
        var filteredNotes: ArrayList<Notes> = arrayListOf()
        Log.d("HomeFragment","notearryList = ${noteArrayList.size}")

        for (notes in noteArrayList) {

                if (notes.noteTitle.toLowerCase().contains(newText!!.toLowerCase()) || notes.noteDescription.toLowerCase().contains(newText!!.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredNotes.add(notes)
                    Log.d("HomeFragment","filteredList = ${filteredNotes.size}")
                }
            }

        if (filteredNotes.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(context, "No Notes Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
           noteAdapter.filterNotes(filteredNotes)

        }
    }
}






