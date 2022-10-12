package com.example.fundonote.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.R
import com.example.fundonote.R.*
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory

class MyAdapter(var noteList: ArrayList<Notes>,var context:Context,val listner: OnItemClickListner) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),View.OnClickListener {


    var allNote = mutableListOf<Notes>().apply {
        Log.d("MyAdapter", noteList.size.toString())
        addAll(noteList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout.note_item_view, parent, false)
        return MyViewHolder(view)
    }

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
//                        val transaction =
//                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//                        transaction.replace(id.fragmaintContainer, NoteFragment())
//                        transaction.addToBackStack(null)
//                        transaction.commit()
                        var fragment:NoteFragment
                          Log.d("MyAdapter", "${note.noteId}")

                    }

                    id.Delete -> {

                        //   noteViewModel.deleteNotes.observe()

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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noteTitle: TextView
        var noteDescription: TextView
        var mMenues: ImageView



        init {
            noteTitle = itemView.findViewById(id.tvNoteTitle)
            noteDescription = itemView.findViewById(id.NoteDescription)
            mMenues = itemView.findViewById(id.mMenus)


        }


    }

    interface OnItemClickListner {
        fun onItemClick(noteId:String)

    }

    override fun onClick(view: View?) {
      //  listner.onItemClick(noteId = "${noteList.get(position).noteId.toString()}" )
    }
}