package com.example.fundonote

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundonote.database.DBHelper
import com.example.fundonote.databinding.FragmentSaveNoteBinding
import com.example.fundonote.model.*
import com.example.fundonote.view.MyAdapter
import com.example.fundonote.view.ArchiveNoteFragment
import com.example.fundonote.view.CreateNote
import com.example.fundonote.view.ProfileFragmet
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import com.example.fundonote.viewmodel.UpdateNoteViewModel
import com.example.fundonote.viewmodel.UpdateNoteViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private var _binding: FragmentSaveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    private lateinit var newNoteArrayList: ArrayList<Notes>
    private lateinit var archiveNoteList: ArrayList<Notes>
    private lateinit var tempNoteArrayList: ArrayList<Notes>


    private lateinit var noteViewModel: NoteViewModel
    private lateinit var updateNoteViewModel: UpdateNoteViewModel
    private lateinit var noteId: String
    private lateinit var archiveNoteId:String
    private lateinit var isArchive: String
    private lateinit var archiveNote: ArchiveNoteFragment
    private lateinit var note: Notes

    // private lateinit var fields:Fields
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


        updateNoteViewModel =
            ViewModelProvider(
                this,
                 UpdateNoteViewModelFactory(NoteService(DBHelper(requireContext())))
            ).get(
                UpdateNoteViewModel::class.java
            )
        setHasOptionsMenu(true)
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.setHasFixedSize(true)
        note = Notes()

        noteArrayList = arrayListOf<Notes>()
        newNoteArrayList = arrayListOf<Notes>()
        archiveNoteList = arrayListOf<Notes>()
        tempNoteArrayList = arrayListOf<Notes>()

        binding.btnfloating.setOnClickListener {
            val fragment = CreateNote()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmaintContainer, fragment)
                ?.commit()
        }


        var bundle = arguments?.getString("NoteId").toString()
        if (bundle != null) {
            noteId = bundle
            removeNote()
        }


        var _Bundle = arguments?.getString("Noteid").toString()
        if (_Bundle != null) {
            archiveNoteId = _Bundle
            isArchiveNote()
        }
        //  readNote()
        //   getNote()
        //   getNoteUsingRetrofit()
        return binding.root
    }

    private fun getNoteUsingRetrofit() {

        RetrofitClient.getInstance()?.getMyApi()?.getNotes()
            ?.enqueue(object : Callback<RetrofitRetrive> {
                override fun onResponse(
                    call: Call<RetrofitRetrive>,
                    response: Response<RetrofitRetrive>
                ) {
//                    var note: ArrayList<Notes>? = response.body()
//                    if (response.isSuccessful) {
//
//                        recyclerView.adapter = note?.let { MyAdapter(it, requireContext()) }
//                        Log.d("HomeFragment", response.body().toString())
//                    }
                    var jsonData: RetrofitRetrive? = response.body()
                    var notesArrayList: ArrayList<Notes> = arrayListOf<Notes>()
                    if (jsonData != null) {
                        jsonData.documents.forEach() {
                            var archivDoc = it.fields.isArchive.stringValue
                            var isArchive: Boolean
                            isArchive = (archivDoc == "true")

                            var note: Notes = Notes(
                                it.fields.NoteId.stringValue,
                                it.fields.NoteTitle.stringValue,
                                it.fields.NoteDescription.stringValue,
                                isArchive
                            )

                            notesArrayList.add(note)

                        }
                        recyclerView.adapter = MyAdapter(notesArrayList, requireContext())
                        Log.d("HomeFragment", "@@@$notesArrayList")

                    }
                    Log.d("HomeFragment", "${response.body()}")
                }

                override fun onFailure(call: Call<RetrofitRetrive>, t: Throwable) {
                    Log.d("HomeFragment", "Failure $t")
                }


            }
            )
    }


    fun readNote() {
     //   noteArrayList.clear()
        noteViewModel.getNote()
        noteViewModel.getNotes.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                Log.d("SaveNoteFragment", it.noteList.size.toString())
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                tempNoteArrayList.clear()
                archiveNoteList.clear()
                noteArrayList.clear()
                tempNoteArrayList = it.noteList
                newNoteArrayList.addAll(tempNoteArrayList)

//                newNoteArrayList.forEach {
//                    if (it.isArchive == true) {
//                        archiveNoteList.add(it)
//                        //           archiveNote.readArchiveNote(archiveNoteList)
//                    } else {
//                        noteArrayList.add(it)
//                    }
//                }
                for(note in tempNoteArrayList){
                    if(note.isArchive == true){
                        archiveNoteList.add(note)
                    }
                    else{
                        noteArrayList.add(note)
                    }
                }
                recyclerView.adapter = MyAdapter(noteArrayList, requireContext())
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
                newNoteArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    noteArrayList.forEach {
                        if (it.noteTitle.toLowerCase(Locale.getDefault())
                                .contains(searchText) || it.noteDescription.toLowerCase(Locale.getDefault())
                                .contains(searchText)
                        ) {
                            newNoteArrayList.add(it)
                        }
                    }
                    recyclerView.adapter = MyAdapter(newNoteArrayList, requireContext())
                } else {
                    newNoteArrayList.clear()
                    newNoteArrayList.addAll(noteArrayList)
                    recyclerView.adapter = MyAdapter(noteArrayList, requireContext())

                }
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

    fun isArchiveNote() {
        // val note = Notes(noteId = noteId, noteTitle = " ", noteDescription = " ",isArchive = "1")
        updateNoteViewModel.readSingleNote(archiveNoteId )
        updateNoteViewModel.readSigleNote.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                note = Notes(
                    noteId = it.note.noteId,
                    noteTitle = it.note.noteTitle,
                    noteDescription = it.note.noteDescription,
                    isArchive = true
                )
                updateNoteViewModel.updateNote(archiveNoteId, note)
                updateNoteViewModel.updateNotes.observe(viewLifecycleOwner, Observer {
                    if (it.status) {
                       Toast.makeText(context,"notes  archived",Toast.LENGTH_LONG).show()
                        readNote()

                    }
                    else{
                        Toast.makeText(context,"notes   not archived",Toast.LENGTH_LONG).show()
                    }
                })
            }
        })

    }
}
