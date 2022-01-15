package fastcampus.aop.part2.permission_practice

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {

    private var currentPosition = 0

    private val photoImageList = mutableListOf<Uri>()

    private var timer: Timer? = null

    private val photoImageView: ImageView by lazy {
        findViewById(R.id.photoImageView)
    }

    private val photoBackgroundImageView: ImageView by lazy {
        findViewById(R.id.photoBackgroundImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getUriFromIntent()

        setImageTimer()
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


    private fun getUriFromIntent() {
        val size = intent.getIntExtra("imageUriSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoImageList.add(Uri.parse(it))
            }
        }
    }

    private fun setImageTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                 var current = currentPosition
                var next = if(photoImageList.size <= currentPosition + 1) 0 else currentPosition + 1

                photoBackgroundImageView.setImageURI(photoImageList[current])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoImageList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next

            }
        }
    }


}