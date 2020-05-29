package com.xubei.shop.ui.module.accessibility;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * 辅助功能相关检查的帮助类
 */
public class AccessibilityUtil {
    private static final String ACCESSIBILITY_SERVICE_PATH = AccessibilityAutoInputService.class.getCanonicalName();
    /**
     * 判断是否有辅助功能权限
     */
    public static boolean isAccessibilitySettingsOn(Context context) {
        if (context == null) {
            return false;
        }

        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        String packageName = context.getPackageName();
        final String serviceStr = packageName + "/" + ACCESSIBILITY_SERVICE_PATH;
        if (accessibilityEnabled == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();

                    if (accessabilityService.equalsIgnoreCase(serviceStr)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Intent getAccessibilitySettingPageIntent(Context context) {
        // 一些品牌的手机可能不是这个Intent,需要适配
        return new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    }
}
