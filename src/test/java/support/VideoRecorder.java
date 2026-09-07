package support;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class VideoRecorder {
    private final Path output = Path.of("target", "videos", UUID.randomUUID() + ".mp4");
    private Process process;

    public void start() {
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) return;
        try {
            Files.createDirectories(output.getParent());
            Process probe = new ProcessBuilder("ffmpeg", "-version").redirectErrorStream(true).start();
            if (probe.waitFor() != 0) return;
            process = new ProcessBuilder("ffmpeg", "-y", "-f", "gdigrab", "-framerate", "10", "-i", "desktop", output.toString())
                    .redirectErrorStream(true).start();
        } catch (Exception ignored) {
            process = null;
        }
    }

    public void stop() {
        if (process == null) return;
        process.destroy();
        try { process.waitFor(); } catch (InterruptedException interrupted) { Thread.currentThread().interrupt(); }
    }

    public void attach() {
        try {
            if (Files.exists(output) && Files.size(output) > 0) {
                Allure.addAttachment("Failure video", "video/mp4", new ByteArrayInputStream(Files.readAllBytes(output)), ".mp4");
            }
        } catch (Exception ignored) {
            // Video is optional when ffmpeg is unavailable.
        }
    }
}