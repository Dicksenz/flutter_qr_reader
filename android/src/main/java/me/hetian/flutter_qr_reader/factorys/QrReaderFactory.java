package me.hetian.flutter_qr_reader.factorys;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import me.hetian.flutter_qr_reader.views.QrReaderView;

public class QrReaderFactory extends PlatformViewFactory {

    private final FlutterPlugin.FlutterPluginBinding pluginBinding;
    private final Activity activity;

    public QrReaderFactory(FlutterPlugin.FlutterPluginBinding pluginBinding, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.pluginBinding = pluginBinding;
        this.activity = activity;
    }

    @Override
    public PlatformView create(@NonNull Context context, int id, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        BinaryMessenger messenger = pluginBinding.getBinaryMessenger();
        // Pass only context and remove the activity reference
        return new QrReaderView(context, messenger, id, params);
    }
}
