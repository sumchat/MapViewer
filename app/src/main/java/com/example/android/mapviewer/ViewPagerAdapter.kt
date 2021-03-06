import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.mapviewer.MapFragment

//class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
//    FragmentStatePagerAdapter(supportFragmentManager) {
private const val ARG_OBJECT = "object"

class ViewPagerAdapter(fragmentActivity: FragmentActivity, total:Int):FragmentStateAdapter(fragmentActivity){
    // declare arrayList to contain fragments and its title
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()


     fun getItem(position: Int): Fragment {
        // return a particular fragment page
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        // return the number of tabs
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = getItem(position)
       /* fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }*/
        return fragment
    }

    fun getPageTitle(position: Int): CharSequence{
        // return title of the tab
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        // add each fragment and its title to the array list
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}