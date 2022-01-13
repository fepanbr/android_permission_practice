package fastcampus.aop.part2.permission_practice

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val photoGalleryButton: Button by lazy {
        findViewById(R.id.photoGalleryButton)
    }

    private val photoViewButton: Button by lazy {
        findViewById(R.id.photoViewButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPhotoGalleryButton()
    }

    private fun initPhotoGalleryButton() {
        photoGalleryButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    // 포토 갤러리 실행
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    initializeRequestPopUp()

                } else -> {
                    // 권한 요청하기
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
            }
        }
    }

    private fun initializeRequestPopUp() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .create().show()
    }


}