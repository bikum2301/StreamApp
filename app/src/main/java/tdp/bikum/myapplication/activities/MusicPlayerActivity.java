package tdp.bikum.myapplication.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.fragments.AlbumsFragment;
import tdp.bikum.myapplication.fragments.SongsFragment;

public class MusicPlayerActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_MEDIA_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_songs) {
                selectedFragment = new SongsFragment();
            } else if (item.getItemId() == R.id.navigation_albums) {
                selectedFragment = new AlbumsFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });

        // Kiểm tra quyền và hiển thị fragment mặc định
        checkPermissionAndSetup();
    }

    private void checkPermissionAndSetup() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_CODE_READ_MEDIA_AUDIO);
        } else {
            setupFragments();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_MEDIA_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupFragments();
            } else {
                Toast.makeText(this, "Quyền truy cập audio bị từ chối", Toast.LENGTH_SHORT).show();
                finish(); // Đóng activity nếu không được cấp quyền
            }
        }
    }

    private void setupFragments() {
        // Hiển thị SongsFragment mặc định
        replaceFragment(new SongsFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}