package com.summer.demo.ui.fragment.sdk.easyar;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public class ContextFactory implements GLSurfaceView.EGLContextFactory {
    private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;


    @Override
    public EGLContext createContext(EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig eglConfig) {
        EGLContext context;
        int[] attrib = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
        context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib);
        return context;
    }

    @Override
    public void destroyContext(EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, EGLContext context) {
        egl.eglDestroyContext(display, context);
    }
}