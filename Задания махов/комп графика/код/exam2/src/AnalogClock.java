import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import java.util.Calendar;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class AnalogClock {
    private long window;
    private int width = 600, height = 600;
    private float radius = 0.9f;

    public static void main(String[] args) {
        new AnalogClock().run();
    }

    private void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Cannot initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, "Analog Clock", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Cannot create window");
        }

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            this.width = w;
            this.height = h;
            glViewport(0, 0, w, h);
            setupProjection();
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        setupProjection();
        glClearColor(0, 0, 0, 1);
    }

    private void setupProjection() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        if (width > height) {
            float ratio = (float) width / height;
            glOrtho(-ratio, ratio, -1, 1, -1, 1);
        } else {
            float ratio = (float) height / width;
            glOrtho(-1, 1, -ratio, ratio, -1, 1);
        }

        glMatrixMode(GL_MODELVIEW);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glLoadIdentity();

            drawClockFace();
            drawHands();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void drawClockFace() {
        glColor3f(1, 1, 1);
        glLineWidth(2);

        // Clock circle
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            double angle = Math.toRadians(i);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            glVertex2f(x, y);
        }
        glEnd();

        // Hour markers
        glLineWidth(3);
        for (int hour = 1; hour <= 12; hour++) {
            double angle = Math.toRadians(90 - hour * 30);
            float x1 = (float) (radius * 0.85 * Math.cos(angle));
            float y1 = (float) (radius * 0.85 * Math.sin(angle));
            float x2 = (float) (radius * 0.95 * Math.cos(angle));
            float y2 = (float) (radius * 0.95 * Math.sin(angle));

            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();
        }

        // Minute markers
        glLineWidth(1);
        glColor3f(0.7f, 0.7f, 0.7f);
        for (int minute = 1; minute <= 60; minute++) {
            if (minute % 5 == 0) continue;
            double angle = Math.toRadians(90 - minute * 6);
            float x1 = (float) (radius * 0.90 * Math.cos(angle));
            float y1 = (float) (radius * 0.90 * Math.sin(angle));
            float x2 = (float) (radius * 0.95 * Math.cos(angle));
            float y2 = (float) (radius * 0.95 * Math.sin(angle));

            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();
        }

        // Center point
        glPointSize(5);
        glBegin(GL_POINTS);
        glVertex2f(0, 0);
        glEnd();
    }

    private void drawHands() {
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        if (hours == 0) hours = 12;

        // Hour hand (with minutes influence)
        float hourAngle = hours * 30 + minutes * 0.5f;
        drawHand(hourAngle, radius * 0.5f, 6, 1, 1, 1);

        // Minute hand
        float minuteAngle = minutes * 6 + seconds * 0.1f;
        drawHand(minuteAngle, radius * 0.7f, 4, 1, 1, 1);

        // Second hand
        float secondAngle = seconds * 6;
        drawHand(secondAngle, radius * 0.8f, 2, 1, 0, 0);
    }

    private void drawHand(float angleDeg, float length, float width, float r, float g, float b) {
        glColor3f(r, g, b);
        glLineWidth(width);

        double angleRad = Math.toRadians(90 - angleDeg);
        float x = (float) (length * Math.cos(angleRad));
        float y = (float) (length * Math.sin(angleRad));

        glBegin(GL_LINES);
        glVertex2f(0, 0);
        glVertex2f(x, y);
        glEnd();
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}