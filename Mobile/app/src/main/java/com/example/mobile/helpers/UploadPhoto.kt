import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class UploadPhoto(private val fragment: Fragment) {
    private val getContent = fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the selected image URI
    }

    init {
        // Call registerForActivityResult in the constructor or init block
        registerForActivityResult()
    }

    private fun registerForActivityResult() {
        fragment.activity?.let { activity ->
            val getContent = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                // Handle the selected image URI
            }
        }
    }

    fun selectPhotoFromGallery() {
        getContent.launch("image/*")
    }
}
