package com.example.fundonote

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.GridView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.databinding.FragmentSaveNoteBinding
import com.example.fundonote.view.MyAdapter
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.view.CreateNote
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FieldValue.delete
import de.hdodenhof.circleimageview.CircleImageView
import java.nio.file.Files.delete
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private var _binding: FragmentSaveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: MyAdapter
    private lateinit var dataBase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var noteId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveNoteBinding.inflate(inflater, container, false)
        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NoteService())
        ).get(NoteViewModel::class.java)
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)


        recyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()

        noteAdapter = MyAdapter(noteArrayList, requireContext())

        recyclerView.adapter = noteAdapter


        binding.btnfloating.setOnClickListener {
            val fragment = CreateNote()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmaintContainer, fragment)
                ?.commit()
        }

        noteId = arguments?.getString("noteId").toString()

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
//        var menuItem : MenuItem = menu!!.findItem(R.id.gridLayout)
//        var view : View = MenuItemCompat.getActionView(menuItem)
//          view.setOnClickListener(View.OnClickListener {
//              binding.recyclerView.layoutManager =
//                  GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//              Toast.makeText(context, "toggal button clicked ", Toast.LENGTH_LONG).show()
//          })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.account_Profile -> {
                Toast.makeText(context, "profile click", Toast.LENGTH_LONG).show()
            }

            R.id.gridLayout -> {
                binding.recyclerView.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

            }

            R.id.search -> {
                Toast.makeText(context, "search click", Toast.LENGTH_LONG).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}




