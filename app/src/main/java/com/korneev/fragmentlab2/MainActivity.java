package com.korneev.fragmentlab2;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUrl;
    private TextView textViewStatus;
    private VideoView videoView;
    private MediaPlayer audioPlayer;
    private boolean isVideoMode = false;
    private Uri currentMediaUri = null;

    // Лаунчер для вибору файлів
    private final ActivityResultLauncher<Intent> pickMediaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    currentMediaUri = result.getData().getData();
                    prepareMedia();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUrl = findViewById(R.id.edit_text_url);
        textViewStatus = findViewById(R.id.text_view_status);
        videoView = findViewById(R.id.video_view);

        Button btnPlayUrl = findViewById(R.id.button_play_url);
        Button btnPickAudio = findViewById(R.id.button_pick_audio);
        Button btnPickVideo = findViewById(R.id.button_pick_video);
        Button btnPlay = findViewById(R.id.button_play);
        Button btnPause = findViewById(R.id.button_pause);
        Button btnStop = findViewById(R.id.button_stop);

        // Обробка кнопок вибору файлів
        btnPickAudio.setOnClickListener(v -> pickFile("audio/*"));
        btnPickVideo.setOnClickListener(v -> pickFile("video/*"));

        // Відтворення з Інтернету
        btnPlayUrl.setOnClickListener(v -> {
            String url = editTextUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                currentMediaUri = Uri.parse(url);
                // Проста перевірка за розширенням (в реальності краще перевіряти MIME-тип)
                isVideoMode = url.toLowerCase().contains(".mp4") || url.toLowerCase().contains(".webm");
                prepareMedia();
            } else {
                Toast.makeText(this, "Введіть URL!", Toast.LENGTH_SHORT).show();
            }
        });

        // Керування відтворенням
        btnPlay.setOnClickListener(v -> playMedia());
        btnPause.setOnClickListener(v -> pauseMedia());
        btnStop.setOnClickListener(v -> stopMedia());
    }

    private void pickFile(String mimeType) {
        isVideoMode = mimeType.equals("video/*");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        pickMediaLauncher.launch(intent);
    }

    private void prepareMedia() {
        if (currentMediaUri == null) return;
        stopMedia(); // Зупиняємо попереднє відтворення

        if (isVideoMode) {
            videoView.setVisibility(View.VISIBLE);
            textViewStatus.setText("Статус: Відео завантажено");
            videoView.setVideoURI(currentMediaUri);
            videoView.requestFocus();
            // Автозапуск відео
            videoView.setOnPreparedListener(mp -> playMedia());
        } else {
            videoView.setVisibility(View.GONE);
            textViewStatus.setText("Статус: Аудіо завантажено");
            audioPlayer = MediaPlayer.create(this, currentMediaUri);
            if (audioPlayer != null) {
                playMedia();
            } else {
                Toast.makeText(this, "Помилка завантаження аудіо", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playMedia() {
        if (isVideoMode) {
            if (!videoView.isPlaying()) {
                videoView.start();
                textViewStatus.setText("Статус: Відтворення відео");
            }
        } else {
            if (audioPlayer != null && !audioPlayer.isPlaying()) {
                audioPlayer.start();
                textViewStatus.setText("Статус: Відтворення аудіо");
            }
        }
    }

    private void pauseMedia() {
        if (isVideoMode) {
            if (videoView.isPlaying()) {
                videoView.pause();
                textViewStatus.setText("Статус: Пауза (Відео)");
            }
        } else {
            if (audioPlayer != null && audioPlayer.isPlaying()) {
                audioPlayer.pause();
                textViewStatus.setText("Статус: Пауза (Аудіо)");
            }
        }
    }

    private void stopMedia() {
        if (isVideoMode) {
            if (videoView.isPlaying() || videoView.getCurrentPosition() > 0) {
                videoView.stopPlayback();
                videoView.resume(); // Скидання стану VideoView
                textViewStatus.setText("Статус: Зупинено (Відео)");
            }
        } else {
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer.release();
                audioPlayer = null;
                textViewStatus.setText("Статус: Зупинено (Аудіо)");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
        }
    }
}