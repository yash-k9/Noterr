package com.yashk9.noterr.view.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yashk9.noterr.databinding.FragmentAddNoteBinding
import com.yashk9.noterr.model.Note
import com.yashk9.noterr.utils.base.BaseFragment
import com.yashk9.noterr.utils.Result
import com.yashk9.noterr.view.viewModel.NoteViewModel

class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {

    private val TAG = "AddNoteFragment"
    private val viewModel : NoteViewModel by activityViewModels()
    private val args: AddNoteFragmentArgs by navArgs()
    private var mNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddNoteBinding {
        return FragmentAddNoteBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initViews()
    }

    private fun initData() {
        mNote = args.note
        binding?.apply {
            todoTitle.setText(mNote?.title)
            todoMessage.setText(mNote?.content)
        }
    }


    private fun initViews() {
        binding?.addButton?.setOnClickListener {
            val note = getNoteContent()
            when{
                note.title.isEmpty() -> binding?.titleField?.error = "Title Cannot be Empty"
                note.content.isEmpty() -> binding?.contentField?.error = "Message Cannot be Empty"
                else -> {
                    when(val res = Result.build { viewModel.addNote(note) }){
                        is Result.Error -> Log.d(TAG, "initViews: ${res.exception.message}")
                        is Result.Success -> findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun getNoteContent(): Note = binding.let {
        val title = it?.todoTitle?.text.toString()
        val message = it?.todoMessage?.text.toString()
        val createdAt = System.currentTimeMillis()

        mNote?.let { note ->
            note.title = title
            note.content = message
            note.createdAt = createdAt
            return note
        }

        return Note(title = title, content = message, createdAt = createdAt)
    }

}