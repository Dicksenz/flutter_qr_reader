package me.hetian.flutter_qr_reader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import me.hetian.flutter_qr_reader.factorys.QrReaderFactory;

/** FlutterQrReaderPlugin */
public class FlutterQrReaderPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private FlutterPluginBinding pluginBinding;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    this.pluginBinding = flutterPluginBinding;
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "me.hetian.flutter_qr_reader");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
    pluginBinding = null;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    pluginBinding.getPlatformViewRegistry().registerViewFactory(
      "me.hetian.flutter_qr_reader.reader_view",
      new QrReaderFactory(pluginBinding, activity)
    );
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activity = null;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("imgQrCode")) {
      imgQrCode(call, result);
    } else {
      result.notImplemented();
    }
  }

  @SuppressLint("StaticFieldLeak")
  void imgQrCode(MethodCall call, final Result result) {
    final String filePath = call.argument("file");
    if (filePath == null) {
      result.error("Not found data", null, null);
      return;
    }

    File file = new File(filePath);
    if (!file.exists()) {
      result.error("File not found", null, null);
      return;
    }

    new AsyncTask<String, Integer, String>() {
      @Override
      protected String doInBackground(String... params) {
        return QRCodeDecoder.syncDecodeQRCode(filePath);
      }

      @Override
      protected void onPostExecute(String s) {
        if (s == null) {
          result.error("not data", null, null);
        } else {
          result.success(s);
        }
      }
    }.execute(filePath);
  }
}
