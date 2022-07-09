package com.example.android.mapviewer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mapviewer.data.Attachment


/**
 * A simple [Fragment] subclass.
 * Use the [AttachmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttachmentFragment : Fragment() {
    lateinit var thiscontext: Context
    //private lateinit var viewModel:LogInViewModel
    //  lateinit var shoes: List<Shoe>

    // private lateinit var viewModel: ShoeListViewModel
    //private lateinit var viewModelFactory: ShoeListViewModelFactory
    private lateinit var viewOfLayout: View

    // private val sharedviewModel: LogInViewModel by activityViewModels()

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    /* private fun adapterOnClick(shoe: Attribute) {
         // val intent = Intent(thiscontext, ShoeDetailFragment()::class.java)
         // intent.putExtra(SHOE_ID, shoe.id)
         // startActivity(intent)
         this.findNavController().navigate(AttributeFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment2(shoe.id))
         // findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment(shoe.id))
     }*/

    private val attachmentListViewModel by viewModels<AttachmentListViewModel> {
        AttachmentListViewModelFactory(thiscontext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // shoes = shoeListViewModel.shoesLiveData.value!!




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (container != null) {
            thiscontext = container.getContext()
        }
        viewOfLayout = inflater.inflate(R.layout.fragment_attribute, container, false)

        // viewModel = sharedviewModel

        val _attachmentAdapter = AttachmentAdapter{ }//AttributeAdapter{ attribute -> adapterOnClick(attribute)}
        val recyclerView: RecyclerView = viewOfLayout.findViewById(R.id.recycler_view)//findViewById(R.id.recycler_view)
        recyclerView.adapter = _attachmentAdapter
        attachmentListViewModel.attachmentsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                _attachmentAdapter.submitList(it as MutableList<Attachment>)
                //headerAdapter.updateFlowerCount(it.size)
            }
        })
        var attributelist = attachmentListViewModel.attachmentsLiveData.value


        // val minObject: Shoe? = shoeslist?.minByOrNull{ it.id }
        /*   val maxObject: Attribute? = attributelist?.maxByOrNull{ it.name }
           var maxId:Long = -1
           if(maxObject != null)
               maxId = maxObject?.name
           setHasOptionsMenu(true)

   */
        // val fab:View = viewOfLayout.findViewById(R.id.fab)
        // fab.setOnClickListener{v:View -> v.findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment2(maxId.plus(1)))}

        return viewOfLayout
    }

    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         super.onCreateOptionsMenu(menu, inflater)
         inflater.inflate(R.menu.overflow_menu,menu)
     }*/
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.logOut()
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }*/

}