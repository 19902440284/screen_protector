package com.prongbang.screen_protector;

import android.app.Activity;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/** ScreenProtectorPlugin */
public class ScreenProtectorPlugin implements
        FlutterPlugin,
        MethodChannel.MethodCallHandler,
        ActivityAware {
    private Activity activity;
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "screen_protector");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if ("protectDataLeakageOn".equals(call.method) || "preventScreenshotOn".equals(call.method)) {
            try {
                if (activity != null) {
                    activity.getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
                }
                result.success(true);
            } catch (Exception ignored) {
                result.success(false);
            }
            return;
        }

        if ("protectDataLeakageOff".equals(call.method) || "preventScreenshotOff".equals(call.method)) {
            try {
                if (activity != null) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
                result.success(true);
            } catch (Exception ignored) {
                result.success(false);
            }
            return;
        }

        result.success(false);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {}

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {}
}
