package fastcampus.aop.part2.permission_practice

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val photoGalleryButton: Button by lazy {
        findViewById(R.id.photoGalleryButton)
    }

    private val photoViewButton: Button by lazy {
        findViewById(R.id.photoViewButton)
    }

    private val imageUriList: MutableList<Uri> = mutableListOf()

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById<ImageView>(R.id.ImageView11))
            add(findViewById<ImageView>(R.id.ImageView12))
            add(findViewById<ImageView>(R.id.ImageView13))
            add(findViewById<ImageView>(R.id.ImageView21))
            add(findViewById<ImageView>(R.id.ImageView22))
            add(findViewById<ImageView>(R.id.ImageView23))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPhotoGalleryButton()
        initphotoViewButton()
    }

    private fun initPhotoGalleryButton() {
        photoGalleryButton.setOnClickListener {
            when {
                // 특정 권한이 있는지 확인한다.
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 포토 갤러리 실행
                    navigatePhotos()
                }
                // 특정 권한에 대해 명시적으로 거부시 true, 처음 보거나 다시 묻지 않음 선택하고 권한을 허용한 경우 false
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    initializeRequestPopUp()

                }
                else -> {
                    // 권한 요청하기
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000
                    )
                }
            }
        }
    }

    private fun initphotoViewButton() {
        photoViewButton.setOnClickListener {
            val intent = Intent(this, PhotoFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index", uri.toString())
            }
            intent.putExtra("imageUriSize", imageUriList.size)

            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_LONG)

                }
            }
        }

    }

    private fun navigatePhotos() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)  // 앨범을 열 때 사용하는 인텐트
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    private fun initializeRequestPopUp() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            2000 -> {
                var selectedImage: Uri? = data?.data
                if (selectedImage != null) {

                    if (imageUriList.size == 6) {
                        Toast.makeText(this, "이미지가 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
                    }

                    imageUriList.add(selectedImage)
                    imageViewList[imageUriList.size - 1].setImageURI(selectedImage)
                } else {
                    Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }


}