package com.yashk9.noterr.view.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yashk9.noterr.R
import com.yashk9.noterr.databinding.FragmentHomeBinding
import com.yashk9.noterr.model.Note
import com.yashk9.noterr.utils.base.BaseFragment
import com.yashk9.noterr.utils.hide
import com.yashk9.noterr.utils.show
import com.yashk9.noterr.utils.viewState.UiState
import com.yashk9.noterr.view.adapters.NoteAdapter
import com.yashk9.noterr.view.viewModel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val TAG = "HomeFragment"
    private val viewModel: NoteViewModel by activityViewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRecycler()
        initSwipe()
        initData()
        observeNotes()
    }

    private fun initViews() {
        binding?.apply {
            addNote.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(null)
                findNavController().navigate(action)
            }
            noteRecycler.hide()
            message.show()
            message.text = getString(R.string.loadingText)
        }
    }

    private fun initRecycler() {
        adapter = NoteAdapter()
        binding?.noteRecycler?.adapter = adapter

        adapter.setOnItemClickListener { note ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(note)
            findNavController().navigate(action)
        }
    }

    private fun initSwipe() {
        val itemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.absoluteAdapterPosition
                val note = adapter.differ.currentList[pos]
                viewModel.deleteNote(note)
            }
        }

        ItemTouchHelper(itemTouchCallback).apply {
            attachToRecyclerView(binding?.noteRecycler)
        }
    }

    private fun initData() {
        lifecycleScope.launchWhenStarted {
            viewModel.getQueryNotes()
        }
    }

    private fun observeNotes() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { value ->
                when (value) {
                    is UiState.Error -> Log.d(TAG, "observeNotes: Error ${value.message}") //Show Error Message
                    is UiState.Loading -> Log.d(TAG, "observeNotes: Loading")
                    is UiState.Empty -> emptyData()//Show Progress bar
                    is UiState.Success -> loadData(value.data)
                }
            }
        }
    }

    private fun emptyData(){
        binding?.apply {
            noteRecycler.hide()
            message.show()
            message.text = getString(R.string.emptyNote)
        }
    }

    private fun loadData(data: List<Note>) {
        binding?.apply{
            message.hide()
            noteRecycler.show()
            adapter.differ.submitList(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true

        Log.d(TAG, "onCreateOptionsMenu: ${viewModel.queryText.value}")
        val query = viewModel.queryText.value
        if(query.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(query, false)
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.queryText.value = newText.orEmpty()
                return true
            }
        })

        lifecycleScope.launchWhenStarted {
            val isDarkModeEnabled = viewModel.uiMode.first()
            val uiToggle = menu.findItem(R.id.darkMode)
            uiToggle.isChecked = isDarkModeEnabled
            setUiMode(isDarkModeEnabled, uiToggle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.darkMode ->{
                item.isChecked = !item.isChecked
                setUiMode(item.isChecked, item)
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    private fun setUiMode(darkModeEnabled: Boolean, uiToggle: MenuItem) {
        if(darkModeEnabled){
            viewModel.setUiMode(true)
            uiToggle.setIcon(R.drawable.ic_light_mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            viewModel.setUiMode(false)
            uiToggle.setIcon(R.drawable.ic_night_mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}