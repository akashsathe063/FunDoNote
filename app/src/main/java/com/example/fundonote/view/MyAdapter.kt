package com.example.fundonote.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.fundonote.R
import com.example.fundonote.R.*
import com.example.fundonote.HomeFragment
import com.example.fundonote.model.Notes

class MyAdapter(var noteList: ArrayList<Notes>, var context: Context) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    var allNote = mutableListOf<Notes>().apply {
        Log.d("MyAdapter", noteList.size.toString())
        addAll(noteList)
        notifyDataSetChanged()
    }
  //  lateinit var filteredNoteArrayList:ArrayList<Notes>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(layout.note_item_view, parent, false)
        return MyViewHolder(view)


    }

//    fun filterNotes(filterNotes: ArrayList<Notes>) {
//       this.noteList = filterNotes
//        notifyDataSetChanged()
//    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var note: Notes = allNote[position]


        holder.noteTitle.text = note.noteTitle
        holder.noteDescription.text = note.noteDescription
        holder.mMenues.setOnClickListener {
            val popupMenu = PopupMenu(it.context, holder.mMenues)

            popupMenu.menuInflater.inflate(R.menu.cardview_list_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    id.editTv -> {
                        val fragment = UpdateNoteFragment()
                        val transaction =
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        var bundle = Bundle()
                        bundle.putString("NoteId", note.noteId)
                        fragment.arguments = bundle
                        transaction.replace(R.id.fragmaintContainer, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                        Log.d("MyAdapter", "${note.noteId}")


                    }

                    id.Delete -> {

                        //   noteViewModel.deleteNotes.observe()
                        val fragment = HomeFragment()
                        val transaction =
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        var bundle = Bundle()
                        bundle.putString("NoteId", note.noteId)
                        fragment.arguments = bundle
                        transaction.replace(R.id.fragmaintContainer, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    }

                }
                true
            }

            popupMenu.show()
            val popUp = PopupMenu::class.java.getDeclaredField("mPopup")
            popUp.isAccessible = true
            val menu = popUp.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun getItemCount(): Int {
        return allNote.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView
        var noteDescription: TextView
        var mMenues: ImageView


        init {
            noteTitle = itemView.findViewById(id.tvNoteTitle)
            noteDescription = itemView.findViewById(id.NoteDescription)
            mMenues = itemView.findViewById(id.mMenus)
       //     filteredNoteArrayList = noteList

        }


    }

//    override fun getFilter(): Filter {
//        var filter = object : Filter(){
//            override fun performFiltering(p0: CharSequence?): FilterResults {
//                var filterResults = FilterResults()
//                if(p0 == null || p0.isEmpty()){
//                    filterResults.values = filteredNoteArrayList
//                    filterResults.count = filteredNoteArrayList.size
//
//                }
//                else{
//                    var searchChar = p0.toString().toLowerCase()
//                    var filterResults = ArrayList<Notes>()
//                    for (notes in filteredNoteArrayList){
//                        if(notes.noteTitle)
//                    }
//                }
//            }
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//                TODO("Not yet implemented")
//            }
//
//        }
//        return  filter
//    }

fun filtering(newFilteredList: ArrayList<Notes>) {
noteList = newFilteredList
    Log.d("MyAdapter","NoteList = $noteList")
    notifyDataSetChanged()
}
}