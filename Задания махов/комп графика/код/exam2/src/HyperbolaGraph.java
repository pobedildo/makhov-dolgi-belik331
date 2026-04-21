import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HyperbolaGraph {
    private long window;
    private int width = 800, height = 600;

    private static final float X_MIN = -4.0f;
    private static final float X_MAX = 4.0f;
    private static final float Y_MIN = -4.0f;
    private static final float Y_MAX = 4.0f;
    private static final float STEP = 0.5f;

    public static void main(String[] args) {
        new HyperbolaGraph().run();
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

        window = glfwCreateWindow(width, height, "Hyperbola y = 1/x", NULL, NULL);
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

        float windowAspect = (float) width / height;
        float worldWidth = X_MAX - X_MIN;
        float worldHeight = Y_MAX - Y_MIN;
        float worldAspect = worldWidth / worldHeight;

        if (windowAspect > worldAspect) {
            float viewHeight = worldHeight;
            float viewWidth = viewHeight * windowAspect;
            float xCenter = (X_MIN + X_MAX) / 2;
            glOrtho(xCenter - viewWidth/2, xCenter + viewWidth/2, Y_MIN, Y_MAX, -1, 1);
        } else {
            float viewWidth = worldWidth;
            float viewHeight = viewWidth / windowAspect;
            float yCenter = (Y_MIN + Y_MAX) / 2;
            glOrtho(X_MIN, X_MAX, yCenter - viewHeight/2, yCenter + viewHeight/2, -1, 1);
        }

        glMatrixMode(GL_MODELVIEW);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glLoadIdentity();

            drawAxes();
            drawHyperbola();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void drawAxes() {
        glColor3f(1, 1, 1);
        glLineWidth(2);

        // X axis
        glBegin(GL_LINES);
        glVertex2f(X_MIN, 0);
        glVertex2f(X_MAX, 0);
        glEnd();
        drawArrow(X_MAX, 0, 0);

        // Y axis
        glBegin(GL_LINES);
        glVertex2f(0, Y_MIN);
        glVertex2f(0, Y_MAX);
        glEnd();
        drawArrow(0, Y_MAX, 90);

        // Ticks on X
        glLineWidth(1);
        for (float x = X_MIN; x <= X_MAX; x += STEP) {
            if (Math.abs(x) > 0.01f) {
                glBegin(GL_LINES);
                glVertex2f(x, -0.1f);
                glVertex2f(x, 0.1f);
                glEnd();
            }
        }

        // Ticks on Y
        for (float y = Y_MIN; y <= Y_MAX; y += STEP) {
            if (Math.abs(y) > 0.01f) {
                glBegin(GL_LINES);
                glVertex2f(-0.1f, y);
                glVertex2f(0.1f, y);
                glEnd();
            }
        }
    }

    private void drawArrow(float x, float y, float angle) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glRotatef(angle, 0, 0, 1);

        float size = 0.15f;
        glBegin(GL_TRIANGLES);
        glVertex2f(0, 0);
        glVertex2f(-size, size/2);
        glVertex2f(-size, -size/2);
        glEnd();

        glPopMatrix();
    }

    private void drawHyperbola() {
        glColor3f(0, 1, 0);
        glLineWidth(2.5f);

        int steps = 500;

        // Left branch (x < 0)
        glBegin(GL_LINE_STRIP);
        for (int i = 0; i <= steps; i++) {
            float x = X_MIN + (0 - X_MIN) * i / steps;
            if (Math.abs(x) < 0.001f) continue;
            float y = 1.0f / x;
            if (y >= Y_MIN && y <= Y_MAX) {
                glVertex2f(x, y);
            }
        }
        glEnd();

        // Right branch (x > 0)
        glBegin(GL_LINE_STRIP);
        for (int i = 0; i <= steps; i++) {
            float x = 0 + (X_MAX - 0) * i / steps;
            if (Math.abs(x) < 0.001f) continue;
            float y = 1.0f / x;
            if (y >= Y_MIN && y <= Y_MAX) {
                glVertex2f(x, y);
            }
        }
        glEnd();
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}