package com.sumver.keepaod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class KeepAODForMIUI implements IXposedHookLoadPackage {

    private static final String TAG = "KeepAod";

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.miui.aod")) {
            return;
        }

        XposedBridge.log(TAG + ": 找到万象息屏APP");

        try {
            // Hook MiuiDozeScreenBrightnessController 类的 onOffTimeout() 方法
            XposedHelpers.findAndHookMethod(
                    "com.miui.aod.doze.MiuiDozeScreenBrightnessController",
                    lpparam.classLoader,
                    "onOffTimeout",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // 阻止关闭屏幕方法运行
                            param.setResult(null);
                            XposedBridge.log(TAG + ": onOffTimeout() blocked: 已阻止息屏时钟关闭");
                        }
                    }
            );
        } catch (NoSuchMethodError e) {
            XposedBridge.log(TAG + ": NoSuchMethodError: " + e.getMessage());
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Exception: " + e.getMessage());
        }
    }
}