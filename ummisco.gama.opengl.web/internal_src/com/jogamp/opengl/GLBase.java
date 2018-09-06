/**
 * Copyright 2009 Sun Microsystems, Inc. All Rights Reserved.
 * Copyright 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

package com.jogamp.opengl;

/**
 * <P>The base interface from which all GL profiles derive, providing
 * checked conversion down to concrete profiles, access to the
 * OpenGL context associated with the GL and extension/function
 * availability queries as described below.</P>
 *
 * <P> While the APIs for vendor extensions are unconditionally
 * exposed, the underlying functions may not be present. The method
 * {@link #isFunctionAvailable} should be used to query the
 * availability of any non-core function before it is used for the
 * first time; for example,
 * <code>gl.isFunctionAvailable("glProgramStringARB")</code>. On
 * certain platforms (Windows in particular), the most "core"
 * functionality is only OpenGL 1.1, so in theory any routines first
 * exposed in OpenGL 1.2, 1.3, and 1.4, 1.5, or 2.0 as well as vendor
 * extensions should all be queried. Calling an unavailable function
 * will cause a {@link GLException} to be raised. </P>
 *
 * {@link #isExtensionAvailable} may also be used to determine whether
 * a specific extension is available before calling the routines or
 * using the functionality it exposes: for example,
 * <code>gl.isExtensionAvailable("GL_ARB_vertex_program");</code>.
 * However, in this case it is up to the end user to know which
 * routines or functionality are associated with which OpenGL
 * extensions. It may also be used to test for the availability of a
 * particular version of OpenGL: for example,
 * <code>gl.isExtensionAvailable("GL_VERSION_1_5");</code>.
 *
 * <P> Exceptions to the window system extension naming rules:
 *
 * <UL>
 *
 * <LI> WGL_ARB_pbuffer, WGL_ARB_pixel_format, and other
 * platform-specific pbuffer functionality; the availability of
 * pbuffers can be queried on Windows, X11 and Mac OS X platforms by
 * querying {@link #isExtensionAvailable} with an argument of
 * "GL_ARB_pbuffer" or "GL_ARB_pixel_format".</LI>
 *
 * </UL> <P>
 *
 */
public class GLBase {

    public static final int GL_MATRIX_MODE = 0x0BA0;
    /** Matrix mode modelview */
    public static final int GL_MODELVIEW = 0x1700;
    /** Matrix mode projection */
    public static final int GL_PROJECTION = 0x1701;
    // public static final int GL_TEXTURE = 0x1702; // Use GL.GL_TEXTURE due to ambiguous GL usage
    /** Matrix access name for modelview */
    public static final int GL_MODELVIEW_MATRIX = 0x0BA6;
    /** Matrix access name for projection */
    public static final int GL_PROJECTION_MATRIX = 0x0BA7;
    /** Matrix access name for texture */
    public static final int GL_TEXTURE_MATRIX = 0x0BA8;
    
    
    
    

    public static final int GL_VERTEX_ARRAY = 0x8074;
    public static final int GL_NORMAL_ARRAY = 0x8075;
    public static final int GL_COLOR_ARRAY = 0x8076;
    public static final int GL_TEXTURE_COORD_ARRAY = 0x8078;

    
    
    

    public static final int GL_LIGHT0 = 0x4000;
    public static final int GL_LIGHT1 = 0x4001;
    public static final int GL_LIGHT2 = 0x4002;
    public static final int GL_LIGHT3 = 0x4003;
    public static final int GL_LIGHT4 = 0x4004;
    public static final int GL_LIGHT5 = 0x4005;
    public static final int GL_LIGHT6 = 0x4006;
    public static final int GL_LIGHT7 = 0x4007;
    public static final int GL_LIGHTING = 0xB50;
    public static final int GL_AMBIENT = 0x1200;
    public static final int GL_DIFFUSE = 0x1201;
    public static final int GL_SPECULAR = 0x1202;
    public static final int GL_POSITION = 0x1203;
    public static final int GL_SPOT_DIRECTION = 0x1204;
    public static final int GL_SPOT_EXPONENT = 0x1205;
    public static final int GL_SPOT_CUTOFF = 0x1206;
    public static final int GL_CONSTANT_ATTENUATION = 0x1207;
    public static final int GL_LINEAR_ATTENUATION = 0x1208;
    public static final int GL_QUADRATIC_ATTENUATION = 0x1209;
    public static final int GL_EMISSION = 0x1600;
    public static final int GL_SHININESS = 0x1601;
    public static final int GL_AMBIENT_AND_DIFFUSE = 0x1602;
    public static final int GL_COLOR_MATERIAL = 0xB57;
    public static final int GL_NORMALIZE = 0xBA1;

    public static final int GL_FLAT = 0x1D00;
    public static final int GL_SMOOTH = 0x1D01;
}

