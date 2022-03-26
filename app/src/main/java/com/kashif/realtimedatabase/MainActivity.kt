package com.kashif.realtimedatabase

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kashif.realtimedatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<DatabaseViewModel>()
    private val notesAdapter = NotesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.getNoteItemsFromDatabase()
        setContentView(binding.root)
        setUpViews()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.status.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        }
        viewModel.notesList.observe(this){
            notesAdapter.submitList(it)
        }
    }

    private fun setUpViews() {
        binding.apply {
            btnAddNoteItem.setOnClickListener {
                val item = NoteItem(
                    itemId = System.currentTimeMillis(),
                    itemTitle = etTitle.text.toString(),
                    itemBody = etBody.text.toString()
                )
                viewModel.addNoteItemToDatabase(item)
                hideKeyboard(it)
                etTitle.text.clear()
                etBody.text.clear()
            }
            rvNotes.adapter = notesAdapter
        }
        notesAdapter.onDeleteClickListener {
            viewModel.deleteNoteItem(it)
        }
        notesAdapter.onLongClickListener {
            viewModel.updateNoteItem(it)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(INPUT_METHOD_SERVICE)
                    as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
    }

}