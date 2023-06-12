package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.DTOs.ManyToManyDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.mobile.adapters.UserCardAdapter
import com.yuyakaido.android.cardstackview.*
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "ARG_USER"

/**
 * A simple [Fragment] subclass.
 * Use the [Explore.newInstance] factory method to
 * create an instance of this fragment.
 */
class Explore : Fragment(), CardStackListener {
    // TODO: Rename and change types of parameters
    private var paramUser: UserToBeStoredDTO? = null
    private var userArray = ArrayList<UserToBeStoredDTO>()
    private val apiCall = ApiCall()
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var card_stack_view: CardStackView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramUser = it.getSerializable(ARG_USER) as UserToBeStoredDTO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_stack_view = view.findViewById(R.id.card_stack_view)
        layoutManager = CardStackLayoutManager(context, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator { it }
        }
        val layoutManager = CardStackLayoutManager(context, this).apply {
            setDirections(Direction.FREEDOM)
            setSwipeThreshold(0.3f)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
        }
        card_stack_view.layoutManager = layoutManager
        card_stack_view.adapter = UserCardAdapter(requireContext(), userArray)

        paramUser?.username?.let { username ->
            apiCall.getFeedAsync(username) { result, error ->
                if (error != null) {
                    println("Error: ${error.message}")
                } else {
                    result?.let {
                        userArray = Gson().fromJson(it, object : TypeToken<List<UserToBeStoredDTO>>() {}.type)
                        println("User array: $userArray")
                        activity?.runOnUiThread {
                            (card_stack_view.adapter as UserCardAdapter).updateData(userArray)
                            println("Updated card stack view adapter: $userArray")
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Explore.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: UserToBeStoredDTO) =
            Explore().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user as Serializable)
                }
            }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        val swipedUser = userArray[layoutManager.topPosition - 1]

        when (direction) {
            Direction.Right -> {
                val dto = ManyToManyDTO(paramUser!!.id, swipedUser.id)
                ApiCall().connectAsync(dto) { result, exception ->
                    if (exception != null) {
                        println("Error connecting users: ${exception.message}")
                    }
                }
            }
            Direction.Left -> {
                val dto = ManyToManyDTO(paramUser!!.id, swipedUser.id)
                ApiCall().rejectAsync(dto) { result, exception ->
                    if (exception != null) {
                        println("Error rejecting users: ${exception.message}")
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }
}