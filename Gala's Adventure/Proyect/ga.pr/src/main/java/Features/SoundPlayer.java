package Features;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;

public class SoundPlayer {
	private Clip clip;
	private boolean looping;

	public void playSound(final String filePath) {
		Thread playbackThread = new Thread(new Runnable() {
			public void run() {
				try {
					clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));
					
					clip.open(inputStream);
					clip.start();

					Thread.sleep(clip.getMicrosecondLength() / 1);

					clip.stop();
					clip.close();
					inputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});

		playbackThread.start();
	}

	public void playSoundLooped(String filePath) {
		try {
			clip = AudioSystem.getClip();
			final AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));

			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			looping = true;

			Thread playbackThread = new Thread(new Runnable() {
				public void run() {
					try {
						while (looping) {
							Thread.sleep(100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					clip.stop();
					clip.close();
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			playbackThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopSound() {
		if (clip != null && clip.isRunning()) {
			if (looping) {
				looping = false;
			} else {
				clip.stop();
			}
		}
	}
}
