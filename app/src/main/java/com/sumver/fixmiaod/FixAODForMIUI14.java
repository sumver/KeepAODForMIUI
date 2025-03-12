package com.sumver.fixmiaod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class FixAODForMIUI14 implements IXposedHookLoadPackage {

    private static final int TARGET_BRIGHTNESS = 8191;
    private static final String TAG = "AODFix";
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.miui.aod")) {
            XposedBridge.log(TAG+": 找到万象息屏APP");
            try {
                XposedHelpers.findAndHookMethod(
                        "com.miui.aod.services.WrappedDreamService",
                        lpparam.classLoader,
                        "setDozeScreenBrightness",
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log(TAG+": 亮度: " + param.args[0]);
                                // 强制替换亮度值
                                int targetBrightness = TARGET_BRIGHTNESS;
                                param.args[0] = targetBrightness;
                            }
                        }
                );
            } catch (NoSuchMethodError e) {
                XposedBridge.log(TAG+": NoSuchMethodError: "+e);
            } catch (Exception e) {
                XposedBridge.log(TAG+": Exception: "+e);
            }
        }
    }
}